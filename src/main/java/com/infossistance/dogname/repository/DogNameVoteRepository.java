package com.infossistance.dogname.repository;

import com.infossistance.dogname.entity.DogNameVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogNameVoteRepository extends JpaRepository<DogNameVote, Long> {
}