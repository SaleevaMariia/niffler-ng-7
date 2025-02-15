package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.UserEntity;
import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.data.entity.user.UserDataEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserdataRepositoryJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserDataJson;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;

public class UserDbClient implements UsersClient {
    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    private final UserdataUserRepository userdataRepository = new UserdataRepositoryJdbc();
    private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();


    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );


    @Override
    public UserDataJson createUser(String username, String password) {
        return xaTransactionTemplate.execute(() -> {
            UserEntity user = userEntity(username, password);
            authUserRepository.create(user);
            return UserDataJson.fromEntity(
                    userdataRepository.create(userDataEntity(username)), null);
        });
    }

    private UserDataEntity userDataEntity(String username) {
        UserDataEntity ue = new UserDataEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }

    public Optional<UserDataEntity> findById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            return userdataRepository.findById(id);
        });
    }

    @Override
    public void createIncomeInvitation(UserDataJson targetUser, int count) {
        if (count > 0) {
            UserDataEntity targetEntity = userdataRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            final String username = randomUserName();
                            System.out.println("username:" + username);
                            UserEntity authuser = userEntity(username, "12345");
                            authUserRepository.create(authuser);
                            UserDataEntity user = userdataRepository.create(userDataEntity(username));
                            userdataRepository.sendInvitation(user, targetEntity);
                            return null;
                        }
                );
            }
        }
    }

    @Override
    public void createOutcomeInvitation(UserDataJson targetUser, int count) {
        if (count > 0) {
            UserDataEntity targetEntity = userdataRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            final String username = randomUserName();
                            System.out.println("username:" + username);
                            UserEntity authuser = userEntity(username, "12345");
                            authUserRepository.create(authuser);
                            UserDataEntity user = userdataRepository.create(userDataEntity(username));
                            userdataRepository.sendInvitation(targetEntity, user);
                            return null;
                        }
                );
            }
        }
    }

    @Override
    public void createFriends(UserDataJson targetUser, int count) {
        if (count > 0) {
            UserDataEntity targetEntity = userdataRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                    final String username = randomUserName();
                    System.out.println("username:" + username);
                    UserEntity authuser = userEntity(username, "12345");
                            authUserRepository.create(authuser);
                            UserDataEntity user = userdataRepository.create(userDataEntity(username));
                    userdataRepository.addFriend(targetEntity, user);
                            return null;
                        }
                );
            }
        }
    }

    private UserEntity userEntity(String username, String password) {
        UserEntity authUser = new UserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
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
        return authUser;
    }
}
