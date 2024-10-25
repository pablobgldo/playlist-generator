package org.example;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONObject;
import io.github.cdimascio.dotenv.Dotenv;


public class SpotifyAuth {
        static Dotenv dotenv = Dotenv.load();
      private static final String CLIENT_ID = dotenv.get("CLIENT_ID");
      private static final String CLIENT_SECRET = dotenv.get("CLIENT_SECRET");
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";

    public static String getAccessToken() {
        HttpResponse<JsonNode> response = Unirest.post(TOKEN_URL)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .basicAuth(CLIENT_ID, CLIENT_SECRET)
                .field("grant_type", "client_credentials")
                .asJson();

        if (response.getStatus() == 200) {
            JSONObject jsonObject = new JSONObject(response.getBody().toString());
            return jsonObject.getString("access_token");
        } else {
            throw new RuntimeException("Failed to authenticate with Spotify API");
        }
    }
}
