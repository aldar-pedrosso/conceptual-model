package statics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pedro.westudy.ActivityMain;
import com.example.pedro.westudy.ActivityStudentComments;
import com.example.pedro.westudy.ActivityStudentCourse;

import java.util.ArrayList;

import objects.Comment;
import objects.Post;
import objects.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = ActivityMain.LOG_TAG_prefix + DatabaseHelper.class.getSimpleName();

    // the current user in the application
    public static objects.User currentUser = null;

    // management variables
    private static final String DATABASE_NAME = "WeStudy";
    private static final int DATABASE_VERSION = 11;
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
        currentUser = null;
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
        db.execSQL("INSERT INTO 'Post' VALUES(1,'What is functional programming?','What is this course actually?',0,1,0,3,2,'2017-11-14 16:10:49');");
        db.execSQL("INSERT INTO 'Post' VALUES(2,'Course start?','When will the course start?',1,0,0,3,2,'2017-11-14 16:11:18');");
        db.execSQL("INSERT INTO 'Post' VALUES(3,'Can''t find classroom! I am totally lost on the campus! What am I supposed to do!','Anyone can tell me where to go?',0,0,1,3,2,'2017-11-14 16:11:28');");

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
    public static ArrayList<objects.User> getUsers() {
        SQLiteDatabase db = ourInstance.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT u.Username, u.Avatar, r.Name FROM User as u INNER JOIN rank as r on u.Rank_id = r.id", null);

        ArrayList<objects.User> result = new ArrayList<>();
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
                objects.User newUser = new objects.User(cursor.getString(0), cursor.getBlob(1), realRank);

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
     * @return Success / fail, when success the current user will be changed -> currentUser.user
     */
    public static Boolean tryLogin(String username, String password){
        Log.d(LOG_TAG, "try login for user: " + username + ", and password: " + password);

        // try to get a result from database
        SQLiteDatabase db = ourInstance.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT u.Username, u.Avatar, r.Name " +
                "FROM User as u " +
                "INNER JOIN rank as r on u.Rank_id = r.id " +
                "WHERE u.Username = ? " +
                "AND u.Password = ? ", new String[]{username, password});

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
            currentUser =  new objects.User(cursor.getString(0), cursor.getBlob(1), realRank);

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

    // all database code concerning the current User
    public static class User {
        private static boolean generalUpdate(ContentValues values){
            // get username
            String username = DatabaseUtils.sqlEscapeString(currentUser.Username);

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
            currentUser.Avatar = newAvatar;
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

        /**
         * Ask for all courses that the current user is assigned to
         * @return a list with all course names, sorted alphabetically
         */
        public static ArrayList<String> getCourses(){
            SQLiteDatabase db = ourInstance.getReadableDatabase();

            // set request query
            Cursor cursor = db.rawQuery("SELECT c.Name " +
                    "FROM User AS u " +
                    "INNER JOIN User_has_Course AS uc ON u.id = uc.User_id " +
                    "INNER JOIN Course AS c ON c.id = uc.Course_id " +
                    "WHERE u.Username = ?" +
                    "ORDER BY c.Name", new String[]{currentUser.Username});

            // get results
            ArrayList<String> results = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    results.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }

            return results;
        }
    }

    // all database code concerning the current Course
    public static class Course{
        public static ArrayList<objects.Post> getPosts(){
            SQLiteDatabase db = ourInstance.getReadableDatabase();

            // set request query
            Cursor cursor = db.rawQuery("SELECT u.Username, p.Title, p.Content, p.Hidden, p.Pinned, p.Requested, p.Time, MAX(cm.Time) AS 'LastComment', COUNT (cm.id) AS 'AmountOfComments' " +
                    "FROM Course AS cs " +
                    "INNER JOIN User AS u ON u.id = p.User_id " +
                    "INNER JOIN Post as p ON p.Course_id = cs.id " +
                    "LEFT OUTER JOIN Comment as cm ON p.id = cm.Post_id " +
                    "WHERE cs.Name = ? " +
                    "GROUP BY p.id " +
                    "ORDER BY p.Pinned DESC, LastComment DESC, p.Time DESC", new String[]{ActivityStudentCourse.currentCourse});

            // get results
            ArrayList<objects.Post> results = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    objects.Post newPost = new objects.Post();
                    newPost.creator = cursor.getString(0);
                    newPost.title = cursor.getString(1);
                    newPost.content = cursor.getString(2);
                    newPost.hidden = intToBool(cursor.getInt(3));
                    newPost.pinned = intToBool(cursor.getInt(4));
                    newPost.requested = intToBool(cursor.getInt(5));
                    newPost.timePosted = cursor.getString(6);
                    newPost.timeLastComment = cursor.getString(7);
                    newPost.amountOfComments = cursor.getInt(8);

                    // only add post to the results if not hidden,
                    // or it is hidden while it is from the current user,
                    // or it is hidden and the current user is a teacher
                    if (!newPost.hidden || newPost.creator.equals(currentUser.Username) || currentUser.Rank == UserRank.Teacher)
                        results.add(newPost);
                } while (cursor.moveToNext());
            }

            // release cursor
            cursor.close();

            return results;
        }
    }

    // all database code concerning the current Post
    public static class Post{
        public static ArrayList<Comment> getComments(){
            SQLiteDatabase db = ourInstance.getReadableDatabase();

            // set request query
            Cursor cursor = db.rawQuery("SELECT u.Username, r.Name, u.Avatar, cm.Content, cm.Time " +
                    "FROM Post AS p " +
                    "INNER JOIN Comment AS cm ON cm.Post_id = p.id " +
                    "INNER JOIN User AS u on u.id = cm.User_id " +
                    "INNER JOIN Rank AS r on r.id = u.Rank_id " +
                    "WHERE p.Title = ? " +
                    "ORDER BY cm.Time ASC", new String[]{ActivityStudentComments.currentPost});

            // get results
            ArrayList<Comment> results = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String rankText = cursor.getString(1);
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
                    objects.User newUser = new objects.User(cursor.getString(0), cursor.getBlob(2), realRank);

                    // create comment
                    Comment newComment = new Comment();
                    newComment.user = newUser;
                    newComment.content = cursor.getString(3);
                    newComment.time = cursor.getString(4);

                    // add result to the list
                    results.add(newComment);
                } while (cursor.moveToNext());
            }

            // release cursor
            cursor.close();

            return results;
        }
    }

    /**
     * Small int to bool converter
     * @param input
     * @return True if input > 0, False otherwise
     */
    static boolean intToBool(int input){
        if (input>0)
            return true;
        return false;
    }
}