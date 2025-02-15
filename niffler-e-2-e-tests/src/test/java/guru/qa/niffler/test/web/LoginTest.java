package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

@WebTest
public class LoginTest {

    private static final Config CFG = Config.getInstance();

    @User(
            categories = {
                    @Category(
                            name = "Магазины", archived = true
                    ),
                    @Category(
                            name = "Бары", archived = false
                    )
            },
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Test",
                            amount = 80000
                    )
            }
    )
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserDataJson user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        LoginPage loginPage = Selenide.open(CFG.frontUrl(), LoginPage.class);
        loginPage.loginWithBadCredentials(RandomDataUtils.randomUserName(), "BAD");
        loginPage.checkErrorAfterBadCredentials();
    }
}