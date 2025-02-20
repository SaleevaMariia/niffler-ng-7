package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.utils.RandomDataUtils;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

import static guru.qa.niffler.utils.RandomDataUtils.defaultPassword;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserApiClient implements UsersClient {
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .cookieJar(new JavaNetCookieJar(
                    new CookieManager(
                            ThreadSafeCookieStore.INSTANCE,
                            CookiePolicy.ACCEPT_ALL
                    )
            ))
            .build();
    private final Retrofit retrofitAuth = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Config.getInstance().authUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    private final UserApi userApiAuth = retrofitAuth.create(UserApi.class);
    private final Retrofit retrofitUserdata = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Config.getInstance().userdataUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    private final UserApi userApiUserData = retrofitUserdata.create(UserApi.class);


    @Override
    public UserDataJson createUser(String username, String password) {
        final Response<UserDataJson> response;
        try {
            userApiAuth.getRegisterPage().execute();
            userApiAuth.registerUser(username, password, password,
                    ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")).execute();
            response = userApiUserData.currentUser(username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body().addTestData(new TestData(password));
    }

    @Override
    public void createIncomeInvitation(UserDataJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final Response<UserDataJson> responseUser;
                final String newUsername = RandomDataUtils.randomUserName();
                final UserDataJson newUser;
                try {
                    newUser = createUser(newUsername, defaultPassword);
                    Thread.sleep(1000);
                    responseUser = userApiUserData.sendInvitation(newUsername, targetUser.username()).execute();
                    targetUser.testData()
                            .incomeInvitations()
                            .add(newUser);

                } catch (IOException e) {
                    throw new AssertionError(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("targetUser.username():" + targetUser.username());
                System.out.println("newUsername" + newUsername);
                assertEquals(200, responseUser.code());
                System.out.println(responseUser.errorBody());
                System.out.println(responseUser.body());
            }
        }

    }

    @Override
    public void createOutcomeInvitation(UserDataJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final Response<UserDataJson> responseUser;
                final String newUsername = RandomDataUtils.randomUserName();
                final UserDataJson newUser;
                try {
                    newUser = createUser(newUsername, defaultPassword);
                    Thread.sleep(1000);
                    responseUser = userApiUserData.sendInvitation(targetUser.username(), newUsername).execute();
                    targetUser.testData()
                            .outcomeInvitations()
                            .add(newUser);

                } catch (IOException e) {
                    throw new AssertionError(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("targetUser.username():" + targetUser.username());
                System.out.println("newUsername" + newUsername);
                assertEquals(200, responseUser.code());
                System.out.println(responseUser.errorBody());
                System.out.println(responseUser.body());

            }
        }
    }

    @Override
    public void createFriends(UserDataJson targetUser, int count) {
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                final Response<UserDataJson> responseUser;
                final String newUsername = RandomDataUtils.randomUserName();
                try {
                    createUser(newUsername, defaultPassword);
                    Thread.sleep(1000);
                    userApiUserData.sendInvitation(newUsername, targetUser.username()).execute();
                    responseUser = userApiUserData.acceptInvitation(targetUser.username(), newUsername).execute();
                    targetUser.testData()
                            .friends()
                            .add(responseUser.body());
                } catch (IOException e) {
                    throw new AssertionError(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("targetUser.username():" + targetUser.username());
                System.out.println("newUsername" + newUsername);
                assertEquals(200, responseUser.code());
                System.out.println(responseUser.errorBody());
                System.out.println(responseUser.body());
            }
        }
    }
}
