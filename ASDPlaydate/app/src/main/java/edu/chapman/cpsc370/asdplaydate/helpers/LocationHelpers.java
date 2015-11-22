package edu.chapman.cpsc370.asdplaydate.helpers;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;

/**
 * Created by Xavi on 21/11/15.
 */
public class LocationHelpers
{
    public static LatLng toLatLng(Location location)
    {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static LatLng toLatLng(ParseGeoPoint parseGeoPoint)
    {
        return new LatLng(parseGeoPoint.getLatitude(), parseGeoPoint.getLongitude());
    }

    public static ParseGeoPoint toParseGeoPoint(Location location)
    {
        return new ParseGeoPoint(location.getLatitude(), location.getLongitude());
    }

    public static ParseGeoPoint toParseGeoPoint(LatLng latLng)
    {
        return new ParseGeoPoint(latLng.latitude, latLng.longitude);
    }
}
