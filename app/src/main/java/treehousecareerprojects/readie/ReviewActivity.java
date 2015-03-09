package treehousecareerprojects.readie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

import treehousecareerprojects.readie.dialog.ErrorDialogFragment;
import treehousecareerprojects.readie.html.PageHandler;
import treehousecareerprojects.readie.html.PageHandlerException;
import treehousecareerprojects.readie.html.PageHandlerUSAT;
import treehousecareerprojects.readie.html.ReviewPageHandler;
import treehousecareerprojects.readie.http.HttpConnection;
import treehousecareerprojects.readie.http.HttpMethod;
import treehousecareerprojects.readie.http.HttpRequest;
import treehousecareerprojects.readie.http.HttpResponse;
import treehousecareerprojects.readie.model.Review;
import treehousecareerprojects.readie.model.SearchResult;
import treehousecareerprojects.readie.view.ReviewActionBar;

public class ReviewActivity extends ActionBarActivity {
    private static final String BOOK_COVER_PATH_FORMAT =
            "http://covers.openlibrary.org/b/ISBN/%s-M.jpg";

    private Review review;
    private SearchResult result;
    private ProgressBar progressBar;
    private ReviewActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        progressBar = (ProgressBar)findViewById(R.id.indeterminateProgressView);
        progressBar.setVisibility(View.VISIBLE);
        actionBar = new ReviewActionBar(this, getSupportActionBar());
        actionBar.assembleActionBar();

        result = (SearchResult)
                getIntent().getSerializableExtra(SearchResultActivity.SEARCH_RESULT_ID);

        HttpConnection.sendInBackground(new ReviewPageRequest(result.getReviewUrl()));
        HttpConnection.sendInBackground(new BookCoverRequest(formatBookCoverPath(result.getIsbn())));
    }

    private String formatBookCoverPath(String isbn) {
        return String.format(BOOK_COVER_PATH_FORMAT, isbn);
    }

    private void insertReview() {
        TextView authorLabel = (TextView)findViewById(R.id.authorLabel);
        TextView descriptionLabel = (TextView)findViewById(R.id.descriptionLabel);
        TextView titleLabel = (TextView)findViewById(R.id.titleLabel);
        TextView reviewLabel = (TextView)findViewById(R.id.reviewLabel);

        String reviewer = result.getReviewer();

        descriptionLabel.setText(reviewer.equals("") ? "" : "Review by: " + reviewer);
        titleLabel.setText(result.getBookTitle());
        authorLabel.setText(result.getAuthor());
        reviewLabel.setText(review.getBody());
    }

    private void displayTerminatingRequestError() {
        Bundle dialogArgs = new Bundle();
        dialogArgs.putInt(ErrorDialogFragment.TITLE_ID, R.string.review_page_error_title);
        dialogArgs.putInt(ErrorDialogFragment.MESSAGE_ID, R.string.review_page_error_message);
        dialogArgs.putBoolean(ErrorDialogFragment.TERMINATE_ID, true);

        ErrorDialogFragment errorDialog = new ErrorDialogFragment();
        errorDialog.setArguments(dialogArgs);
        errorDialog.show(getFragmentManager(), "review_page_error_dialog");
    }

    private class ReviewPageRequest extends HttpRequest {
        public ReviewPageRequest(String url) {
            super(HttpMethod.GET, url);
        }

        @Override
        public void onSuccess(HttpResponse response) {
            progressBar.setVisibility(View.GONE);

            PageHandler handler = new ReviewPageHandler(PageHandlerUSAT.EARLY_2000s);
            handler.register(new ReviewPageHandler(PageHandlerUSAT.LATE_2000s));
            handler.register(new ReviewPageHandler(PageHandlerUSAT.EARLY_2013));
            handler.register(new ReviewPageHandler(PageHandlerUSAT.LATE_2013));

            try {
                review = handler.handle(IOUtils.toString(response.getResponseBody(), "UTF-8"));
                insertReview();
            }
            catch(IOException | PageHandlerException e){
                displayTerminatingRequestError();
            }
        }

        @Override
        public void onFailure(HttpResponse response) {
            progressBar.setVisibility(View.GONE);

            displayTerminatingRequestError();
        }
    }

    private class BookCoverRequest extends HttpRequest {
        public BookCoverRequest(String url) {
            super(HttpMethod.GET, url);
        }

        @Override
        public void onSuccess(HttpResponse response) {
            byte[] image = response.getResponseBody();
            Bitmap cover = BitmapFactory.decodeByteArray(image, 0, image.length);

           ((ImageView)findViewById(R.id.testImageView)).setImageBitmap(cover);
        }

        @Override
        public void onFailure(HttpResponse response) {}
    }

    private class OnBackAction implements MenuItem.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            finish();

            return false;
        }
    }
}
