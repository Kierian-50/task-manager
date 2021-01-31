package com.example.gestiondeprojet.models;

import java.util.ArrayList;

/**
 * This object represents a user with a id, a password and a list of task.
 * Cette objet represente un utilisateur avec un identifiant, un mot de passe et une liste de taches.
 * @author Kierian Tirlemont
 */
public class User {

    /**
     * The id/username of the user
     * Le nom d'utilisateur
     */
    private String name;

    /**
     * The password of the user
     * Le mot de passe de l'utilisateur
     */
    private String password;

    /**
     * The lists of tasks of the user
     * La liste de taches de l'utilisateur
     */
    private ArrayList<Task> tasks;

    /**
     * Init attributes.
     * @param name The name of the user.
     * @param password The password of the user.
     * @param tasks The tasks of the user.
     */
    public User(String name, String password, ArrayList<Task> tasks) {
        this.name = name;
        this.password = password;
        this.tasks = tasks;
    }

    public User(String name, String password){
        this.name = name;
        this.password = password;
        this.tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
}
