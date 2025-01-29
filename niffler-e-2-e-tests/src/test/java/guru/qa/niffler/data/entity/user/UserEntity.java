package guru.qa.niffler.data.entity.user;

import guru.qa.niffler.model.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class UserEntity implements Serializable {
    private UUID id;

    private String username;

    private String password;

    private Boolean enabled;

    private Boolean accountNonExpired;

    private Boolean accountNonLocked;

    private Boolean credentialsNonExpired;

    public static UserEntity fromJson(UserJson json) {
        UserEntity ue = new UserEntity();
        ue.setId(json.id());
        ue.setUsername(json.username());
        ue.setPassword(json.password());
        ue.setCredentialsNonExpired(true);
        ue.setAccountNonLocked(true);
        ue.setAccountNonExpired(true);
        ue.setEnabled(true);
        return ue;
    }
}