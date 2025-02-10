package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient {

  private final Retrofit retrofit = new Retrofit.Builder()
          .baseUrl(Config.getInstance().spendUrl())
          .addConverterFactory(JacksonConverterFactory.create())
          .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  public SpendJson createSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.addSpend(spend).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(201, response.code());
    return response.body();
  }

  public SpendJson editSpend(SpendJson spend) {
    final Response<SpendJson> response;
    try {
      response = spendApi.editSpend(spend).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public SpendJson getSpend(String id, String username) {
    final Response<SpendJson> response;
    try {
      response = spendApi.getSpend(id, username).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public List<SpendJson> getSpends(String username, DataFilterValues data, CurrencyValues currency) {
    final Response<List<SpendJson>> response;
    try {
      response = spendApi.getSpends(username, data, currency).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public void deleteSpends(String username, List<String> ids) {
    final Response<Void> response;
    try {
      response = spendApi.deleteSpends(username, ids).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(202, response.code());
  }

  public CategoryJson addCategory(CategoryJson categoryJson) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.addCategory(categoryJson).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public CategoryJson updateCategory(CategoryJson categoryJson) {
    final Response<CategoryJson> response;
    try {
      response = spendApi.updateCategory(categoryJson).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }

  public List<CategoryJson> getCategories(String username, boolean excludeArch) {
    final Response<List<CategoryJson>> response;
    try {
      response = spendApi.getCategories(username, excludeArch).execute();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    assertEquals(200, response.code());
    return response.body();
  }
}
