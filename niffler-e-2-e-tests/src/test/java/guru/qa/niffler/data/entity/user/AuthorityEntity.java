package guru.qa.niffler.data.entity.user;

import guru.qa.niffler.model.Authority;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AuthorityEntity {
    private UUID id;
    private Authority authority;
    private UserEntity user;
}
