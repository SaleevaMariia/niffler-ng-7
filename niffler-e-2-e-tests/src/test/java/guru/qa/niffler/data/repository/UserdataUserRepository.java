package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.user.UserDataEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserdataUserRepository {
    UserDataEntity create(UserDataEntity user);

    Optional<UserDataEntity> findById(UUID id);

    Optional<UserDataEntity> findByUsername(String username);

    void sendInvitation(UserDataEntity requester, UserDataEntity addressee);

    void addFriend(UserDataEntity requester, UserDataEntity addressee);

    UserDataEntity update(UserDataEntity user);

    void remove(UserDataEntity user);
}
