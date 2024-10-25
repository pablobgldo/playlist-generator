package org.example;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;

public class PlaylistGenerator {

    private static final String SPOTIFY_SEARCH_URL = "https://api.spotify.com/v1/search";
    private static final String SPOTIFY_RECOMMENDATIONS_URL = "https://api.spotify.com/v1/recommendations";

    // search for artist
    public static String searchArtist(String artistName, String accessToken) {
        HttpResponse<JsonNode> response = Unirest.get(SPOTIFY_SEARCH_URL)
                .header("Authorization", "Bearer " + accessToken)
                .queryString("q", artistName)
                .queryString("type", "artist")
                .asJson();

        if (response.getStatus() == 200) {
            JSONObject jsonObject = new JSONObject(response.getBody().toString());
            JSONArray artists = jsonObject.getJSONObject("artists").getJSONArray("items");
            if (artists.length() > 0) {
                return artists.getJSONObject(0).getString("id"); // return first artist's id
            }
        }
        throw new RuntimeException("Artist not found");
    }

    // create playlist based on artist, genre & mood
    public static JSONArray generatePlaylist(String artistId, String genre, String mood, String accessToken) {
        HttpResponse<JsonNode> response = Unirest.get(SPOTIFY_RECOMMENDATIONS_URL)
                .header("Authorization", "Bearer " + accessToken)
                .queryString("seed_artists", artistId)
                .queryString("seed_genres", genre)
                .queryString("target_valence", moodToValence(mood)) // determines specific mood (0.0 sad, 1.0 happy)
                .asJson();

        if (response.getStatus() == 200) {
            JSONObject jsonObject = new JSONObject(response.getBody().toString());
            return jsonObject.getJSONArray("tracks"); // return tracks array
        } else {
            throw new RuntimeException("Couldn't generate playlist");
        }
    }

    // map mood to valence
    private static String moodToValence(String mood) {
        return switch (mood.toLowerCase()) {
            case "happy" -> "0.9";
            case "sad" -> "0.1";
            case "energetic" -> "0.8";
            default -> "0.5";
        };
    }
}
