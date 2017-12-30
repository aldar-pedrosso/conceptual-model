package objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pedro.westudy.R;

import java.util.ArrayList;

/**
 * Created by Pedro on 2017-11-14.
 */

public class AdapterRequestedPost extends ArrayAdapter<Post> {
    public AdapterRequestedPost(Context context, ArrayList<Post> objects) {
        super(context, R.layout.list_item_requested_post, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater myInflater = LayoutInflater.from(getContext());

        if (convertView == null){
            // new view
            convertView = myInflater.inflate(R.layout.list_item_requested_post, parent, false);

            holder = new ViewHolder();

            // get view items you need
            holder.tvPostName = convertView.findViewById(R.id.listItem_requested_post_tvPostName);
            holder.tvCreator = convertView.findViewById(R.id.listItem_requested_post_tvCreator);
            holder.tvLastUpdatedTime = convertView.findViewById(R.id.listItem_requested_post_tvLastUpdatedTime);
            holder.imgPin = convertView.findViewById(R.id.listItem_requested_post_imgPin);
            holder.imgHidden = convertView.findViewById(R.id.listItem_requested_post_imgHidden);
            holder.imgRequested = convertView.findViewById(R.id.listItem_requested_post_imgRequested);
            holder.tvAmountOfComments = convertView.findViewById(R.id.listItem_requested_post_tvAmountOfComments);
            holder.tvCourseName = convertView.findViewById(R.id.listItem_requested_post_tvCourseName);

            // set tag
            convertView.setTag(holder);
        }
        else {
            // recycle old view
            holder = (ViewHolder) convertView.getTag();
        }

        // select data
        Post selectedPost = getItem(position);

        // set data
        holder.tvPostName.setText(selectedPost.title);
        holder.tvCreator.setText(selectedPost.creator);
        holder.tvAmountOfComments.setText("" + selectedPost.amountOfComments);
        holder.tvCourseName.setText(selectedPost.course);

        // if no comments, set time of the post itself
        if (selectedPost.timeLastComment == null || selectedPost.timeLastComment.isEmpty())
            holder.tvLastUpdatedTime.setText(selectedPost.timePosted);
        else
            holder.tvLastUpdatedTime.setText(selectedPost.timeLastComment);

        // set visibilities
        if (selectedPost.pinned)
            holder.imgPin.setVisibility(View.VISIBLE);
        else
            holder.imgPin.setVisibility(View.GONE);

        if (selectedPost.hidden)
            holder.imgHidden.setVisibility(View.VISIBLE);
        else
            holder.imgHidden.setVisibility(View.GONE);

        if (selectedPost.requested)
            holder.imgRequested.setVisibility(View.VISIBLE);
        else
            holder.imgRequested.setVisibility(View.GONE);

        return convertView;
    }

    static class ViewHolder{
        private TextView tvPostName;
        private TextView tvCreator;
        private TextView tvLastUpdatedTime;
        private ImageView imgPin;
        private ImageView imgHidden;
        private ImageView imgRequested;
        private TextView tvAmountOfComments;
        private TextView tvCourseName;
    }
}
