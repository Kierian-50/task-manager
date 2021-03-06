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
        switch (newState) {
            case "toDo":
                this.toDo = true;
                this.doing = false;
                this.closed = false;
                break;
            case "doing":
                this.toDo = false;
                this.doing = true;
                this.closed = false;
                break;
            case "closed":
                this.toDo = false;
                this.doing = false;
                this.closed = true;
                break;
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

    /**
     * This method allows to compare two states and returns an int that represents the comparison.
     * Cette méthode permet de comparer les deux états et retourne un entier qui represente la
     * comparaison.
     * @param other The other task / L'autre tâche
     * @return An int that represents the comparison.
     *         Un entier qui represente la comparaison.
     *         Return 0 = two object equals
     *         Return 1 = current object must be first
     *         Return -1 = the object in param must be first
     */
    public int compareTo(State other){
        if(getStatue().equalsIgnoreCase(other.getStatue())){
            return 0;
        }else{
            if((getStatue().equalsIgnoreCase(TODO) || getStatue().equalsIgnoreCase(DOING)) &&
                    (other.getStatue().equalsIgnoreCase(DOING) || other.getStatue().equalsIgnoreCase(CLOSED))){
                return 1;
            }else{
                return -1;
            }
        }
    }
}
