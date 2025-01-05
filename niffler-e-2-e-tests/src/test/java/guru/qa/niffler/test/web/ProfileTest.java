package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileTest {
    private static final Config CFG = Config.getInstance();
    private static final String login = "maria";
    private static final String password = "123456";

    @Category(
            username = login,
            archived = true
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesListWhenShowArchivedOn(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(login, password).goToProfile()
                .clickArchivedSwitcher()
                .checkThatCategoryVisible(category.name());
    }

    @Category(
            username = login,
            archived = true
    )
    @Test
    void archivedCategoryShouldNotPresentInCategoriesListWhenShowArchivedOff(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(login, password).goToProfile()
                .checkThatCategoryIsNotVisible(category.name());
    }

    @Category(
            username = login,
            archived = false
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(login, password)
                .goToProfile()
                .checkThatCategoryVisible(category.name());
    }

    @Category(
            username = login,
            archived = false
    )
    @Test
    void activeCategoryCanBeArchivedInProfile(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(login, password)
                .goToProfile()
                .archiveCategoryByName(category.name())
                .clickArchivedSwitcher()
                .checkThatCategoryArchived(category.name());
    }

    @Category(
            username = login,
            archived = true
    )
    @Test
    void archivedCategoryCanBeActivatedInProfile(CategoryJson category) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(login, password)
                .goToProfile()
                .clickArchivedSwitcher()
                .unArchiveCategoryByName(category.name())
                .checkThatCategoryActive(category.name());
    }
}
