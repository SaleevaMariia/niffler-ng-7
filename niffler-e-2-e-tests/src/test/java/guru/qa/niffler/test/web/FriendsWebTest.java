package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsWebTest {
    private static final Config CFG = Config.getInstance();

    @User(friends = 1)
    @Test
    void friendShouldBePresentInFriendsTable(UserDataJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .goToFriends()
                .checkPersonInFriends(user.testData().friendsUsernames()[0]);
    }

    @User
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserDataJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .goToFriends()
                .checkFriendsTableIsEmpty();
    }

    @User(incomeInvitations = 1)
    @Test
    void incomeInvitationBePresentInFriendsTable(UserDataJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .goToFriends()
                .checkPersonInFriendsRequests(user.testData().incomeInvitationsUsernames()[0]);
    }

    @User(outcomeInvitations = 1)
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserDataJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .goToAllPeople()
                .checkPersonWasSentOutcomeRequest(user.testData().outcomeInvitationsUsernames()[0]);
    }

}
