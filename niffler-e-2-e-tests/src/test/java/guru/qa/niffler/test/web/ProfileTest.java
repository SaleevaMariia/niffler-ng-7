package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.utils.RandomDataUtils.defaultPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;
import static org.junit.jupiter.api.Assertions.assertFalse;


@WebTest

public class ProfileTest {
    private static final Config CFG = Config.getInstance();


    @User(
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesListWhenShowArchivedOn(UserDataJson user) {
        String categoryName = user.testData().categories().getFirst().name();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), defaultPassword).getHeader().toProfilePage()
                .clickArchivedSwitcher()
                .checkThatCategoryVisible(categoryName);
    }

    @User(
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryShouldNotPresentInCategoriesListWhenShowArchivedOff(UserDataJson user) {
        String categoryName = user.testData().categories().getFirst().name();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), defaultPassword).getHeader().toProfilePage()
                .checkThatCategoryIsNotVisible(categoryName);
    }

    @User(
            categories = @Category()
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(UserDataJson user) {
        String categoryName = user.testData().categories().getFirst().name();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), defaultPassword)
                .getHeader().toProfilePage()
                .checkThatCategoryVisible(categoryName);
    }

    @User(
            categories = @Category()
    )
    @Test
    void activeCategoryCanBeArchivedInProfile(UserDataJson user) {
        String categoryName = user.testData().categories().getFirst().name();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), defaultPassword)
                .getHeader().toProfilePage()
                .archiveCategoryByName(categoryName)
                .clickArchivedSwitcher()
                .checkAlertMessage("Category " + categoryName + " is archived")
                .checkThatCategoryArchived(categoryName);
    }

    @User(
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryCanBeActivatedInProfile(UserDataJson user) {
        String categoryName = user.testData().categories().getFirst().name();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), defaultPassword)
                .getHeader().toProfilePage()
                .clickArchivedSwitcher()
                .unArchiveCategoryByName(categoryName)
                .checkAlertMessage("Category " + categoryName + " is unarchived")
                .checkThatCategoryActive(categoryName);
    }

    @User
    @Test
    void nameCanBeChangedInProfile(UserDataJson user) {
        String username = user.username();
        String newName = randomUserName();
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), defaultPassword)
                .getHeader().toProfilePage()
                .checkUsername(username)
                .checkName("")
                .changeName(newName)
                .checkAlertMessage("Profile successfully updated")
                .checkName(newName)
                .checkUsername(username);
    }

    @ScreenShotTest(value = "img/avatar.png")
    void checkAvatar(BufferedImage expected) throws IOException {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin("maria", "123456")
                .getHeader().toProfilePage().checkThatPageLoaded();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        BufferedImage actual = ImageIO.read($("#image__input + div img").screenshot());
        assertFalse(new ScreenDiffResult(
                actual,
                expected
        ));
    }

}
