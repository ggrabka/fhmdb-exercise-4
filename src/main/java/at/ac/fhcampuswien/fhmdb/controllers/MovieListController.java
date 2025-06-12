package at.ac.fhcampuswien.fhmdb.controllers;

import at.ac.fhcampuswien.fhmdb.ClickEventHandler;
import at.ac.fhcampuswien.fhmdb.api.MovieAPI;
import at.ac.fhcampuswien.fhmdb.api.MovieApiException;
import at.ac.fhcampuswien.fhmdb.database.*;
import at.ac.fhcampuswien.fhmdb.models.Genre;
import at.ac.fhcampuswien.fhmdb.models.Movie;
import at.ac.fhcampuswien.fhmdb.patterns.observer.Observable;
import at.ac.fhcampuswien.fhmdb.patterns.observer.Observer;
import at.ac.fhcampuswien.fhmdb.patterns.state.SortButton;
import at.ac.fhcampuswien.fhmdb.ui.DialogUtil;
import at.ac.fhcampuswien.fhmdb.ui.MovieCell;
import at.ac.fhcampuswien.fhmdb.ui.UserDialog;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MovieListController implements Initializable, Observer {
    @FXML
    public JFXButton searchBtn;

    @FXML
    public TextField searchField;

    @FXML
    public JFXListView movieListView;

    @FXML
    public JFXComboBox genreComboBox;

    @FXML
    public JFXComboBox releaseYearComboBox;

    @FXML
    public JFXComboBox ratingFromComboBox;

//    @FXML
//    public JFXButton sortBtn;

    private SortButton sortButton = new SortButton();

    public List<Movie> allMovies;

    private static MovieListController instance;

    public MovieListController() {
        System.out.println("MovieListController instantiated");
        MovieListController.instance = this;
    }

    public static void setInstance(MovieListController ctrl) {
        instance = ctrl;
    }

    public static MovieListController getInstance() {
        return instance;
    }
//    public ObservableList<Movie> observableMovies = FXCollections.observableArrayList();

//    public ObservableList<Movie> observableMovies = sortButton.getObservableMovies();

//    public SortedState sortedState;

    private final ClickEventHandler onAddToWatchlistClicked = (clickedItem) -> {
        if (clickedItem instanceof Movie movie) {
            WatchlistMovieEntity watchlistMovieEntity = new WatchlistMovieEntity(
                    movie.getId());
            try {
                onMovieAdded(watchlistMovieEntity);
                WatchlistRepository repository = WatchlistRepository.getInstance();
                repository.addToWatchlist(watchlistMovieEntity);


            } catch (DataBaseException e) {
                UserDialog dialog = new UserDialog("Database Error", "Could not add movie to watchlist");
                dialog.show();
                e.printStackTrace();
            }
        }
    };


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeState();
        initializeLayout();
        try {
            WatchlistRepository.getInstance().addObserver(this);
        } catch (DataBaseException e) {
            e.printStackTrace();
        }
    }

    public void initializeState() {
        List<Movie> result;
        try {
            result = MovieAPI.getAllMovies();
            writeCache(result);
        } catch (MovieApiException e) {
            UserDialog dialog = new UserDialog("MovieAPI Error", "Could not load movies from api. Get movies from db cache instead");
            dialog.show();
            result = readCache();
        }

        setMovies(result);
        setMovieList(result);
//        sortedState = SortedState.NONE;
        sortButton.returnToIdleState();
    }

    private List<Movie> readCache() {
        try {
            MovieRepository movieRepository = MovieRepository.getInstance();
            return MovieEntity.toMovies(movieRepository.getAllMovies());
        } catch (DataBaseException e) {
            UserDialog dialog = new UserDialog("DB Error", "Could not load movies from DB");
            dialog.show();
            return new ArrayList<>();
        }
    }

    private void writeCache(List<Movie> movies) {
        try {
            // cache movies in db
            MovieRepository movieRepository = MovieRepository.getInstance();
            movieRepository.removeAll();
            movieRepository.addAllMovies(movies);

        } catch (DataBaseException e) {
            UserDialog dialog = new UserDialog("DB Error", "Could not write movies to DB");
            dialog.show();
        }
    }

    public void initializeLayout() {
        movieListView.setItems(sortButton.getObservableMovies());   // set the items of the listview to the observable list
        movieListView.setCellFactory(movieListView -> new MovieCell(onAddToWatchlistClicked)); // apply custom cells to the listview

        // genre combobox
        Object[] genres = Genre.values();   // get all genres
        genreComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        genreComboBox.getItems().addAll(genres);    // add all genres to the combobox
        genreComboBox.setPromptText("Filter by Genre");

        // year combobox
        releaseYearComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        // fill array with numbers from 1900 to 2023
        Integer[] years = new Integer[124];
        for (int i = 0; i < years.length; i++) {
            years[i] = 1900 + i;
        }
        releaseYearComboBox.getItems().addAll(years);    // add all years to the combobox
        releaseYearComboBox.setPromptText("Filter by Release Year");

        // rating combobox
        ratingFromComboBox.getItems().add("No filter");  // add "no filter" to the combobox
        // fill array with numbers from 0 to 10
        Integer[] ratings = new Integer[11];
        for (int i = 0; i < ratings.length; i++) {
            ratings[i] = i;
        }
        ratingFromComboBox.getItems().addAll(ratings);    // add all ratings to the combobox
        ratingFromComboBox.setPromptText("Filter by Rating");
    }


    public void setMovies(List<Movie> movies) {
        allMovies = movies;
    }

    public void setMovieList(List<Movie> movies) {
        sortButton.getObservableMovies().clear();
        sortButton.getObservableMovies().addAll(movies);
    }

    public void sortMovies() {
        if (sortButton.getCurrentState() == sortButton.getSortButtonIdleState()
                || sortButton.getCurrentState() == sortButton.getSortButtonDescendingState()) {
//            sortMovies(SortedState.ASCENDING);
            sortButton.sortAscending();
        } else if (sortButton.getCurrentState() == sortButton.getSortButtonAscendingState()) {
//            sortMovies(SortedState.DESCENDING);
            sortButton.sortDescending();
        }
    }
    // sort movies based on sortedState
    // by default sorted state is NONE
    // afterwards it switches between ascending and descending
