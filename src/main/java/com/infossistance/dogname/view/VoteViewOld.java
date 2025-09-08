package com.infossistance.dogname.view;

import com.infossistance.dogname.entity.DogName;
import com.infossistance.dogname.entity.DogNameVote;
import com.infossistance.dogname.entity.Voter;
import com.infossistance.dogname.repository.DogNameRepository;
import com.infossistance.dogname.repository.DogNameVoteRepository;
import com.infossistance.dogname.repository.PersonRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Route("old")
public class VoteViewOld extends VerticalLayout {

    private final DogNameRepository dogNameRepository;
    private final PersonRepository personRepository;
    private final DogNameVoteRepository voteRepository;

    private final ComboBox<Voter> personComboBox; // Voor selectie van een bestaande gebruiker
    private final TextField newPersonField; // Voor het aanmaken van een nieuwe gebruiker
    private final Grid<DogName> maleNameGrid; // Overzicht van mannennamen
    private final Grid<DogName> femaleNameGrid; // Overzicht van vrouwennamen
    private final DogNameVoteRepository dogNameVoteRepository;

    @Autowired
    public VoteViewOld(DogNameRepository dogNameRepository, PersonRepository personRepository,
                       DogNameVoteRepository voteRepository, DogNameVoteRepository dogNameVoteRepository) {
        this.dogNameRepository = dogNameRepository;
        this.personRepository = personRepository;
        this.voteRepository = voteRepository;

        // Layout opbouw
        setSpacing(true);
        setPadding(true);

        // 1. Gebruikerselectie
        personComboBox = new ComboBox<>("Selecteer een persoon");
        personComboBox.setItems(personRepository.findAll());
        personComboBox.setItemLabelGenerator(Voter::getName);

        newPersonField = new TextField("Voeg een nieuwe persoon toe");
        Button addPersonButton = new Button("Toevoegen", e -> addNewPerson());

        HorizontalLayout personLayout = new HorizontalLayout(personComboBox, newPersonField, addPersonButton);
        add(personLayout);

        // 2. Mannennamenoverzicht
        maleNameGrid = new Grid<>(DogName.class, false);
        maleNameGrid.addColumn(DogName::getName).setHeader("Mannennaam");
        maleNameGrid.addColumn(name -> {
            return name.getDogNameVotes() != null ? name.getDogNameVotes().size() : 0;
        }).setHeader("Aantal Stemmen");
        maleNameGrid.addComponentColumn(this::createVoteButton);
        maleNameGrid.setItems(dogNameRepository.findAllByGenderWithVotes(DogName.GenderType.MALE));

        VerticalLayout maleLayout = new VerticalLayout();
        H2 maleLabel = new H2("Mannennamen"); // Correct gebruik voor weergave van tekst
        maleLayout.add(maleLabel, maleNameGrid);

        // Tekstinvoervelden voor nieuwe namen
        TextField newMaleNameField = new TextField("Nieuwe Mannennaam");
        Button saveMaleNameButton = new Button("Opslaan",
                                               event -> saveNewName(newMaleNameField.getValue(), DogName.GenderType.MALE));
        HorizontalLayout maleFormLayout = new HorizontalLayout(newMaleNameField, saveMaleNameButton);
        maleLayout.add(maleFormLayout);
        add(maleLayout);

        // 3. Vrouwennamenoverzicht
        femaleNameGrid = new Grid<>(DogName.class, false);
        femaleNameGrid.addColumn(DogName::getName).setHeader("Vrouwennaam");
        femaleNameGrid.addColumn(name -> {
            return name.getDogNameVotes() != null ? name.getDogNameVotes().size() : 0;
        }).setHeader("Aantal Stemmen");
        femaleNameGrid.addComponentColumn(this::createVoteButton);
        femaleNameGrid.setItems(dogNameRepository.findAllByGenderWithVotes(DogName.GenderType.FEMALE));

        VerticalLayout femaleLayout = new VerticalLayout();
        H2 femaleLabel = new H2("Vrouwennamen"); // Correct gebruik voor weergave van tekst
        femaleLayout.add(femaleLabel, femaleNameGrid);

        TextField newFemaleNameField = new TextField("Nieuwe Vrouwennaam");
        Button saveFemaleNameButton = new Button("Opslaan", event -> saveNewName(newFemaleNameField.getValue(),
                                                                                 DogName.GenderType.FEMALE));
        HorizontalLayout femaleFormLayout = new HorizontalLayout(newFemaleNameField, saveFemaleNameButton);
        femaleLayout.add(femaleFormLayout);
        add(femaleLayout);
        this.dogNameVoteRepository = dogNameVoteRepository;
    }


