package guru.qa.niffler.data.mapper;


import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class AuthorityEntityRowMapper implements RowMapper<AuthorityEntity> {

    public static final AuthorityEntityRowMapper instance = new AuthorityEntityRowMapper();

    private AuthorityEntityRowMapper() {
    }

    @Override
    @Nonnull
    public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthorityEntity result = new AuthorityEntity();
        UserEntity user = new UserEntity();
        result.setId(rs.getObject("id", UUID.class));
        user.setId(rs.getObject("user_id", UUID.class));
        result.setUser(user);
        result.setAuthority(Authority.valueOf(rs.getString("authority")));
        return result;
    }
}
