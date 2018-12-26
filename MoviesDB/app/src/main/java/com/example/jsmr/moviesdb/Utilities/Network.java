package com.example.jsmr.moviesdb.Utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Network {

    final static String TRENDING_URL = "https://api.themoviedb.org/3/trending/all/day";
    final static String IN_THEATERS_URL = "https://api.themoviedb.org/3/discover/movie?primary_release_date.gte=2018-12-25&primary_release_date.lte=2018-12-25";
    final static String GENRE_URL = "https://api.themoviedb.org/3/discover/movie?primary_release_year=2018";

    final static String PARAM_KEY = "api_key";
    final static String PARAM_GENRE = "with_genres";
    final static String PARAM_LANG = "language";

    final static String key_value = "c98e63a4bd8b39e9799ba6ac0b3bd4d8";
    final static String lang = "es";

    //https://api.themoviedb.org/3/movie/550?api_key=c98e63a4bd8b39e9799ba6ac0b3bd4d8

    public static URL buildTrendingUrl(){
        Uri builtUri = Uri.parse(TRENDING_URL).buildUpon()
                .appendQueryParameter(PARAM_KEY, key_value)
                .appendQueryParameter(PARAM_LANG, lang)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildInTeathersUrl(){
        Uri builtUri = Uri.parse(IN_THEATERS_URL).buildUpon()
                .appendQueryParameter(PARAM_KEY, key_value)
                .appendQueryParameter(PARAM_LANG, lang)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildGenreUrl(int genre){
        Uri builtUri = Uri.parse(GENRE_URL).buildUpon()
                .appendQueryParameter(PARAM_KEY, key_value)
                .appendQueryParameter(PARAM_GENRE, String.valueOf(genre))
                .appendQueryParameter(PARAM_LANG, lang)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponse(URL url)throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();;

        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if(hasInput){
                return  scanner.next();
            }else{
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }
}
