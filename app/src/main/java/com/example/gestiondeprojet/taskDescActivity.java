package com.example.gestiondeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.gestiondeprojet.Constants.DOING;
import static com.example.gestiondeprojet.Constants.JSON_EXTENSION;
import static com.example.gestiondeprojet.Constants.TODO;
import static com.example.gestiondeprojet.Constants.currentIdTask;
import static com.example.gestiondeprojet.Constants.currentUsername;
import static com.example.gestiondeprojet.Util.findPositionWithId;

/**
 * This class is link to the activity which displays the task and his values and manage the
 * logic behind.
 * Cette classe est lié à l'activité d'affichage des valeurs de la tâche et gère la logique
 * derrière.
 */
public class taskDescActivity extends AppCompatActivity {

    /**
     * The component where the user can go back on the list task activity.
     * Le composant où l'utilisateur peut retourner à l'activité de listage des tâches.
     */
    private ImageButton backButton;

    /**
     * The component where the user can click to update the task.
     * Le composant où l'utilisateur peut cliquer pour mettre à jour la tâche.
     */
    private Button updateButton;

    /**
     * The component where the user can click to remove the task.
     * Le composant où l'utilisateur peut cliquer pour supprimer la tâche.
     */
    private ImageButton deleteButton;

    /**
     * The component where the user can see the name of the task.
     * Le composant où l'utilisateur peut voir le nom de la tâche.
     */
    private TextView taskName;

    /**
     * The component where the user can see the description of the task.
     * Le composant où l'utilisateur peut voir la description de la tâche.
     */
    private TextView taskDesc;

    /**
     * The component where the user can see the estimate duration of the task.
     * Le composant où l'utilisateur peut voir la durée estimée de la tâche.
     */
    private TextView taskDuration;

    /**
     * The component where the user can see the begin date and the end max date of the task.
     * Le composant où l'utilisateur peut voir la date de début et de fin de la tâche de la tâche.
     */
    private TextView taskDate;

    /**
     * The component where the user can see the context of the task.
     * Le composant où l'utilisateur peut voir le contexte de la tâche.
     */
    private TextView taskContext;

    /**
     * The component where the user can see the project name link with the task.
     * Le composant où l'utilisateur peut voir le nom du projet lié à la tâche.
     */
    private TextView taskProjectName;

    /**
     * The component where the user can see the state of the task.
     * Le composant où l'utilisateur peut voir l'état d'avancement de la tâche.
     */
    private TextView taskState;

    /**
     * The context of the activity
     * Le contexte de l'activité
     */
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_a_task);

        // Init attributes of the toolbar
        this.context = this;
        this.backButton = findViewById(R.id.display_back_to_list);
        this.updateButton = findViewById(R.id.display_update_task);
        this.deleteButton = findViewById(R.id.display_delete_task);

        // Go to the list task activity on click
        // Au click de l'utilisateur, retourne sur l'activité de listage des tâches.
        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListTask.class);
                startActivity(intent);
                finish();
            }
        });

        // Go to the update task activity on click
        // Au click de l'utilisateur, va sur l'activité de mise à jour des tâches.
        this.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdateTask.class);
                startActivity(intent);
                finish();
            }
        });

        // Remove the task on click
        // Supprime la tâche au click de l'utilisateur
        this.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.removeTask(currentUsername+JSON_EXTENSION, findPositionWithId(currentIdTask, context), context);
                Intent intent = new Intent(getApplicationContext(), ListTask.class);
                startActivity(intent);
                finish();
            }
        });

        // Init attributes of the main content of the page
        this.taskName = findViewById(R.id.display_task_name);
        this.taskDesc = findViewById(R.id.display_task_desc);
        this.taskDuration = findViewById(R.id.display_task_duration);
        this.taskDate = findViewById(R.id.display_task_begin_date);
        this.taskContext = findViewById(R.id.display_task_context);
        this.taskProjectName = findViewById(R.id.display_task_project);
        this.taskState = findViewById(R.id.display_task_state);


        // Recover the content of the json
        // Recupère le contenu du json
        JSONObject json = Util.readJsonFile(currentUsername+ JSON_EXTENSION, context);

        JSONArray taskArray = null;
        JSONObject currentTask = null;
        try {
            // Find the tasks / Trouve les tâches
            taskArray = json.getJSONArray("Task");
            // Find the task that the user wants to display
            // Trouve la tâche que l'utilisateur veux voir
            currentTask = taskArray.getJSONObject(findPositionWithId(currentIdTask, this));

            // Display the values of the task in the components
            // Affiche les valeurs de la tâche dans les composants
            this.taskName.setText(currentTask.getString("Name"));
            this.taskDesc.setText(getResources().getString(R.string.description)+" : "+currentTask.getString("Description"));
            this.taskDate.setText("🕒 : "+currentTask.getString("BeginDate") +"=>"+currentTask.getString("MaxEndDate"));
            this.taskContext.setText(getResources().getString(R.string.context)+" : "+currentTask.getString("Context"));
            this.taskState.setText(translateState(currentTask.getString("State")));
            this.taskDuration.setText(getResources().getString(R.string.estimated_duration)+" : "+currentTask.getString("EstimateDuration"));

            // Display the name of the project if it exists
            // Affiche le nom du projet s'il y en a un
            if(currentTask.getString("Project").toString().equals("")){
                this.taskProjectName.setText(getResources().getString(R.string.no_project_found));
            }else{
                this.taskProjectName.setText(getResources().getString(R.string.project_found)+" : "+currentTask.getString("Project"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        // Check the dates
        SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();

        // Change the color of the date in red if the date is past and green else
        // Change la couleur de la date en rouge si la date est passé et en vert sinon
        try {
            Date endDate = newFormat.parse(currentTask.getString("MaxEndDate"));
            if(currentDate.after(endDate)) { // Change la couleur de la date si la date de fin de tache est passé
                taskDate.setTextColor(0xffff0000);
            }else{
                taskDate.setTextColor(0xff008000);
            }
        } catch (ParseException | JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * When we click oon the back button, don't leave the app but go to the listTask activity.
     * Lorsque l'on clique sur le bouton retour, ne quitte pas l'app mais va à l'activité listTask.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ListTask.class);
        startActivity(intent);
        finish();
    }

    /**
     * This method allows to translate the formal name of the state by a name that can be understood
     * by the user.
     * Cette méthode permet de traduire l'état formel de la tache par un nom qui peut etre mieux
     * compris par l'utilisateur.
     * @param state The formel name of the state of the task / Le nom formel de l'état de la tâche.
     * @return The name understandable for the user that represents the state
     *         Le nom compréhensible pour l'utilisateur de l'état de la tâche.
     */
    private String translateState(String state){
        if(state.equals(TODO)){
            return getResources().getString(R.string.todo);
        }else if(state.equals(DOING)){
            return getResources().getString(R.string.doing);
        }else{
            return getResources().getString(R.string.finish);
        }
    }
}