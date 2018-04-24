package asp.studio.com.firebase.model;

import java.util.HashMap;

/**
 * Created by Amrit on 30/01/2018.
 */

public class Post {
    public String uid;
    public String author;
    public String authorPicUrl;
    public String content;
    public HashMap <String, Boolean> likes;
    public HashMap <String, Comment> comment;
    public String mediaUrl;
    public String mediaTYPE;

    public Post(String uid, String author, String authorPicUrl, String content) {
        this.uid = uid;
        this.author = author;
        this.authorPicUrl = authorPicUrl;
        this.content = content;
    }

    public Post(String uid, String author, String authorPicUrl, String content,String mediaUrl, String mediaTYPE) {
        this.uid = uid;
        this.author = author;
        this.authorPicUrl = authorPicUrl;
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.mediaTYPE = mediaTYPE;
    }
    public Post() {}
}
