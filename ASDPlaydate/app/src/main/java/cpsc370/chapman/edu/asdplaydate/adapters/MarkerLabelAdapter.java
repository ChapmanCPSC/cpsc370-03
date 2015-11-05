package cpsc370.chapman.edu.asdplaydate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

import cpsc370.chapman.edu.asdplaydate.R;

/**
 * Created by Kelly on 11/4/15.
 */
public class MarkerLabelAdapter implements GoogleMap.InfoWindowAdapter
{

    Context _ctx;
    HashMap<LatLng, String> _temp;
    TextView profileInfo;
    Button chatRequest;

    // TODO: constructor will pass a model of the actual data
    public MarkerLabelAdapter(Context ctx, HashMap<LatLng, String> temp)
    {
        _ctx = ctx;
        _temp = temp;
    }

    @Override
    public View getInfoWindow(Marker marker)
    {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker)
    {
        LayoutInflater inflater = LayoutInflater.from(_ctx);
        View label = inflater.inflate(R.layout.marker_label, null);
        profileInfo = (TextView) label.findViewById(R.id.tv_parent_name);

        chatRequest = (Button) label.findViewById(R.id.btn_chat_request);
        chatRequest.setOnClickListener(onClickListener);

        if(_temp.containsKey(marker.getPosition()))
        {
            profileInfo.setText(_temp.get(marker.getPosition()));
        }

        return label;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch(v.getId())
            {
                case R.id.btn_chat_request:
                    // TODO: Open chat
                    break;
                default:
                    break;
            }
        }
    };
}
