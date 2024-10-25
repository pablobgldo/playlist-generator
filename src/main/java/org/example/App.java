package org.example;

import org.json.JSONArray;

public class App {

    public static void main(String[] args) {
        // get access token
        String accessToken = SpotifyAuth.getAccessToken();

        String artistName = "Judeline";
        String genre = "indie";
        String mood = "sad";

        // search for artist
        String artistId = PlaylistGenerator.searchArtist(artistName, accessToken);

        // generate playlist
        JSONArray playlist = PlaylistGenerator.generatePlaylist(artistId, genre, mood, accessToken);

        // print tracks
        for (int i = 0; i < playlist.length(); i++) {
            System.out.println(playlist.getJSONObject(i).getString("name"));
        }
    }
}
