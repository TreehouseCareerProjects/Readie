package treehousecareerprojects.readie;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class ReviewActivity extends Activity {
    private Review review;
    private SearchResult result;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        progressBar = (ProgressBar)findViewById(R.id.indeterminateProgressView);
        progressBar.setVisibility(View.VISIBLE);

        result = (SearchResult)
                getIntent().getSerializableExtra(SearchResultActivity.SEARCH_RESULT_ID);

        HttpConnection.sendInBackground(new ReviewPageRequest(result.getReviewUrl()));
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
                review = handler.handle(response.getResponseBody());
                insertReview();
            }
            catch(PageHandlerException e) {
                displayTerminatingRequestError();
            }
        }

        @Override
        public void onFailure(HttpResponse response) {
            progressBar.setVisibility(View.GONE);

            displayTerminatingRequestError();
        }
    }
}
