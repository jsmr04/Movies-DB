package com.example.jsmr.moviesdb;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.jsmr.moviesdb.Utilities.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    ProgressBar pb_progress;
    RecyclerView rv_list;
    Toolbar tb_main;
    List<Movie> movies = new ArrayList<>();
    MovieAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tb_main = (Toolbar)findViewById(R.id.my_toolbar);
        tb_main.setTitle("MovieDB - Populares");
        setSupportActionBar(tb_main);

        try {
            rv_list = findViewById(R.id.rv_movies);
            adapter = new MovieAdapter(movies,this);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,2);
            rv_list.setLayoutManager(mLayoutManager);
            rv_list.setItemAnimator(new DefaultItemAnimator());
            //rv_list.addItemDecoration(new  DividerItemDecoration(this, LinearLayout.VERTICAL));
            rv_list.setAdapter(adapter);

            rv_list.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rv_list, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Movie movie = movies.get(position);
                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                    intent.putExtra("TITLE", movie.getTitle());
                    intent.putExtra("YEAR", movie.getYear());
                    intent.putExtra("POSTER", movie.getPoster());
                    intent.putExtra("BACKDROP", movie.getBackdrop_path());
                    intent.putExtra("VOTE", movie.getVote_average());
                    intent.putExtra("OVERVIEW", movie.getOverview());
                    intent.putExtra("TIME", movie.getTime());

                    startActivity(intent);

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));


            URL url = Network.buildTrendingUrl();
            MovieDBTask task = new MovieDBTask();
            task.execute(url);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_movies, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.populares){
            movies.clear();
            tb_main.setTitle("MovieDB - Populares");
            URL url = Network.buildTrendingUrl();
            MovieDBTask task = new MovieDBTask();
            task.execute(url);

            return true;
        }else if(id==R.id.en_cines){
            movies.clear();
            tb_main.setTitle("MovieDB - Liberados Hoy");
            URL url = Network.buildInTeathersUrl();
            MovieDBTask task = new MovieDBTask();
            task.execute(url);

            return true;
        }else if(id==R.id.accion){
            movies.clear();
            tb_main.setTitle("MovieDB - Acción" );
            URL url = Network.buildGenreUrl(28);
            MovieDBTask task = new MovieDBTask();
            task.execute(url);

            return true;
        }else if(id==R.id.aventura) {
            movies.clear();
            tb_main.setTitle("MovieDB - Aventura");
            URL url = Network.buildGenreUrl(12);
            MovieDBTask task = new MovieDBTask();
            task.execute(url);

            return true;
        }else if(id==R.id.comedia) {
            movies.clear();
            tb_main.setTitle("MovieDB - Comedia");
            URL url = Network.buildGenreUrl(35);
            MovieDBTask task = new MovieDBTask();
            task.execute(url);

            return true;
        }else if(id==R.id.documental) {
            movies.clear();
            tb_main.setTitle("MovieDB - Documental");
            URL url = Network.buildGenreUrl(99);
            MovieDBTask task = new MovieDBTask();
            task.execute(url);

            return true;
        }else if(id==R.id.drama) {
            movies.clear();
            tb_main.setTitle("MovieDB - Drama");
            URL url = Network.buildGenreUrl(18);
            MovieDBTask task = new MovieDBTask();
            task.execute(url);

            return true;
        }else if(id==R.id.fantasia) {
            movies.clear();
            tb_main.setTitle("MovieDB - Fantasía");
            URL url = Network.buildGenreUrl(14);
            MovieDBTask task = new MovieDBTask();
            task.execute(url);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class MovieDBTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb_progress = findViewById(R.id.progress_circular);
            pb_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            if (s!=null && !s.equals("")){
                Calendar cal = Calendar.getInstance();
                try {
                    JSONObject jObj = new JSONObject(s);
                    JSONArray jArr = jObj.getJSONArray("results");

                    for (int i=0; i < jArr.length(); i++) {
                        JSONObject obj = jArr.getJSONObject(i);

                        if (obj.has("title") ||  obj.has("original_title")) {
                            Movie movie = new Movie();
                            cal.setTime(Date.valueOf(obj.getString("release_date")));

                            if (obj.has("title")) {
                                movie.setTitle(obj.getString("title"));
                            } else {
                                movie.setTitle(obj.getString("original_title"));
                            }

                            movie.setDate(Date.valueOf(obj.getString("release_date")));
                            movie.setYear(cal.get(Calendar.YEAR));
                            //movie.setTime(Integer.parseInt(obj.getString("runtime")));
                            movie.setOverview(obj.getString("overview"));
                            movie.setPoster("https://image.tmdb.org/t/p/w500" + obj.getString("poster_path"));
                            movie.setBackdrop_path("https://image.tmdb.org/t/p/w500" + obj.getString("backdrop_path"));
                            movie.setVote_average(Double.parseDouble(obj.getString("vote_average")));

                            movies.add(movie);
                        }

                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                pb_progress.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL url = urls[0];
            String response = null;
            try {
                response = Network.getResponse(url);
            }catch (Exception e){
                e.printStackTrace();
            }
            return response;
        }
    }
}
