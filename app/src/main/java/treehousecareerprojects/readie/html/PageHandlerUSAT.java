package treehousecareerprojects.readie.html;

/**
 * Created by Dan on 2/16/2015.
 */
public enum PageHandlerUSAT {
    EARLY_2000s("div.intro-copy, p.inside-copy"),
    LATE_2000s("*:not([style]).inside-copy, p.firstParagraph"),
    EARLY_2013("p:not([class])"),
    LATE_2013("p:not([class=\"firstParagraph\"])");

    private final String bodySelector;

    private PageHandlerUSAT(String bodySelector) {
        this.bodySelector = bodySelector;
    }

    public String getBodySelector() {
        return bodySelector;
    }
}
