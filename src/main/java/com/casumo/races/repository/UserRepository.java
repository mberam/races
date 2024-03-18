package com.casumo.races.repository;

import com.casumo.races.db.RacesUser;
import com.casumo.races.db.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<RacesUser,Long> {
    List<RacesUser> getRacesUserByTypeEquals(UserType type);

    RacesUser getRacesUserByTypeEqualsAndIdEquals(UserType type,long id);
    RacesUser getRacesUserById(long id);
    RacesUser findByUsername(String name);

}
