package at.ac.fhcampuswien.fhmdb.patterns.state;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Comparator;

public class SortButtonDescendingState implements SortingState {
    private SortButton sortButton;

    public SortButtonDescendingState(SortButton sortButton) {
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
        System.out.println("Selection is already sorted descending.");
    }

    @Override
    public void returnToIdleState() {
        System.out.println("Returning to idle state.");
        sortButton.setState(sortButton.getSortButtonIdleState());
    }
}
