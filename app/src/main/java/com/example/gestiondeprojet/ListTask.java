package com.example.gestiondeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.gestiondeprojet.adapter.TaskAdapter;
import com.example.gestiondeprojet.models.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.gestiondeprojet.Constants.BEGIN_DATE;
import static com.example.gestiondeprojet.Constants.CONTEXT;
import static com.example.gestiondeprojet.Constants.DEBUGG;
import static com.example.gestiondeprojet.Constants.DESCRIPTION;
import static com.example.gestiondeprojet.Constants.ESTIMATE_DURATION;
import static com.example.gestiondeprojet.Constants.ID;
import static com.example.gestiondeprojet.Constants.JSON_EXTENSION;
import static com.example.gestiondeprojet.Constants.MAX_END_DATE;
import static com.example.gestiondeprojet.Constants.NAME;
import static com.example.gestiondeprojet.Constants.PROJECT;
import static com.example.gestiondeprojet.Constants.STATE;
import static com.example.gestiondeprojet.Constants.TASK;
import static com.example.gestiondeprojet.Constants.currentUsername;
import static com.example.gestiondeprojet.Constants.currentIndexTask;

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
     * This imageButton displays a popup that indicates the user on how to delete a task.
     * Cette imageButton affiche une popup qui indique comment supprimer une tâche.
     */
    private ImageButton removeTask;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_task);

        // Init var
        this.context = this;
        this.addTask = findViewById(R.id.add_task);
        this.removeTask = findViewById(R.id.remove_task);
        this.listView = findViewById(R.id.shop_list_view);
        this.searchIcon = findViewById(R.id.search_icon);
        this.searchContent = findViewById(R.id.search_content);

        // List that contains the task which are in the json.
        // Liste qui contient les taches contenues dans le json.
        this.taskList = new ArrayList<>();

        JSONObject json = Util.readJsonFile(currentUsername+ JSON_EXTENSION, context);
        Log.e(DEBUGG, currentUsername+ JSON_EXTENSION +" json content :"+json.toString());
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
                //    Task(int id, String name, String memonic, String description, String duration, Date beginDate, Date maxEndDate, String context)
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

        // If the user click on the remove button it displays a popup that helps the user on the
        // deletion of tasks.
        // Si l'utilisateur clique sur le boutton de suppression une popup s'affiche qui aide
        // l'utilisateur sur la suppression de tâche.
        this.removeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(ListTask.this);
                adb.setTitle("Suppression de tache :");
                adb.setMessage("Pour supprimer une tache, il vous suffit de cliquer sur la tache et " +
                        "dans la page affichant les attributs de la tache, il faut cliquer sur la " +
                        "corbeille ou vous pouvez rester appuyer sur une tache pendant une seconde " +
                        "et une fênetre va vous inviter à supprimer la tache");
                adb.setPositiveButton("Ok", null);
                adb.show();
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
                adb.setTitle("Que voulez vous faire avec la tache ?");
                final int positionToModify = position;
                adb.setNeutralButton("Annuler", null);
                adb.setPositiveButton("Mettre à jour", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // If he wants to update go to the update' activity
                        // S'il veut mettre à jour alors la page de mise à jour va s'afficher.
                        currentIndexTask = positionToModify; // Set the index of the task (not the id)
                        Intent createTask = new Intent(getApplicationContext(), UpdateTask.class);
                        startActivity(createTask);
                        finish();
                    }});
                adb.setNegativeButton("Supprimer", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove the task, if he clicks on delete
                        // Supprime la tache si l'utilisateur clique sur le bouton.
                        taskList.remove(positionToModify);
                        taskAdapter.notifyDataSetChanged();
                        Util.removeTask(currentUsername+ JSON_EXTENSION, positionToModify, context);
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
                currentIndexTask = position;
                Intent createTask = new Intent(getApplicationContext(), taskDescActivity.class);
                startActivity(createTask);
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
        // Quand l'utilisateur écrit, ça affiche que les tâches qui ont le même nom
        this.searchContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                taskAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}