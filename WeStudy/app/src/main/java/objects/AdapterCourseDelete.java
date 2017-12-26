package objects;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pedro.westudy.ActivityJoinCourse;
import com.example.pedro.westudy.R;
import com.example.pedro.westudy.school.ActivitySchoolManageCourses;
import com.example.pedro.westudy.student.ActivityStudentHome;

import java.util.ArrayList;

import statics.DatabaseHelper;

import static android.content.ContentValues.TAG;

/**
 * Created by Pedro on 2017-11-14.
 */

public class AdapterCourseDelete extends ArrayAdapter<String> {
    public AdapterCourseDelete(Context context, ArrayList<String> objects) {
        super(context, R.layout.list_item_course_with_delete, objects);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater myInflater = LayoutInflater.from(getContext());

        if (convertView == null){
            // new view
            convertView = myInflater.inflate(R.layout.list_item_course_with_delete, parent, false);

            holder = new ViewHolder();

            // get view items you need
            holder.tvName = convertView.findViewById(R.id.listItem_course_with_delete_tvName);
            holder.btnDelete = convertView.findViewById(R.id.listItem_course_with_delete_btnDelete);

            // set tag
            convertView.setTag(holder);
        }
        else {
            // recycle old view
            holder = (ViewHolder) convertView.getTag();
        }

        // select data
        final String selectedCourse = getItem(position);

        // set data
        holder.tvName.setText(selectedCourse);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "User clicked to deleted the course: " + selectedCourse);

                new AlertDialog.Builder(parent.getContext())
                        .setTitle("Delete?")
                        .setMessage("Are you sure you want to delete this course? \n\n" + selectedCourse)
                        .setIcon(R.drawable.ic_warning)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d(TAG, "User pressed 'no', and thus the course will not be deleted.");
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //feedback to parent
                                Log.d(TAG, "User pressed 'no', and thus the course will be deleted.");
                                Toast.makeText(parent.getContext(), "Course deleted", Toast.LENGTH_SHORT).show();

                                // delete
                                DatabaseHelper.School.deleteCourse(selectedCourse);

                                // notify course manager
                                ActivitySchoolManageCourses.filterChanged = true;
                            }
                        })
                        .create().show();
            }
        });

        return convertView;
    }

    static class ViewHolder{
        private TextView tvName;
        private ImageButton btnDelete;
    }
}
