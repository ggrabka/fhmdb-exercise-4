package at.ac.fhcampuswien.fhmdb.patterns.state;

public interface SortingState {
    void sortAscending();
    void sortDescending();
    void returnToIdleState();
}
