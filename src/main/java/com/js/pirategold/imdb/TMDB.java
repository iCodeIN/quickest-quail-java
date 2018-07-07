package com.js.pirategold.imdb;

import com.js.pirategold.model.Movie;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TMDB implements IMovieProvider {

    private static String API_KEY = "caee14d8ab9e074ae59f4e85e83662fa";


    public Movie getMovie(String query)
    {
        try {
            return _getMovie(query);
        } catch (IOException ex) {
            Logger.getLogger(TMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Movie();
    }

    private Movie _getMovie(String query) throws IOException {
        query = query.trim().replaceAll(" +", "+");

        // build query url
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=" + query;

        // lookup
        Scanner sc = new Scanner(new URL(url).openStream());
        String body = "";
        while(sc.hasNextLine())
        {
            body += sc.nextLine();
        }
        sc.close();

        // process query result
        JSONArray results = new JSONObject(body).getJSONArray("results");
        if(results.length() == 0)
            return new Movie();

        JSONObject firstResult = results.getJSONObject(0);

        // delegate
        return getMovieByTMDBID(firstResult.get("id").toString());
    }

    @Override
    public Movie getMovieByID(String imdbID){
        try {
            return _getMovieByID(imdbID);
        } catch (IOException ex) {
            Logger.getLogger(TMDB.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Movie();
    }

    public Movie _getMovieByID(String imdbID) throws IOException {
        String url = "https://api.themoviedb.org/3/find/" + imdbID + "?api_key=" + API_KEY + "&language=en-US&external_source=imdb_id";

        // lookup
        Scanner sc = new Scanner(new URL(url).openStream());
        String body = "";
        while(sc.hasNextLine())
        {
            body += sc.nextLine();
        }
        sc.close();

        // process query result
        JSONArray results = new JSONObject(body).getJSONArray("movie_results");
        if(results.length() == 0)
            return new Movie();

        JSONObject firstResult = results.getJSONObject(0);

        // delegate
        return getMovieByTMDBID(firstResult.get("id").toString());
    }

    private Movie getMovieByTMDBID(String tmdbID) throws IOException {
        String url = "https://api.themoviedb.org/3/movie/" + tmdbID + "?api_key=" + API_KEY + "&language=en-US&append_to_response=credits";

        // lookup
        Scanner sc = new Scanner(new URL(url).openStream());
        String body = "";
        while(sc.hasNextLine())
        {
            body += sc.nextLine();
        }
        sc.close();

        Movie retval = new Movie();
        retval.putAll(new JSONObject(body).toMap());

        // alias
        retval.put("Poster", "http://image.tmdb.org/t/p/w185/" + retval.get("poster_path"));
        retval.put("Plot", retval.get("overview"));
        retval.put("Title", retval.get("original_title"));
        retval.put("imdbID", retval.get("imdb_id"));
        retval.put("Language", retval.get("original_language").toString());
        retval.put("Year", retval.get("release_date").toString().substring(0, 4));
        retval.put("imdbRating", retval.get("vote_average"));
        retval.put("imdbVotes", retval.get("vote_count"));

        // genres
        List<String> genres = new ArrayList<>();
        for(HashMap genreMap : (List<HashMap>) retval.get("genres")){
            genres.add(genreMap.get("name").toString());
        }
        retval.put("Genre", toArrayString(genres));

        // actors
        List<String> actors = new ArrayList<>();
        for(Object tmp : (List) ((Map) retval.get("credits")).get("cast")){
            Map tmpB = (Map) tmp;
            actors.add(tmpB.get("name").toString());
        }
        retval.put("Actors", toArrayString(actors));

        // directors
        List<String> directors = new ArrayList<>();
        for(Object tmp : (List) ((Map) retval.get("credits")).get("crew")){
            Map tmpB = (Map) tmp;
            if("Directing".equals(tmpB.get("department")))
                directors.add(tmpB.get("name").toString());
        }
        retval.put("Director", toArrayString(directors));

        // writers
        List<String> writers = new ArrayList<>();
        for(Object tmp : (List) ((Map) retval.get("credits")).get("crew")){
            Map tmpB = (Map) tmp;
            if("Writing".equals(tmpB.get("department")))
                writers.add(tmpB.get("name").toString());
        }
        retval.put("Writer", toArrayString(writers));

        // producers
        List<String> producers = new ArrayList<>();
        for(Object tmp : (List) ((Map) retval.get("credits")).get("crew")){
            Map tmpB = (Map) tmp;
            if("Production".equals(tmpB.get("department")))
                producers.add(tmpB.get("name").toString());
        }
        retval.put("Producer", toArrayString(producers));

        // return
        return retval;
    }

    private String toArrayString(List<String> l) {
        if(l.isEmpty())
            return "";
        String txt = "";
        for(int i = 0 ;i < l.size() ; i++)
            txt += (i == 0 ? "" : ", ") + l.get(i);
        return txt;
    }
}
