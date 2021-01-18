package com.example.gestiondeprojet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import static com.example.gestiondeprojet.Constants.SPLASH_SCREEN_DELAY;

/**
 * This class is link to the Slashscreen activity which displays a waiting page at the beginning of
 * the app.
 * Cette classe est lié à l'activité slashscreen qui permet d'afficher une page d'attente au
 * démarrage de l'app.
 * @author Kierian Tirlemont
 */
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Displays the version of the app
        // Affiche la version de l'activité
        TextView versionDiplay = findViewById(R.id.version);
        versionDiplay.setText("Version "+BuildConfig.VERSION_NAME);

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