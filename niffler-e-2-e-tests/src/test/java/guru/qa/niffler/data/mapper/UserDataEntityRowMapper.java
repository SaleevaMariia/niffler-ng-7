package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.data.entity.user.UserDataEntity;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class UserDataEntityRowMapper implements RowMapper<UserDataEntity> {

    public static final UserDataEntityRowMapper instance = new UserDataEntityRowMapper();

    private UserDataEntityRowMapper() {
    }

    @Override
    @Nonnull
    public UserDataEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDataEntity result = new UserDataEntity();
        result.setId(rs.getObject("id", UUID.class));
        result.setUsername(rs.getString("username"));
        result.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
        result.setFirstname(rs.getString("firstname"));
        result.setSurname(rs.getString("surname"));
        result.setFullname(rs.getString("full_name"));
        result.setPhoto(rs.getBytes("photo"));
        result.setPhotoSmall(rs.getBytes("photo_small"));
        return result;
    }
}
