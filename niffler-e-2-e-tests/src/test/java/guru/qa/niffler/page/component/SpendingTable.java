package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.MainPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class SpendingTable extends BaseComponent<SpendingTable> {
    public SpendingTable(SelenideElement self) {
        super(self);
    }

    public SpendingTable() {
        super($("#spendings"));
    }

    private final SearchField search = new SearchField(self.$("input[placeholder='Search'"));
    private final ElementsCollection tableRows = self.$("tbody").$$("tr");

    private final SelenideElement deleteBtn = self.$("#delete");
    private final SelenideElement deleteSpending = $$("div.MuiDialogActions-spacing button").find(text("Delete"));
    private final SelenideElement periodBtn = $("#period");
    private final ElementsCollection periods = $$("ul[role='listbox'] li");
    private final SelenideElement noSpending = $("div.MuiBox-root p");


    @Step("Редактируем описание на {description}")
    @Nonnull
    public EditSpendingPage editSpending(String description) {
        searchSpendingByDescription(description);
        tableRows.find(text(description)).$("button[aria-label='Edit spending']").click();
        return new EditSpendingPage();
    }

    @Step("Удаляем трату с описанием: {description}")
    @Nonnull
    public MainPage deleteSpending(String description) {
        searchSpendingByDescription(description);
        tableRows.find(text(description)).$("input").click();
        deleteBtn.click();
        deleteSpending.click();
        return new MainPage();
    }

    @Step("Ищем трату с описанием: {description}")
    @Nonnull
    public SpendingTable searchSpendingByDescription(String description) {
        search.search(description);
        return this;
    }

    @Step("Проверяем, что существует трата с описанием: {description}")
    public void checkTableContains(String description) {
        tableRows.find(text(description)).should(visible);
    }

    @Step("Проверяем, что размер таблицы с тратами равен {expectedSize}")
    public void checkTableSize(int expectedSize) {
        if (expectedSize == 0) {
            noSpending.shouldHave(exactText("There are no spendings"));
        } else {
            assertEquals(tableRows.size(), expectedSize);
        }
    }

    @Step("Выбираем период для поиска трат: {period}")
    @Nonnull
    public SpendingTable selectPeriod(DataFilterValues period) {
        periodBtn.click();
        switch (period) {
            case TODAY -> periods.find(text("Today")).click();
            case MONTH -> periods.find(text("Last month")).click();
            case WEEK -> periods.find(text("Last week")).click();
            default -> periods.find(text("All time")).click();
        }
        return this;
    }


}
