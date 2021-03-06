package com.example.gestiondeprojet.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.example.gestiondeprojet.R;
import com.example.gestiondeprojet.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static com.example.gestiondeprojet.Constants.BEGIN_DATE;
import static com.example.gestiondeprojet.Constants.CLOSED;
import static com.example.gestiondeprojet.Constants.CONTEXT;
import static com.example.gestiondeprojet.Constants.DESCRIPTION;
import static com.example.gestiondeprojet.Constants.DOING;
import static com.example.gestiondeprojet.Constants.ESTIMATE_DURATION;
import static com.example.gestiondeprojet.Constants.ID;
import static com.example.gestiondeprojet.Constants.JSON_EXTENSION;
import static com.example.gestiondeprojet.Constants.MAX_END_DATE;
import static com.example.gestiondeprojet.Constants.NAME;
import static com.example.gestiondeprojet.Constants.PROJECT;
import static com.example.gestiondeprojet.Constants.STATE;
import static com.example.gestiondeprojet.Constants.TASK;
import static com.example.gestiondeprojet.Constants.TASK_ID;
import static com.example.gestiondeprojet.Constants.TODO;
import static com.example.gestiondeprojet.Constants.URL;
import static com.example.gestiondeprojet.Constants.currentUsername;
import static com.example.gestiondeprojet.Util.findPositionWithId;

/**
 * This class is link to the update task activity and manage the logic behind this activity.
 * Cette classe est lié à l'activité de mise à jour des tâches et gère toutes la logique derrière.
 * @author Kierian Tirlemont
 */
public class UpdateTaskActivity extends AppCompatActivity {

    /**
     * The spinner that shows the different state of the task.
     * Le menu déroulant qui affiche l'état de la tache.
     */
    private Spinner stateSpinner;

    /**
     * The component where the user can enter the task' name.
     * Le composant où l'utilisateur peut renter le nom de la tâche.
     */
    private EditText taskName;

    /**
     * The component where the user can enter the description of the task.
     * Le composant ou l'utilisateur peut rentrer la description de la tâche.
     */
    private EditText taskDesc;

    /**
     * The component where the user can select the numbers of hours that the task will take. This
     * component allows a better display to select the number.
     * Le composant où l'utilisateur peut selectionner le nombre d'heures que prendra la tâche. Ce
     * composant permet un affichage des nombres plus conviviale.
     */
    private NumberPicker nbPickerHour;

    /**
     * The component where the user can select the numbers of minutes that the task will take. This
     * component allows a better display to select the number.
     * Le composant où l'utilisateur peut selectionner le nombre de minutes que prendra la tâche. Ce
     * composant permet un affichage des nombres plus conviviale.
     */
    private NumberPicker nbPickerMinutes;

    /**
     * The component where the user can select the begin date of the task.
     * Le composant ou l'utilisateur peut choisir la date de début de la tâche.
     */
    private EditText taskBeginDate;

    /**
     * The component where the user can select the max end date of the task.
     * Le composant ou l'utilisateur peut choisir la date de fin maximale de la tâche.
     */
    private EditText taskEndDate;

    /**
     * The component where the user can enter the context of the task.
     * Le composant ou l'utilisateur peut rentrer le contexte de la tâche.
     */
    private EditText taskContext;

    /**
     * The component where the user can enter the project name link to the task. This component
     * allows to displays the name of project that already exists.
     * Le composant ou l'utilisateur peut écrire le nom du projet lié à la tâche. Ce composant
     * permet d'afficher le nom des projets existes déjà.
     */
    private AutoCompleteTextView project;

    /**
     * The button that allows to save the modification
     * Le boutton qui permet d'enregistrer les modifications
     */
    private Button updateTask;

    /**
     * This component allows a better display of the selection of the date.
     * Ce composant permet un affichage plus conviviale de la date.
     */
    private DatePickerDialog.OnDateSetListener dateSetListenerBegin;

