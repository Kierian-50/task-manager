package com.example.gestiondeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.gestiondeprojet.Constants.DEBUGG;
import static com.example.gestiondeprojet.Constants.currentSort;
import static com.example.gestiondeprojet.Constants.currentUsername;

/**
 * This class allows to make all the logic behind the connection' activity
 * Cette classe permet de mettre en place toute la logique derrière l'activité de connexion.
 * @author Kierian Tirlemont
 */
public class Connexion extends AppCompatActivity {

    /**
     * The button that returns the activity that allows to create an account.
     * Le boutton qui retourne l'activité qui permet de creer un compte
     */
    private Button createAccount;

    /**
     * The button that allows to log the account and go to the task page.
     * Le boutton qui permet de se connecter et d'aller vers l'activité task page.
     */
    private Button connection;

    /**
     * The id that the user entered.
     * L'identifiant que l'utilisateur a entré.
     */
    private EditText id;

    /**
     * The password that the user entered.
     * Le mot de passe que l'utilisateur a entré.
     */
    private EditText password;

    /**
     * The context of activity.
     * Le contexte de l'activité.
     */
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        // Init attributes
        this.createAccount = findViewById(R.id.create_account_button);
        this.connection = findViewById(R.id.connection_button);
        this.id = findViewById(R.id.identifient);
        this.password = findViewById(R.id.password);
        this.context = this;

        // Go to the create account' activity
        // Va à la page de création de compte
        this.createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateAccount.class);
                startActivity(intent);
                finish();
            }
        });

        // Try to connect the user and go to the ListTask' activity if the login are ok
        // Essaye de connecter l'utilisateur, si l'identification est correct, va à la page listant
        // les taches.
        this.connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the user' json exist then the user has an account
                // Si le json de l'utilisateur existe alors l'utilisateur a un compte
                if(Util.fileAlreadyExist(id.getText()+".json", context)){
                    Log.e(DEBUGG, "User file "+id.getText()+".json exist !");

                    // Lis le json / Read the json
                    JSONObject userJson = Util.readJsonFile(id.getText()+".json", context);
                    String passwordStr = null;
                    try {
                        // Trouve le password dans le json
                        passwordStr = userJson.getString("Password");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Check the password
                    if(passwordStr == null || !passwordStr.equals(String.valueOf(password.getText()))){
                        password.setText("");
                        String text = "Le mot de passe n'est pas correct ! ";
                        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                        toast.show();
                    }else{
                        // Entry point of the next activity
                        // Point d'entrée de la prochaine activité
                        currentUsername = String.valueOf(id.getText());
                        currentSort = 0; // By default the current sort is 0
                        Intent intent = new Intent(getApplicationContext(), ListTask.class);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    // Si l'utilisateur n'est pas trouvé
                    // If the user isn't found
                    Log.e(DEBUGG, "User file "+id.getText()+".json doesn't exist !");
                    id.setText("");
                    password.setText("");
                    String text = "L'identifiant n'est pas dans nos bases de données ! ";
                    Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}