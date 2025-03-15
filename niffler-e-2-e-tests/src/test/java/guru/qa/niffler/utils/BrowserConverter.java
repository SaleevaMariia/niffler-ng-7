package guru.qa.niffler.utils;

import com.codeborne.selenide.SelenideDriver;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

public class BrowserConverter implements ArgumentConverter {
    @Override
    public Object convert(Object source, ParameterContext context) throws ArgumentConversionException {
        if (!(source instanceof Browser browser)) {
            throw new IllegalArgumentException(
                    "The argument should be a Browser: " + source);
        }
        SelenideDriver driver;
        switch (browser) {
            case CHROME -> driver = new SelenideDriver(SelenideUtils.chromeConfig);
            case FIREFOX -> driver = new SelenideDriver(SelenideUtils.firefoxConfig);
            default -> throw new IllegalArgumentException(
                    "Don't know this browser yet: " + source);
        }
        return driver;
    }
}
