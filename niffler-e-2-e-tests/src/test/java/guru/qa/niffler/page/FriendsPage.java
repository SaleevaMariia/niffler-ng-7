package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.SearchField;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class FriendsPage {

    private final ElementsCollection friendRequests = $("tbody#requests").$$("tr");
    private final ElementsCollection friends = $("tbody#friends").$$("tr");
    private final SelenideElement friendsArea = $("#simple-tabpanel-friends");
    private final SearchField search = new SearchField($("input[placeholder='Search'"));

    @Step("Проверяем, что пользователь {username} есть в списке друзей")
    public void checkPersonInFriends(String username) {
        search.search(username);
        friends.find(text(username)).shouldBe(visible);
    }

    @Step("Проверяем, что пользователя {username} нет в списке друзей")
    public void checkPersonNotInFriends(String username) {
        search.search(username);
        friends.find(text(username)).shouldNotBe(visible);
    }

    @Step("Проверяем, что пользователь {username} отправим заявку в друзья")
    public void checkPersonInFriendsRequests(String username) {
        search.search(username);
        friendRequests.find(text(username)).shouldBe(visible);
    }

    @Step("Принимаем заявку в друзья от пользователя {username}")
    @Nonnull
    public FriendsPage acceptFriendsRequest(String username) {
        checkPersonInFriendsRequests(username);
        friendRequests.find(text(username)).$$("button").find(text("Accept")).click();
        return this;
    }

    @Step("Отклоняем заявку в друзья от пользователя {username}")
    @Nonnull
    public FriendsPage declineFriendsRequest(String username) {
        checkPersonInFriendsRequests(username);
        friendRequests.find(text(username)).$$("button").find(text("Decline")).click();
        $$("div[role='dialog'] button").find(text("Decline")).click();
        return this;
    }

    @Step("Проверяем, что таблица с друзьями пуста")
    public void checkFriendsTableIsEmpty() {
        friendsArea.shouldHave(text("There are no users yet"));
    }

}
