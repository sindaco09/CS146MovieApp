package com.indaco.cs146movieapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by stefan on 12/8/2015.
 */
public class MovieAdapter extends BaseExpandableListAdapter{

    //Declare Member Variables
    //Group = Movie titles, Children = Movie Summaries
    public ArrayList<String> mGRP_Titles, mCHLD_Summaries;
    public ArrayList<Object> Childtem = new ArrayList<Object>();

    //attempt to use objects instead of string
    public ArrayList<MovieDemo> mMovieDemos;

    public LayoutInflater mLayoutInflater;
    public Activity mActivity;
    public Context mContext;

    //Constructor
    public MovieAdapter(Context context,ArrayList<String> titles,ArrayList<String> summaries){
        mGRP_Titles =titles;
        mCHLD_Summaries = summaries;
        this.mContext = context;
    }
    public MovieAdapter(ArrayList<MovieDemo> movieDemo){
        mMovieDemos=movieDemo;
    }

    public void setInflater(LayoutInflater mInflater, Activity act) {
        this.mLayoutInflater = mInflater;
        mActivity = act;
    }

    @Override
    public int getGroupCount() {
        return mGRP_Titles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.parent_layout, parent, false);
        }
        TextView parentTextView = (TextView) convertView.findViewById(R.id.textViewParent);
        parentTextView.setText(mGRP_Titles.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition,
                             int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.child_layout, parent, false);
        }
        TextView childTextView = (TextView) convertView.findViewById(R.id.textViewChild);
        childTextView.setText(mCHLD_Summaries.get(groupPosition));
        return convertView;
    }


    /*@Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.explist_group_title,null);
        }
        ((CheckedTextView) convertView).setText(mGRP_Titles.get(groupPosition));
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //Here it seems we do... everything
        //mCHLD_Summaries = (ArrayList<String>) mGRP_Titles.get(groupPosition);
        TextView text = null;
        if(convertView==null){
            convertView = mLayoutInflater.inflate(R.layout.explist_child_summary,null);
        }
        text = (TextView) convertView.findViewById(R.id.title);
        text.setText(mCHLD_Summaries.get(groupPosition));

        return convertView;
    }*/

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
