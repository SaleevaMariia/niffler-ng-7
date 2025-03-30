package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Bubble;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;

@ExtendWith(BrowserExtension.class)
public class SpendingWebTest {
    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ApiLogin
    @Test
    void categoryDescriptionShouldBeChangedFromTable(UserDataJson user) {
        final String newDescription = "Обучение Niffler Next Generation";
        String spendDescription = user.testData().spendings().getFirst().description();

        Selenide.open(MainPage.URL, MainPage.class)
                .editSpending(spendDescription)
                .setNewSpendingDescription(newDescription)
                .save();

        new MainPage().getSpendingTable().checkSpendings(SpendJson.toTestSpendings(
                "Обучение",
                79990,
                CurrencyValues.RUB,
                "Обучение Niffler Next Generation",
                new Date()
        ));
    }

    @User
    @ApiLogin
    @Test
    void addNewSpendingShouldBeAvailable() {
        String category = randomUserName();
        Selenide.open(MainPage.URL, MainPage.class)
                .addNewSpendingClick().addNewSpending("300", category,
                        "add new spending", CurrencyValues.EUR)
                .checkAlertMessage("New spending is successfully created")
                .getSpendingTable().checkSpendings(SpendJson.toTestSpendings(
                        category,
                        300,
                        CurrencyValues.EUR,
                        "add new spending",
                        new Date()
                ));
    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ApiLogin
    @ScreenShotTest(value = "img/one_spend.png")
    void checkStatComponentWithOneSpendTest(BufferedImage expected) throws IOException {
        Selenide.open(MainPage.URL, MainPage.class)
                .getStatComponent()
                .checkBubbles(new Bubble(Color.yellow, "Обучение 79990 ₽"))
                .checkStatisticImage(expected);
    }

    @User(
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Обучение Advanced 2.0",
                            amount = 79990
                    ),
                    @Spending(
                            category = "Отдых",
                            description = "Спа-отель",
                            amount = 30000
                    )}
    )
    @ApiLogin
    @ScreenShotTest(value = "img/one_spend.png")
    void checkStatComponentAfterSpendDeletedTest(BufferedImage expected) throws IOException {
        Selenide.open(MainPage.URL, MainPage.class)
                .getStatComponent()
                .checkBubblesInAnyOrder(new Bubble(Color.green, "Отдых 30000 ₽"),
                        new Bubble(Color.yellow, "Обучение 79990 ₽"));

        new MainPage().getSpendingTable()
                .deleteSpending("Спа-отель")
                .getStatComponent()
                .checkBubbles(new Bubble(Color.yellow, "Обучение 79990 ₽"))
                .checkStatisticImage(expected);
    }

    @User(
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Обучение Advanced 2.0",
                            amount = 79990
                    ),
                    @Spending(
                            category = "Отдых",
                            description = "Спа-отель",
                            amount = 30000
                    )}
    )
    @ApiLogin
    @ScreenShotTest(value = "img/two_spend.png")
    void checkStatComponentTwoSpendsTest(BufferedImage expected) throws IOException {
        Selenide.open(MainPage.URL, MainPage.class)
                .getStatComponent()
                .checkBubbles(new Bubble(Color.yellow, "Обучение 79990 ₽"),
                        new Bubble(Color.green, "Отдых 30000 ₽"))
                .checkStatisticImage(expected);

    }

    @User(
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Обучение Advanced 2.0",
                            amount = 79990
                    ),
                    @Spending(
                            category = "Отдых",
                            description = "Спа-отель",
                            amount = 30000
                    )}
    )
    @ApiLogin
    @ScreenShotTest(value = "img/two_edited_spend.png")
    void checkStatComponentAfterSpendEditTest(BufferedImage expected) throws IOException {
        Selenide.open(MainPage.URL, MainPage.class)
                .getSpendingTable().editSpending("Спа-отель").setNewCategory("Массаж").save()
                .getSpendingTable().checkSpendings(
                        SpendJson.toTestSpendings(
                                "Обучение",
                                79990,
                                CurrencyValues.RUB,
                                "Обучение Advanced 2.0",
                                new Date()
                        ),
                        SpendJson.toTestSpendings(
                                "Массаж",
                                30000,
                                CurrencyValues.RUB,
                                "Спа-отель",
                                new Date()
                        )
                );
        new MainPage().getStatComponent()
                .checkBubbles(new Bubble(Color.yellow, "Обучение 79990 ₽"),
                        new Bubble(Color.green, "Массаж 30000 ₽"))
                .checkStatisticImage(expected);
    }

    @User(
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Обучение Advanced 2.0",
                            amount = 79990
                    ),
                    @Spending(
                            category = "Отдых",
                            description = "Спа-отель",
                            amount = 30000
                    )}
    )
    @ApiLogin
    @ScreenShotTest(value = "img/two_other_spend.png")
    void checkStatComponentNoArchiveCategoryTest(BufferedImage expected) throws IOException {
        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .archiveCategoryByName("Обучение")
                .getHeader()
                .toMainPage()
                .getStatComponent()
                .checkBubblesContains(
                        new Bubble(Color.yellow, "Отдых 30000 ₽"),
                        new Bubble(Color.green, "Archived 79990 ₽"))
                .checkStatisticImage(expected);
    }
}

