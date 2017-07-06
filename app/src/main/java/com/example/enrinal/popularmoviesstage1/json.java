package com.example.enrinal.popularmoviesstage1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enrinal on 7/6/2017.
 */

public class json {

    public static List<Entity> parseJson(String json) {
        List<Entity> movieEntities = new ArrayList<Entity>();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray results = object.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                movieEntities.add(new Entity(
                        result.getInt("id"),
                        result.getString("original_title"),
                        DataFilm.buildImageUrl(result.getString("poster_path")),
                        result.getString("overview"),
                        result.getDouble("vote_average"),
                        result.getString("release_date")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieEntities;
    }
}
