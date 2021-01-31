package com.example.gestiondeprojet.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gestiondeprojet.models.User;

/**
 * The class that allows to make the link with the database.
 * La classe qui permet de faire le lien avec la base de données.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * The version of the data base.
     * La version de la base de données
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * The name of the database.
     * Le nom de la base de données.
     */
    private static final String DATABASE_NAME = "UserManager.db";

    /**
     * The name of the table that contains the user.
     * Le nom de la table qui contient l'utilisateur.
     */
    private static final String TABLE_USER = "user";

    /**
     * The name of the column that contains the username.
     * Le nom de la colonne qui contient le nom de l'utilisateur.
     */
    private static final String COLUMN_USER_NAME = "user_name";

    /**
     * The name of the column that contains the password.
     * Le nom de la colonne qui contient le mot de passe.
     */
    private static final String COLUMN_USER_PASSWORD = "user_password";

    /**
     * The sql request that allows to create the table.
     * La requete sql qui permet de creer la table.
     */
    private final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_NAME + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    /**
     * The sql request that allows to delete the table.
     * La requete sql qui permet de supprimer la table.
     */
    private final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    /**
     * Call the superclass
     * Appelle la super classe.
     * @param context The context of the activity / Le contexte de l'activité.
     */
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * At the creation of the database
     * A la creation de la base de données
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    /**
     * At the changement of the version of the database.
     * Au changement de version de la base de données (Pas très optimisé)
     * @param db The new database / La nouvelle base de donées.
     * @param oldVersion The old version of the database / L'ancienne version.
     * @param newVersion The new version of the database / La nouvelle version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }

    /**
     * This method allows to create a user in the data base.
     * Cette méthode permet de creer un utilisateur dans la base de données.
     * @param user The user to add in the database / L'utilisateur à mettre en base de données.
     */
    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /**
     * This method allows to check if the id of a user is in the database.
     * Cette method permet de vérifier si l'identifiant de l'utilisateur est en base de données.
     * @param id The id to check / L'identifiant à verifier.
     * @return True : If the id is already in database / Si l'id de utilisateur est déjà en base.
     *         False : If the id isn't in database / Si l'id est libre.
     */
    public boolean checkUser(String id){
        String[] columns = {
                COLUMN_USER_NAME
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_NAME + " = ?";
        String[] selectionArgs = { id };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }

    /**
     * This method allows to check if the id and the password of a user are in the database.
     * Cette method permet de vérifier si l'identifiant et le mot de passe de l'utilisateur sont
     * en base de données.
     * @param id The id to check / L'identifiant à verifier.
     * @param password The password to check / Le mot de passe à verifier.
     * @return True : If the password and id are ok / Si l'id et le mot de passe sont corrects
     *         False : If the id and the password aren't ok / Si l'id et le mot de passe ne sont pas
     *         corrects.
     */
    public boolean checkUser(String id, String password){
        String[] columns = {
                COLUMN_USER_NAME
        };
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " =?";
        String[] selectionArgs = { id, password };

        Cursor cursor = db.query(TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0){
            return true;
        }
        return false;
    }
}
