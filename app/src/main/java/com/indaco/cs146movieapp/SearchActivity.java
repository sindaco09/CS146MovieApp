package com.indaco.cs146movieapp;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class SearchActivity extends ListActivity {

    //Member Variables, Tag for debugging, searchResult (textView if nothing is found)
    //stringList to hold list of all imported items to search
    private static final String TAG = SearchActivity.class.getSimpleName();

    private TextView searchResult;

    protected ArrayList<String> mStringList = new ArrayList<>();

    //set the Views for the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchResult = (TextView) findViewById(R.id.search_result);
        searchResult.setMovementMethod(new ScrollingMovementMethod());
        Log.d(TAG,"Entered onCreate method");

        handleIntent(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "event <onPause> called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "event <onRestart> called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "event <onResume> called");
    }

    //pass in new intent to global search activity
    @Override
    protected void onNewIntent(Intent intent){
        Log.i(TAG,"Entered onNewIntent method");
        setIntent(intent);
        handleIntent(intent);
    }

    //pass in the query to a local variable and perform search function
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleIntent(Intent intent) {
        Log.i(TAG,"Entered handleIntent method");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            if(Objects.equals(query, "stefan")){
                Toast.makeText(SearchActivity.this, "you typed stefan!", Toast.LENGTH_SHORT).show();
            }

            Bundle appData = getIntent().getBundleExtra(SearchManager.APP_DATA);

            doMySearch(query);
        }
    }

    //main search function, due to time constraints, list is hardcoded specifically for
    //EditMoviesFragment only
    private void doMySearch(String query) {
        Log.i(TAG,"doMySearch.....");
        createList();
        ArrayList<String> resultsList = new ArrayList<>();
        for(String title:mStringList){
            if(title.equals(query)){
                resultsList.add(query);
                Toast.makeText(SearchActivity.this, "Found it!", Toast.LENGTH_SHORT).show();

                //use adapter to pass raw data to a UI as displayable objects
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchActivity.this,
                        android.R.layout.simple_list_item_checked,
                        resultsList);
                setListAdapter(adapter);
            }
        }
        if(resultsList==null){
            searchResult.setText("Sorry, nothing found for query: "+query);
        }

    }

    public void createList(){
        //method to take in a Raw\movie_titles.txt file and pass it through to a list

        InputStream inputStream = this.getResources().openRawResource(R.raw.movie_titles);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        try{
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


}
