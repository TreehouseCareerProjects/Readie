package treehousecareerprojects.readie.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import treehousecareerprojects.readie.R;
import treehousecareerprojects.readie.ReadieApplication;
import treehousecareerprojects.readie.http.HttpConnection;
import treehousecareerprojects.readie.http.HttpMethod;
import treehousecareerprojects.readie.http.HttpRequest;
import treehousecareerprojects.readie.http.HttpResponse;
import treehousecareerprojects.readie.model.SearchResult;

/**
 * Created by Dan on 2/17/2015.
 */
public class SearchResultAdapter extends BaseAdapter {
    private static final String BOOK_COVER_PATH_FORMAT = "http://covers.openlibrary.org/b/ISBN/%s-M.jpg";
    private static final int EMPTY_IMAGE_BYTE_COUNT = 807;

    private Context context;
    private List<SearchResult> searchResults;

    public SearchResultAdapter(Context context, List<SearchResult> searchResults) {
        this.context = context;
        this.searchResults = searchResults;

        requestBookCovers();
    }

    private void requestBookCovers() {
        for(SearchResult result : searchResults)
            HttpConnection.sendInBackground(
                    new BookCoverRequest(formatBookCoverPath(result.getIsbn()), result));
    }

    private String formatBookCoverPath(String isbn) {
        return String.format(BOOK_COVER_PATH_FORMAT, isbn);
    }

    private void assignBookCover(SearchResult result, ViewHolder holder) {
        final Context context = ReadieApplication.getContext();
        final byte[] imageData = result.getBookCoverMedium();

        final boolean isNull = imageData == null;
        final boolean wasNotFound = !isNull && imageData.length == EMPTY_IMAGE_BYTE_COUNT; //Short circuit to avoid null pointer exception.

        if(wasNotFound || isNull) {
            Drawable defaultImage = context.getResources().getDrawable(R.drawable.ic_launcher);
            holder.bookCover.setImageDrawable(defaultImage);
        }
        else {
            Bitmap image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
            holder.bookCover.setImageBitmap(image);
        }
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
        ViewHolder holder;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.search_result_item, null);

            holder = new ViewHolder();
            holder.bookCover = (ImageView)convertView.findViewById(R.id.bookCoverImage);
            holder.bookTitle = (TextView)convertView.findViewById(R.id.bookTitleLabel);
            holder.author = (TextView)convertView.findViewById(R.id.bookAuthorLabel);
            holder.snippet = (TextView)convertView.findViewById(R.id.reviewSnippetLabel);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        SearchResult result = searchResults.get(position);

        assignBookCover(result, holder);
        holder.bookTitle.setText(result.getBookTitle());
        holder.author.setText("By: " + result.getAuthor());
        holder.snippet.setText(result.getReviewSnippet());

        return convertView;
    }

    static class ViewHolder {
        ImageView bookCover;
        TextView bookTitle;
        TextView author;
        TextView snippet;
    }

    private class BookCoverRequest extends HttpRequest {
        private SearchResult searchResult;

        public BookCoverRequest(String url, SearchResult searchResult) {
            super(HttpMethod.GET, url);
            this.searchResult = searchResult;
        }

        @Override
        public void onSuccess(HttpResponse response) {
            byte[] image = response.getResponseBody();
            searchResult.setBookCoverMedium(response.getResponseBody());

            notifyDataSetChanged();
        }

        @Override
        public void onFailure(HttpResponse response) {}
    }
}
