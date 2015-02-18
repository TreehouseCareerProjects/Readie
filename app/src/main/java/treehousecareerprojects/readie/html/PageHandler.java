package treehousecareerprojects.readie.html;

import treehousecareerprojects.readie.model.Review;

/**
 * Created by Dan on 2/16/2015.
 */

abstract class PageHandler {
    private PageHandler next;

    public void handle(String page, Review review) {
        if(next != null)
            next.handle(page, review);
        else
            throw new RuntimeException("No handler can parse the page.");
    }

    public void register(PageHandler handler) {
        if(next != null)
            next.register(handler);
        else
            next = handler;
    }
}
