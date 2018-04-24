package asp.studio.com.firebase.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import asp.studio.com.firebase.R;

/**
 * Created by Amrit on 16/03/2018.
 */

public class CommentViewHolder extends RecyclerView.ViewHolder {
    ImageView authorpic_comment;
    TextView comment_user;
    TextView comments;

    public CommentViewHolder(View itemView) {
        super(itemView);
        this.authorpic_comment = itemView.findViewById(R.id.photo_person);
        this.comment_user = itemView.findViewById(R.id.comment_user);
        this.comments = itemView.findViewById(R.id.commentss);

    }
}
