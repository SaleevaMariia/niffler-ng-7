package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
  private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");
  private final SelenideElement statistics = $("#stat canvas");
  private final SelenideElement noSpending = $("div.MuiBox-root p");
  private final SelenideElement avatarButton = $("button.MuiButtonBase-root[aria-label=Menu]");
  private final SelenideElement profileMenu = $("a[href='/profile']");

  public EditSpendingPage editSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).$$("td").get(5).click();
    return new EditSpendingPage();
  }

  public ProfilePage goToProfile() {
    avatarButton.click();
    profileMenu.shouldBe(visible).click();
    return new ProfilePage();
  }

  public void checkThatTableContainsSpending(String spendingDescription) {
    tableRows.find(text(spendingDescription)).should(visible);
  }

  public void checkNewUserLogin() {
    statistics.should(visible);
    noSpending.shouldHave(exactText("There are no spendings"));
  }
}
