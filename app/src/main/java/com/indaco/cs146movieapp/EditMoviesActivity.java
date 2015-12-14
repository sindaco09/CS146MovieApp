package com.indaco.cs146movieapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class EditMoviesActivity extends AppCompatActivity {

    public static final String TAG = EditMoviesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movies);
    }

    //actionbar creation, Using Search Widget if Search icon is selected
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);

        //set search manager to use built-in service SEARCH_SERVICE
        //then set view (searchView) and display it from the id ic_action_search (visible search button)
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.ic_action_search).getActionView();

        //set the searchview to display a hint for what user should search for
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        //show search button, not as an item in dropdown menu
        searchView.setIconified(false);

        searchManager.setOnCancelListener(new SearchManager.OnCancelListener(){
            @Override
            public void onCancel(){
                Log.d(TAG,"event <setOnCancelListener> called");
            }
        });

        searchManager.setOnDismissListener(new SearchManager.OnDismissListener() {
            @Override
            public void onDismiss() {
                Log.d(TAG, "event <setOnDismissListener> called");
            }
        });
        return true;
    }

    //set what happens when either the search icon is pressed or the settings is selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();

        switch(id){
            case R.id.action_settings:
                Intent intent = new Intent(EditMoviesActivity.this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.ic_action_search:
                onSearchRequested();
                return true;
            default:
                return false;
        }
    }

    //overriding the onSearchRequested to log where the search is coming from
    //Only need 1 searchActivity per android project, so pass correct data to it
    @Override
    public boolean onSearchRequested() {
        Log.d(TAG,"event <onSearchRequested> called");

        Bundle appData = new Bundle();
        appData.putString("Come from",TAG);
        startSearch(null,false,appData,false);
        return true;
    }
}
