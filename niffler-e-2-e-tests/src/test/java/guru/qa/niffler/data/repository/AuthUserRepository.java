package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.auth.UserEntity;

import java.util.List;
import java.util.Optional;

public interface AuthUserRepository {
    UserEntity create(UserEntity user);

    Optional<UserEntity> findUserByUsername(String username);

    List<UserEntity> findAll();
}
