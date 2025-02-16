package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.defaultPassword;


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
                .successLogin(user.username(), defaultPassword).goToProfile()
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
                .successLogin(user.username(), defaultPassword).goToProfile()
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
                .goToProfile()
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
                .goToProfile()
                .archiveCategoryByName(categoryName)
                .clickArchivedSwitcher()
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
                .goToProfile()
                .clickArchivedSwitcher()
                .unArchiveCategoryByName(categoryName)
                .checkThatCategoryActive(categoryName);
    }

}
