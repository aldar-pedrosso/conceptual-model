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

import com.example.pedro.westudy.ActivityMain;
import com.example.pedro.westudy.R;
import com.example.pedro.westudy.ActivityCoursePosts;

import java.util.ArrayList;

import objects.AdapterCourse;
import statics.DatabaseHelper;

/**
 * Created by Pedro on 2017-12-27.
 */

public class ActivityTeacherTabCourses extends Fragment {
    private final String TAG = ActivityMain.TAG_prefix + this.getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_teacher_courses, container, false);

        // make list adapter
        final ArrayList<String> myCourses = DatabaseHelper.User.getCourses();
        AdapterCourse adapter = new AdapterCourse(getActivity(), myCourses);

        // set listview to adapter
        ListView listView = rootView.findViewById(R.id.tab_teacher_home_lvCourses);
        listView.setAdapter(adapter);

        // set list item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Open posts of the course '" + myCourses.get(position) + "'");

                ActivityCoursePosts.currentCourse = myCourses.get(position);
                Intent ChosenCourse = new Intent(getActivity(), ActivityCoursePosts.class);
                startActivity(ChosenCourse);
            }
        });

        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser)
            ActivityTeacherHome.fabAddCourse.setVisibility(View.VISIBLE);
        else
            ActivityTeacherHome.fabAddCourse.setVisibility(View.GONE);
    }
}
