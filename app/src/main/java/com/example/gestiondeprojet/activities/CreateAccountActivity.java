package com.example.gestiondeprojet.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.gestiondeprojet.Constants;
import com.example.gestiondeprojet.R;
import com.example.gestiondeprojet.Util;
import com.example.gestiondeprojet.models.User;
import com.example.gestiondeprojet.sql.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.example.gestiondeprojet.Constants.JSON_EXTENSION;
import static com.example.gestiondeprojet.Constants.currentSort;
import static com.example.gestiondeprojet.Constants.currentUsername;

/**
 * This class allows to make all the logic behind the activity that create an account.
 * Cette classe permet de mettre en place toute la logique derrière l'activité de création de compte.
 * @author Kierian Tirlemont
 */
public class CreateAccountActivity extends AppCompatActivity {

    /**
     * The button that allows to validate the creation of the account.
     * Le boutton qui permet de valider la création du compte.
     */
    private Button createButton;

    /**
     * The id that the user entered.
     * L'identifiant que l'utilisateur a entré.
     */
    private EditText id;

    /**
     * The first password that the user entered.
     * Le premier mot de passe que l'utilisateur a entré.
     */
    private EditText firstPassword;

    /**
     * The second password that the user entered.
     * Le second mot de passe que l'utilisateur a entré.
     */
    private EditText secondPassword;

    /**
     * The context of the activity.
     * Le contexte de l'activité.
     */
    private Context context;

    /**
     * The component that allows to go back in the activity.
     * Le composant qui permet de revenir dans les activités.
     */
    private ImageButton backButton;

    /**
     * The attribute that allows to make the link with the database.
     * L'attribut qui permet de faire le lien avec la base de données.
     */
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        databaseHelper = new DatabaseHelper(this);

        // Init attributes
        this.createButton = findViewById(R.id.create_account_button);
        this.id = findViewById(R.id.identifient_create_account);
        this.firstPassword = findViewById(R.id.password_create_account_one);
        this.secondPassword = findViewById(R.id.password_create_account_two);
        this.backButton = findViewById(R.id.create_account_back_button);
        this.context = this;

        // When we click on create account
        // Lorsque l'on clique sur créer le compte
        this.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do the verification with sqlite
                // Fait la vérification avec SQLite

                // If the chosen id is not already used
                // Si l'id choisi n'est pas déjà utilisé
                if(!databaseHelper.checkUser(id.getText().toString())){
                    // If the passwords are the same
                    // Si les mots de passe sont identiques
                    if(String.valueOf(firstPassword.getText()).equals(String.valueOf(secondPassword.getText())) && !String.valueOf(firstPassword.getText()).equals("")){

                        // SQLite Part : Add user
                        // Ajoute l'utilisateur
                        databaseHelper.addUser(new User(id.getText().toString(), firstPassword.getText().toString()));

                        // Create the user' json and put the user value.
                        // Crée le json de l'utilisateur et mets les valeurs utilisateurs
                        JSONObject info = new JSONObject();
                        try {
                            info.put(Constants.ID, String.valueOf(id.getText()));
                            info.put(Constants.PASSWORD, String.valueOf(firstPassword.getText()));
                            info.put(Constants.TASK, new JSONArray());

                            Util.writeJsonFile(id.getText()+ JSON_EXTENSION,info, context);

                            currentUsername = String.valueOf(id.getText()); // Update the current user

                            // Start the next activity
                            // Lance la prochaine activity
                            currentSort = 0; // By default the current sort is 0
                            Intent intent = new Intent(getApplicationContext(), ListTaskActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }else{
                        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(CreateAccountActivity.this);
                        adb.setTitle(getResources().getString(R.string.password_not_same));
                        adb.setPositiveButton(getResources().getString(R.string.ok), null);
                        adb.show();
                        firstPassword.getText().clear();
                        secondPassword.getText().clear();
                    }

                }else{
                    android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(CreateAccountActivity.this);
                    adb.setTitle(getResources().getString(R.string.id_already_used));
                    adb.setPositiveButton(getResources().getString(R.string.ok), null);
                    adb.show();
                    firstPassword.getText().clear();
                    secondPassword.getText().clear();
                    id.getText().clear();
                }
            }
        });

        // If the user clicks on the back button then it goes back to the list.
        // Si l'utilisateur appuie sur le bouton de retour alors on retourne à la liste
        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConnexionActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * When we click oon the back button, don't leave the app but go to the connexion activity.
     * Lorsque l'on clique sur le bouton retour, ne quitte pas l'app mais va à l'activité connexion.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ConnexionActivity.class);
        startActivity(intent);
        finish();
    }
}