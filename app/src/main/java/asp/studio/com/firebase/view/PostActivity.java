package asp.studio.com.firebase.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import asp.studio.com.firebase.R;
import asp.studio.com.firebase.model.Comment;
import asp.studio.com.firebase.model.Post;

/**
 * Created by Amrit on 05/03/2018.
 */

public class PostActivity extends AppCompatActivity {
    DatabaseReference mReference;
    FirebaseRecyclerAdapter mAdapter;
    TextView autor;
    TextView content;
    String postKey;
    Button commentbtn;
    EditText comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        comment = findViewById(R.id.comment);
        commentbtn = findViewById(R.id.combtn);
        Intent intent = getIntent();
        postKey = intent.getStringExtra("POST_KEY");

        mReference = FirebaseDatabase.getInstance().getReference();
        commentbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                writeNewComment();
                comment.getText().clear();
            }

        });
        loadPost();
        loadComments();

    }

    void writeNewComment() {
        Comment newComment;
        String commentKey = mReference.child("posts/data").child(postKey).child("comments").push().getKey();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        newComment = new Comment(firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString(), comment.getText().toString());
        mReference.child("posts/data").child(postKey).child("comments").child(commentKey).setValue(newComment);
    }
    void loadPost() {
        mReference.child("posts/data").child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                autor = findViewById(R.id.author);
                autor.setText(post.author);
                content = findViewById(R.id.content);
                content.setText(post.content);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    void loadComments() {
            RecyclerView recyclerView = findViewById(R.id.post_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


            FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Comment>()
                    .setQuery(mReference.child("posts/data").child(postKey).child("comments"), Comment.class)
                    .setLifecycleOwner(this)
                    .build();

            mAdapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull CommentViewHolder holder, final int position, @NonNull final Comment model) {
                    holder.comment_user.setText(model.author);
                    Glide.with(PostActivity.this)
                            .load(model.authorPicUrl)
                            .apply(new RequestOptions().circleCrop())
                            .into(holder.authorpic_comment);
                    holder.comments.setText(model.comment);
                }

                @Override
                public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
                    return new CommentViewHolder(view);
                }


            };
        recyclerView.setAdapter(mAdapter);

    }
}
