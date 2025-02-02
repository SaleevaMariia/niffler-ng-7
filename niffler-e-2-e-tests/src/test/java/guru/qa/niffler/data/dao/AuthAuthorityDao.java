package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.user.AuthorityEntity;

import java.util.List;

public interface AuthAuthorityDao {
    void create(AuthorityEntity... authority);

    List<AuthorityEntity> findAll();
}
