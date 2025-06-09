package at.ac.fhcampuswien.fhmdb.patterns.state;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Comparator;

public class SortButtonIdleState implements SortingState {
    private SortButton sortButton;

    public SortButtonIdleState(SortButton sortButton) {
        this.sortButton = sortButton;
    }


    @Override
    public void sortAscending() {
        System.out.println("Switching to ascending sort.");
        sortButton.getObservableMovies().sort(Comparator.comparing(Movie::getTitle));
        sortButton.setState(sortButton.getSortButtonAscendingState());
    }

    @Override
    public void sortDescending() {
        System.out.println("Switching to descending sort.");
        sortButton.getObservableMovies().sort(Comparator.comparing(Movie::getTitle).reversed());
        sortButton.setState(sortButton.getSortButtonDescendingState());
    }

    @Override
    public void returnToIdleState() {
        System.out.println("Selection is alreday in idle state");
    }
}
