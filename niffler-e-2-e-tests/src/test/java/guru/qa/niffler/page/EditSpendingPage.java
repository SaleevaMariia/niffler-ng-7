package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import jaxb.userdata.Currency;

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
    public EditSpendingPage setNewCurrency(Currency currency) {
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
        return addNewSpending(amount, category, description, Currency.RUB);
    }

    @Step("Добавляем трату с параметрами: сумма {amount}, категория {category}, описание {description}, валюта {currency}")
    @Nonnull
    public MainPage addNewSpending(String amount, String category, String description, Currency currency) {
        setNewAmount(amount);
        setNewCategory(category);
        setNewCurrency(currency);
        setNewSpendingDescription(description);
        save();
        return new MainPage();
    }

    @Step("Нажимаем на кнопку сохранить")
    public void save() {
        saveBtn.click();
    }

    @Override
    @Nonnull
    @Step("Проверяем, что страница редактирования траты отобразилась")
    public EditSpendingPage checkThatPageLoaded() {
        descriptionInput.shouldBe(visible);
        return this;
    }
}
