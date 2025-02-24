package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class AllPeoplePage extends BasePage<AllPeoplePage> {
    private final ElementsCollection allPeople = $("tbody#all").$$("tr");
    private final SearchField searchField = new SearchField($("input[placeholder='Search'"));

    @Step("Проверяем, что пользователь отправил запрос в друзья для {username}")
    public void checkPersonWasSentOutcomeRequest(String username) {
        searchField.search(username);
        allPeople.find(text(username)).shouldBe(visible);
        allPeople.find(text(username)).shouldHave(text("Waiting..."));
    }

    @Override
    @Nonnull
    @Step("Проверяем, что страница с списком пользователей отобразилась")
    public AllPeoplePage checkThatPageLoaded() {
        searchField.getSelf().shouldBe(visible);
        return this;
    }
}
