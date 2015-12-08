package com.indaco.cs146movieapp;

import android.app.AlertDialog;
import android.content.Context;
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
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link ListFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoviesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesFragment extends ListFragment {

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    public static final String TAG = FriendsFragment.class.getSimpleName();

    protected List<ParseUser> mMovies;
    protected ParseRelation<ParseUser> mMoviesRelation;
    protected ParseUser mCurrentUser;
    //protected ParseObject mMovie = new ParseObject("Movies");


    private OnFragmentInteractionListener mListener;

    public MoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MoviesFragment.
     */
    // TODO: Rename and change types and number of parameters
    /*
    public static MoviesFragment newInstance(String param1, String param2) {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();

        /*
        ArrayList<String> movies = new ArrayList<String>();
        String line;
        InputStream inputStream = getResources().openRawResource(R.raw.movie_titles);

        try {//try to read the file from res/raw/movie_titles.txt
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            if(!reader.ready()){
                throw new IOException("Reader was not ready");
            }

            //Populate the ArrayList with movies
            while((line=reader.readLine()) != null){
                movies.add(line);
            }

            //Remember to close all I/O readers once you're done using them!
            reader.close();
            inputStream.close();
        }
        //if you cannot find the file, throw an Exception
        catch (FileNotFoundException e){
            //e.printStackTrace();
            System.out.println(TAG+": Can't find the file: "+e);
        }

        catch (IOException e) {//if there is an error reading the file, throw an IO Exception
            //e.printStackTrace();
            System.out.println(TAG+": IOException : "+e);
        }

        //Sort the movies in alphabetical order
        Collections.sort(movies, new Comparator<String>(){
            @Override
            public int compare(String s1, String s2){
                return s1.compareToIgnoreCase(s2);
            }
        });

        //Populate the UI list to be visible to user using adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(),
                android.R.layout.simple_list_item_1,movies);
        setListAdapter(adapter);
        */

        mCurrentUser = ParseUser.getCurrentUser();
        mMoviesRelation = mCurrentUser.getRelation(ParseConstants.KEY_MOVIES_RELATION);

        ParseQuery<ParseUser> query = mMoviesRelation.getQuery();
        query.addAscendingOrder(ParseConstants.KEY_MOVIE_TITLE);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> movies, ParseException e) {
                if (e == null) {//movies successfully found, proceed to populate list
                    mMovies = movies;



                    ArrayList<String> movieTitles = new ArrayList<String>();
                    int i=0;
                    for (ParseObject title : mMovies) {
                        movieTitles.add(i, title.getString("Title"));
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getListView().getContext(),
                            android.R.layout.simple_list_item_1,
                            movieTitles);
                    setListAdapter(adapter);
                } else {//couldn't retrieve list
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
