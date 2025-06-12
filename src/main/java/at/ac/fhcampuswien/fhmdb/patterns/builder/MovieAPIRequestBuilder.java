package at.ac.fhcampuswien.fhmdb.patterns.builder;

import at.ac.fhcampuswien.fhmdb.models.Genre;

public class MovieAPIRequestBuilder {
    //Required parameters
    private final String DELIMITER;
    private final String URL; // https if certificates work

    //optional parameters
    private final String query;
    private final Genre genre;
    private final String releaseYear;
    private final String ratingFrom;

    public MovieAPIRequestBuilder(Builder builder) {
        this.DELIMITER = builder.DELIMITER;
        this.URL = builder.URL;
        this.query = builder.query;
        this.genre = builder.genre;
        this.releaseYear = builder.releaseYear;
        this.ratingFrom = builder.ratingFrom;
    }

    public static class Builder {
        //required
        private final String DELIMITER;
        private final String URL;

        //optional

        private String query = null;
        private Genre genre = null;
        private String releaseYear = null;
        private String ratingFrom = null;

        public Builder(String DELIMITER, String URL) {
            this.DELIMITER = DELIMITER;
            this.URL = URL;
        }

        public Builder query(String query) {
            this.query = query;
            return this;
        }

        public Builder genre(Genre genre) {
            this.genre = genre;
            return this;
        }

        public Builder releaseYear(String releaseYear) {
            this.releaseYear = releaseYear;
            return this;
        }

        public Builder ratingFrom(String ratingFrom) {
            this.ratingFrom = ratingFrom;
            return this;
        }

        public MovieAPIRequestBuilder build() {
            return new MovieAPIRequestBuilder(this);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(URL);
        boolean isFirstParam = true;

        if (query != null && !query.isEmpty()) {
            sb.append(isFirstParam ? "?" : DELIMITER).append("query=").append(query);
            isFirstParam = false;
        }
        if (genre != null) {
            sb.append(isFirstParam ? "?" : DELIMITER).append("genre=").append(genre.name());
            isFirstParam = false;
        }
        if (releaseYear != null && !releaseYear.isEmpty()) {
            sb.append(isFirstParam ? "?" : DELIMITER).append("releaseYear=").append(releaseYear);
            isFirstParam = false;
        }
        if (ratingFrom != null && !ratingFrom.isEmpty()) {
            sb.append(isFirstParam ? "?" : DELIMITER).append("ratingFrom=").append(ratingFrom);
        }

        return sb.toString();
    }
}
