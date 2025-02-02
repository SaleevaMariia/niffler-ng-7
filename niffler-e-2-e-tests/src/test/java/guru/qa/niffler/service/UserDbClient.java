package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.user.AuthorityEntity;
import guru.qa.niffler.data.entity.user.UserDataEntity;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.model.UserJson;

import java.sql.Connection;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.xaTransaction;

public class UserDbClient {
    private static final Config CFG = Config.getInstance();

    public UserDataJson createUserSpringJdbc(UserJson user, UserDataJson userDataJson) {
        UserEntity auth = new AuthUserDaoSpringJdbc(dataSource(CFG.authJdbcUrl())).create(UserEntity.fromJson(user));
        AuthorityEntity authorityWrite = new AuthorityEntity();
        AuthorityEntity authorityRead = new AuthorityEntity();
        authorityRead.setUser(auth);
        authorityRead.setAuthority(Authority.read);
        authorityWrite.setUser(auth);
        authorityWrite.setAuthority(Authority.write);
        new AuthAuthorityDaoSpringJdbc(dataSource(CFG.authJdbcUrl())).create(authorityRead, authorityWrite);

        return UserDataJson.fromEntity(
                new UserdataUserDaoSpringJdbc(dataSource(CFG.userdataJdbcUrl())).
                        createUser(UserDataEntity.fromJson(userDataJson)));
    }

    public Record createUser(UserJson user, UserDataJson userDataJson) {
        return xaTransaction(Connection.TRANSACTION_REPEATABLE_READ,
                new Databases.XaFunction<>(
                        connection -> {
                            UserEntity auth = new AuthUserDaoJdbc(connection).create(UserEntity.fromJson(user));
                            AuthorityEntity authorityWrite = new AuthorityEntity();
                            AuthorityEntity authorityRead = new AuthorityEntity();
                            authorityRead.setUser(auth);
                            authorityRead.setAuthority(Authority.read);
                            authorityWrite.setUser(auth);
                            authorityWrite.setAuthority(Authority.write);
                            new AuthAuthorityDaoJdbc(connection).create(authorityRead, authorityWrite);
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
