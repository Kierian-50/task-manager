package com.example.gestiondeprojet.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestiondeprojet.R;
import com.example.gestiondeprojet.Util;
import com.example.gestiondeprojet.adapter.TaskAdapter;
import com.example.gestiondeprojet.models.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.example.gestiondeprojet.Constants.BEGIN_DATE;
import static com.example.gestiondeprojet.Constants.CONTEXT;
import static com.example.gestiondeprojet.Constants.DESCRIPTION;
import static com.example.gestiondeprojet.Constants.ESTIMATE_DURATION;
import static com.example.gestiondeprojet.Constants.ID;
import static com.example.gestiondeprojet.Constants.JSON_EXTENSION;
import static com.example.gestiondeprojet.Constants.MAX_END_DATE;
import static com.example.gestiondeprojet.Constants.NAME;
import static com.example.gestiondeprojet.Constants.PROJECT;
import static com.example.gestiondeprojet.Constants.STATE;
import static com.example.gestiondeprojet.Constants.TASK;
import static com.example.gestiondeprojet.Constants.availableLanguege;
import static com.example.gestiondeprojet.Constants.currentSort;
import static com.example.gestiondeprojet.Constants.currentUsername;
import static com.example.gestiondeprojet.Util.findPositionWithId;
import static java.util.Locale.FRANCE;
import static java.util.Locale.GERMANY;
import static java.util.Locale.UK;

/**
 * This class allows to displays the list of tasks and add the logic of some button to add interaction.
 * Cette classe permet d'afficher la liste de tâches et d'ajouter la logique de quelques boutons pour
 * ajouter quelques interactions avec l'utilisateur.
 * @author Kierian Tirlemont
 */
public class ListTask extends AppCompatActivity {

    /**
     * This imageButton allows to display an image and have the attribute of a button. When the
     * user click on it, it refers to the create task activity.
     * Cet imageButton permet d'afficher une image et a les mêmes attributs qu'un boutton. Quand
     * l'utilisateur clique dessus, il arrive à l'activité de création de tache.
     */
    private ImageButton addTask;

    /**
     * This refers to the componant which displays the list of tasks.
     * Cet attribut fait référence au composant qui affiche la liste de tache
     */
    private ListView listView;

    /**
     * The context of the activity.
     * Le contexte de l'activité.
     */
    private Context context;

    /**
     * The list of tasks which will be displayed.
     * La liste de tâches qui sera affiché.
     */
    private List<Task> taskList;

    /**
     * The adapter which allows to make the link between the list of task and the view.
     * L'adapter qui permet de faire le lien entre la liste de tâche et la vue.
     */
    private TaskAdapter taskAdapter;

    /**
     * The component which indicates to the user the function of the edittext and if you click on
     * it it focus the edit text.
     * Le compossant qui indique la fonction du edittext à l'utilisateur et si il clique dessus
     * ca ouvre l'edittext.
     */
    private ImageButton searchIcon;

    /**
     * The component where the user can search a task.
     * Le composant où l'utilisateur peut chercher une tâche.
     */
    private EditText searchContent;

    /**
     * The component where the user can sort the task.
     * Le composant où l'utilisateur peut trier les tâches.
     */
    private Spinner sortBy;

    /**
     * The component that allows to change the language of the app.
     * Le composant qui permet de changer la langue de l'application.
     */
    private ImageButton translationButton;

    /**
     * The component that allows to change the brightness mode
     * Le composant qui permet de changer le mode de luminosité
     */
    private ImageButton buttonMode;

    /**
     * The saved preferences are contains in this attributes.
     * Les préférences enregistrées sont contenues dans cet attributs
     */
    private SharedPreferences mPreferences;

