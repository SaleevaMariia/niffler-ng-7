package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.*;
import guru.qa.niffler.data.entity.user.AuthorityEntity;
import guru.qa.niffler.data.entity.user.UserDataEntity;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;

public class UserDbClient {
    private static final Config CFG = Config.getInstance();
    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc();
    private final UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();

    private final AuthUserDao authUserDaoSpring = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDaoSpring = new AuthAuthorityDaoSpringJdbc();
    private final UserdataUserDao userdataUserDaoSpring = new UserdataUserDaoSpringJdbc();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    TransactionTemplate txTemplate = new TransactionTemplate(
            new ChainedTransactionManager(
                    new JdbcTransactionManager(
                            dataSource(CFG.authJdbcUrl())
                    ),
                    new JdbcTransactionManager(
                            dataSource(CFG.userdataJdbcUrl())
                    )
            )
    );

    public UserDataJson createUserChainedTransactionManagerJdbc(UserJson user, UserDataJson userDataJson) {
        return txTemplate.execute((x) -> {
            UserEntity auth = authUserDao.create(UserEntity.fromJson(user));
            AuthorityEntity authorityWrite = new AuthorityEntity();
            AuthorityEntity authorityRead = new AuthorityEntity();
            authorityRead.setUser(auth);
            authorityRead.setAuthority(Authority.read);
            authorityWrite.setUser(auth);
            authorityWrite.setAuthority(Authority.write);
            authAuthorityDao.create(authorityRead, authorityWrite);
            return UserDataJson.fromEntity(
                    userdataUserDao.createUser(UserDataEntity.fromJson(userDataJson)));
        });
    }

    public UserDataJson createUserXaTransactionManagerJdbc(UserJson user, UserDataJson userDataJson) {
        return xaTransactionTemplate.execute(() -> {
            UserEntity auth = authUserDao.create(UserEntity.fromJson(user));
            AuthorityEntity authorityWrite = new AuthorityEntity();
            AuthorityEntity authorityRead = new AuthorityEntity();
            authorityRead.setUser(auth);
            authorityRead.setAuthority(Authority.read);
            authorityWrite.setUser(auth);
            authorityWrite.setAuthority(Authority.write);
            authAuthorityDao.create(authorityRead, authorityWrite);
            return UserDataJson.fromEntity(
                    userdataUserDao.createUser(UserDataEntity.fromJson(userDataJson)));
        });
    }

    public UserDataJson createUserChainedTransactionManagerSpringJdbc(UserJson user, UserDataJson userDataJson) {
        return txTemplate.execute((x) -> {
            UserEntity auth = authUserDaoSpring.create(UserEntity.fromJson(user));
            AuthorityEntity authorityWrite = new AuthorityEntity();
            AuthorityEntity authorityRead = new AuthorityEntity();
            authorityRead.setUser(auth);
            authorityRead.setAuthority(Authority.read);
            authorityWrite.setUser(auth);
            authorityWrite.setAuthority(Authority.write);
            authAuthorityDaoSpring.create(authorityRead, authorityWrite);
            return UserDataJson.fromEntity(
                    userdataUserDaoSpring.createUser(UserDataEntity.fromJson(userDataJson)));
        });
    }

    public UserDataJson createUserWithoutTxSpring(UserJson user, UserDataJson userDataJson) {
        UserEntity auth = authUserDaoSpring.create(UserEntity.fromJson(user));
        AuthorityEntity authorityWrite = new AuthorityEntity();
        AuthorityEntity authorityRead = new AuthorityEntity();
        authorityRead.setUser(auth);
        authorityRead.setAuthority(Authority.read);
        authorityWrite.setUser(auth);
        authorityWrite.setAuthority(Authority.write);
        authAuthorityDaoSpring.create(authorityRead, authorityWrite);
        return UserDataJson.fromEntity(
                userdataUserDaoSpring.createUser(UserDataEntity.fromJson(userDataJson)));
    }

    public UserDataJson createUserWithoutTxJdbc(UserJson user, UserDataJson userDataJson) {
        UserEntity auth = authUserDao.create(UserEntity.fromJson(user));
        AuthorityEntity authorityWrite = new AuthorityEntity();
        AuthorityEntity authorityRead = new AuthorityEntity();
        authorityRead.setUser(auth);
        authorityRead.setAuthority(Authority.read);
        authorityWrite.setUser(auth);
        authorityWrite.setAuthority(Authority.write);
        authAuthorityDao.create(authorityRead, authorityWrite);
        return UserDataJson.fromEntity(
                userdataUserDao.createUser(UserDataEntity.fromJson(userDataJson)));
    }
}
