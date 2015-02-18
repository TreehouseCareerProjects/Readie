package treehousecareerprojects.readie;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import treehousecareerprojects.readie.model.SearchResult;


public class ReviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // Example
        SearchResult result = (SearchResult)
                getIntent().getSerializableExtra(SearchResultActivity.SEARCH_RESULT_ID);
        String url = "Review picked: " + result.getReviewUrl();

        ((TextView)findViewById(R.id.reviewUrlTextView)).setText(url);
    }
}
