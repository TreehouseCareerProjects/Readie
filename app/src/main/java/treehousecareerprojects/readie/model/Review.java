package treehousecareerprojects.readie.model;

/**
 * Created by Dan on 2/16/2015.
 */
public class Review {
    private String body;
    private String author;

    public String getBody() {
        return body;
    }

    public String getAuthor() {
        return author;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void set(Review review) {
        body = review.getBody();
        author = review.getAuthor();
    }
}
