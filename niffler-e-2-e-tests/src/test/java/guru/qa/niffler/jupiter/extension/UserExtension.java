package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.UserApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.service.UsersClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;

import static guru.qa.niffler.utils.RandomDataUtils.defaultPassword;
import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;

public class UserExtension implements BeforeEachCallback, ParameterResolver {
    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);
    private final UsersClient usersClient = new UserApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if ("".equals(userAnno.username())) {
                        final String username = randomUserName();
                        UserDataJson user = usersClient.createUser(username, defaultPassword);
                        usersClient.createIncomeInvitation(user, userAnno.incomeInvitations());
                        usersClient.createOutcomeInvitation(user, userAnno.outcomeInvitations());
                        usersClient.createFriends(user, userAnno.friends());
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                user.addTestData(
                                        new TestData(
                                                defaultPassword,
                                                new ArrayList<>(),
                                                new ArrayList<>(),
                                                new ArrayList<>(),
                                                new ArrayList<>(),
                                                new ArrayList<>()
                                        )
                                )
                        );
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserDataJson.class);
    }

    @Override
    public UserDataJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(
                extensionContext.getUniqueId(),
                UserDataJson.class
        );
    }
}
