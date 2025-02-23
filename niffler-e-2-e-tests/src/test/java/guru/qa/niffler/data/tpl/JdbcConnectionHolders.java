package guru.qa.niffler.data.tpl;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class JdbcConnectionHolders implements AutoCloseable {
    @Nonnull
    private final List<JdbcConnectionHolder> holders;

    @Nonnull
    public JdbcConnectionHolders(List<JdbcConnectionHolder> holders) {
        this.holders = holders;
    }

    @Override
    public void close() {
        holders.forEach(JdbcConnectionHolder::close);
    }
}
