package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.SelenideUtils;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();
    SelenideDriver driver = new SelenideDriver(SelenideUtils.chromeConfig);
    @User(friends = 1)
    @Test
    void friendShouldBePresentInFriendsTable(UserDataJson user) {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .checkPersonInFriends(user.testData().friendsUsernames()[0]);
    }

    @User
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserDataJson user) {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .checkFriendsTableIsEmpty();
    }

    @User(incomeInvitations = 1)
    @Test
    void incomeInvitationBePresentInFriendsTable(UserDataJson user) {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .checkPersonInFriendsRequests(user.testData().incomeInvitationsUsernames()[0]);
    }

    @User(outcomeInvitations = 1)
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserDataJson user) {
        driver.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .getHeader()
                .toAllPeoplePage()
                .checkPersonWasSentOutcomeRequest(user.testData().outcomeInvitationsUsernames()[0]);
    }


    @User(incomeInvitations = 1)
    @Test
    void userCanAcceptFriendInvitation(UserDataJson user) {
        String userWithInvitationsName = user.testData().incomeInvitationsUsernames()[0];
        driver.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .acceptFriendsRequest(userWithInvitationsName)
                .checkAlertMessage("Invitation of " + userWithInvitationsName + " accepted")
                .checkPersonInFriends(userWithInvitationsName);
    }

    @User(incomeInvitations = 1)
    @Test
    void userCanDeclineFriendInvitation(UserDataJson user) {
        String userWithInvitationsName = user.testData().incomeInvitationsUsernames()[0];
        driver.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .getHeader()
                .toFriendsPage()
                .declineFriendsRequest(userWithInvitationsName)
                .checkAlertMessage("Invitation of " + userWithInvitationsName + " is declined")
                .checkPersonNotInFriends(userWithInvitationsName);
    }

}
