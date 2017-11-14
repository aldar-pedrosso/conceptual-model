package statics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pedro.westudy.ActivityMain;

import java.util.ArrayList;

import objects.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = ActivityMain.LOG_TAG_prefix + DatabaseHelper.class.getSimpleName();

    // the current user in the application
    public static User CurrentUser = null;

    // management variables
    private static final String DATABASE_NAME = "WeStudy";
    private static final int DATABASE_VERSION = 7;
    private static DatabaseHelper ourInstance = null;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Set up instance for static usage.
     * @param context basic context -> getBaseContext()
     */
    public static void instantiate(Context context) {
        ourInstance = new DatabaseHelper(context);
    }

    public static void resetCurrentUser(){
        CurrentUser = null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // default database structure
        db.execSQL("CREATE TABLE 'Comment' ('id' INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL , 'Content' VARCHAR NOT NULL , 'Post_id' INTEGER NOT NULL , 'User_id' INTEGER NOT NULL , 'Time' DATETIME NOT NULL  DEFAULT (CURRENT_TIMESTAMP));");
        db.execSQL("CREATE TABLE 'Course' ('id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 'Name' VARCHAR NOT NULL );");
        db.execSQL("CREATE TABLE 'Post' ('id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 'Title' VARCHAR NOT NULL , 'Content' VARCHAR NOT NULL , 'Hidden' BOOL NOT NULL , 'Pinned' BOOL NOT NULL , 'Requested' BOOL NOT NULL , 'User_id' INTEGER NOT NULL , 'Course_id' INTEGER NOT NULL , 'Time' DATETIME NOT NULL  DEFAULT (CURRENT_TIMESTAMP) );");
        db.execSQL("CREATE TABLE 'Rank' ('id' INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE , 'Name' VARCHAR NOT NULL  UNIQUE );");
        db.execSQL("CREATE TABLE 'User' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,'Username' VARCHAR NOT NULL ,'Password' VARCHAR NOT NULL ,'Avatar' BLOB,'Rank_id' INTEGER NOT NULL );");
        db.execSQL("CREATE TABLE 'User_has_Course' ('User_id' INTEGER NOT NULL , 'Course_id' INTEGER NOT NULL , PRIMARY KEY ('User_id', 'Course_id'));");

        // insert ranks
        db.execSQL("INSERT INTO 'Rank' VALUES(1,'School');");
        db.execSQL("INSERT INTO 'Rank' VALUES(2,'Teacher');");
        db.execSQL("INSERT INTO 'Rank' VALUES(3,'Student');");

        // default users
        db.execSQL("INSERT INTO 'User' VALUES(1,'SchoolDefault','Default',NULL,1);");
        db.execSQL("INSERT INTO 'User' VALUES(2,'TeacherDefault','Default',NULL,2);");
        db.execSQL("INSERT INTO 'User' VALUES(3,'StudentDefault','Default',NULL,3);");

        // default comments
        db.execSQL("INSERT INTO 'Comment' VALUES(1,'It''s just programming, but functional.',1,2,'2017-11-14 16:12:30');");
        db.execSQL("INSERT INTO 'Comment' VALUES(2,'Oh, ok.',1,3,'2017-11-14 16:15:45');");
        db.execSQL("INSERT INTO 'Comment' VALUES(3,'Soon',2,2,'2017-11-14 16:52:13');");
        db.execSQL("INSERT INTO 'Comment' VALUES(5,'1st building, 2nd floor, just around the corner',3,2,'2017-11-14 16:57:43');");
        db.execSQL("INSERT INTO 'Comment' VALUES(6,'Ok thank you!',3,3,'2017-11-14 16:57:56');");

        // default courses
        db.execSQL("INSERT INTO 'Course' VALUES(1,'Modeling');");
        db.execSQL("INSERT INTO 'Course' VALUES(2,'Functional programming');");
        db.execSQL("INSERT INTO 'Course' VALUES(3,'Declarative programming');");
        db.execSQL("INSERT INTO 'Course' VALUES(4,'Project management');");

        // default posts
        db.execSQL("INSERT INTO 'Post' VALUES(1,'What is functional programming','What is this course actually?','False','True','False',3,2,'2017-11-14 16:10:49');");
        db.execSQL("INSERT INTO 'Post' VALUES(2,'Course start?','When will the course start?','True','False','False',3,2,'2017-11-14 16:11:18');");
        db.execSQL("INSERT INTO 'Post' VALUES(3,'Can''t find classroom!','HELP I can''t find the classroom!','False','False','True',3,2,'2017-11-14 16:11:28');");

        // default user-course links
        db.execSQL("INSERT INTO 'User_has_Course' VALUES(2,1);");
        db.execSQL("INSERT INTO 'User_has_Course' VALUES(2,2);");
        db.execSQL("INSERT INTO 'User_has_Course' VALUES(2,3);");
        db.execSQL("INSERT INTO 'User_has_Course' VALUES(2,4);");
        db.execSQL("INSERT INTO 'User_has_Course' VALUES(3,1);");
        db.execSQL("INSERT INTO 'User_has_Course' VALUES(3,2);");
        db.execSQL("INSERT INTO 'User_has_Course' VALUES(3,3);");
        db.execSQL("INSERT INTO 'User_has_Course' VALUES(3,4);");

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
            CurrentUser=  new User(cursor.getString(0), cursor.getBlob(1), realRank);

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

    // all database code concerning the User
    public static class UpdateUser {
        private static boolean generalUpdate(ContentValues values){
            // get username
            String username = DatabaseUtils.sqlEscapeString(CurrentUser.Username);

            SQLiteDatabase db = ourInstance.getWritableDatabase();

            // updating row
            int result = db.update("User", values, "Username = " + username, null);

            // check result
            switch (result){
                case 1:
                    Log.d(LOG_TAG,"Update successful!");
                    return true;

                case 0:
                    Log.d(LOG_TAG,"Update failed!");
                    break;

                default:
                    Log.d(LOG_TAG,"Something went wrong, multiple rows got updated!!!");
                    break;
            }

            return false;
        }

        /**
         * Change avatar for the current user
         * @param newAvatar The new avatar image object
         */
        public static void changeAvatar(byte[] newAvatar){
            ContentValues values = new ContentValues();
            values.put("Avatar", newAvatar);

            // make changes
            generalUpdate(values);

            // also change on current user
            CurrentUser.Avatar = newAvatar;
        }

        /**
         * Change password for the current user
         * @param newPassword the new password
         */
        public static void changePassword(String newPassword){
            ContentValues values = new ContentValues();
            values.put("Password", newPassword);

            // make changes
            generalUpdate(values);
        }
    }
}

