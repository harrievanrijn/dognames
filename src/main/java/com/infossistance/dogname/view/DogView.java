package com.infossistance.dogname.view;

import com.infossistance.dogname.entity.Dog;
import com.infossistance.dogname.repository.DogRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("dog")
public class DogView extends VerticalLayout {

    private final DogRepository dogRepository;
    private final Grid<Dog> dogGrid = new Grid<>(Dog.class);

    public DogView(DogRepository dogRepository) {

        this.dogRepository = dogRepository;

        // Configureren van de grid
        dogGrid.setColumns("id", "name");
        refreshGrid();

        // Tekstveld voor nieuwe hondennaam
        TextField nameField = new TextField("Voer de naam van een hond in");

        // Knop om nieuwe honden toe te voegen
        Button addButton = new Button("Naam toevoegen", event -> {
            String name = nameField.getValue();
            if (name.isEmpty()) {
                Notification.show("Naam mag niet leeg zijn!");
            } else {
                // Hond opslaan in database
                dogRepository.save(new Dog(name));
                Notification.show("Naam toegevoegd: " + name);
                nameField.clear();
                refreshGrid();
            }
        });

        // Layout opbouwen
        HorizontalLayout formLayout = new HorizontalLayout(nameField, addButton);
        add(formLayout, dogGrid);
    }

    // Helper-method om de grid te verversen
    private void refreshGrid() {
        dogGrid.setItems(dogRepository.findAll());
    }
}
