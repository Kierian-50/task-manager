package com.example.gestiondeprojet.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gestiondeprojet.R;

/**
 * This class allows to display a web page record by the user.
 * Cette classe permet d'afficher une page web enregistré par l'utilisateur.
 * @author Kierian Tirlemont
 */
public class WebViewActivity extends AppCompatActivity {

    /**
     * The component that allows to display the web page.
     * Le composant qui permet d'afficher la page web.
     */
    private WebView webView;

    /**
     * The id of the current task send by the previous activity.
     * L'id de la tâche courrante envoyé par l'activité précédente.
     */
    private int taskId;

    /**
     * The back button on the toolbar.
     * Le boutton de retour sur la toulbar.
     */
    private ImageButton backButton;

    /**
     * The string of the url.
     * Le string de l'url.
     */
    private String urlStr;

    /**
     * The component that allows to display the url.
     * Le composant qui permet d'afficher l'url.
     */
    private TextView urlToDisplay;

    /**
     * The component that allows to display a loading popup.
     * Le composant qui permet d'afficher une page de chargement.
     */
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        // Recovery the params of the previous activity
        // Retrouve les parametres de l'activité précédente
        Bundle bundle = getIntent().getExtras();
        this.taskId = bundle.getInt("taskId", 0);
        this.urlStr = bundle.getString("url", "");

        // Init component
        this.webView = findViewById(R.id.web_page_to_display);
        this.backButton = findViewById(R.id.web_view_back_to_desc);
        this.urlToDisplay = findViewById(R.id.web_view_url_to_display);

        // Load the web page with his name
        // Charge la page web avec son nom
        this.webView.loadUrl(this.urlStr);
        this.urlToDisplay.setText(this.urlStr);

        // Init var
        this.mProgressDialog = new ProgressDialog(this);

        // Improve the quality of the web page
        // Améliore la qualité de la page web
        WebSettings params = this.webView.getSettings();
        params.setJavaScriptEnabled(true);
        params.setBuiltInZoomControls(true);

        // If the user press the back button on the toolbar
        // Si l'utilisateur appuie sur le bouton de retour de la barre de tache.
        this.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), taskDescActivity.class);
                intent.putExtra("taskId", taskId);
                startActivity(intent);
                finish();
            }
        });

        // Management of the error and the load of the web page to avoid that the user feels
        // forgotten
        // Gestion des erreurs et du chargement de la page web pour éviter que l'utilisateur se
        // sentent oublié au chargement ou en cas d'erreur.
        this.webView.setWebViewClient(new WebViewClient() {

            /**
             * In case of an error occurred due to a wrong url, or bad connection, display a popup
             * En cas d'erreur à cause d'une mauvaise url ou d'une mauvaise connexion, affiche une
             * popup.
             * @param view The view / La vue
             * @param errorCode The code of the error / Le code de l'erreur
             * @param description The description
             * @param failingUrl The Url
             */
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                AlertDialog.Builder adb = new AlertDialog.Builder(WebViewActivity.this);
                adb.setTitle(getResources().getString(R.string.loading_error));
                adb.setMessage(getResources().getString(R.string.loading_error_text));
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // At the click go to the previous activity.
                        // Au clique retourne à la page précedente.
                        Intent intent = new Intent(getApplicationContext(), taskDescActivity.class);
                        intent.putExtra("taskId", taskId);
                        startActivity(intent);
                        finish();
                    }});
                adb.show();
            }

            /**
             * At the end of the loading of the web page, remove the loading popup.
             * A la fin du chargement de la page, retire la popup de chargement.
             * @param view The view / La vue
             * @param url The url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                //after loading page, remove loading page
                mProgressDialog.dismiss();
            }

            /**
             * At the moment of the loading of the page, display a loading popup.
             * Au moment du chargement de la page, affiche une popup de chargement.
             * @param view The view / La vue.
             * @param url The url.
             * @param favicon ...
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // on page started, show loading page
                // Au chargement de la page affiche un message d'attente
                mProgressDialog.setCancelable(false);
                mProgressDialog.setMessage(getResources().getString(R.string.loading));
                mProgressDialog.show();

            }
        });
    }

    /**
     * If the user press on the back button of his phone.
     * Si l'utilisateur appuie sur le bouton retour de son téléphone.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), taskDescActivity.class);
        intent.putExtra("taskId", taskId);
        startActivity(intent);
        finish();
    }
}