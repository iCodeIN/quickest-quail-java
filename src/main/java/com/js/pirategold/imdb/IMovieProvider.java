package com.js.pirategold.imdb;

import com.js.pirategold.model.Movie;

import java.io.IOException;
import java.net.MalformedURLException;

public interface IMovieProvider {

    Movie getMovie(String query);

    Movie getMovieByID(String imdbID);

}
