package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.data.repository.SpendRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class SpendRepositoryJdbc implements SpendRepository {
    private static final Config CFG = Config.getInstance();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();

    @Override
    public SpendEntity create(SpendEntity spend) {
        UUID categoryId = spend.getCategory().getId();
        if (categoryId == null || categoryDao.findCategoryById(categoryId) == null) {
            spend.setCategory(categoryDao.create(spend.getCategory()));
        }
        return spendDao.create(spend);
    }

    @Override
    public SpendEntity update(SpendEntity spend) {
        categoryDao.update(spend.getCategory());
        return spendDao.update(spend);
    }

    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.create(category);
    }

    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findCategoryById(id);
    }

    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String name) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, name);
    }

    @Override
    public Optional<SpendEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "select * from spend s join category c on c.id = s.category_id where s.id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                SpendEntity spend = new SpendEntity();
                CategoryEntity category = new CategoryEntity();
                if (rs.next()) {
                    spend.setId(rs.getObject("s.id", UUID.class));
                    spend.setUsername(rs.getString("s.username"));
                    spend.setSpendDate(rs.getDate("spend_date"));
                    spend.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    spend.setAmount(rs.getDouble("amount"));
                    spend.setDescription(rs.getString("description"));
                    category.setId(rs.getObject("c.id", UUID.class));
                    category.setName(rs.getString("name"));
                    category.setUsername(rs.getString("c.username"));
                    category.setArchived(rs.getBoolean("archived"));
                }
                if (spend == null) {
                    return Optional.empty();
                } else {
                    spend.setCategory(category);
                    return Optional.of(spend);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "select * from spend s join category c on c.id = s.category_id " +
                        "where s.username = ? and s.description = ?"
        )) {
            ps.setString(1, username);
            ps.setString(2, description);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                SpendEntity spend = new SpendEntity();
                CategoryEntity category = new CategoryEntity();
                if (rs.next()) {
                    spend.setId(rs.getObject("s.id", UUID.class));
                    spend.setUsername(rs.getString("s.username"));
                    spend.setSpendDate(rs.getDate("spend_date"));
                    spend.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    spend.setAmount(rs.getDouble("amount"));
                    spend.setDescription(rs.getString("description"));
                    category.setId(rs.getObject("c.id", UUID.class));
                    category.setName(rs.getString("name"));
                    category.setUsername(rs.getString("c.username"));
                    category.setArchived(rs.getBoolean("archived"));
                }
                if (spend == null) {
                    return Optional.empty();
                } else {
                    spend.setCategory(category);
                    return Optional.of(spend);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(SpendEntity spend) {
        spendDao.deleteSpend(spend);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        categoryDao.deleteCategory(category);
    }
}
