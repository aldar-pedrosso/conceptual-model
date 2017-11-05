package statics;

public class User {
    public static String username;
    public static UserRank rank;

    private User() {
    }

    public static void reset(){
        username = null;
        rank = null;
    }
}

