package com.example.pedro.westudy.teacher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pedro.westudy.R;

/**
 * Created by Pedro on 2017-12-27.
 */

public class ActivityTeacherTabRequests extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_teacher_requests, container, false);
        return rootView;
    }


}
