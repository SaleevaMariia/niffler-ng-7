package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.user.UserEntity;

import java.util.Optional;

public interface AuthUserDao {
    UserEntity create(UserEntity user);

    Optional<UserEntity> findUserByUsername(String username);
}
