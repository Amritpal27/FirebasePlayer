package asp.studio.com.firebase.model;

/**
 * Created by Amrit on 15/03/2018.
 */

public class Comment {
    public String author;
    public String authorPicUrl;
    public String comment;

    public Comment(){}

    public Comment(String author, String authorPicUrl, String comment) {
        this.author = author;
        this.authorPicUrl = authorPicUrl;
        this.comment = comment;
    }
}
