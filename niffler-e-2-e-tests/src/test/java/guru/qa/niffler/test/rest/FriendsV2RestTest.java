package guru.qa.niffler.test.rest;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.pageable.RestResponsePage;
import guru.qa.niffler.model.rest.UserDataJson;
import guru.qa.niffler.service.impl.GatewayV2ApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static io.qameta.allure.Allure.step;

@RestTest
public class FriendsV2RestTest {

    @RegisterExtension
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.api();

    private final GatewayV2ApiClient gatewayApiClient = new GatewayV2ApiClient();

    @ApiLogin
    @User(friends = 1, incomeInvitations = 1)
    @Test
    void friendsAndIncomeInvitationsListShouldBeReturned(UserDataJson user, @Token String token) {
        final UserDataJson expectedFriend = user.testData().friends().getFirst();
        final UserDataJson expectedInvitation = user.testData().incomeInvitations().getFirst();

        final RestResponsePage<UserDataJson> response = gatewayApiClient.allFriends(token, 0, 2, null, null);

        step("Check that response contains expected users", () -> {
            Assertions.assertEquals(2, response.getContent().size());
        });

        final UserDataJson actualInvitation = response.getContent().getFirst();
        final UserDataJson actualFriend = response.getContent().getLast();

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
        final UserDataJson expectedInvitation = user.testData().incomeInvitations().getFirst();

        final RestResponsePage<UserDataJson> response = gatewayApiClient.allFriends(token, 0, 2, null, expectedInvitation.username());

        step("Check that response contains expected users", () -> {
            Assertions.assertEquals(1, response.getContent().size());
        });

        final UserDataJson actualInvitation = response.getContent().getFirst();

        step("Check income invitation in response", () -> {
            Assertions.assertEquals(expectedInvitation.id(), actualInvitation.id());
            Assertions.assertEquals(expectedInvitation.username(), actualInvitation.username());
            Assertions.assertEquals(FriendState.INVITE_RECEIVED, actualInvitation.friendState());
        });
    }
}