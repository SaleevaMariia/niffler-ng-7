package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.UserEntity;
import guru.qa.niffler.data.extractor.AuthUserEntityExtractor;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {
    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity create(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    """
                               INSERT INTO "user" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) 
                               VALUES (?,?,?,?,?,?)
                            """,
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());
            return ps;
        }, kh);

        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        user.setId(generatedKey);
        List<AuthorityEntity> a = user.getAuthorities();

        jdbcTemplate.batchUpdate("INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setObject(1, generatedKey);
                        preparedStatement.setString(2, a.get(i).getAuthority().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return a.size();
                    }
                });
        return user;
    }

    @Override
    public UserEntity update(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    """
                               UPDATE "user" SET username = ?, password = ?, enabled = ?, 
                                 account_non_expired = ?, account_non_locked = ?, credentials_non_expired = ? 
                               WHERE id = ?
                            """
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());
            ps.setObject(7, user.getId());
            return ps;
        });

        List<AuthorityEntity> a = user.getAuthorities();

        jdbcTemplate.batchUpdate("UPDATE \"authority\" SET authority = ? WHERE user_id = ?",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setString(1, a.get(i).getAuthority().name());
                        preparedStatement.setObject(2, user.getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return a.size();
                    }
                });
        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.query(
                        """
                                 SELECT a.id as authority_id,
                                 authority,
                                 user_id as id,
                                 u.username,
                                 u.password,
                                 u.enabled,
                                 u.account_non_expired,
                                 u.account_non_locked,
                                 u.credentials_non_expired
                                FROM "user" u join public.authority a on u.id = a.user_id WHERE u.id = ? """,
                        AuthUserEntityExtractor.instance,
                        id
                ).getFirst()
        );
    }

    @Override
    public void remove(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("DELETE FROM \"user\" WHERE id = ?");
            ps.setObject(1, user.getId());
            return ps;
        });
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("DELETE FROM \"authority\" WHERE user_id = ?");
            ps.setObject(1, user.getId());
            return ps;
        });
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.query(
                        """
                                 SELECT a.id as authority_id,
                                 authority,
                                 user_id as id,
                                 u.username,
                                 u.password,
                                 u.enabled,
                                 u.account_non_expired,
                                 u.account_non_locked,
                                 u.credentials_non_expired
                                FROM "user" u join public.authority a on u.id = a.user_id WHERE u.username = ? """,
                        AuthUserEntityExtractor.instance,
                        username
                ).getFirst()
        );
    }
}
