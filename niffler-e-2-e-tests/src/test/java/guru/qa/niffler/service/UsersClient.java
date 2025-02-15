package guru.qa.niffler.service;

import guru.qa.niffler.model.UserDataJson;

public interface UsersClient {
    UserDataJson createUser(String username, String password);

    void createIncomeInvitation(UserDataJson targetUser, int count);

    void createOutcomeInvitation(UserDataJson targetUser, int count);

    void createFriends(UserDataJson targetUser, int count);
}
