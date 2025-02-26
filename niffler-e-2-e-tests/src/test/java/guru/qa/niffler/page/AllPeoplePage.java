package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class AllPeoplePage {
    private final ElementsCollection allPeople = $("tbody#all").$$("tr");
    private final SelenideElement search = $("input[placeholder='Search'");

    @Step("Проверяем, что пользователь отправил запрос в друзья для {username}")
    public void checkPersonWasSentOutcomeRequest(String username) {
        search.setValue(username).pressEnter();
        allPeople.find(text(username)).shouldBe(visible);
        allPeople.find(text(username)).shouldHave(text("Waiting..."));
    }
}
