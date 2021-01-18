package com.example.gestiondeprojet;

import java.util.ArrayList;
import java.util.Arrays;

public final class Constants {

    public static final int SPLASH_SCREEN_DELAY = 3000;
    public static final String DEBUGG = "DEBUGG";
    public static final String ID = "id";
    public static final String PASSWORD = "Password";
    public static final String TASK = "Task";
    public static final String TODO = "todo";
    public static final String DOING = "doing";
    public static final String CLOSED = "closed";
    public static final String JSON_EXTENSION = ".json";
    public static final ArrayList<String> POSSIBLE_TASK_STATE = new ArrayList<>(Arrays.asList("A faire", "En cours", "Fait"));
    public static final int NB_MAX_LETTER_IN_DESC = 35;
    public static final String NAME = "Name";
    public static final String STATE = "State";
    public static final String DESCRIPTION ="Description";
    public static final String ESTIMATE_DURATION ="EstimateDuration";
    public static final String BEGIN_DATE ="BeginDate";
    public static final String MAX_END_DATE ="MaxEndDate";
    public static final String CONTEXT = "Context";
    public static final String PROJECT ="Project";

    public static String currentUsername;
    public static int currentIndexTask;

}
