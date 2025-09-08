package com.infossistance.dogname.view;

import com.infossistance.dogname.repository.DogNameRepository;
import com.infossistance.dogname.repository.DogNameVoteRepository;
import com.infossistance.dogname.repository.PersonRepository;
import com.infossistance.dogname.service.VoteService;
import com.infossistance.dogname.view.components.DogNameGenderComponent;
import com.infossistance.dogname.view.components.PersonSection;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class VoteView extends VerticalLayout {
    private final VoteService voteService;
    private final MaleNameTable maleNameTable;
    private final FemaleNameTable femaleNameTable;

    @Autowired
    public VoteView(DogNameRepository dogNameRepository, PersonRepository personRepository,
                    DogNameVoteRepository voteRepository) {
        this.voteService = new VoteService(dogNameRepository, personRepository, voteRepository);

        this.maleNameTable = new MaleNameTable(dogNameRepository);
        this.femaleNameTable = new FemaleNameTable(dogNameRepository);

        // Voeg de koptekst toe
        add(new H2("Voting System"));

        // Voeg de PersonSection toe
        add(new PersonSection(voteService));

        add(new DogNameGenderComponent(voteService));
    }
}