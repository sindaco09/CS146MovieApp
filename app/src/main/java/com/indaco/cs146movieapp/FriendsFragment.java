package com.indaco.cs146movieapp;

import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;



public class FriendsFragment extends ListFragment {

    /* MEMBER VARIABLES:
     * TAG for logs
     * List of friends for user <Type ParseUser from Parse.com>
     * Relationship of user to user (many to many)
     * ParseUser for current user logged in
      */
    public static final String TAG = FriendsFragment.class.getSimpleName();

    protected List<ParseUser> mFriends;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;

    /** GENERAL LIFECYCLE OF FRAGMENT REVIEW:
     *
     * onAttach(Context): Called 1st
     *      - called once the fragment is associated with its activity
     *      - onCreate(Bundle) will be called after this
     *
     * onCreate(Bundle): Called 2nd
     *      - called to do initial creation of the fragment
     *      - called after the ACTIVITY's onAttachFragment() but before FRAGMENTS's onCreateView()
     *
     * onCreateView(LayoutInflater, ViewGroup, Bundle): Called 3rd
     *      creates and returns the view hierarchy associated with fragment
     *      - Here you can assign your View variables, expected to return a View to this method
     *      - THIS is the main UI view
     *          - if no layouts or graphics, return null
     *
     * onPause(): user is leaving fragment (not destroyed)
     *      - good point to commit any changes that should be persisted
     *
     * onResume(): makes fragment begin interacting with user
     *      - works as it sounds, when you interacting with the fragment, reloads the
     *          info for the user, in this case, current user and his friends
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.addAscendingOrder(ParseConstants.KEY_USERNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if(e==null) {//friends successfully found, proceed to populate list
                    mFriends = friends;

                    String[] usernames = new String[mFriends.size()];
                    int i = 0;
                    for (ParseUser user : mFriends) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getListView().getContext(),
                            android.R.layout.simple_list_item_1,
                            usernames);
                    setListAdapter(adapter);
                }else{//couldn't retrieve list
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
