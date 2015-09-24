package com.example.thehollowmanv.movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by TheHollowManV on 9/22/2015.
 */
public class WebRequest
{
    private HttpURLConnection con;

    public WebRequest(String urlS)
    {
        URL url;
        try
        {
            url = new URL(urlS);
            con = (HttpURLConnection) url.openConnection();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public String get()
    {
        try
        {
            con.setRequestMethod("GET");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return getResponse();
    }

    private String getResponse()
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer response = new StringBuffer();
            String line;
            while ((line = in.readLine()) != null)
            {
                response.append(line);
            }
            return response.toString();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
