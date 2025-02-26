package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Header extends BaseComponent<Header> {

    public Header(SelenideElement self) {
        super(self);
    }

    public Header() {
        super($("#root header"));
    }

    private final SelenideElement menuBtn = self.$("button");
    private final SelenideElement mainPage = self.$("a[href='/main']");
    private final SelenideElement addSpending = self.$("a[href='/spending']");
    private final ElementsCollection menuList = $("ul[role='menu']").$$("li");

    @Step("Переходим на страницу с друзьями")
    @Nonnull
    public FriendsPage toFriendsPage() {
        menuBtn.click();
        menuList.find(text("Friends")).click();
        return new FriendsPage();
    }

    @Step("Переходим на страницу с всеми пользователями")
    @Nonnull
    public AllPeoplePage toAllPeoplePage() {
        menuBtn.click();
        menuList.find(text("All People")).click();
        return new AllPeoplePage();
    }

    @Step("Переходим на страницу профиля")
    @Nonnull
    public ProfilePage toProfilePage() {
        menuBtn.click();
        menuList.find(text("Profile")).click();
        return new ProfilePage();
    }

    @Step("Переходим на основную страницу")
    @Nonnull
    public MainPage toMainPage() {
        mainPage.click();
        return new MainPage();
    }

    @Step("Нажимаем на кнопку Добавить трату")
    @Nonnull
    public EditSpendingPage addSpendingPage() {
        addSpending.click();
        return new EditSpendingPage();
    }

    @Step("Выходим из приложения")
    @Nonnull
    public LoginPage singOut() {
        menuBtn.click();
        menuList.find(text("Sign out")).click();
        return new LoginPage();
    }
}
