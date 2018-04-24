package asp.studio.com.firebase.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import asp.studio.com.firebase.R;

/**
 * Created by Amrit on 05/02/2018.
 */

public class PostViewHolder extends RecyclerView.ViewHolder{
    TextView author;
    ImageView authorPhoto;
    ImageView image;
    TextView content;
    ImageView like;
    TextView  likecount;
    LinearLayout likelayout;

    public PostViewHolder(View itemView) {
        super(itemView);
        author = itemView.findViewById(R.id.author);
        authorPhoto = itemView.findViewById(R.id.photo);
        content = itemView.findViewById(R.id.content);
        like = itemView.findViewById(R.id.like);
        likecount = itemView.findViewById(R.id.num_likes);
        likelayout = itemView.findViewById(R.id.like_layout);
        image = itemView.findViewById(R.id.image);
    }
}
