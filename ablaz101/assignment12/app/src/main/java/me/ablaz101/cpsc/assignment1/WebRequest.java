package me.ablaz101.cpsc.assignment1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Xavi Ablaza on 9/19/15.
 */
public class WebRequest
{
    private HttpURLConnection connection;

    public WebRequest(String urlString) throws Exception
    {
        URL url = new URL(urlString);
        connection = (HttpURLConnection) url.openConnection();
    }

    public String get() throws Exception
    {
        connection.setRequestMethod("GET");
        return getResponse();
    }

    private String getResponse() throws Exception
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer response = new StringBuffer();
        String line;

        while ((line = in.readLine())!=null)
        {
            response.append(line);
        }

        in.close();

        return response.toString();
    }
}
