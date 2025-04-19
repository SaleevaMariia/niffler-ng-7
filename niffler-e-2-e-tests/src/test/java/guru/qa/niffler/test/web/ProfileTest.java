package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;


@WebTest
public class ProfileTest {
    @User(
            categories = @Category(
                    archived = true
            )
    )
    @ApiLogin
    @Test
    void archivedCategoryShouldPresentInCategoriesListWhenShowArchivedOn(UserDataJson user) {
        String categoryName = user.testData().categories().getFirst().name();
        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .clickArchivedSwitcher()
                .checkThatCategoryVisible(categoryName);
    }

    @User(
            categories = @Category(
                    archived = true
            )
    )
    @ApiLogin
    @Test
    void archivedCategoryShouldNotPresentInCategoriesListWhenShowArchivedOff(UserDataJson user) {
        String categoryName = user.testData().categories().getFirst().name();
        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .checkThatCategoryIsNotVisible(categoryName);
    }

    @User(
            categories = @Category()
    )
    @ApiLogin
    @Test
    void activeCategoryShouldPresentInCategoriesList(UserDataJson user) {
        String categoryName = user.testData().categories().getFirst().name();
        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .checkThatCategoryVisible(categoryName);
    }

    @User(
            categories = @Category()
    )
    @ApiLogin
    @Test
    void activeCategoryCanBeArchivedInProfile(UserDataJson user) {
        String categoryName = user.testData().categories().getFirst().name();
        Selenide.open(ProfilePage.URL, ProfilePage.class)
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
    @ApiLogin
    @Test
    void archivedCategoryCanBeActivatedInProfile(UserDataJson user) {
        String categoryName = user.testData().categories().getFirst().name();
        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .clickArchivedSwitcher()
                .unArchiveCategoryByName(categoryName)
                .checkAlertMessage("Category " + categoryName + " is unarchived")
                .checkThatCategoryActive(categoryName);
    }

    @User
    @ApiLogin
    @Test
    void nameCanBeChangedInProfile(UserDataJson user) {
        String username = user.username();
        String newName = randomUserName();
        Selenide.open(ProfilePage.URL, ProfilePage.class)
                .checkUsername(username)
                .checkName("")
                .changeName(newName)
                .checkAlertMessage("Profile successfully updated")
                .checkName(newName)
                .checkUsername(username);
    }

    @ScreenShotTest(value = "img/avatar.png")
    @ApiLogin(username = "maria", password = "123456")
    void checkAvatar(BufferedImage expected) throws IOException {
        Selenide.open(ProfilePage.URL, ProfilePage.class).checkThatPageLoaded()
                .checkAvatarImage(expected);
    }
}
