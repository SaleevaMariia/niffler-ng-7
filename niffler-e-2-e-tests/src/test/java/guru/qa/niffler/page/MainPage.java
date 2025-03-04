package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {
    private final SelenideElement statistics = $("#stat canvas");
    private final SelenideElement statComponent = $("#stat");
    private final SelenideElement addSpendingBtn = $("a[href='/spending']");

    private final ElementsCollection legends = $$("#legend-container li");
    private final Header header = new Header();

    public Header getHeader() {
        return header;
    }

    private final SpendingTable spendingTable = new SpendingTable();

    public SpendingTable getSpendingTable() {
        return spendingTable;
    }

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

    @Step("Ждем прогрузки статической компоненты")
    @Nonnull
    public MainPage waitToLoadAll() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Step("Проверяем, что в ячейках под статистикой есть блоки")
    @Nonnull
    public MainPage checkLegendsContainsName(String... names) {
        for (String name : names) {
            legends.filter(text(name)).first().shouldBe(visible);
        }
        return this;
    }

}
