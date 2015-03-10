package treehousecareerprojects.readie.http.request;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

import treehousecareerprojects.readie.R;
import treehousecareerprojects.readie.ReviewActivity;
import treehousecareerprojects.readie.dialog.ErrorDialogFragment;
import treehousecareerprojects.readie.html.PageHandler;
import treehousecareerprojects.readie.html.PageHandlerException;
import treehousecareerprojects.readie.html.PageHandlerUSAT;
import treehousecareerprojects.readie.html.ReviewPageHandler;
import treehousecareerprojects.readie.http.HttpMethod;
import treehousecareerprojects.readie.http.HttpRequest;
import treehousecareerprojects.readie.http.HttpResponse;
import treehousecareerprojects.readie.model.Review;
import treehousecareerprojects.readie.model.SearchResult;

/**
 * Created by Dan on 3/10/2015.
 */
public class ReviewRequest extends HttpRequest {
    private ReviewActivity context;
    private SearchResult result;

    public ReviewRequest(ReviewActivity context, SearchResult result) {
        super(HttpMethod.GET, result.getReviewUrl());

        this.context = context;
        this.result = result;
    }

    @Override
    public void onSuccess(HttpResponse response) {
        context.toggleProgressBar();

        PageHandler handler = new ReviewPageHandler(PageHandlerUSAT.EARLY_2000s);
        handler.register(new ReviewPageHandler(PageHandlerUSAT.LATE_2000s));
        handler.register(new ReviewPageHandler(PageHandlerUSAT.EARLY_2013));
        handler.register(new ReviewPageHandler(PageHandlerUSAT.LATE_2013));

        try {
            Review review = handler.handle(IOUtils.toString(response.getResponseBody(), "UTF-8"));
            context.insertReview(review, result);
        }
        catch(IOException | PageHandlerException e){
            ErrorDialogFragment.displayTerminatingErrorDialog(
                    context,
                    R.string.review_page_error_title,
                    R.string.review_page_error_message,
                    "review_page_error_dialog");
        }

    }

    @Override
    public void onFailure(HttpResponse response) {
        context.toggleProgressBar();

        ErrorDialogFragment.displayTerminatingErrorDialog(
                context,
                R.string.review_page_error_title,
                R.string.review_page_error_message,
                "review_page_error_dialog");
    }
}
