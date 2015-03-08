package treehousecareerprojects.readie.model;

import java.util.Comparator;

/**
 * Created by Dan on 3/4/2015.
 */
public class SearchResultComparator implements Comparator<SearchResult> {
    private SortBy order;

    public enum SortBy {TITLE, AUTHOR}

    public SearchResultComparator(SortBy order) {
        this.order = order;
    }

    private int compareByAuthor(SearchResult lhs, SearchResult rhs) {
        return lhs.getAuthor().compareTo(rhs.getAuthor());
    }

    private int compareByTitle(SearchResult lhs, SearchResult rhs) {
        return lhs.getBookTitle().compareTo(rhs.getBookTitle());
    }

    @Override
    public int compare(SearchResult lhs, SearchResult rhs) {
        if(order == SortBy.AUTHOR)
            return compareByAuthor(lhs, rhs);
        else
            return compareByTitle(lhs, rhs);
    }
}
