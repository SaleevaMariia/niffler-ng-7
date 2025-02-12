package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.user.FriendshipStatus;
import guru.qa.niffler.data.entity.user.UserDataEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import java.util.Optional;
import java.util.UUID;

public class UserdataRepositorySpringJdbc implements UserdataUserRepository {

    private final UserdataUserDao udUserDao = new UserdataUserDaoSpringJdbc();

    @Override
    public UserDataEntity create(UserDataEntity user) {
        return udUserDao.createUser(user);
    }

    @Override
    public Optional<UserDataEntity> findById(UUID id) {
        return udUserDao.findById(id);
    }

    @Override
    // requester отправил addressee приглашение
    public void addIncomeInvitation(UserDataEntity requester, UserDataEntity addressee) {
        requester.addFriends(FriendshipStatus.PENDING, addressee);
        udUserDao.update(requester);
    }

    @Override
    public void addOutcomeInvitation(UserDataEntity requester, UserDataEntity addressee) {
        addressee.addFriends(FriendshipStatus.PENDING, requester);
        udUserDao.update(addressee);
    }

    @Override
    public void addFriend(UserDataEntity requester, UserDataEntity addressee) {
        requester.addFriends(FriendshipStatus.ACCEPTED, addressee);
        addressee.addFriends(FriendshipStatus.ACCEPTED, requester);
        udUserDao.update(requester);
        udUserDao.update(addressee);
    }
}
