package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import jaxb.userdata.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;

@ExtendWith(BrowserExtension.class)
public class SpendingWebTest {

    private static final Config CFG = Config.getInstance();

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                  amount = 79990
          )
  )
  @Test
  void categoryDescriptionShouldBeChangedFromTable(UserDataJson user) {
    final String newDescription = "Обучение Niffler Next Generation";
        String spendDescription = user.testData().spendings().getFirst().description();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .editSpending(spendDescription)
                .setNewSpendingDescription(newDescription)
                .save();

        new MainPage().checkThatTableContainsSpending(newDescription);
    }

    @User
    @Test
    void addNewSpendingShouldBeAvailable(UserDataJson user) {
        String category = randomUserName();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .addNewSpendingClick().addNewSpending("300", category,
                        "add new spending", Currency.EUR)
                .checkAlertMessage("New spending is successfully created")
                .checkThatTableContainsSpending("add new spending");
    }
}

