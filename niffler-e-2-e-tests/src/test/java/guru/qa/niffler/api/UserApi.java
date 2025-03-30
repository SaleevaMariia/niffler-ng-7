package guru.qa.niffler.api;

import guru.qa.niffler.model.rest.UserDataJson;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

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

    @GET("/internal/users/all")
    Call<List<UserDataJson>> allUsers(@Query("username") String username, @Query("searchQuery") String searchQuery);

    @GET("/internal/friends/all")
    Call<List<UserDataJson>> friends(@Query("username") String username, @Query("searchQuery") String searchQuery);


    @POST("internal/invitations/send")
    Call<UserDataJson> sendInvitation(@Query("username") String username,
                                      @Query("targetUsername") String targetUsername);

    @POST("internal/invitations/accept")
    Call<UserDataJson> acceptInvitation(@Query("username") String username,
                                        @Query("targetUsername") String targetUsername);


}
