package com.example.gestiondeprojet.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gestiondeprojet.R;
import com.example.gestiondeprojet.models.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.gestiondeprojet.Constants.DEBUGG;
import static com.example.gestiondeprojet.Constants.NB_MAX_LETTER_IN_DESC;

/**
 * Adapter of the task object. It allows to make the link between the list of task and the view.
 * Adapter de l'objet Task. Elle permet de faire le lien entre l'ensemble de task et la vue.
 * @author Kierian Tirlemont
 */
public class TaskAdapter extends BaseAdapter {

    /**
     * The context of the activity.
     * Le contexte de l'activité.
     */
    private Context context;

    /**
     * The list of tasks to display.
     * La liste de taches à afficher.
     */
    private List<Task> taskList;

    /**
     * LayoutInflater allows to make the link between a xml code and a java view.
     * LayoutInflater permet de faire le lien entre le code xml et la vue java.
     */
    private LayoutInflater inflater;

    /**
     * Init the attributes.
     * @param context The context of the activity.
     * @param taskList The list of Tasks to display.
     */
    public TaskAdapter(Context context, List<Task> taskList){
        this.context = context;
        this.taskList = taskList;
        this.inflater = LayoutInflater.from(this.context);
    }


    @Override
    public int getCount() {
        return this.taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.taskList.get(position);
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
        String currentDateStr = newFormat.format(currentDate);

        Log.e(DEBUGG, "Date courrante : "+currentDateStr);

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
        displayEstimateDuration.setText("Durée estimée : "+estimateDuration);
        displayProjectName.setText(projectName);

        ImageView itemIconView = view.findViewById(R.id.item_icon);
        int resId = context.getResources().getIdentifier(memonic,"drawable",
                context.getPackageName());
        itemIconView.setImageResource(resId);

        return view;
    }

    private String oneLineDescription(String completeDesc){
        int nbChar = completeDesc.length();
        if(nbChar < NB_MAX_LETTER_IN_DESC){
            return completeDesc;
        }else{
            return completeDesc.substring(0, NB_MAX_LETTER_IN_DESC -4)+"...";
        }
    }
}
