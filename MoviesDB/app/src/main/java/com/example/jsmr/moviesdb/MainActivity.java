package com.example.jsmr.moviesdb;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.jsmr.moviesdb.Utilities.Network;
import com.example.jsmr.moviesdb.Utilities.NotificationJobSchedule;

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
    BottomNavigationView bnNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Cargar valores por defecto
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        tb_main = (Toolbar)findViewById(R.id.my_toolbar);
        tb_main.setTitle("MovieDB - Populares");
        setSupportActionBar(tb_main);

        try {

            bnNavigation = findViewById(R.id.navigation);
            bnNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                Fragment fragment;
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    try{
                        switch (menuItem.getItemId()){
                            case R.id.nav_trending:
                                movies.clear();
                                tb_main.setTitle("MovieDB - Populares");
                                URL trendingUrl = Network.buildTrendingUrl();
                                MovieDBTask trendingTask = new MovieDBTask();
                                trendingTask.execute(trendingUrl);

                                break;
                            case R.id.nav_release:
                                movies.clear();
                                tb_main.setTitle("MovieDB - Liberados Hoy");
                                URL releaseUrl = Network.buildInTeathersUrl();
                                MovieDBTask releaseTask = new MovieDBTask();
                                releaseTask.execute(releaseUrl);

                                break;
                            case R.id.nav_favorites:
                                movies.clear();
                                tb_main.setTitle("MovieDB - Tus Favoritas");
                                getFavorites();

                                break;
                            case R.id.nav_genre:
                                movies.clear();
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    return true;
                }
            });

            rv_list = findViewById(R.id.rv_movies);
            adapter = new MovieAdapter(movies,this);

            int count_columns = Integer.parseInt(preferences.getString("columns_rv","2"));

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,count_columns);
            rv_list.setLayoutManager(mLayoutManager);
            rv_list.setItemAnimator(new DefaultItemAnimator());
            //rv_list.addItemDecoration(new  DividerItemDecoration(this, LinearLayout.VERTICAL));
            rv_list.setAdapter(adapter);

            rv_list.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rv_list, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Movie movie = movies.get(position);
                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                    intent.putExtra("ID", movie.getId());
                    intent.putExtra("TITLE", movie.getTitle());
                    intent.putExtra("YEAR", movie.getYear());
                    intent.putExtra("POSTER", movie.getPoster());
                    intent.putExtra("BACKDROP", movie.getBackdrop_path());
                    intent.putExtra("VOTE", movie.getVote_average());
                    intent.putExtra("OVERVIEW", movie.getOverview());
                    intent.putExtra("TIME", movie.getTime());

                    ImageView ivPoster = findViewById(R.id.poster);
                    TextView tvTitle = findViewById(R.id.title);
                    ImageView tvStar = findViewById(R.id.star);
                    TextView tvVote = findViewById(R.id.vote_average);
                    TextView tvYear = findViewById(R.id.year);

                    Pair<View, String> p1 = Pair.create((View)ivPoster, "poster");
                    Pair<View, String> p2 = Pair.create((View)tvTitle, "title");
                    Pair<View, String> p3 = Pair.create((View)tvStar, "star");
                    Pair<View, String> p4 = Pair.create((View)tvVote, "vote");
                    Pair<View, String> p5 = Pair.create((View)tvYear, "year");

                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation(MainActivity.this,p1 , p2,p3,p4,p5);

                    startActivity(intent,options.toBundle());

                }

                @Override
                public void onLongClick(View view, int position) {

                }
            }));

            //Task para cargar el listado de peliculas trendings
            URL url = Network.buildTrendingUrl();
            MovieDBTask task = new MovieDBTask();
            task.execute(url);

            boolean use_notification =  preferences.getBoolean("notification",true);
            if (use_notification) {
                //Programar job para notificaciones
                JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
                JobInfo info = new JobInfo.Builder(11, new ComponentName(this, NotificationJobSchedule.class))
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        //.setExtras(extras)
                        .setOverrideDeadline(1000)
                        .setMinimumLatency(1000)
                        //.setPeriodic(960000)
                        .build();
                //Programando tarea
                scheduler.schedule(info);
            }
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
        if(id==R.id.configurar) {
            Intent i = new Intent(this, SettingActivity.class);
            startActivity(i);

            return true;
        }
        /*if(id==R.id.populares){
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
        }else if(id==R.id.configurar) {
            Intent i = new Intent(this, SettingActivity.class);
            startActivity(i);

            return true;
        }*/

        return super.onOptionsItemSelected(item);
    } private void getFavorites(){

        AsyncTask<Void, Void, List<Movie>> FavoriteTask = new AsyncTask<Void, Void, List<Movie>>(){

            @Override
            protected List<Movie> doInBackground(Void... voids) {
                List<Movie> movie_fav;
                movie_fav = MovieDatabase.getInstance().MovieDao().listAll();
                return movie_fav;
            }

            @Override
            protected void onPostExecute(List<Movie> pMovies) {
                movies.addAll(pMovies);
                adapter.notifyDataSetChanged();
                //pb_progress.setVisibility(View.INVISIBLE);
            }
        };

        FavoriteTask.execute();
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

                            movie.setId(Integer.parseInt(obj.getString("id")));

                            if (obj.has("title")) {
                                movie.setTitle(obj.getString("title"));
                            } else {
                                movie.setTitle(obj.getString("original_title"));
                            }

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
