package treehousecareerprojects.readie.html;

import treehousecareerprojects.readie.model.Review;

/**
 * Created by Dan on 2/16/2015.
 */

abstract class PageHandler {
    private PageHandler next;

    public Review handle(String page) {
        if(next != null)
            return next.handle(page);
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
