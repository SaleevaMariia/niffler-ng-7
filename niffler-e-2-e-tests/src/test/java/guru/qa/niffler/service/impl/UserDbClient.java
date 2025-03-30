package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.UserEntity;
import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.data.entity.user.FriendshipStatus;
import guru.qa.niffler.data.entity.user.UserDataEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.repository.impl.UserdataRepositoryJdbc;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.rest.UserDataJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.utils.RandomDataUtils.defaultPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;

@ParametersAreNonnullByDefault
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
    @Step("Создаем пользователя {username} используя DB")
    @Nonnull
    public UserDataJson createUser(String username, String password) {
        return xaTransactionTemplate.execute(() -> {
            UserEntity user = userEntity(username, password);
            authUserRepository.create(user);
            return UserDataJson.fromEntity(
                    userdataRepository.create(userDataEntity(username)), null).addTestData(new TestData(password));
        });
    }
    @Nonnull
    private UserDataEntity userDataEntity(String username) {
        UserDataEntity ue = new UserDataEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }

    @Step("Ищем пользователя {id} используя DB")
    public Optional<UserDataEntity> findById(UUID id) {
        return xaTransactionTemplate.execute(() -> {
            return userdataRepository.findById(id);
        });
    }

    @Override
    @Step("Создаем входящее приглашение в друзья используя DB")
    public void createIncomeInvitation(UserDataJson targetUser, int count) {
        if (count > 0) {
            UserDataEntity targetEntity = userdataRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            final String username = randomUserName();
                            System.out.println("username:" + username);
                            UserEntity authuser = userEntity(username, defaultPassword);
                            authUserRepository.create(authuser);
                            UserDataEntity user = userdataRepository.create(userDataEntity(username));
                            userdataRepository.sendInvitation(user, targetEntity);
                    targetUser.testData()
                            .incomeInvitations()
                            .add(UserDataJson.fromFriendEntity(user, FriendshipStatus.PENDING));
                            return null;
                        }
                );
            }
        }
    }

    @Override
    @Step("Создаем исходящее приглашение в друзья используя DB")
    public void createOutcomeInvitation(UserDataJson targetUser, int count) {
        if (count > 0) {
            UserDataEntity targetEntity = userdataRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            final String username = randomUserName();
                            System.out.println("username:" + username);
                            UserEntity authuser = userEntity(username, defaultPassword);
                            authUserRepository.create(authuser);
                            UserDataEntity user = userdataRepository.create(userDataEntity(username));
                    userdataRepository.sendInvitation(targetEntity, user);
                    targetUser.testData()
                            .outcomeInvitations()
                            .add(UserDataJson.fromUserEntity(user, FriendshipStatus.PENDING));
                            return null;
                        }
                );
            }
        }
    }

    @Override
    @Step("Создаем друга используя DB")
    public void createFriends(UserDataJson targetUser, int count) {
        if (count > 0) {
            UserDataEntity targetEntity = userdataRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            final String username = randomUserName();
                            System.out.println("username:" + username);
                            UserEntity authuser = userEntity(username, defaultPassword);
                            authUserRepository.create(authuser);
                            UserDataEntity user = userdataRepository.create(userDataEntity(username));
                    userdataRepository.addFriend(targetEntity, user);
                    targetUser.testData()
                            .friends()
                            .add(UserDataJson.fromFriendEntity(user, FriendshipStatus.ACCEPTED));
                            return null;
                        }
                );
            }
        }
    }

    @Nonnull
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
