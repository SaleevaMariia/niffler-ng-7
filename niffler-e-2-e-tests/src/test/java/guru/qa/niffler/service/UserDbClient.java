package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.user.AuthorityEntity;
import guru.qa.niffler.data.entity.user.UserDataEntity;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.model.UserJson;

import java.sql.Connection;

import static guru.qa.niffler.data.Databases.xaTransaction;

public class UserDbClient {
    private static final Config CFG = Config.getInstance();

    public Record createUser(UserJson user, UserDataJson userDataJson) {
        return xaTransaction(Connection.TRANSACTION_REPEATABLE_READ,
                new Databases.XaFunction<>(
                        connection -> {
                            UserEntity auth = new AuthUserDaoJdbc(connection).create(UserEntity.fromJson(user));
                            AuthorityEntity authority = new AuthorityEntity();
                            authority.setUser(auth);
                            authority.setAuthority(Authority.read);
                            new AuthAuthorityDaoJdbc(connection).create(authority);
                            authority.setAuthority(Authority.write);
                            new AuthAuthorityDaoJdbc(connection).create(authority);
                            return UserJson.fromEntity(auth);
                        }, CFG.authJdbcUrl()),
                new Databases.XaFunction<>(
                        connection -> {
                            UserDataEntity userDataEntity = UserDataEntity.fromJson(userDataJson);
                            return UserDataJson.fromEntity(
                                    new UserdataUserDaoJdbc(connection).createUser(userDataEntity)
                            );
                        }, CFG.userdataJdbcUrl())
        );
    }
}