    // Methode om een nieuwe persoon toe te voegen
    private void addNewPerson() {
        String personName = newPersonField.getValue();
        if (personName == null || personName.trim().isEmpty()) {
            Notification.show("Naam mag niet leeg zijn!", 3000, Notification.Position.MIDDLE);
            return;
        }

        Voter newVoter = new Voter();
        newVoter.setName(personName.trim());
        newVoter.setCreatedOn(LocalDateTime.now());
        personRepository.save(newVoter);

        personComboBox.setItems(personRepository.findAll()); // Herlaad personenlijst
        newPersonField.clear();
        Notification.show("Nieuwe persoon toegevoegd: " + newVoter.getName(), 3000, Notification.Position.MIDDLE);
    }

    // Maak een stemknop voor een naam
    private Button createVoteButton(DogName dogName) {
        Button voteButton = new Button("Stem");
        voteButton.addClickListener(event -> castVote(dogName));
        return voteButton;
    }

    // Methode om een stem te registeren
    private void castVote(DogName dogName) {
        Voter selectedVoter = personComboBox.getValue();
        if (selectedVoter == null) {
            Notification.show("Selecteer eerst een persoon voordat je stemt!", 3000, Notification.Position.MIDDLE);
            return;
        }

        // Controleer of de stem al is uitgebracht
        boolean hasVoted = voteRepository
                .findAll()
                .stream()
                .anyMatch(vote -> vote.getName().equals(dogName) && vote.getPerson().equals(selectedVoter));
        if (hasVoted) {
            Notification.show("Je hebt al gestemd voor deze naam!", 3000, Notification.Position.MIDDLE);
            return;
        }

        // Stem opslaan in de database
        DogNameVote vote = new DogNameVote();
        vote.setName(dogName);
        vote.setPerson(selectedVoter);
        vote.setCreatedOn(LocalDateTime.now());
        voteRepository.save(vote);

        Notification.show("Je hebt gestemd op " + dogName.getName() + "!", 3000, Notification.Position.MIDDLE);

        // Tabellen bijwerken om de nieuwe stemmen weer te geven
        updateMaleNameTable();
        updateFemaleNameTable();
    }

    // Tabellen bijwerken voor mannennamen
    private void updateMaleNameTable() {
        List<DogName> maleDogNames = dogNameRepository.findAllByGender(DogName.GenderType.MALE);
        maleNameGrid.setItems(maleDogNames);
    }

    // Tabellen bijwerken voor vrouwennamen
    private void updateFemaleNameTable() {
        List<DogName> femaleDogNames = dogNameRepository.findAllByGender(DogName.GenderType.FEMALE);
        femaleNameGrid.setItems(femaleDogNames);
    }

    private void saveNewName(String name, DogName.GenderType gender) {
        // Controleer invoer
        if (name == null || name.trim().isEmpty()) {
            Notification.show("Naam mag niet leeg zijn!", 3000, Notification.Position.MIDDLE);
            return;
        }

        // Opslaan in de repository
        DogName newDogName = new DogName();
        String creator = personComboBox.getValue() != null ? personComboBox.getValue().getName() : "Onbekend";
        newDogName.setName(name.trim());
        newDogName.setGender(gender);
        newDogName.setCreator(creator);
        newDogName.setCreatedOn(LocalDateTime.now());

        DogNameVote dogNameVote = new DogNameVote();
        dogNameVote.setName(newDogName);
        dogNameVote.setPerson(personComboBox.getValue());
        dogNameVoteRepository.save(dogNameVote);

        dogNameRepository.save(newDogName);

        // Grids bijwerken
        updateMaleNameTable();
        updateFemaleNameTable();

        Notification.show("Nieuwe naam toegevoegd: " + name, 3000, Notification.Position.MIDDLE);
    }
}