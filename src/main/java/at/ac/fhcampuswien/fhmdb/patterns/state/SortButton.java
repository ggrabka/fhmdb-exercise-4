package at.ac.fhcampuswien.fhmdb.patterns.state;

import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.models.SortedState;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class SortButton {

    @FXML
    public JFXButton sortBtn;

    private SortingState sortButtonAscendingState;
    private SortingState sortButtonDescendingState;
    private SortingState sortButtonIdleState;

    private SortingState currentState;
    private ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

    public SortButton() {
        sortButtonIdleState = new SortButtonIdleState(this);
        sortButtonAscendingState = new SortButtonAscendingState(this);
        sortButtonDescendingState = new SortButtonDescendingState(this);

        currentState = sortButtonIdleState;
    }

    public SortingState getCurrentState() {
        return currentState;
    }

    public ObservableList<Movie> getObservableMovies() {
        return observableMovies;
    }

    public SortingState getSortButtonAscendingState() {
        return sortButtonAscendingState;
    }

    public SortingState getSortButtonDescendingState() {
        return sortButtonDescendingState;
    }

    public SortingState getSortButtonIdleState() {
        return sortButtonIdleState;
    }

    public void setState(SortingState state) {
        currentState = state;
    }

    public void sortAscending() {
        currentState.sortAscending();
    }

    public void sortDescending() {
        currentState.sortDescending();
    }

    public void returnToIdleState() {
        currentState.returnToIdleState();
    }
}
