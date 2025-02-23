package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.UserEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
public interface AuthUserDao {
    @Nonnull
    UserEntity create(UserEntity user);

    Optional<UserEntity> findUserByUsername(String username);

    @Nonnull
    List<UserEntity> findAll();
}
