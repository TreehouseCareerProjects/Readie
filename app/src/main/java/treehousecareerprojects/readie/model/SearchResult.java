package treehousecareerprojects.readie.model;

/**
 * Created by Dan on 2/17/2015.
 */
public class SearchResult {
    public static final String REVIEW_ARRAY_JSON_ID = "BookReviews";

    public static final String REVIEWER_JSON_ID = "Reviewer";
    public static final String BOOK_TITLE_JSON_ID = "Title";
    public static final String REVIEW_SNIPPET_JSON_ID = "Brief";
    public static final String REVIEW_URL_JSON_ID = "ReviewUrl";

    private String reviewer;
    private String bookTitle;
    private String reviewSnippet;
    private String reviewUrl;

    public SearchResult(String reviewer, String bookTitle, String reviewSnippet, String reviewUrl) {
        this.reviewer = reviewer;
        this.bookTitle = bookTitle;
        this.reviewSnippet = reviewSnippet;
        this.reviewUrl = reviewUrl;
    }

    public SearchResult() {
        this("", "", "", "");
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getReviewSnippet() {
        return reviewSnippet;
    }

    public void setReviewSnippet(String reviewSnippet) {
        this.reviewSnippet = reviewSnippet;
    }

    public String getReviewUrl() {
        return reviewUrl;
    }

    public void setReviewUrl(String reviewUrl) {
        this.reviewUrl = reviewUrl;
    }
}
