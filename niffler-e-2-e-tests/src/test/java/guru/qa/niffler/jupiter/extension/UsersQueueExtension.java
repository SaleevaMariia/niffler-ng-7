package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.model.StaticUser;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("test1", "123456", null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("maria", "123456", "maria2", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("maria2", "12345", null, "test", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("test123", "12345", null, null, "maria"));
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .map(p -> p.getAnnotation(UserType.class))
                .forEach(ut -> {
                    Optional<StaticUser> user = Optional.empty();
                    StopWatch sw = StopWatch.createStarted();
                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        switch (ut.value()) {
                            case EMPTY -> user = Optional.ofNullable(EMPTY_USERS.poll());
                            case WITH_FRIENDS -> user = Optional.ofNullable(WITH_FRIEND_USERS.poll());
                            case WITH_INCOME_REQUEST -> user = Optional.ofNullable(WITH_INCOME_REQUEST_USERS.poll());
                            case WITH_OUTCOME_REQUEST -> user = Optional.ofNullable(WITH_OUTCOME_REQUEST_USERS.poll());
                        }
                    }
                    Allure.getLifecycle().updateTestCase(testCase ->
                            testCase.setStart(new Date().getTime())
                    );
                    user.ifPresentOrElse(
                            u -> ((Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                                    .getOrComputeIfAbsent(
                                            context.getUniqueId(), key -> new HashMap<>()
                                    )).put(ut, u),
                            () -> {
                                throw new IllegalStateException("Can`t obtain user after 30s.");
                            }
                    );
                });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(
                context.getUniqueId(),
                Map.class
        );
        for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
            switch (e.getKey().value()) {
                case EMPTY:
                    EMPTY_USERS.add(e.getValue());
                case WITH_FRIENDS:
                    WITH_FRIEND_USERS.add(e.getValue());
                case WITH_INCOME_REQUEST:
                    WITH_INCOME_REQUEST_USERS.add(e.getValue());
                case WITH_OUTCOME_REQUEST:
                    WITH_OUTCOME_REQUEST_USERS.add(e.getValue());
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return (StaticUser) extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), Map.class)
                .get(AnnotationSupport.findAnnotation(parameterContext.getParameter(), UserType.class).get());
    }
}
