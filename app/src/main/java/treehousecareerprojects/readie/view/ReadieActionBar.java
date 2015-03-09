package treehousecareerprojects.readie.view;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.lang.reflect.Field;

import treehousecareerprojects.readie.R;

/**
 * Created by Dan on 3/8/2015.
 */
//TODO: Require callback map to action bar icons.
public abstract class ReadieActionBar {
    protected Activity activity;
    protected ActionBar actionBar;

    public ReadieActionBar(Activity activity, ActionBar actionBar) {
        this.activity = activity;
        this.actionBar = actionBar;
    }

    protected void init(int layout) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View actionBarView = inflater.inflate(layout, null);

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarView);
    }

    protected void rewriteSearchView(int searchId) {
        SearchView searchView = (SearchView)activity.findViewById(searchId);

        try {
            Field field = SearchView.class.getDeclaredField("mSearchButton");
            field.setAccessible(true);
            ImageView icon = (ImageView)field.get(searchView);
            icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_search));

            field = SearchView.class.getDeclaredField("mCloseButton");
            field.setAccessible(true);
            icon = (ImageView)field.get(searchView);
            icon.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_action_cancel));

            field = SearchView.class.getDeclaredField("mQueryTextView");
            field.setAccessible(true);
            SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)field.get(searchView);
            searchAutoComplete.setTextColor(activity.getResources().getColor(R.color.bianca));
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public abstract void assembleActionBar();
}
