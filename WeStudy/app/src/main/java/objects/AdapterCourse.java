package objects;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pedro.westudy.R;

import java.util.ArrayList;

/**
 * Created by Pedro on 2017-11-14.
 */

public class AdapterCourse extends ArrayAdapter<String> {
    public AdapterCourse(Context context, ArrayList<String> objects) {
        super(context, R.layout.list_item_course, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater myInflater = LayoutInflater.from(getContext());

        if (convertView == null){
            // new view
            convertView = myInflater.inflate(R.layout.list_item_course, parent, false);

            holder = new ViewHolder();

            // get view items you need
            holder.tvName = convertView.findViewById(R.id.listItem_course_name);

            // set tag
            convertView.setTag(holder);
        }
        else {
            // recycle old view
            holder = (ViewHolder) convertView.getTag();
        }

        // select data
        String selectedCourse = getItem(position);

        // set data
        holder.tvName.setText(selectedCourse);

        return convertView;
    }

    static class ViewHolder{
        private TextView tvName;
    }
}
