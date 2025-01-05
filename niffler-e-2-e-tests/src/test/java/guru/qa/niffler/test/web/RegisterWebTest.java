package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class RegisterWebTest {
    private static final Config CFG = Config.getInstance();
    private static final Faker faker = new Faker();

    @Test
    void shouldRegisterNewUser() {
        final String login = faker.internet().emailAddress();
        final String password = faker.internet().password(3, 12);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccount()
                .setUsername(login)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .singInAfterRegistration()
                .login(login, password)
                .checkNewUserLogin();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        final String login = faker.internet().emailAddress();
        final String password = faker.internet().password(3, 12);
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
        final String login = faker.internet().emailAddress();
        final String password = faker.internet().password(3, 12);
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
        final String login = faker.internet().emailAddress();
        final String password = faker.internet().password(3, 12);
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .loginWithBadCredentials(login, password)
                .checkErrorAfterBadCredentials();
    }
}
