package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.auth.UserEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface AuthUserRepository {
    @Nonnull
    UserEntity create(UserEntity user);

    @Nonnull
    UserEntity update(UserEntity user);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findById(UUID id);

    void remove(UserEntity user);
}
