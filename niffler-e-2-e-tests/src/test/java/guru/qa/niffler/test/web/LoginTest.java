package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.Browser;
import guru.qa.niffler.utils.BrowserConverter;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;

public class LoginTest {

    private static final Config CFG = Config.getInstance();


    @RegisterExtension
    private final BrowserExtension browserExtension = new BrowserExtension();

    @ParameterizedTest
    @EnumSource(value = Browser.class, names = {"CHROME", "FIREFOX"})
    void mainPageShouldBeDisplayedAfterSuccessLogin(@ConvertWith(BrowserConverter.class) SelenideDriver driver) {
        browserExtension.add(driver);
        driver.open(CFG.frontUrl());
        new LoginPage(driver)
                .successLogin("maria", "123456");
    }

    @ParameterizedTest
    @EnumSource(value = Browser.class, names = {"CHROME", "FIREFOX"})
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials(@ConvertWith(BrowserConverter.class) SelenideDriver driver) {
        browserExtension.add(driver);
        driver.open(CFG.frontUrl());
        new LoginPage(driver)
                .loginWithBadCredentials(RandomDataUtils.randomUserName(), "BAD")
                .checkErrorAfterBadCredentials();
    }
}