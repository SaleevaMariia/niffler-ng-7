package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class SpendConditions {
    @Nonnull
    public static WebElementsCondition spends(@Nonnull SpendJson... expectedSpends) {
        return new WebElementsCondition() {

            private String expectedSpend = "";

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedSpends)) {
                    throw new IllegalArgumentException("No expected spends given");
                }
                if (expectedSpends.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedSpends.length, elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;
                String message = "";
                final List<String> actualSpendsList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final List<WebElement> cells = elements.get(i).findElements(By.cssSelector("td"));
                    final String categoryToCheck = expectedSpends[i].category().name();
                    final String amountToCheck = new DecimalFormat("0.#").format(expectedSpends[i].amount());
                    final CurrencyValues currencyToCheck = expectedSpends[i].currency();
                    final String descriptionToCheck = expectedSpends[i].description();
                    final String dateToCheck = new SimpleDateFormat("MMM dd, yyyy", Locale.US)
                            .format(expectedSpends[i].spendDate());
                    actualSpendsList.add(
                            cells.get(1).getText() + " " +
                                    cells.get(2).getText() + " " +
                                    cells.get(3).getText() + " " +
                                    cells.get(4).getText()
                    );
                    expectedSpend = categoryToCheck + " " + amountToCheck + " " + currencyToCheck.sign + " " +
                            descriptionToCheck + " " + dateToCheck;

                    if (!cells.get(1).getText().equals(categoryToCheck)) {
                        passed = false;
                        message = message + String.format(
                                "Spend category mismatch (expected: %s, actual: %s)",
                                categoryToCheck, cells.get(1).getText());
                    }
                    if (!cells.get(2).getText().equals(amountToCheck + " " + currencyToCheck.sign)) {
                        passed = false;
                        message = message + String.format(
                                "Spend amount + currency mismatch (expected: %s, actual: %s)",
                                amountToCheck + " " + currencyToCheck.sign,
                                cells.get(2).getText());
                    }
                    if (!cells.get(3).getText().equals(descriptionToCheck)) {
                        passed = false;
                        message = message + String.format(
                                "Spend description mismatch (expected: %s, actual: %s)",
                                descriptionToCheck, cells.get(3).getText());
                    }
                    if (!cells.get(4).getText().equals(dateToCheck)) {
                        passed = false;
                        message = message + String.format(
                                "Spend date mismatch (expected: %s, actual: %s)",
                                dateToCheck, cells.get(4).getText());
                    }
                }

                if (!passed) {
                    return rejected(message, actualSpendsList.stream().toList());
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedSpend;
            }
        };
    }
}
