package asp.studio.com.firebase.view;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;

/**
 * Created by Amrit on 13/02/2018.
 */

public class MyPostFragment extends PostListFragment {
    @Override
    Query setQuery() {
        return mReference.child("posts/user-posts").child(FirebaseAuth.getInstance().getUid());
    }
}
