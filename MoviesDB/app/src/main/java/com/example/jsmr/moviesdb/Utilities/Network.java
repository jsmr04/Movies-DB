package com.example.jsmr.moviesdb.Utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Network {

    final static String TRENDING_URL = "https://api.themoviedb.org/3/trending/all/day";
    final static String IN_THEATERS_URL = "https://api.themoviedb.org/3/discover/movie";
    final static String GENRE_URL = "https://api.themoviedb.org/3/discover/movie?primary_release_year=2018";

    final static String PARAM_KEY = "api_key";
    final static String PARAM_GENRE = "with_genres";
    final static String PARAM_LANG = "language";
    final static  String PARAM_PRI_RLSE_DATE_GTE = "primary_release_date.gte";
    final static  String PARAM_PRI_RLSE_DATE_LTE = "primary_release_date.lte";

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
        //Date date = new Date();
        Calendar cal = Calendar.getInstance();
        //cal.setTime(date);
        String today = String.valueOf(cal.get(Calendar.YEAR)) + "-" +
                       String.valueOf(cal.get(Calendar.MONTH) + 1) + "-" +
                       String.valueOf(cal.get(Calendar.DAY_OF_MONTH));

        Uri builtUri = Uri.parse(IN_THEATERS_URL).buildUpon()
                .appendQueryParameter(PARAM_PRI_RLSE_DATE_GTE, today)
                .appendQueryParameter(PARAM_PRI_RLSE_DATE_LTE, today)
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
