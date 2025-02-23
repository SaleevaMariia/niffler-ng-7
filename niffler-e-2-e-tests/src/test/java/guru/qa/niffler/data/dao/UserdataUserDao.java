package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.user.UserDataEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UserdataUserDao {
    @Nonnull
    UserDataEntity createUser(UserDataEntity user);

    Optional<UserDataEntity> findById(UUID id);

    Optional<UserDataEntity> findByUsername(String username);

    @Nonnull
    List<UserDataEntity> findAll();

    void delete(UserDataEntity user);

    @Nonnull
    UserDataEntity update(UserDataEntity user);
}
