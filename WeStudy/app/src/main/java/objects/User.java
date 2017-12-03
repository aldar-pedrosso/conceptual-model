package objects;

import enums.UserRank;

public class User {
    public String Username;
    public byte[] Avatar;
    public UserRank Rank;

    public User(String username, byte[] avatar, UserRank rank) {
        Username = username;
        Avatar = avatar;
        Rank = rank;
    }
}
