package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class StatConditions {

    @Nonnull
    public static WebElementCondition color(Color expectedColor) {
        return new WebElementCondition("color " + expectedColor.rgb) {
            @NotNull
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                final String rgba = element.getCssValue("background-color");
                return new CheckResult(
                        expectedColor.rgb.equals(rgba),
                        rgba
                );
            }
        };
    }

    @Nonnull
    public static WebElementsCondition color(@Nonnull Color... expectedColors) {
        return new WebElementsCondition() {

            private final String expectedRgba = Arrays.stream(expectedColors).map(c -> c.rgb).toList().toString();
            private String errorMessage = "Collection check failed";

            @Override
            public String errorMessage() {
                return errorMessage;
            }

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedColors)) {
                    throw new IllegalArgumentException("No expected colors given");
                }
                if (expectedColors.length != elements.size()) {
                    errorMessage = String.format("List size mismatch (expected: %s, actual: %s)", expectedColors.length, elements.size());
                    return rejected(errorMessage, elements);
                }

                boolean passed = true;
                final List<String> actualRgbaList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final Color colorToCheck = expectedColors[i];
                    final String rgba = elementToCheck.getCssValue("background-color");
                    actualRgbaList.add(rgba);
                    if (passed) {
                        passed = colorToCheck.rgb.equals(rgba);
                    }
                }

                if (!passed) {
                    final String actualRgba = actualRgbaList.toString();
                    errorMessage = String.format(
                            "List colors mismatch (expected: %s, actual: %s)", expectedRgba, actualRgba
                    );
                    return rejected(errorMessage, actualRgba);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedRgba;
            }
        };
    }

    @Nonnull
    public static WebElementCondition statBubbles(Bubble expectedBubble) {
        return new WebElementCondition("bubble (color " + expectedBubble.color().rgb
                + " text " + expectedBubble.text()) {
            @NotNull
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                final String rgba = element.getCssValue("background-color");
                final String text = element.getText();
                return new CheckResult(
                        expectedBubble.color().rgb.equals(rgba) && expectedBubble.text().equals(text),
                        "bubble (color " + rgba + " text " + text
                );
            }
        };
    }

    @Nonnull
    public static WebElementsCondition statBubbles(@Nonnull Bubble... expectedBubbles) {
        return new WebElementsCondition() {
            private String errorMessage = "Collection check failed";

            @Override
            public String errorMessage() {
                return errorMessage;
            }

            private final String expectedBubblesStr =
                    Arrays.stream(expectedBubbles)
                            .map(c -> "Color: " + c.color().rgb + "Text: " + c.text())
                            .toList()
                            .toString();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected bubbles given");
                }
                if (expectedBubbles.length != elements.size()) {
                    errorMessage = String.format("List size mismatch (expected: %s, actual: %s)", expectedBubbles.length, elements.size());
                    return rejected(errorMessage, elements);
                }

                boolean passed = true;
                final List<String> actualBubble = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final Color colorToCheck = expectedBubbles[i].color();
                    final String text = expectedBubbles[i].text();
                    final String rgba = elementToCheck.getCssValue("background-color");
                    final String actualText = elementToCheck.getText();
                    actualBubble.add("Color: " + rgba + "Text: " + actualText);
                    if (passed) {
                        passed = colorToCheck.rgb.equals(rgba) && text.equals(actualText);
                    }
                }

                if (!passed) {
                    errorMessage = String.format(
                            "List bubbles mismatch (expected: %s, actual: %s)", expectedBubblesStr, actualBubble
                    );
                    return rejected(errorMessage, actualBubble);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedBubblesStr;
            }
        };
    }

    @Nonnull
    public static WebElementsCondition statBubblesInAnyOrder(@Nonnull Bubble... expectedBubbles) {
        return new WebElementsCondition() {
            private String errorMessage = "Collection check failed";

            @Override
            public String errorMessage() {
                return errorMessage;
            }

            private final String expectedBubblesStr =
                    Arrays.stream(expectedBubbles)
                            .map(c -> "Color: " + c.color().rgb + "Text: " + c.text())
                            .toList()
                            .toString();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected bubbles given");
                }
                if (expectedBubbles.length != elements.size()) {
                    errorMessage = String.format("List size mismatch (expected: %s, actual: %s)", expectedBubbles.length, elements.size());
                    return rejected(errorMessage, elements);
                }

                boolean passed = true;
                boolean mainFlag = true;
                final List<String> actualBubble = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    for (int j = 0; j < expectedBubbles.length; j++) {
                        final Color colorToCheck = expectedBubbles[j].color();
                        final String text = expectedBubbles[j].text();
                        final String rgba = elementToCheck.getCssValue("background-color");
                        final String actualText = elementToCheck.getText();
                        passed = colorToCheck.rgb.equals(rgba) && text.equals(actualText);
                        if (passed) break;
                    }
                    actualBubble.add("Color: " + elementToCheck.getCssValue("background-color")
                            + "Text: " + elementToCheck.getText());
                    if (!passed) mainFlag = false;
                }

                if (!mainFlag) {
                    errorMessage = String.format(
                            "List bubbles mismatch (expected: %s, actual: %s)", expectedBubblesStr, actualBubble
                    );
                    return rejected(errorMessage, actualBubble);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedBubblesStr;
            }
        };
    }

    @Nonnull
    public static WebElementsCondition statBubblesContains(@Nonnull Bubble... expectedBubbles) {
        return new WebElementsCondition() {
            private String errorMessage = "Collection check failed";

            @Override
            public String errorMessage() {
                return errorMessage;
            }

            private final String expectedBubblesStr =
                    Arrays.stream(expectedBubbles)
                            .map(c -> "Color: " + c.color().rgb + "Text: " + c.text())
                            .toList()
                            .toString();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected bubbles given");
                }
                if (expectedBubbles.length > elements.size()) {
                    errorMessage = String.format("List size too big (expected: %s, actual: %s)", expectedBubbles.length, elements.size());
                    return rejected(errorMessage, elements);
                }

                boolean passed = true;
                boolean mainFlag = true;
                final List<String> actualBubble = new ArrayList<>();
                for (int i = 0; i < expectedBubbles.length; i++) {
                    for (int j = 0; j < elements.size(); j++) {
                        final WebElement elementToCheck = elements.get(j);
                        final Color colorToCheck = expectedBubbles[i].color();
                        final String text = expectedBubbles[i].text();
                        final String rgba = elementToCheck.getCssValue("background-color");
                        final String actualText = elementToCheck.getText();
                        passed = colorToCheck.rgb.equals(rgba) && text.equals(actualText);

                        if (actualBubble.size() < elements.size()) {
                            actualBubble.add("Color: " + elementToCheck.getCssValue("background-color")
                                    + "Text: " + elementToCheck.getText());
                        }
                        if (passed) break;
                    }
                    if (!passed) mainFlag = false;
                }

                if (!mainFlag) {
                    errorMessage = String.format(
                            "List bubbles doesn't contain some bubbles (expected: %s, actual: %s)", expectedBubblesStr, actualBubble
                    );
                    return rejected(errorMessage, actualBubble);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedBubblesStr;
            }
        };
    }
}
