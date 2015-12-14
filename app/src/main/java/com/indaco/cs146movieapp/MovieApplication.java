package com.indaco.cs146movieapp;

import android.app.Application;
import com.parse.Parse;

/**
 * Created by stefan on 11/29/2015.
 * This class is used to initiate any external libraries/dependencies like parse
 * Initialize with keys
 */
public class MovieApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "NNw69tcZKjypT1sNJgSsiwbtK1V1suIlHVbcuvMR", "CZEbJRtXJLsinmlkjbn1bs1F6XAZfXlDuVwu0WgV");
    }
}