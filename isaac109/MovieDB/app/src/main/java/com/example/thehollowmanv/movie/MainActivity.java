package com.example.thehollowmanv.movie;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.thehollowmanv.movie.db.MovieDataProvider;
import com.example.thehollowmanv.movie.fragments.RecentFragment;
import com.example.thehollowmanv.movie.models.MovieResultModel;
import com.example.thehollowmanv.movie.models.RecentMovieResultModel;
import com.example.thehollowmanv.movie.tasks.GetMovieTask;

public class MainActivity extends AppCompatActivity {

    private final String MOVIE_KEY = "movie";
    EditText movieField;
    TextView titleField;
    TextView yearField;
    TextView ratingField;
    TextView runTimeField;
    TextView plotField;
    TextView awardsField;
    TextView metaField;
    TextView imdbField;
    ProgressBar loadingView;
    Button search;
    Button history;
    SharedPreferences prefs;
    boolean display = false;
    Fragment f;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        String storedMovie = prefs.getString(MOVIE_KEY,"");
        f = new RecentFragment();
        scrollView = (ScrollView)findViewById(R.id.sv_info);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        movieField = (EditText) findViewById(R.id.et_entry);
        titleField = (TextView) findViewById(R.id.tv_title_text);
        yearField = (TextView) findViewById(R.id.tv_Year_text);
        ratingField = (TextView)findViewById(R.id.tv_Rating_text);
        runTimeField = (TextView)findViewById(R.id.tv_RunTime_text);
        plotField = (TextView)findViewById(R.id.tv_Plot_text);
        awardsField = (TextView)findViewById(R.id.tv_Awards_text);
        metaField = (TextView)findViewById(R.id.tv_MetaScore_text);
        imdbField = (TextView)findViewById(R.id.tv_IMDBScore_text);
        loadingView = (ProgressBar)findViewById(R.id.pb_loading_movie);
        search = (Button)findViewById(R.id.btn_search);
        search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String movie = movieField.getText().toString();
                if(!movie.isEmpty())
                {
                    /*InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    GetMovieTask movieTask = new GetMovieTask(MainActivity.this);
                    movieTask.execute(movie);*/
                    storeMovie(movie);
                    RecentMovieResultModel rMovie = new RecentMovieResultModel(movie,getString(R.string.dYear),getString(R.string.dRating),getString(R.string.dTime),getString(R.string.dPlot),getString(R.string.dAwards),getString(R.string.dMScore),getString(R.string.dimdbScore));
                    MovieDataProvider.InsertRecentMovie(rMovie,MainActivity.this);

                    final GetMovieTask movieTask = new GetMovieTask(MainActivity.this);
                    movieTask.onFinish = new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            loadingFinished(movieTask._result);
                        }
                    };
                    loadingStarted();
                    movieTask.execute(movie);
                }
            }
        });
        history = (Button) findViewById(R.id.btn_History);
        history.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(!display)
                {
                    display = true;
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.layout, f);
                    fragmentTransaction.commit();
                    scrollView.setVisibility(View.INVISIBLE);
                    history.setText("Hide History");
                }
                else
                {
                    display = false;
                    Log.e("Here", "here");
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.remove(f);
                    fragmentTransaction.commit();
                    scrollView.setVisibility(View.VISIBLE);
                    history.setText("Show History");
                }

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadingStarted()
    {
        loadingView.setVisibility(View.VISIBLE);
    }

    public void loadingFinished(MovieResultModel result)
    {
        loadingView.setVisibility(View.INVISIBLE);
        movieField.setText("");
        titleField.setText(R.string.dTitle);
        yearField.setText(R.string.dYear);
        ratingField.setText(R.string.dRating);
        runTimeField.setText(R.string.dTime);
        plotField.setText(R.string.dPlot);
        awardsField.setText(R.string.dAwards);
        metaField.setText(R.string.dMScore);
        imdbField.setText(R.string.dimdbScore);
        //if(!result.title.isEmpty())
       // {
            titleField.setText(result.Title);
       // }
       // if(!result.year.isEmpty())
        //{
            yearField.setText(result.Year);
       // }
      //  if(!result.rated.isEmpty())
       // {
            ratingField.setText(result.Rated);
       // }
       // if(!result.runTime.isEmpty())
       // {
            runTimeField.setText(result.Runtime);
       // }
       // if(!result.plot.isEmpty())
       // {
            plotField.setText(result.Plot);
        //}
       // if(!result.awards.isEmpty())
       // {
            awardsField.setText(result.Awards);
       // }
       // if(!result.metaScore.isEmpty())
       // {
            metaField.setText(result.Metascore);
       // }
        //if(!result.imdbRating.isEmpty())
      //  {
            imdbField.setText(result.imdbRating);
      //  }
    }
    private void storeMovie(String movie)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(MOVIE_KEY, movie);
        editor.commit();
    }

}
