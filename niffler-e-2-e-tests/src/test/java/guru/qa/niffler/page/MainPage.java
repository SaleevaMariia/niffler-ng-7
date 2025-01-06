package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement avatarButton = $("button.MuiButtonBase-root[aria-label=Menu]");
  private final SelenideElement friendsMenu = $("a[href='/people/friends']");
  private final SelenideElement allPeopleMenu = $("a[href='/people/all']");

  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public FriendsPage goToFriends() {
    avatarButton.click();
    friendsMenu.shouldBe(visible).click();
    return new FriendsPage();
  }

  public AllPeoplePage goToAllPeople() {
    avatarButton.click();
    allPeopleMenu.shouldBe(visible).click();
    return new AllPeoplePage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).should(visible);
  }
}
