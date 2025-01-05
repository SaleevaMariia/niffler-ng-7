package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ProfilePage {
    private final SelenideElement showArchived = $("span.MuiSwitch-switchBase");
    private final ElementsCollection tableRows = $("div.MuiGrid-container", 1)
            .findAll("div.MuiGrid-item")
            .excludeWith(text("Categories"));
    private final SelenideElement archiveCategoryBtn = $(By.xpath("//button[text()='Archive']"));
    private final SelenideElement unArchiveCategoryBtn = $(By.xpath("//button[text()='Unarchive']"));

    private final ElementsCollection archivedCategories = $$(".MuiChip-filled.MuiChip-colorDefault");
    private final ElementsCollection activeCategories = $$(".MuiChip-filled.MuiChip-colorPrimary");

    public ProfilePage clickArchivedSwitcher() {
        showArchived.click();
        return this;
    }

    public ProfilePage archiveCategoryByName(String name) {
        tableRows.find(text(name))
                .$("button[aria-label='Archive category']")
                .click();
        archiveCategoryBtn.click();
        return this;
    }

    public ProfilePage unArchiveCategoryByName(String name) {
        tableRows.find(text(name))
                .$("button[aria-label='Unarchive category']")
                .click();
        unArchiveCategoryBtn.click();
        return this;
    }

    public void checkThatCategoryVisible(String name) {
        tableRows.find(text(name)).should(visible);
    }

    public void checkThatCategoryIsNotVisible(String name) {
        tableRows.find(text(name)).shouldNotBe(visible);
    }

    public void checkThatCategoryArchived(String name) {
        archivedCategories.find(text(name)).shouldBe(visible);
    }

    public void checkThatCategoryActive(String name) {
        activeCategories.find(text(name)).shouldBe(visible);
    }
}
