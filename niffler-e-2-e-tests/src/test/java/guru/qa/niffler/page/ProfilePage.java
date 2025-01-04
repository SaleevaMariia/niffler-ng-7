package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.*;

public class ProfilePage {
    private final SelenideElement showArchived = $("span.MuiSwitch-switchBase");
    private final ElementsCollection tableRows = $$(By.xpath("//input[@id='category']/parent::*/following-sibling::div"));

    private final SelenideElement archiveCategoryBtn = $(By.xpath("//button[text()='Archive']"));
    private final SelenideElement unArchiveCategoryBtn = $(By.xpath("//button[text()='Unarchive']"));

    public ProfilePage showArchivedCategory() {
        final String classes = showArchived.getAttribute("class");
        if (!classes.contains("Mui-checked")) {
            showArchived.click();
        }
        return new ProfilePage();
    }

    public ProfilePage hideArchivedCategory() {
        final String classes = showArchived.getAttribute("class");
        if (classes.contains("Mui-checked")) {
            showArchived.click();
        }
        return new ProfilePage();
    }

    public ProfilePage archiveCategoryByName(String name) {
        tableRows.find(text(name))
                .$("button[aria-label='Archive category']").click();
        archiveCategoryBtn.click();
        return new ProfilePage();
    }

    public ProfilePage unArchiveCategoryByName(String name) {
        tableRows.find(text(name))
                .$("button[aria-label='Unarchive category']").click();
        unArchiveCategoryBtn.click();
        return new ProfilePage();
    }

    public void checkThatCategoryVisible(String name) {
        tableRows.find(text(name)).should(visible);
    }

    public void checkThatCategoryIsNotVisible(String name) {
        tableRows.find(text(name)).shouldNotBe(visible);
    }

    public boolean isCategoryArchived(String name) {
        return !tableRows.find(text(name))
                .$("div.MuiButtonBase-root")
                .getAttribute("class")
                .contains("MuiChip-clickableColorPrimary");
    }

    public void checkThatCategoryArchived(String name) {
        assertTrue(isCategoryArchived(name));
    }

    public void checkThatCategoryActive(String name) {
        assertFalse(isCategoryArchived(name));
    }

}
