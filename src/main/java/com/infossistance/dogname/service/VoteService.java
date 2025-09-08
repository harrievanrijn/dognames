package com.infossistance.dogname.service;

import com.infossistance.dogname.entity.DogName;
import com.infossistance.dogname.entity.DogNameVote;
import com.infossistance.dogname.entity.Voter;
import com.infossistance.dogname.repository.DogNameRepository;
import com.infossistance.dogname.repository.DogNameVoteRepository;
import com.infossistance.dogname.repository.PersonRepository;
import com.vaadin.flow.component.notification.Notification;

import java.time.LocalDateTime;
import java.util.List;

public class VoteService {
    private final DogNameRepository dogNameRepository;
    private final PersonRepository personRepository;
    private final DogNameVoteRepository dogNameVoteRepository;

    public VoteService(DogNameRepository dogNameRepository, PersonRepository personRepository, DogNameVoteRepository voteRepository) {
        this.dogNameRepository = dogNameRepository;
        this.personRepository = personRepository;
        this.dogNameVoteRepository = voteRepository;
    }

    public void addNewPerson(String name) {
        if (personExistsByName(name)) {
            throw new IllegalArgumentException("Persoon met deze naam bestaat al!");
        }
        Voter newVoter = new Voter();
        newVoter.setName(name);
        personRepository.save(newVoter);
    }

    public void castVote(DogName dogName, Voter voter) {
        // Controleer of de stem al is uitgebracht
        boolean hasVoted = dogNameVoteRepository
                .findAll()
                .stream()
                .anyMatch(vote -> vote.getName().equals(dogName) && vote.getPerson().equals(voter));
        if (hasVoted) {
            Notification.show("Je hebt al gestemd voor deze naam!", 3000, Notification.Position.MIDDLE);
            return;
        }

        // Stem opslaan in de database
        DogNameVote dogNameVote = new DogNameVote();
        dogNameVote.setName(dogName);
        dogNameVote.setPerson(voter);
        dogNameVote.setCreatedOn(LocalDateTime.now());
        dogNameVoteRepository.save(dogNameVote);
    }

    // Nieuwe methode om alle personen op te halen
    public List<Voter> getAllPersons() {
        return personRepository.findAll();
    }

    public boolean personExistsByName(String name) {
        return personRepository.existsByName(name);
    }

    public List<DogNameVote> getAllNames() {
        return dogNameVoteRepository.findAll();
    }

    public int getTotalVotesForName(DogName dogName) {
        return 3;
    }
}