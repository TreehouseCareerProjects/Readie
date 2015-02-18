package treehousecareerprojects.readie.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import treehousecareerprojects.readie.model.Review;

/**
 * Created by Dan on 2/16/2015.
 */
public class ReviewPageHandler extends PageHandler {
    private static final Pattern bodyPattern = Pattern.compile("([^©]*)");
    private static final int BODY_REGEX_ID = 1;

    private final String bodySelector;

    private Document document;

    public ReviewPageHandler(String bodySelector) {
        this.bodySelector = bodySelector;
    }

    public ReviewPageHandler(PageHandlerUSAT type) {
        this(type.getBodySelector());
    }

    @Override
    public void handle(String page, Review review) {
        readDocument(page);

        if(canPageBeHandled(page))
            review.set(extractReview(page));
        else
            super.handle(page, review);
    }

    private void readDocument(String page) {
        document = Jsoup.parse(page, "UTF-8");
    }

    private boolean canPageBeHandled(String page) {
        boolean canFindBody = document.select(bodySelector).first() != null;

        return canFindBody;
    }

    private String cleanBodyString(String bodyString) {
        Matcher result = bodyPattern.matcher(bodyString);
        result.find();

        return result.group(BODY_REGEX_ID).trim();
    }

    private Review extractReview(String page) {
        Review review = new Review();
        StringBuilder bodyText = new StringBuilder("");

        Elements body = document.select(bodySelector);

        for(Element element : body)
            bodyText.append(element.text() + " ");

        review.setBody(cleanBodyString(bodyText.toString()));

        return review;
    }
}
