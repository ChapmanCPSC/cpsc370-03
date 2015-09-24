package richt111.nutritioninfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class WebRequest {

    private HttpURLConnection connection;

    public WebRequest(String urlString) {

        URL url = null;

        try {
            url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setHeader(String headerId[], String headerContent[]) {

        for(int i = 0; i < headerId.length; i++) {
            connection.setRequestProperty(headerId[i], headerContent[i]);
        }

    }

    public String get() throws IOException {

        try {
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }

        return getResponse();

    }

    private String getResponse() throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuffer response = new StringBuffer();
        String line;

        while((line = in.readLine()) != null)
            response.append(line);

        in.close();

        return response.toString();

    }

}
