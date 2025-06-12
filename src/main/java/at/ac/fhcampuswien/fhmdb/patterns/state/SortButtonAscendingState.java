package at.ac.fhcampuswien.fhmdb.patterns.state;

import at.ac.fhcampuswien.fhmdb.models.Movie;

import java.util.Comparator;

public class SortButtonAscendingState implements SortingState {
    private final SortButton sortButton;

    public SortButtonAscendingState(SortButton sortButton) {
        this.sortButton = sortButton;
    }

    @Override
    public void sortAscending() {
        System.out.println("Selection is already sorted ascending.");
    }

    @Override
    public void sortDescending() {
        System.out.println("Switching to descending sort.");
        sortButton.getObservableMovies().sort(Comparator.comparing(Movie::getTitle).reversed());
        sortButton.setState(sortButton.getSortButtonDescendingState());
    }

    @Override
    public void returnToIdleState() {
        System.out.println("Returning to idle state.");
        sortButton.setState(sortButton.getSortButtonIdleState());
    }


}
