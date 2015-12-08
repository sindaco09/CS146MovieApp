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

    public static final String TAG = EditFriendsActivity.class.getSimpleName();

    protected List<ParseUser> mList;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;

    /* onCreate Method controls content view, possibly actionbar setup
     * and progressBar visibility
     *
     * onResume controls flow whenever cycle of activity
     *
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
                    setListAdapter(adapter);

                    addFriendCheckmarks();

                } else {
                    //OH NO!!! basic AlertDialog + Logging is in this loop
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

    /*
    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }*/


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

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(item.getItemId()){
        case android.R.id.home:
            NavUtils.navigateUpFromSameTask(this);
            return;
        }
        return super.onOptionsItemSelected(item);
    }
     */
}

