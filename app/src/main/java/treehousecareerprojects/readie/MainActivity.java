package treehousecareerprojects.readie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class MainActivity extends Activity {
    public static final String SEARCH_QUERY_ID = "search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent searchIntent = new Intent(this, SearchResultActivity.class);
        searchIntent.putExtra(SEARCH_QUERY_ID, "Cell");
        startActivity(searchIntent);
    }
}
