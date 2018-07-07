package com.js.quickestquail.imdb;

import com.js.quickestquail.model.Movie;

public interface IMovieProvider {

    Movie getMovie(String query);

    Movie getMovieByID(String imdbID);

}
