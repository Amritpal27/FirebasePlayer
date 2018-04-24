package asp.studio.com.firebase.view;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.Query;

/**
 * Created by Amrit on 13/02/2018.
 */

public class LikePostFragment extends PostListFragment {
    @Override
    Query setQuery() {
        return mReference.child("posts/user-likes").child(FirebaseAuth.getInstance().getUid());
    }
}
