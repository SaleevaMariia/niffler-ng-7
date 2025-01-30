package guru.qa.niffler.data.entity.user;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserDataJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class UserDataEntity implements Serializable {

    private UUID id;
    private String username;
    private CurrencyValues currency;
    private String firstname;
    private String surname;
    private String fullname;
    private byte[] photo;
    private byte[] photoSmall;

    public static UserDataEntity fromJson(UserDataJson json) {
        UserDataEntity ue = new UserDataEntity();
        ue.setId(json.id());
        ue.setUsername(json.username());
        ue.setCurrency(json.currency());
        ue.setFirstname(json.firstname());
        ue.setSurname(json.surname());
        ue.setFullname(json.fullname());
        return ue;
    }
}
