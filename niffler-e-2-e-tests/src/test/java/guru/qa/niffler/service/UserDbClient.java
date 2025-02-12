package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.UserEntity;
import guru.qa.niffler.data.entity.user.UserDataEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositorySpringJdbc;
import guru.qa.niffler.data.repository.impl.UserdataRepositorySpringJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserDataJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class UserDbClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final UserdataUserDao userdataUserDaoSpring = new UserdataUserDaoSpringJdbc();
    private final UserdataUserRepository userdataUserDao = new UserdataRepositorySpringJdbc();

    private final UserdataUserRepository userdataUserDaoSpringRepository = new UserdataRepositorySpringJdbc();

    private final AuthUserRepository authUserRepository = new AuthUserRepositorySpringJdbc();


    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );


    public UserDataJson createUserXaTransactionManagerJdbc(UserDataJson userDataJson) {
        return xaTransactionTemplate.execute(() -> {
            UserEntity authUser = new UserEntity();
            authUser.setUsername(userDataJson.username());
            authUser.setPassword(pe.encode("12345"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);
            authUser.setAuthorities(
                    Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(authUser);
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toList()
            );
            authUserRepository.create(authUser);
            return UserDataJson.fromEntity(
                    userdataUserDaoSpring.createUser(UserDataEntity.fromJson(userDataJson)));
        });
    }


    public UserDataJson createUserFromEntityXaTransactionManagerSpringJdbc(UserDataEntity userData) {
        return xaTransactionTemplate.execute(() -> {
            UserEntity authUser = new UserEntity();
            authUser.setUsername(userData.getUsername());
            authUser.setPassword(pe.encode("12345"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);
            authUser.setAuthorities(
                    Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(authUser);
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toList()
            );
            authUserRepository.create(authUser);
            return UserDataJson.fromEntity(
                    userdataUserDaoSpringRepository.create(userData));
        });
    }

    public UserDataEntity createUserFromEntityXaTransactionManagerJdbc(UserDataEntity userData) {
        return xaTransactionTemplate.execute(() -> {
            UserEntity authUser = new UserEntity();
            authUser.setUsername(userData.getUsername());
            authUser.setPassword(pe.encode("12345"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);
            authUser.setAuthorities(
                    Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(authUser);
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toList()
            );
            authUserRepository.create(authUser);
            return userdataUserDao.create(userData);
        });
    }

    public Optional<UserDataEntity> findById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            return userdataUserDao.findById(id);
        });
    }

    public void addIncomeInvitation(UserDataEntity user1, UserDataEntity user2) {
        userdataUserDao.addIncomeInvitation(user1, user2);
    }

    public void addOutcomeInvitation(UserDataEntity user1, UserDataEntity user2) {
        userdataUserDao.addOutcomeInvitation(user1, user2);
    }

    public void addFriend(UserDataEntity user1, UserDataEntity user2) {
        userdataUserDao.addFriend(user1, user2);
    }
}
