package treehousecareerprojects.readie.html;

import treehousecareerprojects.readie.ReadieApplication;

/**
 * Created by Dan on 2/27/2015.
 */
public class PageHandlerException extends RuntimeException {
    private int stringId;

    public PageHandlerException(int stringId) {
        this.stringId = stringId;
    }

    @Override
    public String getMessage() {
        return ReadieApplication.getContext().getString(stringId);
    }
}
