package guru.qa.niffler.api;

import guru.qa.niffler.model.AuthJson;
import retrofit2.Call;
import retrofit2.http.*;

public interface AuthApi {
    @GET("oauth2/authorize")
    Call<Void> authorize(@Query("response_type") String responseType,
                         @Query("client_id") String clientId,
                         @Query("scope") String scope,
                         @Query("redirect_uri") String redirectUri,
                         @Query("code_challenge") String codeChallenge,
                         @Query("code_challenge_method") String codeChallengeMethod
    );

    @FormUrlEncoded
    @POST("login")
    Call<Void> login(@Field("_csrf") String csrf,
                     @Field("username") String username,
                     @Field("password") String password
    );

    @FormUrlEncoded
    @POST("oauth2/token")
    Call<AuthJson> token(@Field("code") String code,
                         @Field("redirect_uri") String redirectUri,
                         @Field("code_verifier") String codeVerifier,
                         @Field("grant_type") String grantType,
                         @Field("client_id") String clientId
    );
}