    /**
     * This component allows a better display of the selection of the date.
     * Ce composant permet un affichage plus conviviale de la date.
     */
    private DatePickerDialog.OnDateSetListener dateSetListenerEnd;

    /**
     * The context of the activity
     * Le contexte de l'activité.
     */
    private Context context;

    /**
     * The id of the task
     * L'id de la tâche
     */
    private int taskId;

    /**
     * The json array which contains every tasks
     * Le tableau json qui contient toutes les tâches
     */
    private JSONArray taskArray;

    /**
     * The json object which contains the current task to display/modify
     * L'objet json qui contient la tâche courante à afficher/modifier
     */
    private JSONObject currentTask;

    /**
     * The component which allows to go back to the list.
     * Le composant qui permet de revenir sur l'activité d'affichage de la liste.
     */
    private ImageButton backButton;

    /**
     * The id of the task to update.
     * L'identifiant de la tache a mettre à jour.
     */
    private int taskIdToDisplay;

    /**
     * The component which allows to enter an URL.
     * Le composant qui permet d'écrire l'url lié à une tâche
     */
    private EditText taskUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        Bundle bundle = getIntent().getExtras();
        this.taskIdToDisplay = bundle.getInt(TASK_ID, 0);

        // Init attributes
        this.stateSpinner = findViewById(R.id.update_spinner_state);
        this.taskName = findViewById(R.id.update_task_name);
        this.taskDesc = findViewById(R.id.update_task_desc);
        this.nbPickerHour = findViewById(R.id.update_task_duration_hour);
        this.nbPickerMinutes = findViewById(R.id.update_task_duration_minutes);
        this.taskBeginDate = findViewById(R.id.update_task_begin_date);
        this.taskEndDate = findViewById(R.id.update_task_end_date);
        this.taskContext = findViewById(R.id.update_task_context);
        this.project = findViewById(R.id.update_task_project);
        this.updateTask = findViewById(R.id.update_task_button);
        this.backButton = findViewById(R.id.update_back_button);
        this.taskUrl = findViewById(R.id.update_task_url);
        this.context = this;

        // Put the possible values of the NumberPicker
        // Met les valeurs possibles de NumberPicker
        this.nbPickerHour.setMinValue(0);
        this.nbPickerHour.setMaxValue(999);
        this.nbPickerMinutes.setMinValue(0);
        this.nbPickerMinutes.setMaxValue(59);

