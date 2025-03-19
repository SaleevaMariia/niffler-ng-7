package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.entity.user.CurrencyValues;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;
import java.util.UUID;

public record SpendJson(
        @JsonProperty("id")
        @Nullable
        UUID id,
        @JsonProperty("spendDate")
        Date spendDate,
        @JsonProperty("category")
        CategoryJson category,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("amount")
        Double amount,
        @JsonProperty("description")
        String description,
        @JsonProperty("username")
        String username) {
    public static @Nonnull SpendJson fromEntity(SpendEntity entity) {
        final CategoryEntity category = entity.getCategory();
        final String username = entity.getUsername();

        return new SpendJson(
                entity.getId(),
                entity.getSpendDate(),
                new CategoryJson(
                        category.getId(),
                        category.getName(),
                        username,
                        category.isArchived()
                ),
                entity.getCurrency(),
                entity.getAmount(),
                entity.getDescription(),
                username
        );
    }

    public static @Nonnull SpendJson toTestSpendings(String categoryName, double amount, CurrencyValues currency,
                                                     String description, Date spendDate) {

        return new SpendJson(
                null,
                spendDate,
                new CategoryJson(
                        null,
                        categoryName,
                        "test",
                        false
                ),
                currency,
                amount,
                description,
                "test"
        );
    }

}
