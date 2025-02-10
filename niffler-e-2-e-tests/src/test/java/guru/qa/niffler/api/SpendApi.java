package guru.qa.niffler.api;

import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.DataFilterValues;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SpendApi {

  @POST("internal/spends/add")
  Call<SpendJson> addSpend(@Body SpendJson spend);

  @PATCH("internal/spends/edit")
  Call<SpendJson> editSpend(@Body SpendJson spend);

  @GET("internal/spends/{id}")
  Call<SpendJson> getSpend(@Path("id") String id, @Query("username") String user);

  @GET("internal/spends/all")
  Call<List<SpendJson>> getSpends(@Query("username") String user,
                                  @Query("filterPeriod") DataFilterValues dat,
                                  @Query("filterCurrency") CurrencyValues cur);

  @DELETE("internal/spends/remove")
  Call<Void> deleteSpends(@Query("username") String user, @Query("ids") List<String> ids);

  @POST("internal/categories/add")
  Call<CategoryJson> addCategory(@Body CategoryJson category);

  @PATCH("internal/categories/update")
  Call<CategoryJson> updateCategory(@Body CategoryJson category);

  @GET("internal/categories/all")
  Call<List<CategoryJson>> getCategories(@Query("username") String user,
                                         @Query("excludeArchived") boolean excludeArch);
}
