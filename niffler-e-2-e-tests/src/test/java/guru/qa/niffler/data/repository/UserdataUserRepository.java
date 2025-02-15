package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.user.UserDataEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserdataUserRepository {
    UserDataEntity create(UserDataEntity user);

    Optional<UserDataEntity> findById(UUID id);

    void addIncomeInvitation(UserDataEntity requester, UserDataEntity addressee);

    void addOutcomeInvitation(UserDataEntity requester, UserDataEntity addressee);

    void addFriend(UserDataEntity requester, UserDataEntity addressee);
}
