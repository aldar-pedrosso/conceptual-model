package com.example.pedro.westudy.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.pedro.westudy.ActivityCoursePosts;
import com.example.pedro.westudy.ActivityMain;
import com.example.pedro.westudy.ActivityPostComments;
import com.example.pedro.westudy.R;

import java.util.ArrayList;

import objects.AdapterCourse;
import objects.AdapterRequestedPost;
import objects.Post;
import statics.DatabaseHelper;

/**
 * Created by Pedro on 2017-12-27.
 */

public class ActivityTeacherTabRequests extends Fragment {
    private final String TAG = ActivityMain.TAG_prefix + this.getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_teacher_requests, container, false);

        // make list adapter
        final ArrayList<Post> myPosts = DatabaseHelper.User.getRequestedPosts();
        AdapterRequestedPost adapter = new AdapterRequestedPost(getActivity(), myPosts);

        // set listview to adapter
        ListView listView = rootView.findViewById(R.id.tab_teacher_home_lvRequests);
        listView.setAdapter(adapter);

        // set list item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Opening post: \n" +
                        "\n Course: " + myPosts.get(position).course +
                        "\n Title: " + myPosts.get(position).title +
                        "\n Creator: " + myPosts.get(position).creator +
                        "\n Time posted: " + myPosts.get(position).timePosted);

                ActivityPostComments.currentPost = myPosts.get(position);
                Intent ChosenPost = new Intent(getActivity(), ActivityPostComments.class);
                startActivity(ChosenPost);
            }
        });

        return rootView;
    }
}
