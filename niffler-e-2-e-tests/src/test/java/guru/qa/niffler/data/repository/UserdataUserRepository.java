package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.user.UserDataEntity;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UserdataUserRepository {
    @Nonnull
    UserDataEntity create(UserDataEntity user);

    Optional<UserDataEntity> findById(UUID id);

    Optional<UserDataEntity> findByUsername(String username);

    void sendInvitation(UserDataEntity requester, UserDataEntity addressee);

    void addFriend(UserDataEntity requester, UserDataEntity addressee);

    @Nonnull
    UserDataEntity update(UserDataEntity user);

    void remove(UserDataEntity user);
}
