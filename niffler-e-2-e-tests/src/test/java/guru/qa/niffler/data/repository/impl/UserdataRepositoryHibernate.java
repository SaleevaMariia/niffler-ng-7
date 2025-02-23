package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.user.FriendshipStatus;
import guru.qa.niffler.data.entity.user.UserDataEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.EntityManagers.em;

@ParametersAreNonnullByDefault
public class UserdataRepositoryHibernate implements UserdataUserRepository {
    private static final Config CFG = Config.getInstance();

    private final EntityManager entityManager = em(CFG.userdataJdbcUrl());

    @Override
    @Nonnull
    public UserDataEntity create(UserDataEntity user) {
        entityManager.joinTransaction();
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<UserDataEntity> findById(UUID id) {
        return Optional.ofNullable(
                entityManager.find(UserDataEntity.class, id)
        );
    }

    @Override
    public Optional<UserDataEntity> findByUsername(String username) {
        try {
            return Optional.of(
                    entityManager.createQuery("select u from UserDataEntity u where u.username =: username", UserDataEntity.class)
                            .setParameter("username", username)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void sendInvitation(UserDataEntity requester, UserDataEntity addressee) {
        entityManager.joinTransaction();
        requester.addFriends(FriendshipStatus.INVITE_SENT, addressee);
    }

    @Override
    public void addFriend(UserDataEntity requester, UserDataEntity addressee) {
        entityManager.joinTransaction();
        addressee.addFriends(FriendshipStatus.FRIEND, requester);
        requester.addFriends(FriendshipStatus.FRIEND, addressee);
    }

    @Override
    @Nonnull
    public UserDataEntity update(UserDataEntity user) {
        entityManager.joinTransaction();
        return entityManager.merge(user);
    }

    @Override
    public void remove(UserDataEntity user) {
        entityManager.joinTransaction();
        entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
    }
}
