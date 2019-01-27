package com.example.jsmr.moviesdb;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    TextView tv_title, tv_vote, tv_time, tv_year, tv_overview;
    ImageView iv_poster, iv_backdrop;
    Toolbar tb_detail;
    String title, overview, poster, backdrop;
    Menu menu;
    double vote;
    int  time, year,id;
    Movie movie;

    boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tb_detail = findViewById(R.id.detail_toolbar);
        tb_detail.setTitle("");
        setSupportActionBar(tb_detail);

        movie = new Movie();
        Intent intent = getIntent();

        tv_title = findViewById(R.id.detail_title);
        tv_vote = findViewById(R.id.detail_vote_average);
        tv_year = findViewById(R.id.detail_year);
        tv_overview = findViewById(R.id.detail_overview);
        iv_poster = findViewById(R.id.detail_poster);
        iv_backdrop = findViewById(R.id.detail_backdrop);

        id = intent.getIntExtra("ID",0);
        title = intent.getStringExtra("TITLE");
        vote = intent.getDoubleExtra("VOTE",0);
        time = intent.getIntExtra("TIME",0);
        year = intent.getIntExtra("YEAR",0);
        overview = intent.getStringExtra("OVERVIEW");
        poster = intent.getStringExtra("POSTER");
        backdrop = intent.getStringExtra("BACKDROP");

        movie.setId(id);
        movie.setTitle(title);
        movie.setVote_average(vote);
        movie.setOverview(overview);
        movie.setTime(time);
        movie.setYear(year);
        movie.setPoster(poster);
        movie.setBackdrop_path(backdrop);

        tv_title.setText(title);
        tv_vote.setText(String.valueOf(vote));
        //tv_time.setText(String.valueOf(time));
        tv_year.setText(String.valueOf(year));
        tv_overview.setText(overview);

        Picasso.with(this).load(poster).into(iv_poster);
        Picasso.with(this).load(backdrop).into(iv_backdrop);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        this.menu = menu;

        validateFavorite();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try{
        int id = item.getItemId();
        if (id  ==  R.id.add_favorites) {

            addMovieToFavorites();

            item.setVisible(false);
            this.menu.findItem(R.id.remove_favorites).setVisible(true);
            return true;
        }else{

            removeMovieFromFavorites();

            item.setVisible(false);
            this.menu.findItem(R.id.add_favorites).setVisible(true);
            return true;
        }
        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addMovieToFavorites(){
        try {

            /*MovieDatabase.getInstance().runInTransaction(new Runnable() {
                @Override
                public void run() {
                    MovieDatabase.getInstance().MovieDao().insert(movie);
                }
            });*/
            new Thread(() -> MovieDatabase.getInstance().MovieDao().insert(movie)).start();
            Toast.makeText(this, "Pelicula agregada a favoritos",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
        }
    }
    private void removeMovieFromFavorites(){
        try {
            /*MovieDatabase.getInstance().runInTransaction(new Runnable() {
                @Override
                public void run() {
                    MovieDatabase.getInstance().MovieDao().delete(movie);
                }
            });*/
            new Thread(() -> MovieDatabase.getInstance().MovieDao().delete(movie)).start();
            Toast.makeText(this, "Pelicula eliminada de favoritos",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void validateFavorite(){

        AsyncTask<Integer, Void, Boolean> FavoriteTask = new AsyncTask<Integer, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Integer... integers) {
                Movie movie_fav;
                movie_fav = MovieDatabase.getInstance().MovieDao().getById(integers[0]);

                if (movie_fav != null && movie_fav.getId() > 0){
                    return  true;
                }
                else {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                isFavorite = aBoolean;
                if (isFavorite) {
                    menu.findItem(R.id.remove_favorites).setVisible(true);
                    menu.findItem(R.id.add_favorites).setVisible(false);
                }else{
                    menu.findItem(R.id.remove_favorites).setVisible(false);
                    menu.findItem(R.id.add_favorites).setVisible(true);
                }
            }
        };

        FavoriteTask.execute(movie.getId());
    }


}
