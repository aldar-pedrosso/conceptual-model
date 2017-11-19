package objects;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pedro.westudy.R;

import java.util.ArrayList;

import statics.UserRank;

import static statics.DatabaseHelper.currentUser;

/**
 * Created by Pedro on 2017-11-14.
 */

public class AdapterComment extends ArrayAdapter<Comment> {
    public AdapterComment(Context context, ArrayList<Comment> objects) {
        super(context, R.layout.list_item_comment, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater myInflater = LayoutInflater.from(getContext());

        if (convertView == null){
            // new view
            convertView = myInflater.inflate(R.layout.list_item_comment, parent, false);

            holder = new ViewHolder();

            // get view items you need
            holder.imgAvatar = convertView.findViewById(R.id.listItem_comments_imgAvatar);
            holder.tvCreator = convertView.findViewById(R.id.listItem_comments_tvCreator);
            holder.tvContent = convertView.findViewById(R.id.listItem_comments_tvContent);
            holder.imgteacher = convertView.findViewById(R.id.listItem_comments_imgTeacher);
            holder.tvTime = convertView.findViewById(R.id.listItem_comments_tvTime);

            // set tag
            convertView.setTag(holder);
        }
        else {
            // recycle old view
            holder = (ViewHolder) convertView.getTag();
        }

        // select data
        Comment selectedComment = getItem(position);

        // set data
        holder.tvContent.setText(selectedComment.content);
        holder.tvCreator.setText(selectedComment.user.Username);
        holder.tvTime.setText(selectedComment.time);

        // set avatar if possible
        if (currentUser.Avatar != null){
            holder.imgAvatar.setImageBitmap(BitmapFactory.decodeByteArray(selectedComment.user.Avatar, 0, selectedComment.user.Avatar.length));
        }

        // set visibility
        if (selectedComment.user.Rank == UserRank.Teacher)
            holder.imgteacher.setVisibility(View.VISIBLE);
        else
            holder.imgteacher.setVisibility(View.GONE);

        return convertView;
    }

    static class ViewHolder{
        private TextView tvContent;
        private TextView tvTime;
        private TextView tvCreator;
        private ImageView imgAvatar;
        private ImageView imgteacher;
    }
}
