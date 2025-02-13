package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.user.FriendshipEntity;
import guru.qa.niffler.data.entity.user.FriendshipStatus;
import guru.qa.niffler.data.entity.user.UserDataEntity;
import guru.qa.niffler.data.mapper.UserDataEntityRowMapper;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataRepositoryJdbc implements UserdataUserRepository {
    private static final Config CFG = Config.getInstance();

    @Override
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
                "select * from \"user\", friendship f " +
                        "where (\"user\".id = f.addressee_id or \"user\".id = f.requester_id) " +
                        "and \"user\".id = ?"
        )) {
            addressPs.setObject(1, id);
            addressPs.execute();
            UserDataEntity user = null;
            try (ResultSet rs = addressPs.getResultSet()) {
                while (rs.next()) {
                    if (user == null) {
                        user = UserDataEntityRowMapper.instance.mapRow(rs, 1);
                    }
                    if (rs.getObject("requester_id") != null) {
                        FriendshipEntity fe = new FriendshipEntity();

                        if (rs.getObject("requester_id", UUID.class).equals(id)) {
                            fe.setRequester(user);
                            UserDataEntity u = new UserDataEntity();
                            u.setId(rs.getObject("addressee_id", UUID.class));
                            fe.setAddressee(u);
                            fe.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
                            fe.setCreatedDate(rs.getDate("created_date"));
                            user.getFriendshipRequests().add(fe);
                        }
                        if (rs.getObject("addressee_id", UUID.class).equals(id)) {
                            UserDataEntity u = new UserDataEntity();
                            u.setId(rs.getObject("requester_id", UUID.class));
                            fe.setRequester(u);
                            fe.setAddressee(user);
                            fe.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
                            fe.setCreatedDate(rs.getDate("created_date"));
                            user.getFriendshipAddressees().add(fe);
                        }
                    }
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
                "select * from \"user\", friendship f " +
                        "where (\"user\".id = f.addressee_id or \"user\".id = f.requester_id) " +
                        "and \"user\".username = ?"
        )) {
            addressPs.setObject(1, username);
            addressPs.execute();
            UserDataEntity user = null;
            try (ResultSet rs = addressPs.getResultSet()) {
                while (rs.next()) {
                    if (user == null) {
                        user = UserDataEntityRowMapper.instance.mapRow(rs, 1);
                    }
                    if (rs.getObject("requester_id") != null) {
                        FriendshipEntity fe = new FriendshipEntity();

                        if (rs.getObject("requester_id", UUID.class).equals(user.getId())) {
                            fe.setRequester(user);
                            UserDataEntity u = new UserDataEntity();
                            u.setId(rs.getObject("addressee_id", UUID.class));
                            fe.setAddressee(u);
                            fe.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
                            fe.setCreatedDate(rs.getDate("created_date"));
                            user.getFriendshipRequests().add(fe);
                        }
                        if (rs.getObject("addressee_id", UUID.class).equals(user.getId())) {
                            UserDataEntity u = new UserDataEntity();
                            u.setId(rs.getObject("requester_id", UUID.class));
                            fe.setRequester(u);
                            fe.setAddressee(user);
                            fe.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
                            fe.setCreatedDate(rs.getDate("created_date"));
                            user.getFriendshipAddressees().add(fe);
                        }
                    }
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
    public void addIncomeInvitation(UserDataEntity requester, UserDataEntity addressee) {
        try (PreparedStatement friendPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date) " +
                        "VALUES (?, ?, ?, ?)"
        )) {
            friendPs.setObject(1, requester.getId());
            friendPs.setObject(2, addressee.getId());
            friendPs.setString(3, FriendshipStatus.PENDING.name());
            friendPs.setDate(4, new Date(System.currentTimeMillis()));
            friendPs.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addOutcomeInvitation(UserDataEntity requester, UserDataEntity addressee) {
        addIncomeInvitation(addressee, requester);
    }

    @Override
    public void addFriend(UserDataEntity requester, UserDataEntity addressee) {
        try (PreparedStatement friendPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status, created_date) " +
                        "VALUES (?, ?, ?, ?)"
        )) {
            friendPs.setObject(1, requester.getId());
            friendPs.setObject(2, addressee.getId());
            friendPs.setString(3, FriendshipStatus.ACCEPTED.name());
            friendPs.setDate(4, new Date(System.currentTimeMillis()));
            friendPs.executeUpdate();
            friendPs.clearParameters();
            friendPs.setObject(1, addressee.getId());
            friendPs.setObject(2, requester.getId());
            friendPs.setString(3, FriendshipStatus.ACCEPTED.name());
            friendPs.setDate(4, new Date(System.currentTimeMillis()));
            friendPs.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
