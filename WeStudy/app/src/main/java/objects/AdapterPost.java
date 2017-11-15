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

public class AdapterPost extends ArrayAdapter<Post> {
    public AdapterPost(Context context, ArrayList<Post> objects) {
        super(context, R.layout.list_item_post, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater myInflater = LayoutInflater.from(getContext());

        if (convertView == null){
            // new view
            convertView = myInflater.inflate(R.layout.list_item_post, parent, false);

            holder = new ViewHolder();

            // get view items you need
            holder.tvPostName = convertView.findViewById(R.id.listItem_post_tvPostName);
            holder.tvCreator = convertView.findViewById(R.id.listItem_post_tvCreator);
            holder.tvLastUpdatedTime = convertView.findViewById(R.id.listItem_post_tvLastUpdatedTime);
            holder.imgPin = convertView.findViewById(R.id.listItem_post_imgPin);
            holder.imgHidden = convertView.findViewById(R.id.listItem_post_imgHidden);
            holder.imgRequested = convertView.findViewById(R.id.listItem_post_imgRequested);

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
    }
}
