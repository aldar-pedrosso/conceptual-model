package statics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import objects.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "WeStudy";
    private static final int DATABASE_VERSION = 5;

    private static DatabaseHelper ourInstance = null;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Set up instance for static usage.
     * @param context basic context -> getBaseContext()
     */
    public static void Instantiate(Context context) {
        ourInstance = new DatabaseHelper(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // default database structure
        db.execSQL("CREATE TABLE 'Comment' ('id' INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL , 'Content' VARCHAR NOT NULL , 'Post_id' INTEGER NOT NULL , 'User_id' INTEGER NOT NULL );");
        db.execSQL("CREATE TABLE 'Course' ('id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 'Name' VARCHAR NOT NULL );");
        db.execSQL("CREATE TABLE 'Post' ('id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 'Title' VARCHAR NOT NULL , 'Content' VARCHAR NOT NULL , 'Hidden' BOOL NOT NULL , 'Pinned' BOOL NOT NULL , 'Requested' BOOL NOT NULL , 'User_id' INTEGER NOT NULL , 'Course_id' INTEGER NOT NULL );");
        db.execSQL("CREATE TABLE 'Rank' ('id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 'Name' VARCHAR NOT NULL  UNIQUE );");
        db.execSQL("CREATE TABLE 'User' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,'Username' VARCHAR NOT NULL ,'Password' VARCHAR NOT NULL ,'Avatar' BLOB,'Rank_id' INTEGER NOT NULL );");
        db.execSQL("CREATE TABLE 'User_has_Course' ('User_id' INTEGER NOT NULL , 'Course_id' INTEGER NOT NULL , PRIMARY KEY ('User_id', 'Course_id'));");

        // default inserts
        db.execSQL("INSERT INTO 'Rank' VALUES(1,'School');");
        db.execSQL("INSERT INTO 'Rank' VALUES(2,'Teacher');");
        db.execSQL("INSERT INTO 'Rank' VALUES(3,'Student');");
        db.execSQL("INSERT INTO 'User' VALUES(1,'SchoolDefault','Default',NULL,1);");
        db.execSQL("INSERT INTO 'User' VALUES(2,'TeacherDefault','Default',NULL,2);");
        db.execSQL("INSERT INTO 'User' VALUES(3,'StudentDefault','Default',NULL,3);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop old tables
        db.execSQL("DROP TABLE IF EXISTS 'Comment';");
        db.execSQL("DROP TABLE IF EXISTS 'Course';");
        db.execSQL("DROP TABLE IF EXISTS 'Post';");
        db.execSQL("DROP TABLE IF EXISTS 'Rank';");
        db.execSQL("DROP TABLE IF EXISTS 'User';");
        db.execSQL("DROP TABLE IF EXISTS 'User_has_Course';");

        // remake tables
        onCreate(db);
    }

    /**
     * For debugging!
     * Show all tables currently in the database.
     */
    public static void showTables() {
        SQLiteDatabase db = ourInstance.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String line = cursor.getString(0);
                for (int i = 1; i < cursor.getColumnCount(); i++) {
                    line += " | " + cursor.getString(i);
                }
                Log.d(LOG_TAG, "table found: " + line);

            } while (cursor.moveToNext());
        }
    }

    /**
     * For debugging!
     * Show all users currently in the database.
     * @return list of users
     */
    public static ArrayList<User> getUsers() {
        SQLiteDatabase db = ourInstance.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT u.Username, u.Avatar, r.Name FROM User as u INNER JOIN rank as r on u.Rank_id = r.id", null);

        ArrayList<User> result = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String rankText = cursor.getString(2);
                UserRank realRank = null;

                // check rank
                switch (rankText) {
                    case "Student":
                        realRank = UserRank.Student;
                        break;

                    case "Teacher":
                        realRank = UserRank.Teacher;
                        break;

                    case "School":
                        realRank = UserRank.School;
                        break;
                }

                // create user
                User newUser = new User(cursor.getString(0), cursor.getBlob(1), realRank);

                // add to list
                result.add(newUser);
            } while (cursor.moveToNext());
        }

        // release cursor
        cursor.close();

        return result;
    }

    /**
     * Try to login.
     * @param username The username
     * @param password The password
     * @return Success / fail, when success the current user will be changed -> CurrentUser.user
     */
    public static Boolean tryLogin(String username, String password){
        // escape special characters, just in case
        username = DatabaseUtils.sqlEscapeString(username);
        password = DatabaseUtils.sqlEscapeString(password);

        Log.d(LOG_TAG, "try login for user: " + username + ", and password: " + password);

        // set up query (doesn't seem to work with the selectionArgs in db.rawQuery)
        String myQuery = "SELECT u.Username, u.Avatar, r.Name FROM User as u INNER JOIN rank as r on u.Rank_id = r.id WHERE u.Username = ";
        myQuery += username;
        myQuery += " AND u.Password = ";
        myQuery += password + ";";

        // try to get a result from database
        SQLiteDatabase db = ourInstance.getReadableDatabase();
        Cursor cursor = db.rawQuery(myQuery, null);

        // check result
        if (cursor != null && cursor.moveToFirst()) {
            String rankText = cursor.getString(2);
            UserRank realRank = null;

            // check rank
            switch (rankText) {
                case "Student":
                    realRank = UserRank.Student;
                    break;

                case "Teacher":
                    realRank = UserRank.Teacher;
                    break;

                case "School":
                    realRank = UserRank.School;
                    break;
            }

            // create user
            CurrentUser.user =  new User(cursor.getString(0), cursor.getBlob(1), realRank);

            // release cursor
            cursor.close();

            Log.d(LOG_TAG, "Login success");
            return true;
        }
        else{
            Log.d(LOG_TAG, "Login failed");
            return false;
        }
    }
}
