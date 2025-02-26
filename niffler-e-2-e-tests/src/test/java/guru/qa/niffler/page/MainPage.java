package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {
    private final SelenideElement statistics = $("#stat canvas");
    private final SelenideElement statComponent = $("#stat");
    private final SelenideElement addSpendingBtn = $("a[href='/spending']");
    private final Header header = new Header();

    public Header getHeader() {
        return header;
    }

    private final SpendingTable spendingTable = new SpendingTable();

    @Nonnull
    public EditSpendingPage editSpending(String spendingDescription) {
        return spendingTable.editSpending(spendingDescription);
    }

    @Nonnull
    public EditSpendingPage addNewSpendingClick() {
        addSpendingBtn.click();
        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        spendingTable.checkTableContains(spendingDescription);
    }

    @Step("Проверяем, что у нового пользователя нет трат")
    public void checkNewUserLogin() {
        statistics.should(visible);
        spendingTable.checkTableSize(0);
    }

    @Step("Проверяем, что пользователь успешно зашел в аккаунт")
    @Nonnull
    public MainPage checkThatPageLoaded() {
        statComponent.should(visible).shouldHave(text("Statistics"));
        return this;
    }
}
