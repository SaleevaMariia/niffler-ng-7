package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class SearchField extends BaseComponent<SearchField> {
    private final SelenideElement clearBtn = $("#input-clear");

    public SearchField(SelenideElement self) {
        super(self);
    }

    @Step("Выполняем поиск по значению {query}")
    @Nonnull
    public SearchField search(String query) {
        clearIfNotEmpty();
        self.setValue(query).pressEnter();
        return this;
    }

    @Step("Очищаем поле поиска")
    @Nonnull
    public SearchField clearIfNotEmpty() {
        if (clearBtn.is(visible)) {
            clearBtn.click();
        }
        return this;
    }
}
