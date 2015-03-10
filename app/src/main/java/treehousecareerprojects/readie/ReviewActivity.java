package treehousecareerprojects.readie;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import treehousecareerprojects.readie.adapter.SearchResultAdapter;
import treehousecareerprojects.readie.http.HttpConnection;
import treehousecareerprojects.readie.http.request.ReviewRequest;
import treehousecareerprojects.readie.model.Review;
import treehousecareerprojects.readie.model.SearchResult;
import treehousecareerprojects.readie.view.ReviewActionBar;

public class ReviewActivity extends ActionBarActivity {
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        progressBar = (ProgressBar)findViewById(R.id.indeterminateProgressView);
        progressBar.setVisibility(View.VISIBLE);

        ReviewActionBar actionBar = new ReviewActionBar(this, getSupportActionBar());
        actionBar.assembleActionBar();

        SearchResult result = (SearchResult)
                getIntent().getSerializableExtra(SearchResultActivity.SEARCH_RESULT_ID);

        HttpConnection.sendInBackground(new ReviewRequest(this, result));
    }

    public void toggleProgressBar() {
        if(progressBar.getVisibility() == View.VISIBLE)
            progressBar.setVisibility(View.INVISIBLE);
        else
            progressBar.setVisibility(View.VISIBLE);
    }

    public void insertReview(Review review, SearchResult result) {
        TextView authorLabel = (TextView)findViewById(R.id.authorLabel);
        TextView descriptionLabel = (TextView)findViewById(R.id.reviewerLabel);
        TextView titleLabel = (TextView)findViewById(R.id.titleLabel);
        TextView reviewLabel = (TextView)findViewById(R.id.reviewLabel);

        String reviewer = result.getReviewer();
        descriptionLabel.setText(reviewer.equals("") ? "" : "Review by: " + reviewer);
        authorLabel.setText("by " + result.getAuthor());
        titleLabel.setText(result.getBookTitle());
        reviewLabel.setText(review.getBody());

        decodeImage(result.getBookCoverMedium());
    }

    private void decodeImage(byte[] imageData) {
        ImageView bookCover = (ImageView)findViewById(R.id.reviewBookCover);

        final boolean isNull = imageData == null;
        final boolean wasNotFound = !isNull && imageData.length == SearchResultAdapter.EMPTY_IMAGE_BYTE_COUNT;

        if(wasNotFound || isNull)
            bookCover.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
        else
            bookCover.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
    }
}
