package asp.studio.com.firebase.view;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import asp.studio.com.firebase.R;


public class MediaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        Intent intent = getIntent();
        if (intent != null){
            String mediatype = intent.getStringExtra("MEDIA_TYPE");
            String mediaurl = intent.getStringExtra("MEDIA_URL");

            if ("video".equals(mediatype) || "audio".equals(mediatype)) {
                VideoView videoView = findViewById(R.id.videoview);
                MediaController mc = new MediaController(this);
                videoView.setMediaController(mc);
                videoView.setVideoPath(mediaurl);
                videoView.start();
            } else if ("photo".equals(mediatype)) {
                ImageView imageView = findViewById(R.id.imageview);
                Glide.with(this).load(mediaurl).into(imageView);
            }
        }
    }
}
