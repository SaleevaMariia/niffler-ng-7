package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.user.AuthorityEntity;
import guru.qa.niffler.data.entity.user.UserEntity;

import java.util.UUID;

public record AuthorityJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("authority")
        Authority authority,
        @JsonProperty("user")
        UserJson user
) {
    public static AuthorityJson fromEntity(AuthorityEntity entity) {
        final UserEntity user = entity.getUser();
        return new AuthorityJson(
                entity.getId(),
                entity.getAuthority(),
                new UserJson(
                        user.getId(),
                        user.getUsername(),
                        user.getPassword()
                )
        );
    }
}
