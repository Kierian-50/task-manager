package com.example.gestiondeprojet.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gestiondeprojet.R;
import com.example.gestiondeprojet.Util;
import com.example.gestiondeprojet.models.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.gestiondeprojet.Constants.NB_MAX_LETTER_IN_DESC;

/**
 * Adapter of the task object. It allows to make the link between the list of task and the view.
 * Adapter de l'objet Task. Elle permet de faire le lien entre l'ensemble de task et la vue.
 * @author Kierian Tirlemont
 */
public class TaskAdapter extends BaseAdapter implements Filterable {

    /**
     * The context of the activity.
     * Le contexte de l'activité.
     */
    private Context context;

    /**
     * LayoutInflater allows to make the link between a xml code and a java view.
     * LayoutInflater permet de faire le lien entre le code xml et la vue java.
     */
    private LayoutInflater inflater;

    /**
     * The list of tasks to display.
     * La liste de taches à afficher.
     */
    private List<Task> originalArray;

    /**
     * The list which contains every tasks
     * La liste contenant toutes les taches
     */
    private List<Task> tempArray;

    /**
     * This hashmap allows to list a unique color for each project.
     * Cet hashmap permet de lister pour chaque projet une couleur unique.
     */
    private HashMap<String, Integer> colorForProject;

    /**
     * Init the attributes.
     * @param context The context of the activity.
     * @param taskList The list of Tasks to display.
     */
    public TaskAdapter(Context context, List<Task> taskList){
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.originalArray = taskList;
        this.tempArray = taskList;
        if(this.originalArray.size() == 0){
            this.colorForProject = new HashMap<>();
        }else{
            this.colorForProject = Util.findColorByProject(originalArray);
        }
    }

    @Override
    public int getCount() {
        return this.originalArray.size();
    }

    @Override
    public Object getItem(int position) {
        return this.originalArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = this.inflater.inflate(R.layout.adapter_task, null);

        SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();

        // Find the information of the task
        Task currentTask = (Task) getItem(i);
        String currentName = currentTask.getName();
        String memonic = currentTask.getMemonic();
        String completeDescription = currentTask.getDescription();
        String estimateDuration = currentTask.getEstimateDuration();
        String beginDate = currentTask.getBeginDate();
        String maxEndDate = currentTask.getMaxEndDate();
        String dateToDisplay = beginDate+" => "+maxEndDate;
        String projectName = currentTask.getProjectName();
        String description = oneLineDescription(completeDescription);

        // Change the value of the adapter_task
        TextView displayName = view.findViewById(R.id.item_name);
        TextView displayDesc = view.findViewById(R.id.item_desc);
        TextView displayDate = view.findViewById(R.id.item_date);
        TextView displayEstimateDuration = view.findViewById(R.id.item_estimate_duration);
        TextView displayProjectName = view.findViewById(R.id.item_project);

        try {
            Date endDate = newFormat.parse(maxEndDate);
            if(currentDate.after(endDate)) { // EChange la couleur de la date si la date de fin de tache est passé
                displayDate.setTextColor(0xffff0000);
            }else{
                displayDate.setTextColor(0xff008000);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        displayName.setText(currentName);
        displayDesc.setText(description);
        displayDate.setText(dateToDisplay);
        displayEstimateDuration.setText(context.getResources().getString(R.string.estimated_duration)+" : "+estimateDuration);
        displayProjectName.setText(projectName);

        ImageView itemIconView = view.findViewById(R.id.item_icon);
        int resId = context.getResources().getIdentifier(memonic,"drawable",
                context.getPackageName());
        itemIconView.setImageResource(resId);

        if(colorForProject.containsKey(projectName)){ // If the project has a color/Si le projet a une couleur.
            displayProjectName.setTextColor(colorForProject.get(projectName)); // Change the color
        }

        return view;
    }

    /**
     * This method allows to avoid to display all the description but only the beginning.
     * Cette méthode permet d'éviter d'afficher toute la description mais seulement le début.
     * @param completeDesc All the description / Toute la description
     * @return The beginning of the description / Le début de la description
     */
    private String oneLineDescription(String completeDesc){
        int nbChar = completeDesc.length();
        if(completeDesc.contains("\n") && completeDesc.substring(0, completeDesc.indexOf("\n")).length() < NB_MAX_LETTER_IN_DESC){
            return completeDesc.substring(0, completeDesc.indexOf("\n"))+"...";
        }else if(nbChar < NB_MAX_LETTER_IN_DESC){
            return completeDesc;
        }else{
            return completeDesc.substring(0, NB_MAX_LETTER_IN_DESC -4)+"...";
        }
    }

    /**
     * This method allows to filter the list by the name entered by the user.
     * Cette méthode permet de filtrer la liste par le nom de tache entré par l'utilisateur.
     * @return The list of task that correspond to the filter
     *         La liste de tâche qui correspond au filtre
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                // If there is a string entered by the user
                // Si l'utilisateur rentre un string dans l'edittext
                if(constraint!=null && constraint.length()>0){
                    constraint = constraint.toString().toUpperCase();
                    ArrayList<Task> filters = new ArrayList<>();

                    for(int i = 0; i<tempArray.size(); i++){
                        if(tempArray.get(i).getName().toUpperCase().contains(constraint)){
                            Task task = new Task(tempArray.get(i).getId(), tempArray.get(i).getName(),
                                    tempArray.get(i).getMemonic(), tempArray.get(i).getDescription(),
                                    tempArray.get(i).getEstimateDuration(),
                                    tempArray.get(i).getBeginDate(), tempArray.get(i).getMaxEndDate(),
                                    tempArray.get(i).getContext(), tempArray.get(i).getProjectName(),
                                    tempArray.get(i).getUrl());
                            filters.add(task);
                        }
                    }

                    results.values = filters;
                    results.count = filters.size();
                }else{
                    results.count = tempArray.size();
                    results.values = tempArray;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                originalArray = (ArrayList<Task>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
