package objects;

import statics.UserRank;

public class User {
    public String Username;
    public Object Avatar;
    public UserRank Rank;

    public User(String username, Object avatar, UserRank rank) {
        Username = username;
        Avatar = avatar;
        Rank = rank;
    }
}
