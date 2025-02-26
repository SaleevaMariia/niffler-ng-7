package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.user.FriendshipStatus;
import guru.qa.niffler.data.entity.user.UserDataEntity;
import guru.qa.niffler.data.mapper.UserDataEntityRowMapper;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.*;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

@ParametersAreNonnullByDefault
public class UserdataRepositoryJdbc implements UserdataUserRepository {
    private static final Config CFG = Config.getInstance();
    private final UserdataUserDao udUserDao = new UserdataUserDaoJdbc();

    @Override
    @Nonnull
    public UserDataEntity create(UserDataEntity user) {

        try (PreparedStatement userPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency, firstname, surname, photo, " +
                        "photo_small, full_name) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
        )) {
            userPs.setString(1, user.getUsername());
            userPs.setString(2, user.getCurrency().name());
            userPs.setString(3, user.getFirstname());
            userPs.setString(4, user.getSurname());
            userPs.setBytes(5, user.getPhoto());
            userPs.setBytes(6, user.getPhotoSmall());
            userPs.setString(7, user.getFullname());
            userPs.executeUpdate();
            final UUID generatedKey;
            try (ResultSet rs = userPs.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can't find id in ResultSet");
                }
            }
            user.setId(generatedKey);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserDataEntity> findById(UUID id) {
        try (PreparedStatement addressPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "select * from \"user\" where \"user\".id = ?"
        )) {
            addressPs.setObject(1, id);
            addressPs.execute();
            UserDataEntity user = null;
            try (ResultSet rs = addressPs.getResultSet()) {
                if (rs.next()) {
                    user = UserDataEntityRowMapper.instance.mapRow(rs, 1);
                }
                if (user == null) {
                    return Optional.empty();
                } else {
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserDataEntity> findByUsername(String username) {
        try (PreparedStatement addressPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "select * from \"user\" where \"user\".username = ?"
        )) {
            addressPs.setObject(1, username);
            addressPs.execute();
            UserDataEntity user = null;
            try (ResultSet rs = addressPs.getResultSet()) {
                if (rs.next()) {
                    user = UserDataEntityRowMapper.instance.mapRow(rs, 1);
                }
                if (user == null) {
                    return Optional.empty();
                } else {
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendInvitation(UserDataEntity requester, UserDataEntity addressee) {
        try (PreparedStatement friendPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date) " +
                        "VALUES (?, ?, ?, ?)"
        )) {
            friendPs.setObject(1, requester.getId());
            friendPs.setObject(2, addressee.getId());
            friendPs.setString(3, FriendshipStatus.INVITE_SENT.name());
            friendPs.setDate(4, new Date(System.currentTimeMillis()));
            friendPs.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addFriend(UserDataEntity requester, UserDataEntity addressee) {
        try (PreparedStatement friendPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date) " +
                        "VALUES (?, ?, ?, ?)"
        )) {
            friendPs.setObject(1, requester.getId());
            friendPs.setObject(2, addressee.getId());
            friendPs.setString(3, FriendshipStatus.FRIEND.name());
            friendPs.setDate(4, new Date(System.currentTimeMillis()));
            friendPs.executeUpdate();
            friendPs.clearParameters();
            friendPs.setObject(1, addressee.getId());
            friendPs.setObject(2, requester.getId());
            friendPs.setString(3, FriendshipStatus.FRIEND.name());
            friendPs.setDate(4, new Date(System.currentTimeMillis()));
            friendPs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