//    public void sortMovies(SortedState sortDirection) {
//        if (sortDirection == SortedState.ASCENDING) {
//            observableMovies.sort(Comparator.comparing(Movie::getTitle));
//            sortedState = SortedState.ASCENDING;
//        } else {
//            observableMovies.sort(Comparator.comparing(Movie::getTitle).reversed());
//            sortedState = SortedState.DESCENDING;
//        }
//    }

    public List<Movie> filterByQuery(List<Movie> movies, String query) {
        if (query == null || query.isEmpty()) return movies;

        if (movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream().filter(movie ->
                        movie.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                                movie.getDescription().toLowerCase().contains(query.toLowerCase()))
                .toList();
    }

    public List<Movie> filterByGenre(List<Movie> movies, Genre genre) {
        if (genre == null) return movies;

        if (movies == null) {
            throw new IllegalArgumentException("movies must not be null");
        }

        return movies.stream().filter(movie -> movie.getGenres().contains(genre)).toList();
    }

    public void applyAllFilters(String searchQuery, Object genre) {
        List<Movie> filteredMovies = allMovies;

        if (!searchQuery.isEmpty()) {
            filteredMovies = filterByQuery(filteredMovies, searchQuery);
        }

        if (genre != null && !genre.toString().equals("No filter")) {
            filteredMovies = filterByGenre(filteredMovies, Genre.valueOf(genre.toString()));
        }

        sortButton.getObservableMovies().clear();
        sortButton.getObservableMovies().addAll(filteredMovies);
    }

    public void searchBtnClicked(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().trim().toLowerCase();
        String releaseYear = validateComboboxValue(releaseYearComboBox.getSelectionModel().getSelectedItem());
        String ratingFrom = validateComboboxValue(ratingFromComboBox.getSelectionModel().getSelectedItem());
        String genreValue = validateComboboxValue(genreComboBox.getSelectionModel().getSelectedItem());

        Genre genre = null;
        if (genreValue != null) {
            genre = Genre.valueOf(genreValue);
        }

        List<Movie> movies = getMovies(searchQuery, genre, releaseYear, ratingFrom);

        setMovies(movies);
        setMovieList(movies);
        applyAllFilters(searchQuery, genre);

        reapplySortState();
    }

    public String validateComboboxValue(Object value) {
        if (value != null && !value.toString().equals("No filter")) {
            return value.toString();
        }
        return null;
    }

    public List<Movie> getMovies(String searchQuery, Genre genre, String releaseYear, String ratingFrom) {
        try {
            return MovieAPI.getAllMovies(searchQuery, genre, releaseYear, ratingFrom);
        } catch (MovieApiException e) {
            System.out.println(e.getMessage());
            UserDialog dialog = new UserDialog("MovieApi Error", "Could not load movies from api.");
            dialog.show();
            return new ArrayList<>();
        }
    }

    public void sortBtnClicked(ActionEvent actionEvent) {
        sortMovies();
    }

    private void reapplySortState() {
        if (sortButton.getCurrentState() == sortButton.getSortButtonAscendingState()) {
            sortButton.returnToIdleState();
            sortButton.sortAscending();
        } else if (sortButton.getCurrentState() == sortButton.getSortButtonDescendingState()) {
            sortButton.returnToIdleState();
            sortButton.sortDescending();
        }
    }

    @Override
    public void onMovieAdded(WatchlistMovieEntity movie) throws DataBaseException {
        WatchlistRepository watchlistRepository = WatchlistRepository.getInstance();
        List<WatchlistMovieEntity> watchlistMovieEntityList = watchlistRepository.getWatchlist();

        for (WatchlistMovieEntity watchlistMovieEntity : watchlistMovieEntityList) {
            if (watchlistMovieEntity.getApiId().equals(movie.getApiId())) {
                DialogUtil.showAlert("Watchlist", null, "Movie already in Watchlist!");
                return;
            }
        }

        DialogUtil.showAlert("Watchlist", null, "Movie added to Watchlist!");
    }
}
