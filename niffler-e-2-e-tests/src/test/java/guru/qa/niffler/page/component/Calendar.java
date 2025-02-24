package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class Calendar extends BaseComponent<Calendar> {
    private final SelenideElement input = $("input[name='date']");

    public Calendar(SelenideElement self) {
        super(self);
    }

    public Calendar() {
        super($(".MuiPickersLayout-root"));
    }

    @Step("Выбираем дату в календаре: {date}")
    @Nonnull
    public Calendar selectDateInCalendar(Date date) {
        DateFormat df = new SimpleDateFormat("MM/DD/YYYY");
        input.setValue(df.format(date));
        return this;
    }
}
