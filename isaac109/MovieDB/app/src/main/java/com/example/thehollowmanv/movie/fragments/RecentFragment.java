package com.example.thehollowmanv.movie.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thehollowmanv.movie.MainActivity;
import com.example.thehollowmanv.movie.R;
import com.example.thehollowmanv.movie.adapters.RecentMovieAdapter;
import com.example.thehollowmanv.movie.db.MovieDataProvider;
import com.example.thehollowmanv.movie.models.RecentMovieResultModel;

import java.util.List;

/**
 * Created by TheHollowManV on 10/11/2015.
 */
public class RecentFragment extends Fragment
{


    public RecentFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_recent, container, false);
        ListView recentList = (ListView) rootView.findViewById(R.id.lv_movies);
        List<RecentMovieResultModel> movies = MovieDataProvider.GetRecentMovies(getActivity());
        RecentMovieAdapter adapter = new RecentMovieAdapter(getActivity(),movies);
        recentList.setAdapter(adapter);
        recentList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String title = ((TextView) view.findViewById(R.id.tv_title_item)).getText().toString();
                String year = ((TextView) view.findViewById(R.id.tv_year_item)).getText().toString();
                String rating = ((TextView) view.findViewById(R.id.tv_rating_item)).getText().toString();
                String run = ((TextView) view.findViewById(R.id.tv_time_item)).getText().toString();
                String plot = ((TextView) view.findViewById(R.id.tv_plot_item)).getText().toString();
                String award = ((TextView) view.findViewById(R.id.tv_award_item)).getText().toString();
                String meta = ((TextView) view.findViewById(R.id.tv_meta_item)).getText().toString();
                String imdb = ((TextView) view.findViewById(R.id.tv_imdb_item)).getText().toString();
                final Dialog dialog = new Dialog(getActivity());

                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Movie Information");

                final TextView ttv =(TextView)dialog.findViewById(R.id.tv_title_text);
                ttv.setText(title);
                final TextView ytv =(TextView)dialog.findViewById(R.id.tv_Year_text);
                ytv.setText(year);
                final TextView ratv =(TextView)dialog.findViewById(R.id.tv_Rating_text);
                ratv.setText(rating);
                final TextView rutv =(TextView)dialog.findViewById(R.id.tv_RunTime_text);
                rutv.setText(run);
                final TextView ptv =(TextView)dialog.findViewById(R.id.tv_Plot_text);
                ptv.setText(plot);
                final TextView atv =(TextView)dialog.findViewById(R.id.tv_Awards_text);
                atv.setText(award);
                final TextView mtv =(TextView)dialog.findViewById(R.id.tv_MetaScore_text);
                mtv.setText(meta);
                final TextView itv =(TextView)dialog.findViewById(R.id.tv_IMDBScore_text);
                itv.setText(imdb);

                Button btnCancel=(Button)dialog.findViewById(R.id.cancel);
                btnCancel.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });

        return rootView;
    }
}
