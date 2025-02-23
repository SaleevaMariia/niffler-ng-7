package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.entity.user.CurrencyValues;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class SpendCategoryEntityRowMapper implements RowMapper<SpendEntity> {
    public static final SpendCategoryEntityRowMapper instance = new SpendCategoryEntityRowMapper();

    private SpendCategoryEntityRowMapper() {
    }

    @Override
    @Nonnull
    public SpendEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        SpendEntity result = new SpendEntity();
        CategoryEntity categoryEntity = new CategoryEntity();
        result.setId(rs.getObject("s.id", UUID.class));
        result.setUsername(rs.getString("s.username"));
        result.setSpendDate(rs.getDate("spend_date"));
        result.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        result.setAmount(rs.getDouble("amount"));
        result.setDescription(rs.getString("description"));
        categoryEntity.setId(rs.getObject("c.id", UUID.class));
        categoryEntity.setUsername(rs.getString("c.username"));
        categoryEntity.setName(rs.getString("c.name"));
        categoryEntity.setArchived(rs.getBoolean("c.archived"));
        result.setCategory(categoryEntity);
        return result;
    }
}