    /**
     * This attribute allows to write the preferences.
     * Cet attribut permet d'écrire les préférences.
     */
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_task);

        // Init attributes
        this.context = this;
        this.addTask = findViewById(R.id.add_task);
        this.listView = findViewById(R.id.shop_list_view);
        this.searchIcon = findViewById(R.id.search_icon);
        this.searchContent = findViewById(R.id.search_content);
        this.sortBy = findViewById(R.id.spinner_sort_by);
        this.translationButton = findViewById(R.id.listTask_translation);
        this.buttonMode = findViewById(R.id.listTask_brightness);
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.mEditor = mPreferences.edit();

        final ArrayList<String> possibleSort = new ArrayList<>(Arrays.asList(
                getResources().getString(R.string.by_state),
                getResources().getString(R.string.by_date),
                getResources().getString(R.string.by_name),
                getResources().getString(R.string.by_duration),
                getResources().getString(R.string.by_project)));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, possibleSort);
        this.sortBy.setAdapter(spinnerArrayAdapter);
        this.sortBy.setSelection(currentSort);

        // List that contains the task which are in the json.
        // Liste qui contient les taches contenues dans le json.
        this.taskList = new ArrayList<>();

        JSONObject json = Util.readJsonFile(currentUsername+ JSON_EXTENSION, context);

        try {
            // Find the tasks in the json file
            // Trouve les taches dans le fichier json.
            JSONArray taskArray = json.getJSONArray(TASK);

            // For each task contains in the jsonArray
            // Pour chaque tache contenue dans le jsonArray
            for (int i = 0; i < taskArray.length(); i++) {
                // Find the task / Trouve la tache
                JSONObject taskObject = taskArray.getJSONObject(i);
                // Add it to the task list / Ajoute la tache dans la liste de tache
                // Task(int id, String name, String memonic, String description, String duration, Date beginDate, Date maxEndDate, String context)
                this.taskList.add(new Task(taskObject.getInt(ID),
                        taskObject.getString(NAME),
                        taskObject.getString(STATE),
                        taskObject.getString(DESCRIPTION),
                        taskObject.getString(ESTIMATE_DURATION),
                        taskObject.getString(BEGIN_DATE),
                        taskObject.getString(MAX_END_DATE),
                        taskObject.getString(CONTEXT),
                        taskObject.getString(PROJECT)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.taskAdapter = new TaskAdapter(this, taskList);

        // Create the TaskAdapter which will be able to display every tasks
        // Crée un objet TaskAdapter afin d'affichier toutes les taches
        this.listView.setAdapter(this.taskAdapter);

        // If the user click on add task.
        // Si l'utilisateur clique sur le boutton d'ajout de tache.
        this.addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the create task activity
                // Lance l'activité de creation de taches
                Intent createTask = new Intent(getApplicationContext(), taskCreateActivity.class);
                startActivity(createTask);
                finish();
            }
        });


        // If the user make a long click on an item it displays a popup to ask him if he wants to
        // delete or update a task or cancel.
        // Si l'utilisateur fait un long clique sur l'item, ça affiche une popup qui lui demande
        // s'il veut supprimer ou mettre à jour la tâche ou annuler.
        this.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Display a small popup that ask if the user want to update, delete or cancel.
                // Affiche une petite popup qui demande si l'utilisateur veut mettre à jour ou
                // supprimer ou annuler.
                AlertDialog.Builder adb = new AlertDialog.Builder(ListTask.this);
                adb.setTitle(getResources().getString(R.string.text_popup));
                adb.setNeutralButton(getResources().getString(R.string.cancel), null);
                adb.setPositiveButton(getResources().getString(R.string.update), new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // If he wants to update go to the update' activity
                        // S'il veut mettre à jour alors la page de mise à jour va s'afficher.
                        Intent intent = new Intent(getApplicationContext(), UpdateTask.class);
                        intent.putExtra("taskId", taskList.get(position).getId());
                        startActivity(intent);
                        finish();
                    }});
                adb.setNegativeButton(getResources().getString(R.string.remove), new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the task, if he clicks on delete
                        // Supprime la tache si l'utilisateur clique sur le bouton.
                        int idToRemove = taskList.get(position).getId();
                        taskList.remove(position);
                        taskAdapter.notifyDataSetChanged();
                        Util.removeTask(currentUsername+JSON_EXTENSION, findPositionWithId(idToRemove, context), context);
                    }});
                adb.show();

                return true;
            }
        });

        // If the user make a small/normal click on a task, it displays the information about the task
        // Si l'utilisateur fait un petit/normal clique sur la tâche, ça affiche les informations
        // concernant la tâche.
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), taskDescActivity.class);
                intent.putExtra("taskId", taskList.get(position).getId());
                startActivity(intent);
                finish();
            }
        });

        // Search bar
        // On click of the imageButton it focuses the edittext to search of tasks.
        // En cliquant sur l'imagebutton ça ouvre l'edittext de recherche de taches
        this.searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchContent.requestFocus();
                searchContent.setFocusableInTouchMode(true);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(searchContent, InputMethodManager.SHOW_FORCED);
                }
        });

        // When the user types it displays only the task which have the same name
        // Quand l'utilisateur écrit dans le composant searchContent, ça affiche que les tâches
        // qui ont le même nom
        this.searchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // This component allows to choose a filter to apply on the list.
        // Ce composant permet de choisir un filtre à appliquer sur la liste.
        this.sortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // hide selection text
                // Cache le texte de selection
                // Avoid crash when the user click a lot on the dark/light mode button
                // Evite le crash quand l'utilisateur appuie beaucoup sur le bouton du mode sombre.
                try {
                    ((TextView)view).setText(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Find the setting chosen
                // Trouve le filtre choisi
                String sortByChoosen = sortBy.getSelectedItem().toString();

                // If the user wants to filter by state
                // Si l'utilisateur veut filtrer par état
                if(sortByChoosen.equalsIgnoreCase(getResources().getString(R.string.by_state))){
                    for(int i = 0; i<taskList.size(); i++){
                        for(int j =0; j<taskList.size(); j++){
                            if(taskList.get(i).compareByState(taskList.get(j)) > 0){
                                Task tmp = taskList.get(i);
                                taskList.set(i, taskList.get(j));
                                taskList.set(j, tmp);
                            }
                        }
                    }
                    currentSort = 0;
                    // If the user wants to filter by date
                    // Si l'utilisateur veut filtrer par date
                }else if(sortByChoosen.equalsIgnoreCase(getResources().getString(R.string.by_date))){
                    for(int i = 0; i<taskList.size(); i++){
                        for(int j =0; j<taskList.size(); j++){
                            if(taskList.get(i).compareByDate(taskList.get(j)) < 0){
                                Task tmp = taskList.get(i);
                                taskList.set(i, taskList.get(j));
                                taskList.set(j, tmp);
                            }
                        }
                    }
                    currentSort = 1;
                    // If the user wants to filter by name
                    // Si l'utilisateur veut filtrer par nom
                }else if(sortByChoosen.equalsIgnoreCase(getResources().getString(R.string.by_name))){
                    for(int i = 0; i<taskList.size(); i++){
                        for(int j =0; j<taskList.size(); j++){
                            if(taskList.get(i).compareByName(taskList.get(j)) < 0){
                                Task tmp = taskList.get(i);
                                taskList.set(i, taskList.get(j));
                                taskList.set(j, tmp);
                            }
                        }
                    }
                    currentSort = 2;
                    // If the user wants to filter by estimate duration
                    // Si l'utilisateur veut filtrer par durée estimée
                }else if(sortByChoosen.equalsIgnoreCase(getResources().getString(R.string.by_duration))){
                    for(int i = 0; i<taskList.size(); i++){
                        for(int j =0; j<taskList.size(); j++){
                            if(taskList.get(i).compareByDuration(taskList.get(j)) < 0){
                                Task tmp = taskList.get(i);
                                taskList.set(i, taskList.get(j));
                                taskList.set(j, tmp);
                            }
                        }
                    }
                    currentSort = 3;
                    // If the user wants to filter by project name
                    // Si l'utilisateur veut filtrer par nom de projet
                }else{
                    for(int i = 0; i<taskList.size(); i++){
                        for(int j =0; j<taskList.size(); j++){
                            if(taskList.get(i).getProjectName().equalsIgnoreCase(taskList.get(j).getProjectName()) && i!=j){
                                i++;
                                Task tmp = taskList.get(i);
                                taskList.set(i, taskList.get(j));
                                taskList.set(j, tmp);
                            }
                        }
                    }
                    currentSort = 4;
                }
                taskAdapter.notifyDataSetChanged();
            }
            public void onNothingSelected(AdapterView<?> arg0) {}
        });

        // If the user click on the translation button, display  a popup with the languages
        // Si l'utilisateur clique sur le boutton de traduction, affiche une popup avec les langues
        this.translationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder mBuilder = new androidx.appcompat.app.AlertDialog.Builder(ListTask.this);
                mBuilder.setTitle(getResources().getString(R.string.choose_language));
                mBuilder.setSingleChoiceItems(availableLanguege, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            Locale.setDefault(FRANCE);
                            saveLanguage("FRANCE");
                        }else if(which==1){
                            Locale.setDefault(UK);
                            saveLanguage("UK");
                        }else if(which==2){
                            Locale.setDefault(GERMANY);
                            saveLanguage("GERMANY");
                        }
                        Configuration config = new Configuration();
                        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), ListTask.class);
                        startActivity(intent);
                        finish();
                    }
                });
                androidx.appcompat.app.AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        // When the user click on the brightness button it turns to dark or light mode according to
        // the current mode
        // Quand l'utilisateur clique sur le boutton de luminotsité, ca change pour le dark mode
        // ou le mode jour selon le mode
        // TODO gérer le bug de changement de langue lors du changement de mode de couleur
        this.buttonMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nightModeFlags =
                        context.getResources().getConfiguration().uiMode &
                                Configuration.UI_MODE_NIGHT_MASK;
                switch (nightModeFlags) {
                    case Configuration.UI_MODE_NIGHT_YES:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        saveMode("LIGHT_MODE");
                        Locale.setDefault(findLanguagePreferences());
                        break;

                    case Configuration.UI_MODE_NIGHT_NO:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        saveMode("DARK_MODE");
                        Locale.setDefault(findLanguagePreferences());
                        break;

                    case Configuration.UI_MODE_NIGHT_UNDEFINED:
                        String text = getResources().getString(R.string.brithness_mode_impossible);
                        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                        toast.show();
                        break;
                }
                // Relaunch the activity else crash
                Intent intent = new Intent(getApplicationContext(), ListTask.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * This method allows to write the brightness mode chosen by the user in the preferences.
     * Cette méthode permet d'écrire les préférences choisies concernant la luminosité dans les
     * préférences.
     * @param mode The selected mode / Le mode choisi
     */
    private void saveMode(String mode){
        mEditor.putString(getString(R.string.brigthness_mode), mode);
        mEditor.commit();
    }

    /**
     * This method allows to write the language chosen by the user in the preferences.
     * Cette méthode permet d'écrire les préférences choisies concernant la langue dans les
     * préférences.
     * @param language The selected language / La langue choisie
     */
    private void saveLanguage(String language){
        mEditor.putString(getString(R.string.language), language);
        mEditor.commit();
    }

    /**
     * This method allows to find the language in the preferences and return the Locale variable
     * associated.
     * Cette methode permet de trouver la langue dans les preferences de l'application et retourne
     * la constante de la classe Locale associée.
     * @return The Locale constant that correspond to the language / La constante de la classe
     *         Locale qui correspond à la langue.
     */
    private Locale findLanguagePreferences(){
        String language = this.mPreferences.getString(getString(R.string.language), "FRANCE");
        if(language.equals("FRANCE")){
            return FRANCE;
        }else if(language.equals("UK")){
            return UK;
        }else{
            return GERMANY;
        }
    }
}