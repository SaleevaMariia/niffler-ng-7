package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class SpendDbClient {
    private static final Config CFG = Config.getInstance();
    private final CategoryDao categoryDao = new CategoryDaoJdbc();
    private final SpendDao spendDao = new SpendDaoJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    Optional<CategoryEntity> categoryEntity = categoryDao.
                            findCategoryByUsernameAndCategoryName(spendEntity.getUsername(),
                                    spendEntity.getCategory().getName());
                    if (categoryEntity.isPresent()) {
                        spendEntity.setCategory(categoryEntity.get());
                    } else if (spendEntity.getCategory().getId() == null) {
                        spendEntity.setCategory(categoryDao
                                .create(spendEntity.getCategory()));
                    }
                    return SpendJson.fromEntity(
                            spendDao.create(spendEntity)
                    );
                }
        );
    }

    public SpendJson createSpendBySpring(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        Optional<CategoryEntity> categoryEntity = categoryDao.
                findCategoryByUsernameAndCategoryName(spendEntity.getUsername(),
                        spendEntity.getCategory().getName());
        if (categoryEntity.isPresent()) {
            spendEntity.setCategory(categoryEntity.get());
        } else if (spendEntity.getCategory().getId() == null) {
            spendEntity.setCategory(categoryDao
                    .create(spendEntity.getCategory()));
        }
        return SpendJson.fromEntity(
                spendDao.create(spendEntity)
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return jdbcTxTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            return CategoryJson.fromEntity(categoryDao.create(categoryEntity));
        });
    }

    public List<CategoryJson> getAllCategoryBySpring() {
        List<CategoryEntity> categoryEntities = categoryDao.findAll();
        return categoryEntities.stream()
                .map(x -> CategoryJson.fromEntity(x))
                .collect(Collectors.toList());
    }

    public void deleteSpend(SpendJson spend) {
        jdbcTxTemplate.execute(() -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            spendDao.deleteSpend(spendEntity);
            return null;
        });
    }

    public void deleteCategory(CategoryJson category) {
        jdbcTxTemplate.execute(() -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            categoryDao.deleteCategory(categoryEntity);
            return null;
        });
    }

    public void deleteCategoryBySpring(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        categoryDao.deleteCategory(categoryEntity);
    }
}


