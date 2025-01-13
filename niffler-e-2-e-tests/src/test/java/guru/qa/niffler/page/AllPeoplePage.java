package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class AllPeoplePage {
    private final ElementsCollection allPeople = $("tbody#all").$$("tr");
    private final SelenideElement search = $("input[placeholder='Search'");

    public void checkPersonWasSentOutcomeRequest(String username) {
        search.setValue(username).pressEnter();
        allPeople.find(text(username)).shouldBe(visible);
        allPeople.find(text(username)).shouldHave(text("Waiting..."));
    }
}
