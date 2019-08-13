package com.web.semantic.api.Modeles;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class Informations {

    public Double latitude;
    public Double longitude;

    public static final String _API_GOOGLE_GEO = "https://maps.googleapis.com/maps/api/geocode/json";
    public static final String _KEY_API_GOOGLE = "AIzaSyBuynhWROWJ7_xoPMJPtiZ3HZ-Maxjx8Qk";

    public static final String _API_OPEN_WEATHER = "https://api.openweathermap.org/data/2.5/weather";
    public static final String _KEY_API_OPEN_WEATHER = "c63361d6c27de81476e81d316cccd0dc";


    public Informations(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public  JSONObject infoCity() throws IOException, JSONException {
        return readJsonFromUrl(_API_GOOGLE_GEO + "?latlng=" + this.latitude + "," + this.longitude + "&key="+ _KEY_API_GOOGLE);
    }

    public JSONObject infoWeather() throws IOException, JSONException {
        return readJsonFromUrl(_API_OPEN_WEATHER + "?lat=" + this.latitude + "&lon=" + this.longitude + "&appid=" + _KEY_API_OPEN_WEATHER + "&lang=fr&units=metric");
    }
}
