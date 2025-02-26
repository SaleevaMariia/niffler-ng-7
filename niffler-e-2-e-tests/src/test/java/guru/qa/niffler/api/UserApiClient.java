package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.utils.RandomDataUtils;
import io.qameta.allure.Step;
import retrofit2.Response;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import static guru.qa.niffler.utils.RandomDataUtils.defaultPassword;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class UserApiClient implements UsersClient {
    private static final Config CFG = Config.getInstance();
    private final UserApi userApiAuth = new RestClient
            .EmtyRestClient(CFG.authUrl())
            .retrofit()
            .create(UserApi.class);
    private final UserApi userApiUserData = new RestClient
            .EmtyRestClient(CFG.userdataUrl())
            .retrofit()
            .create(UserApi.class);

    @Override
    @Step("Создаем пользователя {username} используя REST API")
    public @Nullable UserDataJson createUser(String username, String password) {
        Response<UserDataJson> response;
        try {
            userApiAuth.getRegisterPage().execute();
            userApiAuth.registerUser(username, password, password,
                    ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")).execute();
            response = userApiUserData.currentUser(username).execute();
            Instant start = Instant.now();
            while (Duration.between(start, Instant.now()).toMillis() < 1000) {
                response = userApiUserData.currentUser(username).execute();
                if (response.body() != null && response.body().id() != null) {
                    return response.body().addTestData(new TestData(password));
                } else {
                    Thread.sleep(100);
                }
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertEquals(200, response.code());
        return response.body().addTestData(new TestData(password));
    }

    @Override
    @Step("Создаем входящее приглашение в друзья используя REST API")
    public void createIncomeInvitation(UserDataJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final Response<UserDataJson> responseUser;
                final String newUsername = RandomDataUtils.randomUserName();
                final UserDataJson newUser;
                try {
                    newUser = createUser(newUsername, defaultPassword);
                    responseUser = userApiUserData.sendInvitation(newUsername, targetUser.username()).execute();
                    targetUser.testData()
                            .incomeInvitations()
                            .add(newUser);

                } catch (IOException e) {
                    throw new AssertionError(e);
                }
                assertEquals(200, responseUser.code());
            }
        }

    }

    @Override
    @Step("Создаем исходящее приглашение в друзья используя REST API")
    public void createOutcomeInvitation(UserDataJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final Response<UserDataJson> responseUser;
                final String newUsername = RandomDataUtils.randomUserName();
                final UserDataJson newUser;
                try {
                    newUser = createUser(newUsername, defaultPassword);
                    responseUser = userApiUserData.sendInvitation(targetUser.username(), newUsername).execute();
                    targetUser.testData()
                            .outcomeInvitations()
                            .add(newUser);

                } catch (IOException e) {
                    throw new AssertionError(e);
                }
                assertEquals(200, responseUser.code());
            }
        }
    }

    @Override
    @Step("Создаем друга используя REST API")
    public void createFriends(UserDataJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final Response<UserDataJson> responseUser;
                final String newUsername = RandomDataUtils.randomUserName();
                try {
                    createUser(newUsername, defaultPassword);
                    userApiUserData.sendInvitation(newUsername, targetUser.username()).execute();
                    responseUser = userApiUserData.acceptInvitation(targetUser.username(), newUsername).execute();
                    targetUser.testData()
                            .friends()
                            .add(responseUser.body());
                } catch (IOException e) {
                    throw new AssertionError(e);
                }
                assertEquals(200, responseUser.code());
            }
        }
    }
}
