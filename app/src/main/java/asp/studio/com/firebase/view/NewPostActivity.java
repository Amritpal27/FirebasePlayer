package asp.studio.com.firebase.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import asp.studio.com.firebase.R;
import asp.studio.com.firebase.model.Post;

@RequiresApi(api = Build.VERSION_CODES.O)
public class NewPostActivity extends AppCompatActivity {
    Button btnImage,btnaudio,btnvideo,publish;
    EditText content;
    DatabaseReference databaseReference;
    ImageView image;
    Uri mediaUri;
    Uri downloaderUrl;
    String mediaTYPE;
    int RC_VIDEO_PICK = 1234;
    int RC_IMAGE_PICK = 5677;
    int RC_AUDIO_PICK = 9875;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        content = findViewById(R.id.content);
        publish = findViewById(R.id.publish);
        btnImage = findViewById(R.id.btnImage);
        btnaudio = findViewById(R.id.btnAudio);
        btnvideo = findViewById(R.id.btnVideo);
        image = findViewById(R.id.image);

        publish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                publish.setEnabled(false);
                if (mediaUri != null){
                    uploadFile();
                }else{
                    writeNewPost();
                }
                finish();
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RC_IMAGE_PICK);
            }
        });

        btnvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RC_VIDEO_PICK);
            }
        });
        btnaudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RC_AUDIO_PICK);
            }
        });
    }




    void writeNewPost() {
        String postKey = databaseReference.child("posts").push().getKey();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Post post;
        if(downloaderUrl == null){
            post = new Post(FirebaseAuth.getInstance().getUid(),firebaseUser.getDisplayName(),firebaseUser.getPhotoUrl().toString(),content.getText().toString());
        }else {
            post =  new Post(FirebaseAuth.getInstance().getUid(),firebaseUser.getDisplayName(),firebaseUser.getPhotoUrl().toString(),content.getText().toString(),downloaderUrl.toString(),mediaTYPE.toString());
        }
        databaseReference.child("posts/data").child(postKey).setValue(post);
        databaseReference.child("posts/all-posts").child(postKey).setValue(true);
        databaseReference.child("posts/user-posts").child(FirebaseAuth.getInstance().getUid()).child(postKey).setValue(true);
    }


    void uploadFile(){
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(mediaTYPE + "/" + UUID.randomUUID() + "-" + mediaUri.getLastPathSegment());
        fileRef.putFile(mediaUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                downloaderUrl = taskSnapshot.getDownloadUrl();

                writeNewPost();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == RC_IMAGE_PICK) {
                mediaUri = data.getData();
                mediaTYPE = "image";
                Glide.with(this).load(mediaUri).into(image);
            } else if (requestCode == RC_VIDEO_PICK) {
                mediaTYPE = "video";
                mediaUri = data.getData();
                Glide.with(this).load(mediaUri).into(image);
            } else if (requestCode == RC_AUDIO_PICK) {
                mediaUri = data.getData();
                mediaTYPE = "audio";
                image.setImageResource(R.drawable.audio);
            }
        }
    }

}
