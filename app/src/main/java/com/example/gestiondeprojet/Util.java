package com.example.gestiondeprojet;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.example.gestiondeprojet.models.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;
import static com.example.gestiondeprojet.Constants.ID;
import static com.example.gestiondeprojet.Constants.JSON_EXTENSION;
import static com.example.gestiondeprojet.Constants.TASK;
import static com.example.gestiondeprojet.Constants.currentUsername;

/**
 * This class centralizes method that can be used in differents class.
 * Cette classe centralises des methodes qui peuvent être utilisés dans différentes classes.
 * @author Kierian Tirlemont
 */
public class Util {

    /**
     * This method allows to write a JSONObject in file.
     * Cette méthode permet d'écrireun JSONObject dans un fichier.
     * @param fileName The name of the file / Le nom du fichier.
     * @param content The JSONObject.
     */
    public static void writeJsonFile(String fileName, JSONObject content, Context context) {
        // Convert JsonObject to String Format
        String contentStr = content.toString();
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, MODE_PRIVATE);
            fos.write(contentStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This method allows to read JSONfile and return a jsonobject.
     * Cette méthode permet de lire un fichier json et le retourne sous forme d'un json object.
     * @param fileName The name of the json file / Le nom du fichier json.
     * @return The jsonobject contains in the file / L'objet json qui était dans le fichier.
     */
    public static JSONObject readJsonFile(String fileName, Context context) {
        FileInputStream fis = null;
        String res = null;
        JSONObject json = null;
        try {
            fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;
            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            res = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(res != null){
            try {
                json = new JSONObject(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return json;
    }

    /**
     * Check if the file in parameter exists.
     * Vérifie que le fichier en parametre existe.
     * @param fileName The name of the file / Le nom du fichier
     * @return True : If the file exists / Si le fichier existe
     *         False : If the file does't exist / Si le fichier n'existe pas
     */
    public static boolean fileAlreadyExist(String fileName, Context context){
        File file = new File(context.getFilesDir() + "/"+fileName);
        return file.exists();
    }

    /**
     * This method allows to remove a task in the json file with his position in the list.
     * Cette méthode permet de supprimer une tache se trouvant dans le fichier json grace à sa
     * position dans la liste.
     * @param fileName The name of the json / Le nom du json
     * @param index The position of the task to remove / La position de la tache à supprimer
     * @param context The context of the activity / Le contexte de l'activité.
     */
    public static void removeTask(String fileName, int index, Context context){
        try{
            // The json file
            JSONObject json = Util.readJsonFile(fileName, context); // Recupère le json de l'utilisateur

            // The task list
            JSONArray taskList = json.getJSONArray("Task"); // Recupère ses taches
            taskList.remove(index);

            json.put(TASK, taskList);

            Util.writeJsonFile(fileName, json, context);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method allows to find every project of the current user.
     * Cette méthode permet de trouver tous les projets de l'utilisateur.
     * @param context The context of the activity.
     * @return An arraylist which contains the name of every project.
     */
    public static ArrayList<String> findProjects(Context context){
        ArrayList<String> projects = new ArrayList<>();

        try{
            // The json file
            JSONObject json = Util.readJsonFile(currentUsername+ JSON_EXTENSION, context); // Recupère le json de l'utilisateur

            // The task list
            JSONArray taskList = json.getJSONArray("Task"); // Recupère ses taches

            for (int i = 0; i < taskList.length(); i++) {
                JSONObject task = taskList.getJSONObject(i);
                if(task.has("Project")){
                    String projectName = task.getString("Project");
                    if(!projects.contains(projectName)){
                        projects.add(projectName);
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return projects;
    }

    /**
     * This method allows to find the position of of the task in the json with his id.
     * Cette méthode permet de trouver la position d'une tâche dans le json avec son id.
     * @param id The unique number that reference the task / Le nombre unique qui reference la tâche
     * @param context The context of the activity / Le contexte  des activité.
     * @return The position in the json / La position dans le json.
     */
    public static int findPositionWithId(int id, Context context){
        try{
            // The json file
            JSONObject json = Util.readJsonFile(currentUsername+ JSON_EXTENSION, context); // Recupère le json de l'utilisateur

            // The task list
            JSONArray taskList = json.getJSONArray("Task"); // Recupère ses taches

            for (int i = 0; i < taskList.length(); i++) {
                JSONObject task = taskList.getJSONObject(i);
                if(task.get(ID).equals(id)){
                    return i;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * This method allows to find the next free id by finding the highest id and add one.
     * @param fileName The name of the json file / Le nom du fichier json.
     * @param context The context of the activity.
     * @return The next free id / Le prochain id libre.
     */
    public static int findId(String fileName, Context context){
        int maxId = 0;
        try{
            // The json file
            JSONObject json = Util.readJsonFile(fileName, context); // Recupère le json de l'utilisateur
            // The task list
            JSONArray taskList = json.getJSONArray(TASK); // Recupère ses taches
            maxId = 0;

            for (int i = 0; i < taskList.length(); i++) {
                JSONObject task = taskList.getJSONObject(i);
                int value = task.getInt(ID);
                if(value >= maxId){
                    maxId = value;
                }
            }
            maxId++;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return maxId;
    }

    /**
     * This method allows to set an unique color for each project past in paramter and put it in an
     * hashmap.
     * Cette méthode permet de lister une unique couleur pour chaque projet passé en paramètre et
     * les enregistre dans un hashmap.
     * @param tasks The list of tasks which contains the tasks /
     *              La liste de tâche contenant les projets.
     * @return A hashmap which contains the project link with his color / Un hashmap contenant son
     *         projet en lien avec sa couleur.
     */
    public static HashMap<String, Integer> findColorByProject(List<Task> tasks){
        HashMap<String, Integer> hm = new HashMap<>();
        for(Task t : tasks){
            if(!hm.containsKey(t.getProjectName())){
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                hm.put(t.getProjectName(), color);
            }
        }
        return hm;
    }

    /**
     * This method allows to check is the url is correct.
     * Cette méthode permet de vérifier que l'url est correct.
     * @param urlStr The url written by the user / L'url écrit par l'utilisateur.
     * @return True : If the url is correct / Si l'url est correct
     *         False : If the url is not correct / Si l'url n'est pas correct.
     */
    public static boolean isUrl(String urlStr){
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        try {
            Pattern patt = Pattern.compile(regex);
            Matcher matcher = patt.matcher(urlStr);
            return matcher.matches();
        } catch (RuntimeException e) {
            return false;
        }
    }
}
