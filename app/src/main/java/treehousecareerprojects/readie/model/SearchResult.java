package treehousecareerprojects.readie.model;

import java.io.Serializable;

/**
 * Created by Dan on 2/17/2015.
 */
public class SearchResult implements Serializable {
    public static final String REVIEW_ARRAY_JSON_ID = "BookReviews";

    public static final String REVIEWER_JSON_ID = "Reviewer";
    public static final String AUTHOR_JSON_ID = "Author";
    public static final String BOOK_TITLE_JSON_ID = "Title";
    public static final String REVIEW_SNIPPET_JSON_ID = "Brief";
    public static final String REVIEW_URL_JSON_ID = "ReviewUrl";
    public static final String BATCH_REVIEW_STATUS_JSON_ID = "BatchReview";
    public static final String AUDIO_REVIEW_STATUS_JSON_ID = "AudioBook";
    public static final String ISBN_JSON_ID = "ISBN";

    private String reviewer;
    private String author;
    private String bookTitle;
    private String reviewSnippet;
    private String reviewUrl;
    private String isbn;
    private byte[] bookCoverMedium;

    public SearchResult(String reviewer,
                        String author,
                        String bookTitle,
                        String reviewSnippet,
                        String reviewUrl,
                        String isbn,
                        byte[] bookCoverMedium) {
        this.reviewer = reviewer;
        this.author = author;
        this.bookTitle = bookTitle;
        this.reviewSnippet = reviewSnippet;
        this.reviewUrl = reviewUrl;
        this.isbn = isbn;
        this.bookCoverMedium = bookCoverMedium;
    }

    public SearchResult() {
        this("", "", "", "", "", "", null);
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public byte[] getBookCoverMedium() {
        return bookCoverMedium;
    }

    public void setBookCoverMedium(byte[] bookCoverMedium) {
        this.bookCoverMedium = bookCoverMedium;
    }

}
