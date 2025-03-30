package guru.qa.niffler.service;

import guru.qa.niffler.model.rest.UserDataJson;
import guru.qa.niffler.service.impl.UserApiClient;
import guru.qa.niffler.service.impl.UserDbClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface UsersClient {
    static UsersClient getInstance() {
        return "api".equals(System.getProperty("client.impl"))
                ? new UserApiClient()
                : new UserDbClient();
    }

    @Nonnull
    UserDataJson createUser(String username, String password);

    void createIncomeInvitation(UserDataJson targetUser, int count);

    void createOutcomeInvitation(UserDataJson targetUser, int count);

    void createFriends(UserDataJson targetUser, int count);
}
