package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage> {
    private final SelenideElement showArchived = $("span.MuiSwitch-switchBase");
    private final ElementsCollection tableRows = $("div.MuiGrid-container", 1)
            .findAll("div.MuiGrid-item")
            .excludeWith(text("Categories"));
    private final SelenideElement archiveCategoryBtn = $(By.xpath("//button[text()='Archive']"));
    private final SelenideElement unArchiveCategoryBtn = $(By.xpath("//button[text()='Unarchive']"));

    private final ElementsCollection archivedCategories = $$(".MuiChip-filled.MuiChip-colorDefault");
    private final ElementsCollection activeCategories = $$(".MuiChip-filled.MuiChip-colorPrimary");

    private final SelenideElement nameInput = $("#name");
    private final SelenideElement saveBtn = $$("button").find(text("Save changes"));
    private final SelenideElement nameField = $("#username");

    @Step("Нажимаем кнопку Показать/скрыть архивные категории")
    @Nonnull
    public ProfilePage clickArchivedSwitcher() {
        showArchived.click();
        return this;
    }

    @Step("Архивируем категорию с именем {name}")
    @Nonnull
    public ProfilePage archiveCategoryByName(String name) {
        tableRows.find(text(name))
                .$("button[aria-label='Archive category']")
                .click();
        archiveCategoryBtn.click();
        return this;
    }

    @Step("Разархивируем категорию с именем {name}")
    @Nonnull
    public ProfilePage unArchiveCategoryByName(String name) {
        tableRows.find(text(name))
                .$("button[aria-label='Unarchive category']")
                .click();
        unArchiveCategoryBtn.click();
        return this;
    }

    @Step("Меняем имя пользователя на {newName}")
    @Nonnull
    public ProfilePage changeName(String newName) {
        nameInput.clear();
        nameInput.setValue(newName);
        saveBtn.click();
        return this;
    }

    @Step("Проверяем что username равен {username}")
    @Nonnull
    public ProfilePage checkUsername(String username) {
        username.equals(nameField.getValue());
        return this;
    }

    @Step("Проверяем что имя пользователя равно {name}")
    @Nonnull
    public ProfilePage checkName(String name) {
        name.equals(nameInput.getValue());
        return this;
    }

    @Step("Проверяем что категория с именем {name} видна")
    public void checkThatCategoryVisible(String name) {
        tableRows.find(text(name)).should(visible);
    }

    @Step("Проверяем что категория с именем {name} не видна")
    public void checkThatCategoryIsNotVisible(String name) {
        tableRows.find(text(name)).shouldNotBe(visible);
    }

    @Step("Проверяем что категория с именем {name} не активна")
    public void checkThatCategoryArchived(String name) {
        archivedCategories.find(text(name)).shouldBe(visible);
    }

    @Step("Проверяем что категория с именем {name} активна")
    public void checkThatCategoryActive(String name) {
        activeCategories.find(text(name)).shouldBe(visible);
    }

    @Override
    @Nonnull
    @Step("Проверяем, что страница профайла пользователя успешно отобразилась")
    public ProfilePage checkThatPageLoaded() {
        nameInput.shouldBe(visible);
        return this;
    }
}
