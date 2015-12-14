package com.indaco.cs146movieapp;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link ListFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoviesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoviesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviesFragment extends Fragment {

    public static final String TAG = FriendsFragment.class.getSimpleName();

    protected List<ParseUser> mMovies;
    protected ExpandableListView mExpandableListView;
    protected ParseRelation<ParseUser> mMoviesRelation;
    protected ParseUser mCurrentUser;

    private OnFragmentInteractionListener mListener;

    public MoviesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MoviesFragment.
     */

    public static MoviesFragment newInstance() {
        MoviesFragment fragment = new MoviesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //creates the view of all the movies related to the user
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movies,container,false);
        mExpandableListView = (ExpandableListView) view.findViewById(R.id.expandableList);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //get current user logged in and movies related to the user
        mCurrentUser = ParseUser.getCurrentUser();
        mMoviesRelation = mCurrentUser.getRelation(ParseConstants.KEY_MOVIES_RELATION);

        //query the movies in alphabetical order
        ParseQuery<ParseUser> query = mMoviesRelation.getQuery();
        query.addAscendingOrder(ParseConstants.KEY_MOVIE_TITLE);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> movies, ParseException e) {
                if (e == null) {//movies successfully found, proceed to populate list
                    mMovies = movies;

                    ArrayList<String> movieTitles = new ArrayList<String>();
                    ArrayList<String> movieSummaries = new ArrayList<String>();
                    int i=0;
                    for (ParseObject title : mMovies) {
                        movieTitles.add(i, title.getString("Title"));
                        movieSummaries.add(i, title.getString("Description"));
                        i++;
                    }

                    //user custom adapter to load movies
                    MovieAdapter movieAdapter = new MovieAdapter(getActivity(),movieTitles,movieSummaries);
                    mExpandableListView.setAdapter(movieAdapter);

                } else {//couldn't retrieve list
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    //mListener is used to listen when fragment is attached again (user navigates between tabs)
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
