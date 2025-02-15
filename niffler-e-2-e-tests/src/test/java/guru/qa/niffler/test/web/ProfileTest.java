package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;


@WebTest
public class ProfileTest {
    private static final Config CFG = Config.getInstance();
    private static final String login = "maria";
    private static final String password = "123456";


    @User(
            username = login,
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesListWhenShowArchivedOn(CategoryJson[] category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(login, password).goToProfile()
                .clickArchivedSwitcher()
                .checkThatCategoryVisible(category[0].name());
    }

    @User(
            username = login,
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryShouldNotPresentInCategoriesListWhenShowArchivedOff(CategoryJson[] category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(login, password).goToProfile()
                .checkThatCategoryIsNotVisible(category[0].name());
    }

    @User(
            username = login,
            categories = @Category()
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson[] category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(login, password)
                .goToProfile()
                .checkThatCategoryVisible(category[0].name());
    }

    @User(
            username = login,
            categories = @Category()
    )
    @Test
    void activeCategoryCanBeArchivedInProfile(CategoryJson[] category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(login, password)
                .goToProfile()
                .archiveCategoryByName(category[0].name())
                .clickArchivedSwitcher()
                .checkThatCategoryArchived(category[0].name());
    }

    @User(
            username = login,
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryCanBeActivatedInProfile(CategoryJson[] category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(login, password)
                .goToProfile()
                .clickArchivedSwitcher()
                .unArchiveCategoryByName(category[0].name())
                .checkThatCategoryActive(category[0].name());
    }

}
