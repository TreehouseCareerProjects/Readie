package treehousecareerprojects.readie.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import treehousecareerprojects.readie.model.Review;

/**
 * Created by Dan on 2/16/2015.
 */
public class ReviewPageHandler extends PageHandler {
    private static final Pattern bodyPattern = Pattern.compile("([^©]*)");

    private final String bodySelector;

    private Document document;

    public ReviewPageHandler(String bodySelector) {
        this.bodySelector = bodySelector;
    }

    public ReviewPageHandler(PageHandlerUSAT type) {
        this(type.getBodySelector());
    }

    @Override
    public void handle(File page, Review review) {
        readDocument(page);

        if(canPageBeHandled(page))
            review.set(extractReview(page));
        else
            super.handle(page, review);
    }

    private void readDocument(File page) {
        try {
            document = Jsoup.parse(page, "UTF-8");
        }
        catch(IOException e) {
            throw new IllegalStateException("Page couldn\'t be parsed. Review can no longer be extracted.");
        }
    }

    private boolean canPageBeHandled(File page) {
        boolean canFindBody = document.select(bodySelector).first() != null;

        return canFindBody;
    }

    private String cleanBodyString(String bodyString) {
        Matcher result = bodyPattern.matcher(bodyString);
        result.find();

        return result.group(1).trim();
    }

    private Review extractReview(File page) {
        Review review = new Review();
        StringBuilder bodyText = new StringBuilder("");

        Elements body = document.select(bodySelector);

        for(Element element : body)
            bodyText.append(element.text() + " ");

        review.setBody(cleanBodyString(bodyText.toString()));

        return review;
    }
}
