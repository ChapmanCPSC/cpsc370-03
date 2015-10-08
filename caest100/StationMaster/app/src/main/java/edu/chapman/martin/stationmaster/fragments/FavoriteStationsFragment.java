package edu.chapman.martin.stationmaster.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import edu.chapman.martin.stationmaster.R;
import edu.chapman.martin.stationmaster.adapters.FavoriteStationDisplayAdapter;
import edu.chapman.martin.stationmaster.db.FavoriteStationProvider;
import edu.chapman.martin.stationmaster.models.FavoriteStationModel;

/**
 * Created by Martin on 10/6/2015.
 */
public class FavoriteStationsFragment extends Fragment {

    View rootView;
    Context context;
    ListView lv_favoriteStationInfo;

    public FavoriteStationsFragment(){
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        lv_favoriteStationInfo = (ListView) rootView.findViewById(R.id.lv_favoriteStationInfo);

        List<FavoriteStationModel> favorites = FavoriteStationProvider.GetFavoriteStations(context);

        FavoriteStationDisplayAdapter adapter = new FavoriteStationDisplayAdapter(context, favorites);
        lv_favoriteStationInfo.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getActivity().getApplicationContext();
    }
}
