package com.example.gestiondeprojet.models;

import static com.example.gestiondeprojet.Constants.CLOSED;
import static com.example.gestiondeprojet.Constants.DOING;
import static com.example.gestiondeprojet.Constants.TODO;

/**
 * This object represents the state of the project.
 * It can be to do, doing, and closed but it can be only one of the three.
 * Cette objet represente l'état d'avancement du projet.
 * Ca peut etre à faire, en cours, et fermé mais ca peut seulement etre un des trois.
 * @author Kierian Tirlemont
 */
public class State {

    /**
     * Boolean which say if the state of the task is to do.
     * Boolean qui dit si l'état est à faire.
     */
    private boolean toDo;

    /**
     * Boolean which say if the state of the task is doing.
     * Boolean qui dit si l'état est en cours.
     */
    private boolean doing;

    /**
     * Boolean which say if the state of the task is closed.
     * Boolean qui dit si l'état est fermé.
     */
    private boolean closed;

    /**
     * Initialise the attributes.
     */
    public State() {
        this.toDo = true;
        this.closed = false;
        this.doing = false;
    }

    /**
     * This method allows to change teh state of the task.
     * Cette méthode permet de changer l'état de la tache
     * @param newState The new state of the task / Le nouvel état de la tache
     */
    public void changeState(String newState){
        if(newState.equals("toDo")){
            this.toDo = true;
            this.doing = false;
            this.closed = false;
        }else if(newState.equals("doing")){
            this.toDo = false;
            this.doing = true;
            this.closed = false;
        }else if(newState.equals("closed")){
            this.toDo = false;
            this.doing = false;
            this.closed = true;
        }
    }

    /**
     * This method allows to return the string state of the object.
     * Cette méthode retourne l'état de l'objet dans un string.
     * @return A string which represent the state of the object / Un string qui contient l'état
     *         de l'objet.
     */
    public String getStatue(){
        if(toDo){
            return TODO;
        }else if(doing){
            return DOING;
        }else{
            return CLOSED;
        }
    }
}
