package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.model.rest.UserDataJson;
import guru.qa.niffler.service.impl.AuthApiClient;
import guru.qa.niffler.service.impl.GatewayApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.defaultPassword;
import static io.qameta.allure.Allure.step;

@RestTest
public class FriendsRestTest {

    @RegisterExtension
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.api();

    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();
    private final AuthApiClient authApiClient = new AuthApiClient();

    @ApiLogin
    @User(friends = 1, incomeInvitations = 1)
    @Test
    void friendsAndIncomeInvitationsListShouldBeReturned(UserDataJson user, @Token String token) {
        final UserDataJson expectedFriend = user.testData().friends().getFirst();
        final UserDataJson expectedInvitation = user.testData().incomeInvitations().getFirst();

        final List<UserDataJson> response = gatewayApiClient.allFriends(token, null);

        step("Check that response contains expected users", () -> {
            Assertions.assertEquals(2, response.size());
        });

        final UserDataJson actualInvitation = response.getFirst();
        final UserDataJson actualFriend = response.getLast();

        step("Check friend in response", () -> {
            Assertions.assertEquals(expectedFriend.id(), actualFriend.id());
            Assertions.assertEquals(expectedFriend.username(), actualFriend.username());
            Assertions.assertEquals(FriendState.FRIEND, actualFriend.friendState());

        });

        step("Check income invitation in response", () -> {
            Assertions.assertEquals(expectedInvitation.id(), actualInvitation.id());
            Assertions.assertEquals(expectedInvitation.username(), actualInvitation.username());
            Assertions.assertEquals(FriendState.INVITE_RECEIVED, actualInvitation.friendState());
        });
    }

    @ApiLogin
    @User(friends = 1, incomeInvitations = 1)
    @Test
    void friendsAndIncomeInvitationsListShouldBeReturnedAndFiltered(UserDataJson user, @Token String token) {
        final UserDataJson expectedFriend = user.testData().friends().getFirst();

        final List<UserDataJson> response = gatewayApiClient.allFriends(token, expectedFriend.username());

        step("Check that response contains expected users", () -> {
            Assertions.assertEquals(1, response.size());
        });

        final UserDataJson actualFriend = response.getFirst();

        step("Check friend in response", () -> {
            Assertions.assertEquals(expectedFriend.id(), actualFriend.id());
            Assertions.assertEquals(expectedFriend.username(), actualFriend.username());
            Assertions.assertEquals(FriendState.FRIEND, actualFriend.friendState());
        });
    }

    @ApiLogin
    @User(friends = 1)
    @Test
    void friendsCanBeDeleted(UserDataJson user, @Token String token) {
        gatewayApiClient.removeFriend(token, user.testData().friends().getFirst().username());

        final List<UserDataJson> response = gatewayApiClient.allFriends(token, null);

        step("Check that response contains expected users", () -> {
            Assertions.assertEquals(0, response.size());
        });
    }

    @ApiLogin
    @User(incomeInvitations = 1)
    @Test
    void incomeInvitationsCanBeAccepted(UserDataJson user, @Token String token) {
        final UserDataJson expectedFriend = user.testData().incomeInvitations().getFirst();

        gatewayApiClient.acceptInvitation
                (token, new FriendJson(expectedFriend.username()));

        final List<UserDataJson> response = gatewayApiClient.allFriends(token, null);

        step("Check that response contains expected users", () -> {
            Assertions.assertEquals(1, response.size());
        });

        final UserDataJson actualFriend = response.getFirst();

        step("Check friend in response", () -> {
            Assertions.assertEquals(expectedFriend.id(), actualFriend.id());
            Assertions.assertEquals(expectedFriend.username(), actualFriend.username());
            Assertions.assertEquals(FriendState.FRIEND, actualFriend.friendState());
        });
    }

    @ApiLogin
    @User(incomeInvitations = 1)
    @Test
    void incomeInvitationsCanBeDeclined(UserDataJson user, @Token String token) {
        gatewayApiClient.declineInvitation
                (token, new FriendJson(user.testData().incomeInvitations().getFirst().username()));

        final List<UserDataJson> response = gatewayApiClient.allFriends(token, null);

        step("Check that response contains expected users", () -> {
            Assertions.assertEquals(0, response.size());
        });
    }

    @ApiLogin
    @User(friends = 1)
    @Test
    void createIncomeAndOutcomeFriendsRequestAfterSendingFriendInvitation(UserDataJson user, @Token String token) {
        final UserDataJson exFriend = user.testData().friends().getFirst();
        gatewayApiClient.removeFriend(token, exFriend.username());

        gatewayApiClient.sendInvitation
                (token, new FriendJson(exFriend.username()));

        final List<UserDataJson> response = gatewayApiClient.allUsers(token, exFriend.username());

        step("Check that response contains expected users", () -> {
            Assertions.assertEquals(1, response.size());
        });

        final UserDataJson actualOutcomeInvitation = response.getFirst();

        step("Check outcome invitation in response", () -> {
            Assertions.assertEquals(exFriend.username(), actualOutcomeInvitation.username());
            Assertions.assertEquals(FriendState.INVITE_SENT, actualOutcomeInvitation.friendState());
        });

        ThreadSafeCookieStore.INSTANCE.removeAll();

        final String token2 = "Bearer " + authApiClient.login(exFriend.username(), defaultPassword);

        final List<UserDataJson> response2 = gatewayApiClient.allFriends(token2, null);

        final UserDataJson actualIncomeInvitation = response2.getFirst();

        step("Check that response contains expected users", () -> {
            Assertions.assertEquals(1, response.size());
        });

        step("Check income invitation in response", () -> {
            Assertions.assertEquals(user.username(), actualIncomeInvitation.username());
            Assertions.assertEquals(FriendState.INVITE_RECEIVED, actualIncomeInvitation.friendState());
        });
    }
}