package com.indaco.cs146movieapp;

import java.util.ArrayList;

/**
 * Created by stefan on 12/8/2015.
 */
public class MovieDemo {
    protected ArrayList<String> mTitles= new ArrayList<>();
    protected ArrayList<String> mSummaries = new ArrayList<>();

    public MovieDemo(){

    }
    public void populateList(){
        mTitles.add("Antman");
        mSummaries.add("Some convict is given a suit to shrink him to the size of an ant");
        mTitles.add("Spiderman");
        mSummaries.add("A dorky photographer is bitten by a spider and is spiderman");
        mTitles.add("Superman");
        mSummaries.add("An alien is a reporter but also superhuman, kind of cheap, hates kryptonite");
        mTitles.add("Ironman");
        mSummaries.add("Rich billionaire playboy philanthropist makes a suit of iron");
        mTitles.add("Handyman");
        mSummaries.add("General overall handy guy to have build things");
        mTitles.add("GreenLanturn");
        mSummaries.add("Some alien dies and his powers transfer to a human");
        mTitles.add("Flash");
        mSummaries.add("Guy is struck by lightening and is superfast now");
        mTitles.add("Batman");mSummaries.add("Rich billionair playboy philanthropist becomes a ninja for good");
        mTitles.add("Daredevil");mSummaries.add("A kid is blinded and becomes super in other senses");
        mTitles.add("Watchmen");mSummaries.add("really long movie about people with super powers during cold war");
        mTitles.add("Guardians of the Galaxy");mSummaries.add("A lot of strange people who work together to overcome things");
        mTitles.add("Hitman");mSummaries.add("A hitman who runs around killing the people who set him up");
        mTitles.add("Lord of the Rings");mSummaries.add("A bunch of races who come together to wipe out orcs");
        mTitles.add("Harry Potter");mSummaries.add("A boy who is really a wizard and faces all sorts of obstacles");
        mTitles.add("Game of Thrones");mSummaries.add("Lots of 'kings' fight for a throne");
        mTitles.add("Martian");mSummaries.add("A guy is trapped on mars");
        mTitles.add("Star Wars");mSummaries.add("Battle in the universe between forces of good and evil");
        mTitles.add("Finding Nemo");mSummaries.add("Guy loses his fish son and must go find him now with forgetful companion");
        mTitles.add("Flight");mSummaries.add("A drug addict miraculously lands a plane");
        mTitles.add("Blow");mSummaries.add("Drug dealer who has everything and then loses everything");
    }
    public String getSummary(String title){
        int position = mTitles.indexOf(title);
        return mSummaries.get(position);
    }
    public int size(){
        return mTitles.size();
    }
    public String getTitle(int position){
        return mTitles.get(position);
    }

}
