package com.infossistance.dogname.view.components;

import com.infossistance.dogname.entity.DogNameVote;
import com.infossistance.dogname.entity.Voter;
import com.infossistance.dogname.service.VoteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.notification.Notification;

import java.util.List;
import java.util.stream.Collectors;

public class DogNameGenderComponent extends VerticalLayout {

    private final VoteService voteService;
    private Voter selectedVoter; // Houd de geselecteerde stemmer bij

    public DogNameGenderComponent(VoteService voteService) {
        this.voteService = voteService;

        // Initialiseer twee tabellen
        initUI();
    }

    private void initUI() {
        // Tabel voor mannen
        VerticalLayout manTable = createGenderTable("Mannen", "man");

        // Tabel voor vrouwen
        VerticalLayout vrouwTable = createGenderTable("Vrouwen", "vrouw");

        // Voeg beide tabellen toe naast elkaar
        HorizontalLayout tablesLayout = new HorizontalLayout(manTable, vrouwTable);
        tablesLayout.setWidthFull();
        tablesLayout.setSpacing(true);

        add(tablesLayout);
    }

    public void setSelectedVoter(Voter voter) {
        // Stel de geselecteerde stemmer in
        this.selectedVoter = voter;
    }

    private VerticalLayout createGenderTable(String title, String gender) {
        // Filter alle namen op basis van gender
        List<DogNameVote> dogNameVotes = voteService.getAllNames().stream()
                .filter(nameVote -> nameVote.getName().getGender() != null &&
                        nameVote.getName().getGender().name().equalsIgnoreCase(gender))
                .collect(Collectors.toList());

        // Maak een grid voor deze gender
        Grid<DogNameVote> grid = new Grid<>(DogNameVote.class, false);
        grid.addColumn(dogNameVote -> dogNameVote.getName().getName()).setHeader("Naam");
        grid.addColumn(dogNameVote -> dogNameVote.getName().getCreator()).setHeader("Bedenker");

        // Voeg een stemknop toe
        grid.addComponentColumn(nameVote -> {
            Button voteButton = new Button("Stem");
            voteButton.addClickListener(click -> {
                if (selectedVoter == null) {
                    Notification.show("Selecteer eerst een stemmer!", 3000, Notification.Position.MIDDLE);
                    return;
                }

                try {
                    // Breng de stem uit
                    voteService.castVote(nameVote.getName(), selectedVoter);
                    Notification.show("Je hebt gestemd op: " + nameVote.getName().getName(),
                            3000, Notification.Position.MIDDLE);
                    grid.getDataProvider().refreshAll();
                } catch (Exception e) {
                    Notification.show("Kan niet stemmen: " + e.getMessage(),
                            3000, Notification.Position.MIDDLE);
                }
            });
            return voteButton;
        }).setHeader("Stem");

        // Vul de grid met gefilterde namen
        grid.setItems(dogNameVotes);

        // Maak een layout om de tabel te bevatten
        VerticalLayout layout = new VerticalLayout();
        layout.add(grid);
        layout.setWidth("100%");
        layout.setPadding(false);
        layout.setSpacing(false);

        return layout; // Return gender-specifieke layout
    }
}