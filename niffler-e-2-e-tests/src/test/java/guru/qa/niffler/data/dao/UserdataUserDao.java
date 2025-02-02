package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.user.UserDataEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserdataUserDao {
    UserDataEntity createUser(UserDataEntity user);

    Optional<UserDataEntity> findById(UUID id);

    Optional<UserDataEntity> findByUsername(String username);

    List<UserDataEntity> findAll();

    void delete(UserDataEntity user);
}
