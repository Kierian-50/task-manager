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

import com.example.gestiondeprojet.R;
import com.example.gestiondeprojet.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import static com.example.gestiondeprojet.Constants.TODO;
import static com.example.gestiondeprojet.Constants.URL;
import static com.example.gestiondeprojet.Constants.currentUsername;

/**
 * This class is link to the task creation by managing the logic behind the creation of a task.
 * Cette classe est lié à la création de tâche en gérant la logique derrière la creation de taches.
 * @author Kierian Tirlemont
 */
public class TaskCreateActivity extends AppCompatActivity {

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
     * The component where the user can enter the context of the task.
     * Le composant ou l'utilisateur peut rentrer le contexte de la tâche.
     */
    private EditText taskContext;

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
     * The button that allows to save the new task.
     * Le boutton qui permet de sauvegarder la nouvelle tâche.
     */
    private Button saveTask;

    /**
     * The context of the activity
     * Le contexte de l'activité.
     */
    private Context context;

    /**
     * The component where the user can enter the project name link to the task. This component
     * allows to displays the name of project that already exists.
     * Le composant ou l'utilisateur peut écrire le nom du projet lié à la tâche. Ce composant
     * permet d'afficher le nom des projets existes déjà.
     */
    private AutoCompleteTextView project;

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
     * The component which allows to go back to the list.
     * Le composant qui permet de revenir sur l'activité d'affichage de la liste.
     */
    private ImageButton backButton;

    /**
     * The component which allows to enter an URL.
     * Le composant qui permet d'écrire l'url lié à une tâche
     */
    private EditText taskUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        // Init attributes
        this.taskName = findViewById(R.id.task_name);
        this.taskDesc = findViewById(R.id.task_desc);
        this.taskContext = findViewById(R.id.task_context);
        this.taskBeginDate = findViewById(R.id.task_begin_date);
        this.taskEndDate = findViewById(R.id.task_end_date);
        this.saveTask = findViewById(R.id.create_task_button);
        this.project = findViewById(R.id.task_project);
        this.backButton = findViewById(R.id.create_back_button);
        this.taskUrl = findViewById(R.id.create_task_url);
        this.context = this;

        this.nbPickerHour = findViewById(R.id.task_duration_hour);
        this.nbPickerMinutes = findViewById(R.id.task_duration_minutes);

        this.nbPickerHour.setMinValue(0);
        this.nbPickerHour.setMaxValue(999);
        this.nbPickerMinutes.setMinValue(0);
        this.nbPickerMinutes.setMaxValue(59);

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
                        TaskCreateActivity.this,
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
                        TaskCreateActivity.this,
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

        // This lines allows to display the project name already saved when the user write it.
        // Ces lignes permettent d'afficher le nom des projets déjà enregistrés quand l'utilisateur
        // ecrit.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.adapter_list_project, R.id.text_completion_project, Util.findProjects(this));
        this.project.setAdapter(adapter);

        // This button' listener allows to save the task in the json file if the data written by the
        // user are correct
        // Le listener de ce boutton permet d'enregistrer la tâche dans le fichier json si les
        // données écrites par l'utilisateur sont correctes.
        this.saveTask.setOnClickListener(new View.OnClickListener() {
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
                            JSONObject task = new JSONObject();
                            task.put(ID, Util.findId(currentUsername+ JSON_EXTENSION, context));
                            task.put(NAME,taskName.getText());
                            task.put(STATE, TODO);
                            task.put(DESCRIPTION, taskDesc.getText());
                            task.put(ESTIMATE_DURATION, nbPickerHour.getValue()+"h"+nbPickerMinutes.getValue()+"m");
                            task.put(BEGIN_DATE, taskBeginDate.getText());
                            task.put(MAX_END_DATE, taskEndDate.getText());
                            task.put(CONTEXT, taskContext.getText());
                            task.put(PROJECT, project.getText());
                            task.put(URL, taskUrl.getText());
                            // The json file
                            JSONObject json = Util.readJsonFile(currentUsername+ JSON_EXTENSION, context);

                            // The task list
                            JSONArray taskList = json.getJSONArray(TASK);
                            taskList.put(task); // Add the task
                            json.put(TASK, taskList);

                            Util.writeJsonFile(currentUsername+ JSON_EXTENSION, json, context);

                            // Return to the list
                            Intent intent = new Intent(getApplicationContext(), ListTaskActivity.class);
                            startActivity(intent);
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        // Display an error message because the url is wrong
                        // Affiche un message d'error car l'url est mauvais
                        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(TaskCreateActivity.this);
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
                    AlertDialog.Builder adb = new AlertDialog.Builder(TaskCreateActivity.this);
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
                    AlertDialog.Builder adb = new AlertDialog.Builder(TaskCreateActivity.this);
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
}