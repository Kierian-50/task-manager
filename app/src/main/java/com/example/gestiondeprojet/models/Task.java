package com.example.gestiondeprojet.models;

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
     */
    public Task(int id, String name, String memonic, String description, String duration, String beginDate, String maxEndDate, String context, String projectName) {
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

    public String getDuration() {
        return estimateDuration;
    }

    public void setDuration(String duration) {
        this.estimateDuration = duration;
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
}
