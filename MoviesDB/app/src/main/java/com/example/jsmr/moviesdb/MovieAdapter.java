package com.example.jsmr.moviesdb;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private List<Movie> movieList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_title;
        public TextView tv_vote_average;
        public TextView tv_year;
        public ImageView iv_poster;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.title);
            tv_vote_average = itemView.findViewById(R.id.vote_average);
            iv_poster = itemView.findViewById(R.id.poster);
            tv_year = itemView.findViewById(R.id.year);
        }
    }

    public MovieAdapter(List<Movie> movieList, Context context){
        this.movieList = movieList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_row, viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.tv_title.setText(movie.getTitle());
        holder.tv_vote_average.setText(String.valueOf(movie.getVote_average()));
        holder.tv_year.setText(String.valueOf(movie.getYear()));
        Picasso.with(context).load(movie.getPoster()).into(holder.iv_poster);

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }
}
