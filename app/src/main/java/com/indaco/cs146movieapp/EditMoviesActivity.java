package com.indaco.cs146movieapp;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class EditMoviesActivity extends ListActivity {

    public static final String TAG = EditMoviesActivity.class.getSimpleName();

    protected List<ParseObject> mList;
    protected ParseRelation<ParseObject> mMoviesRelation;
    protected ParseUser mCurrentUser;
    protected SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movies);

        //allow multiple items to be selected
        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //From Dev Site:
        Intent intent = getIntent();
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            //doMySearch(query);
        }
    }
    @Override
    public boolean onSearchRequested(){
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mMoviesRelation = mCurrentUser.getRelation(ParseConstants.KEY_MOVIES_RELATION);

        //this query returns all the movies in ascending order
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Movies");
        query.orderByAscending(ParseConstants.KEY_MOVIE_TITLE);
        query.setLimit(1000);

        //Query of all the movies in the database
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {//successfully loaded list of movies
                    mList = list;
                    ArrayList<String> movies = new ArrayList<String>();
                    int i = 0;
                    for (ParseObject title : mList) {
                        movies.add(i, title.getString("Title"));
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditMoviesActivity.this,
                            android.R.layout.simple_list_item_checked,
                            movies);
                    setListAdapter(adapter);

                    addMovieCheckmarks();
                }
            }
        });

        //mSearchView.setIconifiedByDefault(false);

        //SearchableInfo searchableInfo = new SearchableInfo()

    }
    private void addMovieCheckmarks(){
        mMoviesRelation.getQuery().findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e==null) {
                    for (int i = 0; i < mList.size(); i++) {
                        ParseObject user = mList.get(i);

                        for (ParseObject movie : list) {
                            if (movie.getObjectId().equals(user.getObjectId())) {
                                //match! - set checkmark!
                                getListView().setItemChecked(i, true);
                            }
                        }
                    }
                } else{
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

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
            mMoviesRelation.add(mList.get(position));
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
            mMoviesRelation.remove(mList.get(position));
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
