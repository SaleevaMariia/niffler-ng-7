package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class RegisterWebTest {
    private static final Config CFG = Config.getInstance();

    @Test
    void shouldRegisterNewUser() {
        final String login = RandomDataUtils.randomEmail();
        final String password = RandomDataUtils.randomPassword();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccount()
                .setUsername(login)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .singInAfterRegistration()
                .successLogin(login, password)
                .checkNewUserLogin();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        final String login = RandomDataUtils.randomEmail();
        final String password = RandomDataUtils.randomPassword();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccount()
                .setUsername(login)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .singInAfterRegistration();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccount()
                .setUsername(login)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkThatUserAlreadyExistsError();
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        final String login = RandomDataUtils.randomEmail();
        final String password = RandomDataUtils.randomPassword();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccount()
                .setUsername(login)
                .setPassword(password)
                .setPasswordSubmit(password + "1")
                .submitRegistration()
                .checkThatPasswordAndSubmitPasswordNotEqualError();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        final String login = RandomDataUtils.randomEmail();
        final String password = RandomDataUtils.randomPassword();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .loginWithBadCredentials(login, password)
                .checkErrorAfterBadCredentials();
    }
}
