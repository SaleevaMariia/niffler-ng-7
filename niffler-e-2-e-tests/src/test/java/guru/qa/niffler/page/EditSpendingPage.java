package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.page.component.Header;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class EditSpendingPage extends BasePage<EditSpendingPage> {

    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement saveBtn = $("#save");
    private final SelenideElement amountInput = $("#amount");
    private final SelenideElement currencyBtn = $("#currency");
    private final ElementsCollection currencyList = $$("ul[role='listbox'] li");
    private final SelenideElement categoryInput = $("#category");

    private final Header header = new Header();

    public Header getHeader() {
        return header;
    }

    @Step("Устанавливаем описание траты равным {description}")
    @Nonnull
    public EditSpendingPage setNewSpendingDescription(String description) {
        descriptionInput.clear();
        descriptionInput.setValue(description);
        return this;
    }

    @Step("Устанавливаем сумму траты равным {amount}")
    @Nonnull
    public EditSpendingPage setNewAmount(String amount) {
        amountInput.clear();
        amountInput.setValue(amount);
        return this;
    }

    @Step("Устанавливаем валюту траты равным {currency}")
    @Nonnull
    public EditSpendingPage setNewCurrency(CurrencyValues currency) {
        currencyBtn.click();
        currencyList.find(text(currency.name())).click();
        return this;
    }

    @Step("Устанавливаем категорию траты равным {category}")
    @Nonnull
    public EditSpendingPage setNewCategory(String category) {
        categoryInput.clear();
        categoryInput.setValue(category);
        return this;
    }

    @Step("Добавляем трату с параметрами: сумма {amount}, категория {category}, описание {description}")
    @Nonnull
    public MainPage addNewSpending(String amount, String category, String description) {
        return addNewSpending(amount, category, description, CurrencyValues.RUB);
    }

    @Step("Добавляем трату с параметрами: сумма {amount}, категория {category}, описание {description}, валюта {currency}")
    @Nonnull
    public MainPage addNewSpending(String amount, String category, String description, CurrencyValues currency) {
        setNewAmount(amount);
        setNewCategory(category);
        setNewCurrency(currency);
        setNewSpendingDescription(description);
        save();
        return new MainPage();
    }

    @Step("Нажимаем на кнопку сохранить")
    public MainPage save() {
        saveBtn.click();
        return new MainPage();
    }

    @Override
    @Nonnull
    @Step("Проверяем, что страница редактирования траты отобразилась")
    public EditSpendingPage checkThatPageLoaded() {
        descriptionInput.shouldBe(visible);
        return this;
    }
}
