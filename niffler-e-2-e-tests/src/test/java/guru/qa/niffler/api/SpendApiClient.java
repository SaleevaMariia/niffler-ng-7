package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import io.qameta.allure.Step;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class SpendApiClient implements SpendClient {
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @Step("Создаем трату используя REST API")
    public @Nullable SpendJson createSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
        return response.body();
    }

    @Step("Редактируем трату используя REST API")
    public @Nullable SpendJson editSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.editSpend(spend).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Получаем трату используя REST API")
    public @Nullable SpendJson getSpend(String id, String username) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpend(id, username).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Получаем траты используя REST API")
    public @Nonnull List<SpendJson> getSpends(String username, @Nullable CurrencyValues currency,
                                              @Nullable Date from, @Nullable Date to) {
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getSpends(username, currency, from, to).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() != null ? response.body() : Collections.emptyList();
    }

    @Step("Удаляем трату используя REST API")
    public void removeSpend(SpendJson spend) {
        final Response<Void> response;
        List<String> ids = new ArrayList<>();
        ids.add(spend.id().toString());
        try {
            response = spendApi.deleteSpends(spend.username(), ids).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(202, response.code());
    }

    @Step("Создаем категорию используя REST API")
    public @Nullable CategoryJson createCategory(CategoryJson categoryJson) {
        Response<CategoryJson> response;
        try {
            response = spendApi.addCategory(categoryJson).execute();
            if (categoryJson.archived()) {
                spendApi.updateCategory(new CategoryJson(
                        response.body().id(),
                        response.body().name(),
                        response.body().username(),
                        true
                )).execute();
            }
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Удаляем категорию используя REST API")
    @Override
    public void removeCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Операция removeCategory не поддерживается");
    }

    @Step("Изменяем категорию используя REST API")
    public @Nullable CategoryJson updateCategory(CategoryJson categoryJson) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(categoryJson).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body();
    }

    @Step("Получаем категории используя REST API")
    public @Nonnull List<CategoryJson> getCategories(String username, boolean excludeArch) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.getCategories(username, excludeArch).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
        return response.body() != null ? response.body() : Collections.emptyList();
    }
}
