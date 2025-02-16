package guru.qa.niffler.api;

import guru.qa.niffler.model.UserDataJson;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface UserApi {
    @FormUrlEncoded
    @POST(value = "register")
    Call<Void> registerUser(@Field("username") String user,
                            @Field("password") String password,
                            @Field("passwordSubmit") String passwordSubmit,
                            @Field("_csrf") String csrf);

    @GET("register")
    Call<ResponseBody> getRegisterPage();

    @GET("internal/users/current")
    Call<UserDataJson> currentUser(@Query("username") String username);

    @POST("internal/invitations/send")
    Call<UserDataJson> sendInvitation(@Query("username") String username,
                                      @Query("targetUsername") String targetUsername);

    @POST("internal/invitations/accept")
    Call<UserDataJson> acceptInvitation(@Query("username") String username,
                                        @Query("targetUsername") String targetUsername);


}
