package com.example.gestiondeprojet.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.example.gestiondeprojet.BuildConfig;
import com.example.gestiondeprojet.R;

import java.util.Locale;

import static com.example.gestiondeprojet.Constants.SPLASH_SCREEN_DELAY;
import static java.util.Locale.FRANCE;
import static java.util.Locale.GERMANY;
import static java.util.Locale.UK;

/**
 * This class is link to the Slashscreen activity which displays a waiting page at the beginning of
 * the app.
 * Cette classe est lié à l'activité slashscreen qui permet d'afficher une page d'attente au
 * démarrage de l'app.
 * @author Kierian Tirlemont
 */
public class SplashScreenActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_splash_screen);

        // Init attributes
        this.mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.mEditor = mPreferences.edit();

        // Set the brightness mode according to the preferences
        String mode = this.mPreferences.getString(getString(R.string.brigthness_mode), "LIGHT_MODE");

        if(mode.equals("LIGHT_MODE")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        // Set the language according to the preferences
        String language = this.mPreferences.getString(getString(R.string.language), "FRANCE");

        if(language.equals("FRANCE")){
            Locale.setDefault(FRANCE);
        }else if(language.equals("UK")){
            Locale.setDefault(UK);
        }else{
            Locale.setDefault(GERMANY);
        }
        Configuration config = new Configuration();
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        // Displays the version of the app
        // Affiche la version de l'activité
        TextView versionDiplay = findViewById(R.id.version);
        versionDiplay.setText("Version "+ BuildConfig.VERSION_NAME);

        // Redirect to the connection' page after the time of SPLASH_SCREEN_DELAY
        // Redirige vers la page de connexion après le temps de SPLASH_SCREEN_DELAY
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Connexion.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_DELAY);
    }
}