package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserDataJson;
import guru.qa.niffler.service.SpendClient;
import org.apache.commons.lang.ArrayUtils;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);
  private final SpendClient spendDbClient = SpendClient.getInstance();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
            .ifPresent(userAnno -> {
              if (ArrayUtils.isNotEmpty(userAnno.spendings())) {
                UserDataJson user = context.getStore(UserExtension.NAMESPACE).get(
                        context.getUniqueId(),
                        UserDataJson.class
                );

                final String username = user != null
                        ? user.username()
                        : userAnno.username();

                final List<SpendJson> createdSpends = new ArrayList<>();

                for (Spending spendAnno : userAnno.spendings()) {
                  SpendJson spend = new SpendJson(
                          null,
                          new Date(),
                          new CategoryJson(
                                  null,
                                  spendAnno.category(),
                                  username,
                                  false
                          ),
                          spendAnno.currency(),
                          spendAnno.amount(),
                          spendAnno.description(),
                          username
                  );

                  createdSpends.add(
                          spendDbClient.createSpend(spend)
                  );
                }
                if (user != null) {
                  user.testData().spendings().addAll(
                          createdSpends
                  );
                } else {
                  context.getStore(NAMESPACE).put(
                          context.getUniqueId(),
                          createdSpends
                  );
                }
              }
            });
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(SpendJson[].class);
  }

  @Override
  public SpendJson[] resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return (SpendJson[]) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), List.class)
            .stream()
            .toArray(SpendJson[]::new);
  }
}
