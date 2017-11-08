package statics;

import objects.User;

public class CurrentUser {
    public static User user = null;

    private CurrentUser() {
    }

    public static void reset(){
        user = null;
    }
}

