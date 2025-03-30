package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserDataJson;
import guru.qa.niffler.page.AllPeoplePage;
import guru.qa.niffler.page.FriendsPage;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsWebTest {

    @User(friends = 1)
    @ApiLogin
    @Test
    void friendShouldBePresentInFriendsTable(UserDataJson user) {
        Selenide.open(FriendsPage.URL, FriendsPage.class)
                .checkPersonInFriends(user.testData().friendsUsernames()[0]);
    }

    @User
    @ApiLogin
    @Test
    void friendsTableShouldBeEmptyForNewUser() {
        Selenide.open(FriendsPage.URL, FriendsPage.class)
                .checkFriendsTableIsEmpty();
    }

    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void incomeInvitationBePresentInFriendsTable(UserDataJson user) {
        Selenide.open(FriendsPage.URL, FriendsPage.class)
                .checkPersonInFriendsRequests(user.testData().incomeInvitationsUsernames()[0]);
    }

    @User(outcomeInvitations = 1)
    @ApiLogin
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserDataJson user) {
        Selenide.open(AllPeoplePage.URL, AllPeoplePage.class)
                .checkPersonWasSentOutcomeRequest(user.testData().outcomeInvitationsUsernames()[0]);
    }


    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void userCanAcceptFriendInvitation(UserDataJson user) {
        String userWithInvitationsName = user.testData().incomeInvitationsUsernames()[0];
        Selenide.open(FriendsPage.URL, FriendsPage.class)
                .acceptFriendsRequest(userWithInvitationsName)
                .checkAlertMessage("Invitation of " + userWithInvitationsName + " accepted")
                .checkPersonInFriends(userWithInvitationsName);
    }

    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void userCanDeclineFriendInvitation(UserDataJson user) {
        String userWithInvitationsName = user.testData().incomeInvitationsUsernames()[0];
        Selenide.open(FriendsPage.URL, FriendsPage.class)
                .declineFriendsRequest(userWithInvitationsName)
                .checkAlertMessage("Invitation of " + userWithInvitationsName + " is declined")
                .checkPersonNotInFriends(userWithInvitationsName);
    }

}
