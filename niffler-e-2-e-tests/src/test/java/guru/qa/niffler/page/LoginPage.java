package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement createButton = $("a.form__register");
  private final SelenideElement errorBadCredentials = $("div.form__error-container > p.form__error");

  public MainPage login(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return new MainPage();
  }

  public RegisterPage clickCreateNewAccount() {
    createButton.click();
    return new RegisterPage();
  }

  public LoginPage loginWithBadCredentials(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return new LoginPage();
  }

  public void checkErrorAfterBadCredentials() {
    errorBadCredentials.shouldBe(visible).shouldHave(text("Неверные учетные данные пользователя"));
    usernameInput.shouldBe(visible);
    passwordInput.shouldBe(visible);
    submitButton.shouldBe(visible);
    createButton.shouldBe(visible);
  }
}
