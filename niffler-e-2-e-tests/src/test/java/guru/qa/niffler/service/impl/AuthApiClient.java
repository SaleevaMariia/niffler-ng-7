package guru.qa.niffler.service.impl;

import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.AuthJson;
import guru.qa.niffler.utils.OauthUtils;
import retrofit2.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@ParametersAreNonnullByDefault
public class AuthApiClient {
    private static final Config CFG = Config.getInstance();
    private final AuthApi authApi = new RestClient
            .EmtyRestClient(CFG.authUrl(), true)
            .retrofit()
            .create(AuthApi.class);
    private String codeVerifier;
    private String codeChallenge;
    private String code;

    public void preRequest() {
        try {
            codeVerifier = OauthUtils.generateCodeVerifier();
            codeChallenge = OauthUtils.generateCodeChallenge(codeVerifier);
            authApi.authorize("code", "client", "openid",
                    "http://127.0.0.1:3000/authorized", codeChallenge, "S256"
            ).execute();

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void login(String username, String password) {
        try {
            Response<Void> response = authApi.login(ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN"),
                    username, password).execute();
            code = response.raw().request().url().queryParameter("code");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String token() {
        try {
            Response<AuthJson> response = authApi.token(code,
                    "http://127.0.0.1:3000/authorized",
                    codeVerifier,
                    "authorization_code",
                    "client").execute();
            return response.body().idToken();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
