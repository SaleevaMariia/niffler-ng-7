package guru.qa.niffler.model;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
public record TestData(String password,
                       List<CategoryJson> categories,
                       List<SpendJson> spendings,
                       List<UserDataJson> friends,
                       List<UserDataJson> outcomeInvitations,
                       List<UserDataJson> incomeInvitations) {

    public TestData(String password) {
        this(password, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public @Nonnull String[] friendsUsernames() {
        return extractUsernames(friends);
    }

    public @Nonnull String[] incomeInvitationsUsernames() {
        return extractUsernames(incomeInvitations);
    }

    public @Nonnull String[] outcomeInvitationsUsernames() {
        return extractUsernames(outcomeInvitations);
    }

    private @Nonnull String[] extractUsernames(List<UserDataJson> users) {
        return users.stream().map(UserDataJson::username).toArray(String[]::new);
    }
}


