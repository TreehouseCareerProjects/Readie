package treehousecareerprojects.readie;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import treehousecareerprojects.readie.adapter.SearchResultAdapter;
import treehousecareerprojects.readie.http.HttpConnection;
import treehousecareerprojects.readie.http.request.RecommendationsRequest;
import treehousecareerprojects.readie.model.SearchResult;
import treehousecareerprojects.readie.view.MainActionBar;


public class MainActivity extends ActionBarActivity {
    public static final String SEARCH_QUERY_ID = "search";

    private SearchResultAdapter recommendationsAdapter;
    private List<SearchResult> recommendations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainActionBar actionBar = new MainActionBar(this, getSupportActionBar());
        actionBar.assembleActionBar();

        recommendations = new ArrayList<>();
        recommendationsAdapter = new SearchResultAdapter(this, recommendations);
        ((ListView)findViewById(R.id.recommendationList)).setAdapter(recommendationsAdapter);

        HttpConnection.sendInBackground(new RecommendationsRequest(this, getString(R.string.api_key)));
    }

    public void updateRecommendations(List<SearchResult> recommendations) {
        for(int i = 0; i < recommendations.size(); i++)
            this.recommendations.add(i, recommendations.get(i));

        recommendationsAdapter.notifyDataSetChanged();
    }

    public void hideRecommendations() {
        findViewById(R.id.recommendationList).setVisibility(View.INVISIBLE);
        findViewById(R.id.recommendationLabel).setVisibility(View.INVISIBLE);
    }
}
