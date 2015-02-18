package treehousecareerprojects.readie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import treehousecareerprojects.readie.R;
import treehousecareerprojects.readie.model.SearchResult;

/**
 * Created by Dan on 2/17/2015.
 */
public class SearchResultAdapter extends BaseAdapter {
    private Context context;
    private List<SearchResult> searchResults;

    public SearchResultAdapter(Context context, List<SearchResult> searchResults) {
        this.context = context;
        this.searchResults = searchResults;
    }

    @Override
    public int getCount() {
        return searchResults.size();
    }

    @Override
    public Object getItem(int position) {
        return searchResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_result_item, null);
        }

        ((TextView)convertView.findViewById(R.id.bookTitleTextView)).setText(searchResults.get(position).getBookTitle());
        ((TextView)convertView.findViewById(R.id.reviewerTextView)).setText(searchResults.get(position).getReviewer());
        ((TextView)convertView.findViewById(R.id.snippetTextView)).setText(searchResults.get(position).getReviewSnippet());

        return convertView;
    }
}
