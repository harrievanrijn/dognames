package com.infossistance.dogname.repository;

import com.infossistance.dogname.entity.DogName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DogNameRepository extends JpaRepository<DogName, Long> {

    @Query("SELECT n FROM DogName n LEFT JOIN FETCH n.dogNameVotes WHERE n.gender = :gender")
    List<DogName> findAllByGenderWithVotes(@Param("gender") DogName.GenderType gender);

    // Custom query method om namen te vinden op basis van gender
    List<DogName> findAllByGender(DogName.GenderType gender);

    // Zoek een naam via een exacte match
    List<DogName> findByName(String name);
}