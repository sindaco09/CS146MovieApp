package com.indaco.cs146movieapp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class EditFriendsActivity extends ListActivity {

    //Tag string used for debugging
    public static final String TAG = EditFriendsActivity.class.getSimpleName();

    /* Member variables:
     * mList = list of all users
     * mRelation = relation between users
     * mCurrentUser = getCurrentUser logged in
     */
    protected List<ParseUser> mList;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;

    /* onCreate(): called to do initial creation of the fragment.
     *
     * onResume(): makes the fragment begin interacting with the user (based on its containing activity being resumed).
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
        //setupActionBar();

        //allow multiple items to be selected
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    @Override
    protected void onResume(){
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        //this query returns all the users in ascending order
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(1000);

        //Query of all the users in the database
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    /* + success! Populate mList
                     * + initiate usernames to be filled with all the users from the list
                     * for loop starts from first user and populates usernames string array
                     * + ArrayAdapter for type <String>, context is this activity, ID the
                     * ListView created in the xml file, and add items based on usernames String
                     * like setContentView, there is setListAdapter (ArrayAdapter)
                     *
                     */
                    mList = list;
                    String[] usernames = new String[mList.size()];
                    int i = 0;
                    for (ParseUser user : mList) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(EditFriendsActivity.this,
                            android.R.layout.simple_list_item_checked,
                            usernames);

                    //display the list from adapter
                    setListAdapter(adapter);

                    //function to show which users are friends and which aren't
                    addFriendCheckmarks();

                } else {
                    //List did not load correctly,
                    // basic debugging messages in both log and on screen of device
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private void addFriendCheckmarks() {
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if(e==null){
                    /* list returned - look for match. in 1st for loop,
                     * the user is populated with first user in the list
                     * 2nd for loop looks for a match in friends list
                     * you want to compare objectID's not usernames
                     * Parse has problems with users vs ID's
                     */
                    for(int i =0;i<mList.size();i++){
                        ParseUser user = mList.get(i);

                        for(ParseUser friend : friends){
                            if(friend.getObjectId().equals(user.getObjectId())){
                                //Match! - set checkmark!
                                getListView().setItemChecked(i,true);
                            }
                        }
                    }
                }else{
                    Log.e(TAG,e.getMessage());
                }
            }
        });
    }


    //Dropdown menu in top right (3 vertical inline dots) if item is selected...
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //if user is selected from the list, add/remove user from friends list
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if(getListView().isItemChecked(position)){
            //add friend
            mFriendsRelation.add(mList.get(position));
            mCurrentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e!=null){
                        Log.e(TAG,e.getMessage());
                    }
                }
            });
        }else{
            //remove him
            mFriendsRelation.remove(mList.get(position));
            mCurrentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
        }

    }

}

