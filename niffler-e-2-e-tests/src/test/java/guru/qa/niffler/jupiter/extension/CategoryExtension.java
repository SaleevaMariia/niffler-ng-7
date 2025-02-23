package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.service.SpendClient;
import org.apache.commons.lang.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.List;

import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;

public class CategoryExtension implements BeforeEachCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);
    private final SpendClient spendDbClient = SpendClient.getInstance();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if (ArrayUtils.isNotEmpty(userAnno.categories())) {
                        UserDataJson user = context.getStore(UserExtension.NAMESPACE).get(
                                context.getUniqueId(),
                                UserDataJson.class
                        );

                        final String username = user != null
                                ? user.username()
                                : userAnno.username();

                        final List<CategoryJson> createdCategories = new ArrayList<>();

                        for (Category categoryAnno : userAnno.categories()) {
                            CategoryJson category = new CategoryJson(
                                    null,
                                    "".equals(categoryAnno.name()) ? randomUserName() : categoryAnno.name(),
                                    username,
                                    categoryAnno.archived()
                            );
                            createdCategories.add(
                                    spendDbClient.createCategory(category)
                            );
                        }
                        if (user != null) {
                            user.testData().categories().addAll(
                                    createdCategories
                            );
                        } else {
                            context.getStore(NAMESPACE).put(
                                    context.getUniqueId(),
                                    createdCategories
                            );
                        }
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson[].class);
    }

    @Override
    public CategoryJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (CategoryJson[]) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class)
                .stream()
                .toArray(CategoryJson[]::new);
    }
}

