package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.mapper.SpendCategoryEntityRowMapper;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class SpendRepositorySpringJdbc implements SpendRepository {
    private static final Config CFG = Config.getInstance();
    private final CategoryDao categoryDao = new CategoryDaoSpringJdbc();
    private final SpendDao spendDao = new SpendDaoSpringJdbc();

    @Override
    @Nonnull
    public SpendEntity create(SpendEntity spend) {
        UUID categoryId = spend.getCategory().getId();
        if (categoryId == null || categoryDao.findCategoryById(categoryId) == null) {
            spend.setCategory(categoryDao.create(spend.getCategory()));
        }
        return spendDao.create(spend);
    }

    @Override
    @Nonnull
    public SpendEntity update(SpendEntity spend) {
        categoryDao.update(spend.getCategory());
        return spendDao.update(spend);
    }

    @Override
    @Nonnull
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
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.query(
                        """
                                select
                                    s.id, s.username, spend_date,
                                    currency, amount, description,
                                    c.id, c.name,
                                    c.username, c.archived
                                from spend s
                                join category c on s.category_id=c.id WHERE s.id = ? """,
                        SpendCategoryEntityRowMapper.instance,
                        id
                ).getFirst()
        );
    }

    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.spendJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.query(
                        """
                                select
                                    s.id, s.username, spend_date,
                                    currency, amount, description,
                                    c.id, c.name,
                                    c.username, c.archived
                                from spend s
                                join category c on s.category_id=c.id
                                WHERE s.username = ? and s.description = ? """,
                        SpendCategoryEntityRowMapper.instance,
                        username, description
                ).getFirst()
        );
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
