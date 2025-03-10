package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import io.qameta.allure.Step;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
@Getter
public class MainPage extends BasePage<MainPage> {
    public static final String URL = CFG.frontUrl() + "main";
    private final SelenideElement addSpendingBtn = $("a[href='/spending']");
    private final ElementsCollection legends = $$("#legend-container li");
    private final Header header = new Header();
    private final StatComponent statComponent = new StatComponent();
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
        statComponent.getSelf().should(visible).shouldHave(text("Statistics"));
        spendingTable.checkTableSize(0);
    }

    @Step("Проверяем, что пользователь успешно зашел в аккаунт")
    @Nonnull
    public MainPage checkThatPageLoaded() {
        statComponent.getSelf().should(visible).shouldHave(text("Statistics"));
        return this;
    }

}
