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
    void archivedCategoryShouldPresentInCategoriesListWhenShowArchivedOn(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(login, password).goToProfile()
                .clickArchivedSwitcher()
                .checkThatCategoryVisible(category.name());
    }

    @User(
            username = login,
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryShouldNotPresentInCategoriesListWhenShowArchivedOff(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(login, password).goToProfile()
                .checkThatCategoryIsNotVisible(category.name());
    }

    @User(
            username = login,
            categories = @Category()
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(login, password)
                .goToProfile()
                .checkThatCategoryVisible(category.name());
    }

    @User(
            username = login,
            categories = @Category()
    )
    @Test
    void activeCategoryCanBeArchivedInProfile(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(login, password)
                .goToProfile()
                .archiveCategoryByName(category.name())
                .clickArchivedSwitcher()
                .checkThatCategoryArchived(category.name());
    }

    @User(
            username = login,
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryCanBeActivatedInProfile(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .successLogin(login, password)
                .goToProfile()
                .clickArchivedSwitcher()
                .unArchiveCategoryByName(category.name())
                .checkThatCategoryActive(category.name());
    }

}
