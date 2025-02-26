package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.auth.UserEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public record UserJson(
        @JsonProperty("id")
        @Nullable
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("password")
        String password) {
        public static @Nonnull UserJson fromEntity(UserEntity entity) {
                return new UserJson(
                        entity.getId(),
                        entity.getUsername(),
                        entity.getPassword()
                );
        }
}

