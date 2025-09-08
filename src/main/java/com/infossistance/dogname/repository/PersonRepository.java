package com.infossistance.dogname.repository;

import com.infossistance.dogname.entity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Voter, Long> {
    // Optioneel: Methode om een persoon te zoeken op naam
    Voter findByName(String name);

    boolean existsByName(String name); // Spring Data JPA genereert deze automatisch
}