package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.CategoryDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoSpringJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static guru.qa.niffler.data.Databases.dataSource;
import static guru.qa.niffler.data.Databases.transaction;

public class SpendDbClient {
    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    Optional<CategoryEntity> categoryEntity = new CategoryDaoJdbc(connection).
                            findCategoryByUsernameAndCategoryName(spendEntity.getUsername(),
                                    spendEntity.getCategory().getName());
                    if (categoryEntity.isPresent()) {
                        spendEntity.setCategory(categoryEntity.get());
                    } else if (spendEntity.getCategory().getId() == null) {
                        spendEntity.setCategory(new CategoryDaoJdbc(connection)
                                .create(spendEntity.getCategory()));
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
                },
                CFG.spendJdbcUrl()
        );
    }

    public SpendJson createSpendBySpring(SpendJson spend) {
        SpendEntity spendEntity = SpendEntity.fromJson(spend);
        Optional<CategoryEntity> categoryEntity = new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).
                findCategoryByUsernameAndCategoryName(spendEntity.getUsername(),
                        spendEntity.getCategory().getName());
        if (categoryEntity.isPresent()) {
            spendEntity.setCategory(categoryEntity.get());
        } else if (spendEntity.getCategory().getId() == null) {
            spendEntity.setCategory(new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl()))
                    .create(spendEntity.getCategory()));
        }
        return SpendJson.fromEntity(
                new SpendDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).create(spendEntity)
        );
    }

    public CategoryJson createCategory(CategoryJson category) {
        return transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
                    return CategoryJson.fromEntity(new CategoryDaoJdbc(connection).create(categoryEntity));
                },
                CFG.spendJdbcUrl());
    }

    public List<CategoryJson> getAllCategoryBySpring() {
        List<CategoryEntity> categoryEntities = new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).findAll();
        return categoryEntities.stream()
                .map(x -> CategoryJson.fromEntity(x))
                .collect(Collectors.toList());
    }

    public void deleteSpend(SpendJson spend) {
        transaction(connection -> {
            SpendEntity spendEntity = SpendEntity.fromJson(spend);
            new SpendDaoJdbc(connection).deleteSpend(spendEntity);
        }, CFG.spendJdbcUrl());
    }

    public void deleteCategory(CategoryJson category) {
        transaction(connection -> {
            CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
            new CategoryDaoJdbc(connection).deleteCategory(categoryEntity);
        }, CFG.spendJdbcUrl());
    }

    public void deleteCategoryBySpring(CategoryJson category) {
        CategoryEntity categoryEntity = CategoryEntity.fromJson(category);
        new CategoryDaoSpringJdbc(dataSource(CFG.spendJdbcUrl())).deleteCategory(categoryEntity);
    }
}


