package com.example.jsmr.moviesdb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    TextView tv_title, tv_vote, tv_time, tv_year, tv_overview;
    ImageView iv_poster, iv_backdrop;
    String title, overview, poster, backdrop;
    double vote;
    int  time, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        tv_title = findViewById(R.id.detail_title);
        tv_vote = findViewById(R.id.detail_vote_average);
        tv_year = findViewById(R.id.detail_year);
        tv_overview = findViewById(R.id.detail_overview);
        iv_poster = findViewById(R.id.detail_poster);
        iv_backdrop = findViewById(R.id.detail_backdrop);

        title = intent.getStringExtra("TITLE");
        vote = intent.getDoubleExtra("VOTE",0);
        time = intent.getIntExtra("TIME",0);
        year = intent.getIntExtra("YEAR",0);
        overview = intent.getStringExtra("OVERVIEW");
        poster = intent.getStringExtra("POSTER");
        backdrop = intent.getStringExtra("BACKDROP");

        tv_title.setText(title);
        tv_vote.setText(String.valueOf(vote));
        //tv_time.setText(String.valueOf(time));
        tv_year.setText(String.valueOf(year));
        tv_overview.setText(overview);

        Picasso.with(this).load(poster).into(iv_poster);
        Picasso.with(this).load(backdrop).into(iv_backdrop);

    }
}
