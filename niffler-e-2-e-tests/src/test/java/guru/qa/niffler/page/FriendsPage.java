package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class FriendsPage {

    private final ElementsCollection friendRequests = $("tbody#requests").$$("tr");
    private final ElementsCollection friends = $("tbody#friends").$$("tr");
    private final SelenideElement friendsArea = $("#simple-tabpanel-friends");
    private final SelenideElement search = $("input[placeholder='Search'");

    public void checkPersonInFriends(String username) {
        search.setValue(username).pressEnter();
        friends.find(text(username)).shouldBe(visible);
    }

    public void checkPersonInFriendsRequests(String username) {
        search.setValue(username).pressEnter();
        friendRequests.find(text(username)).shouldBe(visible);
    }

    public void checkFriendsTableIsEmpty() {
        friendsArea.shouldHave(text("There are no users yet"));
    }

}
