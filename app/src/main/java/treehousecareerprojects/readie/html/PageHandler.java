package treehousecareerprojects.readie.html;

import treehousecareerprojects.readie.R;
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
            throw new PageHandlerException(R.string.page_handler_exception_message);
    }

    public void register(PageHandler handler) {
        if(next != null)
            next.register(handler);
        else
            next = handler;
    }
}
