package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthJson(

        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("scope")
        String scope,
        @JsonProperty("id_token")
        String idToken,
        @JsonProperty("token_type")
        String tokenType,
        @JsonProperty("expires_in")
        String expiresIn

) {
}
