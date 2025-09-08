package com.infossistance.dogname.repository;

import com.infossistance.dogname.entity.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository
public interface DogRepository extends JpaRepository<Dog, Long> {
}