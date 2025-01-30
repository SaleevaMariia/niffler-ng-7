package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.user.UserEntity;

import java.util.UUID;

public record UserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("password")
        String password) {
        public static UserJson fromEntity(UserEntity entity) {
                return new UserJson(
                        entity.getId(),
                        entity.getUsername(),
                        entity.getPassword()
                );
        }
}

