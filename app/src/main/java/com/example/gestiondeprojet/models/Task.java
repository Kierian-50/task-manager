package com.example.gestiondeprojet.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class represents a task.
 * Cette classe represente une tache.
 * @author Kierian Tirlemont
 */
public class Task {

    /**
     * The unique id of the task.
     * L'id de la tache qui est unique.
     */
    private int id;

    /**
     * The name of the task.
     * Le nom de la tache.
     */
    private String name;

    /**
     * Memonic which represents the image that represents the state of the task.
     * Memonic qui represents l'image qui represente l'état de la tache.
     */
    private String memonic;

    /**
     * The description of the task.
     * La description de la tache.
     */
    private String description;

    /**
     * The estimated of the task.
     * La durée de la tache.
     */
    private String estimateDuration;

    /**
     * The begin date of the task.
     * La date de début de tache.
     */
    private String beginDate;

    /**
     * The max end date of the task.
     * La date maximum de fin de tache.
     */
    private String maxEndDate;

    /**
     * The state of the task.
     * L'état de la tache.
     */
    private State stateTask;

    /**
     * The context of the task.
     * Le contexte de la tache.
     */
    private String context;

    /**
     * The name of the project which is link to the task.
     * Le nom du projet qui est lié avec la tache.
     */
    private String projectName;

    /**
     * An url link to the task.
     * Une url liée à une tâche.
     */
    private String url;

    /**
     * The constructor of the task object / Le constructeur de l'objet task
     * @param id The id of the task
     * @param name The name of the task
     * @param memonic The memonic of the task which is link to an image.
     * @param description The description of the task.
     * @param duration The estimate duration of the task.
     * @param beginDate The begin date of the task
     * @param maxEndDate The max end date of the task.
     * @param context The context of the task.
     * @param projectName The name of the project which is link to the task.
     * @param url An url link to the task / Une url liée à la tâche.
     */
    public Task(int id, String name, String memonic, String description, String duration, String beginDate, String maxEndDate, String context, String projectName, String url) {
        this.name = name;
        this.description = description;
        this.memonic = memonic;
        this.estimateDuration = duration;
        this.beginDate = beginDate;
        this.maxEndDate = maxEndDate;
        this.id = id;
        this.stateTask = new State();
        this.stateTask.changeState(memonic);
        this.context = context;
        this.projectName = projectName;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemonic() {
        return memonic;
    }

    public void setMemonic(String memonic) {
        this.memonic = memonic;
    }

    public State getStateTask() {
        return stateTask;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getEstimateDuration() {
        return estimateDuration;
    }

    public void setEstimateDuration(String estimateDuration) {
        this.estimateDuration = estimateDuration;
    }

    public String getMaxEndDate() {
        return maxEndDate;
    }

    public void setMaxEndDate(String maxEndDate) {
        this.maxEndDate = maxEndDate;
    }

    public void setStateTask(State stateTask) {
        this.stateTask = stateTask;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * This method allows to compare the name of two task
     * Cette méthode permet de comparer le nom de deux tâches.
     * @param other The other task / L'autre tache
     * @return A number that represent the comparaison of the two string
     *         Un nombre qui represente la comparaison des deux string
     */
    public int compareByName(Task other){
        return name.compareTo(other.getName());
    }

    /**
     * This method allows to compare the two dates
     * Cette méthode permet de comparer les deux dates
     * @param other The other task / L'autre tache
     * @return A number that represents the comparison of the two dates
     *         Un nombre qui represente la comparison des deux dates
     */
    public int compareByDate(Task other){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int res = 0;
        try {
            // Parse in Date object to compare it.
            Date dateBegin = sdf.parse(maxEndDate.toString());
            Date dateEnd = sdf.parse(other.maxEndDate.toString());

            res = dateBegin.compareTo(dateEnd);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return res;
    }

    /**
     * This method allows to compare the two states
     * Cette méthode permet de comparer les deux états
     * @param other The other task / L'autre tache
     * @return A number that represents the comparison of the two states
     *         Un nombre qui represente la comparison des deux states
     */
    public int compareByState(Task other){
        return stateTask.compareTo(other.getStateTask());
    }

    /**
     * This method allows to compare the two estimated durations
     * Cette méthode permet de comparer les deux durée estimées
     * @param other The other task / L'autre tache
     * @return A number that represents the comparison of the two states
     *         Un nombre qui represente la comparison des deux états
     */
    public int compareByDuration(Task other){
        String[] values = estimateDuration.split("h");
        Integer currentDuration = Integer.parseInt(values[0])*60+Integer.parseInt(values[1].split("m")[0]);

        values = other.getEstimateDuration().split("h");
        Integer otherDuration = Integer.parseInt(values[0])*60+Integer.parseInt(values[1].split("m")[0]);

        return currentDuration.compareTo(otherDuration);
    }
}
