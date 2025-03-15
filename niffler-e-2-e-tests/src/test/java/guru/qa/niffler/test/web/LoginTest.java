package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.utils.SelenideUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class LoginTest {

    private static final Config CFG = Config.getInstance();


    @RegisterExtension
    private final BrowserExtension browserExtension = new BrowserExtension();
    SelenideDriver driver = new SelenideDriver(SelenideUtils.chromeConfig);

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
        browserExtension.drivers().add(driver);
        driver.open(CFG.frontUrl());
        new LoginPage(driver)
                .successLogin(user.username(), user.testData().password())
                .checkThatPageLoaded();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        SelenideDriver firefox = new SelenideDriver(SelenideUtils.firefoxConfig);
        browserExtension.drivers().add(driver);
        browserExtension.drivers().add(firefox);

        firefox.open(CFG.frontUrl());
        new LoginPage(firefox).checkThatPageLoaded();

        driver.open(CFG.frontUrl());
        new LoginPage(driver)
                .loginWithBadCredentials(RandomDataUtils.randomUserName(), "BAD")
                .checkErrorAfterBadCredentials();
    }
}