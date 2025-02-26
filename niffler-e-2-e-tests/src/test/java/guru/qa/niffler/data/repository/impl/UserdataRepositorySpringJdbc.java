package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.user.FriendshipStatus;
import guru.qa.niffler.data.entity.user.UserDataEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class UserdataRepositorySpringJdbc implements UserdataUserRepository {

    private final UserdataUserDao udUserDao = new UserdataUserDaoSpringJdbc();

    @Override
    @Nonnull
    public UserDataEntity create(UserDataEntity user) {
        return udUserDao.createUser(user);
    }

    @Override
    public Optional<UserDataEntity> findById(UUID id) {
        return udUserDao.findById(id);
    }

    @Override
    public Optional<UserDataEntity> findByUsername(String username) {
        return udUserDao.findByUsername(username);
    }

    @Override
    public void sendInvitation(UserDataEntity requester, UserDataEntity addressee) {
        requester.addFriends(FriendshipStatus.INVITE_SENT, addressee);
        udUserDao.update(requester);
    }

    @Override
    public void addFriend(UserDataEntity requester, UserDataEntity addressee) {
        requester.addFriends(FriendshipStatus.FRIEND, addressee);
        addressee.addFriends(FriendshipStatus.FRIEND, requester);
        udUserDao.update(requester);
        udUserDao.update(addressee);
    }

    @Override
    @Nonnull
    public UserDataEntity update(UserDataEntity user) {
        return udUserDao.update(user);
    }

    @Override
    public void remove(UserDataEntity user) {
        udUserDao.delete(user);
    }
}
