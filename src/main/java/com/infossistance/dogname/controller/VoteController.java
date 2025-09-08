package com.infossistance.dogname.controller;


import com.infossistance.dogname.entity.DogName;
import com.infossistance.dogname.entity.DogNameVote;
import com.infossistance.dogname.entity.Voter;
import com.infossistance.dogname.repository.DogNameRepository;
import com.infossistance.dogname.repository.DogNameVoteRepository;
import com.infossistance.dogname.repository.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vote")
public class VoteController {

    private final DogNameRepository dogNameRepository;
    private final PersonRepository personRepository;
    private final DogNameVoteRepository voteRepository;

    public VoteController(DogNameRepository dogNameRepository, PersonRepository personRepository, DogNameVoteRepository voteRepository) {
        this.dogNameRepository = dogNameRepository;
        this.personRepository = personRepository;
        this.voteRepository = voteRepository;
    }

    // Haalt stemmenoverzicht op inclusief namen en stemmers
    @GetMapping
    public String showVotingPage(Model model) {
        List<DogName> maleDogNames = dogNameRepository.findAllByGender(DogName.GenderType.MALE);
        List<DogName> femaleDogNames = dogNameRepository.findAllByGender(DogName.GenderType.FEMALE);
        List<Voter> voters = personRepository.findAll();

        model.addAttribute("maleNames", maleDogNames);
        model.addAttribute("femaleNames", femaleDogNames);
        model.addAttribute("persons", voters);

        return "vote"; // Verwijst naar de HTML-pagina
    }

    // Nieuw persoon aanmaken
    @PostMapping("/person")
    public String createNewPerson(@RequestParam String name) {
        Voter newVoter = new Voter();
        newVoter.setName(name);
        newVoter.setCreatedOn(java.time.LocalDateTime.now());
        personRepository.save(newVoter);
        return "redirect:/vote";
    }

    // Opslaan van een stem
    @PostMapping("/{nameId}/addVote")
    public String addVote(@PathVariable Long nameId, @RequestParam Long personId) {
        DogName dogName = dogNameRepository.findById(nameId).orElseThrow(() -> new RuntimeException("Naam niet gevonden"));
        Voter voter = personRepository.findById(personId).orElseThrow(() -> new RuntimeException("Persoon niet gevonden"));

        DogNameVote vote = new DogNameVote();
        vote.setName(dogName);
        vote.setPerson(voter);
        vote.setCreatedOn(java.time.LocalDateTime.now());
        voteRepository.save(vote);

        return "redirect:/vote";
    }
}
