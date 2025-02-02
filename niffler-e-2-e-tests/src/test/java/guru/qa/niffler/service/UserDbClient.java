package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.user.AuthorityEntity;
import guru.qa.niffler.data.entity.user.UserDataEntity;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.Authority;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.model.UserJson;

public class UserDbClient {
    private static final Config CFG = Config.getInstance();
    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
    private final UserdataUserDao userdataUserDao = new UserdataUserDaoSpringJdbc();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserDataJson createUserSpringJdbc(UserJson user, UserDataJson userDataJson) {
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
}
