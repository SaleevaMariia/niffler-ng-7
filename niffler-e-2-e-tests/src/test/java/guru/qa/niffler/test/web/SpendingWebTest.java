package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.Currency;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenShotTest(value = "img/one_spend.png")
    void checkStatComponentWithOneSpendTest(UserDataJson user, BufferedImage expected) throws IOException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .waitToLoadAll()
                .checkLegendsContainsName("Обучение");

        BufferedImage actual = ImageIO.read($("#stat canvas").screenshot());
        assertFalse(new ScreenDiffResult(
                actual,
                expected
        ));
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
    @ScreenShotTest(value = "img/one_spend.png")
    void checkStatComponentAfterSpendDeletedTest(UserDataJson user, BufferedImage expected) throws IOException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .checkLegendsContainsName("Обучение", "Отдых")
                .getSpendingTable().deleteSpending("Спа-отель")
                .waitToLoadAll()
                .checkLegendsContainsName("Обучение");

        BufferedImage actual = ImageIO.read($("#stat canvas").screenshot());
        assertFalse(new ScreenDiffResult(
                actual,
                expected
        ));
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
    @ScreenShotTest(value = "img/two_spend.png")
    void checkStatComponentTwoSpendsTest(UserDataJson user, BufferedImage expected) throws IOException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .waitToLoadAll()
                .checkLegendsContainsName("Обучение", "Отдых");


        BufferedImage actual = ImageIO.read($("#stat canvas").screenshot());
        assertFalse(new ScreenDiffResult(
                actual,
                expected
        ));
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
    @ScreenShotTest(value = "img/two_edited_spend.png")
    void checkStatComponentAfterSpendEditTest(UserDataJson user, BufferedImage expected) throws IOException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .waitToLoadAll()
                .checkLegendsContainsName("Обучение", "Отдых")
                .getSpendingTable().editSpending("Спа-отель").setNewCategory("Массаж").save()
                .waitToLoadAll()
                .checkLegendsContainsName("Обучение", "Массаж");

        BufferedImage actual = ImageIO.read($("#stat canvas").screenshot());
        assertFalse(new ScreenDiffResult(
                actual,
                expected
        ));
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
    @ScreenShotTest(value = "img/two_other_spend.png")
    void checkStatComponentNoArchiveCategoryTest(UserDataJson user, BufferedImage expected) throws IOException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .getHeader().toProfilePage()
                .archiveCategoryByName("Обучение")
                .getHeader()
                .toMainPage()
                .waitToLoadAll()
                .checkLegendsContainsName("Отдых", "Archived");

        BufferedImage actual = ImageIO.read($("#stat canvas").screenshot());
        assertFalse(new ScreenDiffResult(
                actual,
                expected
        ));
    }
}

