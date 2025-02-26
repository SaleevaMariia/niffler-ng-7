package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class PeoplePage {
    private final SelenideElement peopleTable = $("#all");

    @Step("Проверяем, что пользователю {username} было отправлено приглашение в друзья")
    @Nonnull
    public PeoplePage checkInvitationSentToUser(String username) {
        SelenideElement friendRow = peopleTable.$$("tr").find(text(username));
        friendRow.shouldHave(text("Waiting..."));
        return this;
    }
}