        // Create the spinner which display teh different state that a task can take.
        // Crée le spinner qui change l'état de la tache
        final ArrayList<String> possibleState = new ArrayList<>(Arrays.asList(
                getResources().getString(R.string.todo),
                getResources().getString(R.string.doing),
                getResources().getString(R.string.finish)));
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, possibleState);
        this.stateSpinner.setAdapter(spinnerArrayAdapter);

        // If the user select the edittext about the begin date of the task it displays a
        // better display to choose the date.
        // Si l'utilisateur choisi l'edit text concernant la date de début de la tache, ça affiche
        // un affichage plus conviviale pour choisir la date.
        this.taskBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        UpdateTaskActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListenerBegin,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        // If the user select the edittext about the max end date of the task it displays a
        // better display to choose the date.
        // Si l'utilisateur choisi l'edit text concernant la date de fin de la tache, ça affiche
        // un affichage plus conviviale pour choisir la date.
        this.taskEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        UpdateTaskActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListenerEnd,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        // After the selection of the begin date it display the date on the component
        // Après la selection de la date de début de la tache, ça affiche la date choisi sur le composant
        this.dateSetListenerBegin = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = day + "/" + month + "/" + year;
                taskBeginDate.setText(date);
            }
        };

        // After the selection of the max end date it display the date on the component
        // Après la selection de la date de fin de la tache, ça affiche la date choisi sur le composant
        this.dateSetListenerEnd = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = day + "/" + month + "/" + year;
                taskEndDate.setText(date);
            }
        };

        // Displays every project already saved with the autocompletion
        // Affichage de tous les projets avec l'autocompletion
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.adapter_list_project, R.id.text_completion_project, Util.findProjects(this));
        this.project.setAdapter(adapter);

        // Recover the current value of the task
        // Recupère les valeurs de la tâche
        JSONObject json = Util.readJsonFile(currentUsername+ JSON_EXTENSION, context);

        try {
            // Display the current values of the tasks
            // Affichage des valeurs actuelles de la tache
            this.taskArray = json.getJSONArray(TASK);
            this.currentTask = taskArray.getJSONObject(findPositionWithId(this.taskIdToDisplay, context));

            this.taskId = this.currentTask.getInt(ID);

            this.taskName.setText(currentTask.getString(NAME));
            this.stateSpinner.setSelection(findDefaultSpinnerPos(String.valueOf(currentTask.get(STATE))));
            this.taskDesc.setText(currentTask.getString(DESCRIPTION));
            this.taskBeginDate.setText(currentTask.getString(BEGIN_DATE));
            this.taskEndDate.setText(currentTask.getString(MAX_END_DATE));
            this.taskContext.setText(currentTask.getString(CONTEXT));
            this.taskUrl.setText(currentTask.getString(URL));

            String duration = currentTask.getString(ESTIMATE_DURATION);
            String[] values = duration.split("h");
            int hours = Integer.parseInt(values[0]);
            int minutes = Integer.parseInt(values[1].split("m")[0]);

            this.nbPickerMinutes.setValue(minutes);
            this.nbPickerHour.setValue(hours);

            if(this.currentTask.has(PROJECT))this.project.setText(currentTask.getString("Project"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        this.updateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check that the end date is not before the begin date.
                // Vérifie que la date de fin n'est pas avant la date de début
                boolean errorDate = false;
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    // Parse in Date object to compare it.
                    Date dateBegin = sdf.parse(taskBeginDate.getText().toString());
                    Date dateEnd = sdf.parse(taskEndDate.getText().toString());

                    // Avoid that the begin date is after the end date but accept dateBegin = dateEnd
                    // Empeche que la date de début soit après la date de fin mais accepte dateBegin = dateEnd
                    if(dateBegin.compareTo(dateEnd) > 0) {
                        errorDate = true;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Check that the field are filled
                // Vérifie que les valeurs sont bien rentrés
                if(!String.valueOf(taskName.getText()).equals("") &&
                        !String.valueOf(taskDesc.getText()).equals("") &&
                        !String.valueOf(taskContext.getText()).equals("") &&
                        !String.valueOf(taskBeginDate.getText()).equals("") &&
                        !String.valueOf(taskEndDate.getText()).equals("") &&
                        !errorDate){

                    // Check that the url entered by the user is correct or is empty
                    // Vérifie que l'url entré par l'utilisateur est correcte ou est vide.
                    if(Util.isUrl(taskUrl.getText().toString()) || taskUrl.getText().toString().equals("")){
                        try {
                            // Create the task
                            taskArray.remove(findPositionWithId(taskIdToDisplay, context)); // Supprime la tache du json avant de la réécrire
                            currentTask.put(ID, taskId);
                            currentTask.put(NAME, taskName.getText().toString());
                            currentTask.put(STATE, findStateInSpinner(stateSpinner.getSelectedItem().toString()));
                            currentTask.put(DESCRIPTION, taskDesc.getText().toString());
                            currentTask.put(ESTIMATE_DURATION, nbPickerHour.getValue()+"h"+nbPickerMinutes.getValue()+"m");
                            currentTask.put(BEGIN_DATE, taskBeginDate.getText().toString());
                            currentTask.put(MAX_END_DATE, taskEndDate.getText().toString());
                            currentTask.put(CONTEXT, taskContext.getText().toString());
                            currentTask.put(PROJECT, project.getText().toString());
                            currentTask.put(URL, taskUrl.getText().toString());
                            // The json file
                            JSONObject json = Util.readJsonFile(currentUsername+ JSON_EXTENSION, context);

                            // The task list
                            taskArray.put(currentTask); // Add the task
                            json.put(TASK, taskArray);

                            Util.writeJsonFile(currentUsername+ JSON_EXTENSION, json, context);

                            // Return to the list
                            Intent intent = new Intent(getApplicationContext(), ListTaskActivity.class);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        // Display an error message because the url is wrong
                        // Affiche un message d'error car l'url est mauvais
                        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(UpdateTaskActivity.this);
                        adb.setTitle(getResources().getString(R.string.error_url_entered));
                        adb.setMessage(getResources().getString(R.string.error_url_entered_text));
                        adb.setPositiveButton(getResources().getString(R.string.ok), new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                taskUrl.getText().clear();
                            }
                        });
                        adb.show();
                    }

                }else if(errorDate) {
                    // Display a popup which says that the begin date is after the end date.
                    // Affiche une popup qui prévient que la date de début est après la date de fin.
                    AlertDialog.Builder adb = new AlertDialog.Builder(UpdateTaskActivity.this);
                    adb.setTitle(getResources().getString(R.string.error_date));
                    adb.setPositiveButton(getResources().getString(R.string.ok), null);
                    adb.show();
                }else{
                    // For each required field which is not filled the hint text becomes red
                    // Pour chaque champs requis non remplis, le texte devient rouge.
                    if(String.valueOf(taskName.getText()).equals("")){
                        taskName.setHintTextColor(0xffff0000);
                    }
                    if(String.valueOf(taskDesc.getText()).equals("")){
                        taskDesc.setHintTextColor(0xffff0000);
                    }
                    if(String.valueOf(taskContext.getText()).equals("")){
                        taskContext.setHintTextColor(0xffff0000);
                    }
                    if(String.valueOf(taskBeginDate.getText()).equals("")){
                        taskBeginDate.setHintTextColor(0xffff0000);
                    }
                    if(String.valueOf(taskEndDate.getText()).equals("")){
                        taskEndDate.setHintTextColor(0xffff0000);
                    }
                    // Display a popup error
                    // Affiche une popup d'erreur
                    AlertDialog.Builder adb = new AlertDialog.Builder(UpdateTaskActivity.this);
                    adb.setTitle(getResources().getString(R.string.fill_required_fill));
                    adb.setPositiveButton(getResources().getString(R.string.ok), null);
                    adb.show();
                }
            }
        });

        // If the user clicks on the back button then it goes back to the list.
        // Si l'utilisateur appuie sur le bouton de retour alors on retourne à la liste
        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListTaskActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * When we click oon the back button, don't leave the app but go to the listTask activity.
     * Lorsque l'on clique sur le bouton retour, ne quitte pas l'app mais va à l'activité listTask.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ListTaskActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * This method allows to make the translation between the name of the formal name of the task
     * and the name of the state that the user will understand.
     * Cette méthode permet de faire la traduction entre le nom formel de l'état de la tache et le
     * nom de l'état qu'il sera en mesure de comprendre.
     * @param stateSelection The state that the user selected / L'état que l'utilisateur à choisi
     * @return The formal name of the state / Le nom formel de l'état de la tache.
     */
    private String findStateInSpinner(String stateSelection) {
        if(stateSelection.equals(getResources().getString(R.string.todo))){
            return TODO;
        }else if(stateSelection.equals(getResources().getString(R.string.doing))){
            return DOING;
        }else{
            return CLOSED;
        }
    }

    /**
     * This method allows to find the number that correspond to the current state of the task.
     * Cette méthode permet de trouver le numbre qui correspond à l'état de la tache.
     * @param state The state of the task / L'etat de la tache
     * @return A int that carrespond to the state of the task / Un int qui correspond à l'état de la
     *         tache.
     */
    private int findDefaultSpinnerPos(String state){
        if(state.equals(TODO)){
            return 0;
        }else if(state.equals(DOING)){
            return 1;
        }else{
            return 2;
        }
    }
}