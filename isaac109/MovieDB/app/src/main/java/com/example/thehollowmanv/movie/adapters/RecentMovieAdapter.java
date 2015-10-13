package com.example.thehollowmanv.movie.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.thehollowmanv.movie.R;
import com.example.thehollowmanv.movie.models.RecentMovieResultModel;
import com.example.thehollowmanv.movie.tasks.GetMovieTask;

import java.util.List;

/**
 * Created by TheHollowManV on 10/11/2015.
 */
public class RecentMovieAdapter extends ArrayAdapter<RecentMovieResultModel>
{
    Context _context;
    List<RecentMovieResultModel> movies;

    public RecentMovieAdapter(Context context, List<RecentMovieResultModel> objects)
    {
        super(context, R.layout.movie_item,objects);
        _context = context;
        movies = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        RecentMovieResultModel movie = movies.get(position);
        LayoutInflater inflater = LayoutInflater.from(_context);
        View row = inflater.inflate(R.layout.movie_item, parent, false);
        final TextView titleView = (TextView) row.findViewById(R.id.tv_title_item);
        final TextView yearView = (TextView) row.findViewById(R.id.tv_year_item);
        final TextView rateView = (TextView) row.findViewById(R.id.tv_rating_item);
        final TextView runView = (TextView) row.findViewById(R.id.tv_time_item);
        final TextView plotView = (TextView) row.findViewById(R.id.tv_plot_item);
        final TextView awardView = (TextView) row.findViewById(R.id.tv_award_item);
        final TextView metaView = (TextView) row.findViewById(R.id.tv_meta_item);
        final TextView imdbView = (TextView) row.findViewById(R.id.tv_imdb_item);

        plotView.setVisibility(View.GONE);
        awardView.setVisibility(View.GONE);
        metaView.setVisibility(View.GONE);
        imdbView.setVisibility(View.GONE);

        final GetMovieTask getMovieTask = new GetMovieTask(_context);
        getMovieTask.onFinish = new Runnable()
        {
            @Override
            public void run()
            {
                titleView.setText(getMovieTask._result.Title);
                yearView.setText(getMovieTask._result.Year);
                rateView.setText(getMovieTask._result.Rated);
                runView.setText(getMovieTask._result.Runtime);
                plotView.setText(getMovieTask._result.Plot);
                awardView.setText(getMovieTask._result.Awards);
                metaView.setText(getMovieTask._result.Metascore);
                imdbView.setText(getMovieTask._result.imdbRating);
            }
        };
        getMovieTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,movie.getTitle());
        return row;
    }
}
