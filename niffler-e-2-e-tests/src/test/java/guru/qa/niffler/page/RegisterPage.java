package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement submitPasswordInput = $("#passwordSubmit");
    private final SelenideElement singUpButton = $("button[type='submit']");
    private final SelenideElement singInButton = $("a.form_sign-in");
    private final SelenideElement errorUserAlreadyExists = $("#username ~ span");
    private final SelenideElement errorPasswordsShouldBeEqual = $("#password ~ span");

    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return new RegisterPage();
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return new RegisterPage();
    }

    public RegisterPage setPasswordSubmit(String passwordSubmit) {
        submitPasswordInput.setValue(passwordSubmit);
        return new RegisterPage();
    }

    public RegisterPage submitRegistration() {
        singUpButton.click();
        return new RegisterPage();
    }

    public LoginPage singInAfterRegistration() {
        singInButton.shouldBe(visible).click();
        return new LoginPage();
    }

    public void checkThatUserAlreadyExistsError() {
        errorUserAlreadyExists.shouldBe(visible).shouldHave(partialText("already exists"));
    }

    public void checkThatPasswordAndSubmitPasswordNotEqualError() {
        errorPasswordsShouldBeEqual.shouldBe(visible).shouldHave(text("Passwords should be equal"));
    }

}
