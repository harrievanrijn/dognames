package com.infossistance.dogname.view.components;

import com.infossistance.dogname.entity.Voter;
import com.infossistance.dogname.service.VoteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class PersonSection extends HorizontalLayout {

    private final VoteService voteService;

    public PersonSection(VoteService voteService) {
        this.voteService = voteService;

        // Initialiseer de UI
        initUI();
    }

    private void initUI() {
        // Creëer de ComboBox voor personen
        ComboBox<Voter> personComboBox = new ComboBox<>("Wie ben je?");
        personComboBox.setItems(voteService.getAllPersons());
        personComboBox.setItemLabelGenerator(Voter::getName);
        personComboBox.setWidth("200px");

        // Creëer het tekstveld en maak het onzichtbaar
        TextField newPersonField = new TextField("Nieuw persoon");
        newPersonField.setVisible(false);
        newPersonField.setWidth("200px");

        // Voeg-knop configuratie
        Button addPersonButton = new Button("Voeg toe", event -> {
            newPersonField.setVisible(true); // Toon het invoerveld
            newPersonField.focus(); // Focus op het invoerveld
        });

        // Voeg keypress listener toe op het invoerveld
        newPersonField.addKeyPressListener(com.vaadin.flow.component.Key.ENTER, keyEvent -> {
            String newName = newPersonField.getValue();

            if (newName == null || newName.isEmpty()) {
                Notification.show("Naam mag niet leeg zijn!", 3000, Notification.Position.MIDDLE);
                return;
            }

            if (voteService.personExistsByName(newName)) {
                Notification.show("De naam bestaat al!", 3000, Notification.Position.MIDDLE);
                return;
            }

            try {
                voteService.addNewPerson(newName);
                personComboBox.setItems(voteService.getAllPersons());

                // Nieuw toegevoegde persoon selecteren
                voteService.getAllPersons().stream()
                        .filter(person -> person.getName().equals(newName))
                        .findFirst()
                        .ifPresent(personComboBox::setValue);

                newPersonField.clear();
                newPersonField.setVisible(false); // Verberg het tekstveld opnieuw
                Notification.show("Persoon toegevoegd en geselecteerd!", 3000, Notification.Position.MIDDLE);
            } catch (IllegalArgumentException e) {
                Notification.show(e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        // Horizontale layout met correcte uitlijning
        this.add(personComboBox, addPersonButton, newPersonField);
        this.setAlignItems(Alignment.BASELINE); // Lijnt alles uit op basis van de basislijn (gelijke hoogte)
        this.setSpacing(true); // Ruimte tussen de componenten
        this.setPadding(false); // Geen extra padding
    }
}