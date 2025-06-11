package at.ac.fhcampuswien.fhmdb.patterns.observer;

import at.ac.fhcampuswien.fhmdb.database.DataBaseException;
import at.ac.fhcampuswien.fhmdb.database.WatchlistMovieEntity;
import at.ac.fhcampuswien.fhmdb.models.Movie;

public interface Observable {
    void notifyObservers(WatchlistMovieEntity movie) throws DataBaseException;
}
