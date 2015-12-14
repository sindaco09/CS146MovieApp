package com.indaco.cs146movieapp;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class EditMoviesFragment extends ListFragment {

    //Tag used for debugging
    private static final String TAG = EditMoviesFragment.class.getSimpleName();

    /* Member Variables
     * mList to hold list of all movies
     * mMoviesRelation to hold relation of movie to user
     * mCurrentUser hold who is logged in
     * mRefresh Button is a dev tool used to upload hardcoded movies into parse db
     * mStringList is used to hold a local db of movies from raw/movie_titles.txt
     */
    protected List<ParseObject> mList;
    protected ParseRelation<ParseObject> mMoviesRelation;
    protected ParseUser mCurrentUser;
    protected Button mRefresh;
    protected ArrayList<String> mStringList = new ArrayList<>();

    public EditMoviesFragment() {
        // Required empty public constructor
    }

    //onCreateView returns the completed view of what the user sees on device
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_movies, container, false);
        createList();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
         *        android.R.layout.simple_list_item_checked,
         *        mStringList);
         * setListAdapter(adapter);
         */
    }

    public void createList(){
        //method to take in a Raw\.txt file and pass it through to a list

        InputStream inputStream = this.getResources().openRawResource(R.raw.movie_titles);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        try{ // read each line of movies_titles.txt into ArrayList
            int i=0;
            while((line = bufferedReader.readLine())!=null){
                mStringList.add(line);
                Log.i("LIST","list has: "+mStringList.get(i));
                i++;
            }
        }catch (IOException e){
            Log.i("OOPS!","something went wrong");
        }

        //Sort the movies in alphabetical order
        Collections.sort(mStringList, new Comparator<String>(){
            @Override
            public int compare(String s1, String s2){
                return s1.compareToIgnoreCase(s2);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        //grab currentUser and his/her relation to movies in the movie list
        mCurrentUser = ParseUser.getCurrentUser();
        mMoviesRelation = mCurrentUser.getRelation(ParseConstants.KEY_MOVIES_RELATION);


        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
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

                    //add list of movies which is just a raw ArrayList into an
                    //adapter to be visible in UI
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_checked,
                            movies);
                    setListAdapter(adapter);

                    addMovieCheckmarks();
                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
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

    //add relation of movie Objects to user ParseUser
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
}
