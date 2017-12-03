package objects;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by Pedro on 2017-11-15.
 */

public class Post implements Comparable{
    public User user = null;
    public String creator = null;
    public String title = null;
    public String content = null;
    public Boolean hidden = null;
    public Boolean pinned = null;
    public Boolean requested = null;
    public String timePosted = null;
    public String timeLastComment = null;
    public int amountOfComments = 0;

    @Override
    public int compareTo(@NonNull Object o) {
        Post otherPost = (Post) o;

        // priority
        if (pinned && !otherPost.pinned)
            return 0;
        if (!pinned && otherPost.pinned)
            return 1;

        // if both pinned or not pinned

        String myTime;
        if (timeLastComment == null || timeLastComment.isEmpty())
            myTime = timePosted;
        else
            myTime = timeLastComment;

        String otherTime;
        if (otherPost.timeLastComment == null || otherPost.timeLastComment.isEmpty())
            otherTime = otherPost.timePosted;
        else
            otherTime = otherPost.timeLastComment;

        return myTime.compareTo(otherTime);
    }
}
