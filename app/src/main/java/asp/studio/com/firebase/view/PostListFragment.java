package asp.studio.com.firebase.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import asp.studio.com.firebase.R;

import asp.studio.com.firebase.model.Post;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class PostListFragment extends Fragment {

    DatabaseReference mReference;
    FirebaseRecyclerAdapter mAdapter;
    public PostListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        mReference = FirebaseDatabase.getInstance().getReference();

        RecyclerView recyclerView = view.findViewById(R.id.post_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        Query postsQuery = setQuery();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Post>()
                .setIndexedQuery(setQuery(), mReference.child("posts/data"), Post.class)
                .setLifecycleOwner(this)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, final int position, @NonNull final Post post) {
                holder.content.setText(post.content);
                holder.author.setText(post.author);
                Glide.with(PostListFragment.this)
                        .load(post.authorPicUrl)
                        .apply(new RequestOptions().circleCrop())
                        .into(holder.authorPhoto);
                int numlikes = 0;
                if(post.mediaUrl != null) {
                    holder.image.setVisibility(View.VISIBLE);
                    if ("audio".equals(post.mediaTYPE)){
                        holder.image.setImageResource(R.drawable.audio);
                    }else {
                        Glide.with(PostListFragment.this)
                                .load(post.mediaUrl)
                                .into(holder.image);
                    }
                    holder.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), MediaActivity.class);
                            intent.putExtra("MEDIA_URL", post.mediaUrl);
                            intent.putExtra("MEDIA_TYPE",post.mediaTYPE);
                            startActivity(intent);
                        }
                    });
                }else{
                    holder.image.setVisibility(View.GONE);
                }


                if(post.likes != null){
                    numlikes = post.likes.size();
                }
                holder.likecount.setText(String.valueOf(numlikes));

                if(post.likes != null && post.likes.containsKey(FirebaseAuth.getInstance().getUid())){
                    holder.like.setImageResource(R.drawable.heart_on);
                } else {
                    holder.like.setImageResource(R.drawable.heart_off);
                }

                holder.likelayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String uid = FirebaseAuth.getInstance().getUid();
                        String postkey = getRef(position).getKey();

                        Long tsLong = System.currentTimeMillis()/1000;


                        if (post.likes != null && post.likes.containsKey(uid)){
                            mReference.child("posts/data").child(postkey).child("likes").child(uid).setValue(null);
                            mReference.child("posts/user-likes").child(uid).child(postkey).setValue(null);

                        } else {
                            mReference.child("posts/data").child(postkey).child("likes").child(uid).setValue(-tsLong);
                            mReference.child("posts/user-likes").child(uid).child(postkey).setValue(-tsLong);
                        }
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PostActivity.class);
                        intent.putExtra("POST_KEY", getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
                return new PostViewHolder(view);
            }
        };

        recyclerView.setAdapter(mAdapter);

        return view;
    }
    abstract Query setQuery();
}
