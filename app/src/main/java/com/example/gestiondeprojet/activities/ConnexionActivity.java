package com.example.gestiondeprojet.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.gestiondeprojet.R;
import com.example.gestiondeprojet.sql.DatabaseHelper;

import java.util.Locale;

import static com.example.gestiondeprojet.Constants.AVAILABLE_LANGUAGE;
import static com.example.gestiondeprojet.Constants.currentSort;
import static com.example.gestiondeprojet.Constants.currentUsername;
import static java.util.Locale.FRANCE;
import static java.util.Locale.GERMANY;
import static java.util.Locale.UK;

/**
 * This class allows to make all the logic behind the connection' activity
 * Cette classe permet de mettre en place toute la logique derrière l'activité de connexion.
 * @author Kierian Tirlemont
 */
public class ConnexionActivity extends AppCompatActivity {

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

    /**
     * The component that allows to change the language of the app.
     * Le composant qui permet de changer la langue de l'application.
     */
    private ImageButton translationButton;

    /**
     * The component that allows to change the brightness mode
     * Le composant qui permet de changer le mode de luminosité
     */
    private ImageButton colorMode;

    /**
     * The component that allows to say if the user want to save the id and password.
     * Le composant qui permet de dire si l'utilisateur veut enregistrer son id et son mdp.
     */
    private CheckBox rememberMeCheckBox;

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

    /**
     * The attribute that allows to make the link with the database.
     * L'attribut qui permet de faire le lien avec la base de données.
     */
    private DatabaseHelper databaseHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        databaseHelper = new DatabaseHelper(this);

        // Init attributes
        this.createAccount = findViewById(R.id.create_account_button);
        this.connection = findViewById(R.id.connection_button);
        this.id = findViewById(R.id.identifient);
        this.password = findViewById(R.id.password);
        this.translationButton = findViewById(R.id.connection_translation);
        this.colorMode = findViewById(R.id.connection_brightness_mode);
        this.rememberMeCheckBox = findViewById(R.id.remember_me);
        this.context = this;
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(this); // MODE_PRIVATE : YES
        this.mEditor = mPreferences.edit();

        // Configure the text edit according to the preferences
        checkSharedPreferences();

        // Go to the create account' activity
        // Va à la page de création de compte
        this.createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
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
                if (databaseHelper.checkUser(id.getText().toString().trim(),
                        password.getText().toString().trim())) {
                    // Entry point of the next activity
                    // Point d'entrée de la prochaine activité
                    savePreferences();
                    currentUsername = String.valueOf(id.getText());
                    currentSort = 0; // By default the current sort is 0
                    Intent intent = new Intent(getApplicationContext(), ListTaskActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    if(databaseHelper.checkUser(id.getText().toString())){
                        password.setText("");
                        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(ConnexionActivity.this);
                        adb.setTitle(getResources().getString(R.string.password_incorrect));
                        adb.setPositiveButton(getResources().getString(R.string.ok), null);
                        adb.show();
                    }else{
                        id.setText("");
                        password.setText("");
                        android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(ConnexionActivity.this);
                        adb.setTitle(getResources().getString(R.string.id_incorrect));
                        adb.setPositiveButton(getResources().getString(R.string.ok), null);
                        adb.show();
                    }
                }
            }
        });

        // If the user click on the translation button, display  a popup with the languages
        // Si l'utilisateur clique sur le boutton de traduction, affiche une popup avec les langues
        this.translationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ConnexionActivity.this);
                mBuilder.setTitle(getResources().getString(R.string.choose_language));
                mBuilder.setSingleChoiceItems(AVAILABLE_LANGUAGE, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            Locale.setDefault(FRANCE);
                            saveLanguage("FRANCE");
                            recreate();
                        }else if(which==1){
                            Locale.setDefault(UK);
                            saveLanguage("UK");
                            recreate();
                        }else if(which==2){
                            Locale.setDefault(GERMANY);
                            saveLanguage("GERMANY");
                            recreate();
                        }
                        Configuration config = new Configuration();
                        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                        dialog.dismiss();
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        // When the user click on the brightness button it turns to dark or light mode according to
        // the current mode
        // Quand l'utilisateur clique sur le boutton de luminotsité, ca change pour le dark mode
        // ou le mode jour selon le mode
        this.colorMode.setOnClickListener(new View.OnClickListener() {
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
                        Configuration config = new Configuration();
                        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                        break;

                    case Configuration.UI_MODE_NIGHT_NO:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        saveMode("DARK_MODE");
                        Locale.setDefault(findLanguagePreferences());
                        Configuration config1 = new Configuration();
                        getBaseContext().getResources().updateConfiguration(config1, getBaseContext().getResources().getDisplayMetrics());
                        break;

                    case Configuration.UI_MODE_NIGHT_UNDEFINED:
                        String text = getResources().getString(R.string.brithness_mode_impossible);
                        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                        toast.show();
                        break;
                }
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
     * This method allows to save the preferences for the id and password.
     * Cette méthode permet d'enregistrer les préférences pour l'id et le mdp.
     */
    private void savePreferences() {
        if(this.rememberMeCheckBox.isChecked()){
            mEditor.putString(getString(R.string.checkbok_remember_me), "True");
            mEditor.commit();

            String name = this.id.getText().toString();
            mEditor.putString(getString(R.string.username), name);
            mEditor.commit();

            String passwordStr = this.password.getText().toString();
            mEditor.putString(getString(R.string.password), passwordStr);
            mEditor.commit();
        }else{
            mEditor.putString(getString(R.string.checkbok_remember_me), "False");
            mEditor.commit();

            mEditor.putString(getString(R.string.username), "");
            mEditor.commit();

            mEditor.putString(getString(R.string.password), "");
            mEditor.commit();
        }
    }

    /**
     * This method allows to use the save preferences and displays this preferences
     * Cette methode permet d'utiliser les préférences enregistrées et de les afficher.
     */
    private void checkSharedPreferences() {
        String checkbox = this.mPreferences.getString(getString(R.string.checkbok_remember_me), "False");
        String name = this.mPreferences.getString(getString(R.string.username), "");
        String password = this.mPreferences.getString(getString(R.string.password), "");

        this.id.setText(name);
        this.password.setText(password);

        if(checkbox.equals("True")){
            this.rememberMeCheckBox.setChecked(true);
        }else{
            this.rememberMeCheckBox.setChecked(false);
        }
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

    /**
     * This method allows to find the language in the preferences and return the string variable
     * associated.
     * Cette methode permet de trouver la langue dans les preferences de l'application et retourne
     * le string associé associée.
     * @return The string that correspond to the language / La string qui correspond à la langue.
     */
    private String findLanguagePreferencesStr(){
        String language = this.mPreferences.getString(getString(R.string.language), "FRANCE");
        if(language.equals("FRANCE")){
            return FRANCE.toString();
        }else if(language.equals("UK")){
            return UK.toString();
        }else{
            return GERMANY.toString();
        }
    }
}