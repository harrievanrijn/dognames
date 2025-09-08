package com.infossistance.dogname.view;

import com.infossistance.dogname.entity.DogName;
import com.infossistance.dogname.repository.DogNameRepository;
import com.vaadin.flow.component.grid.Grid;

public class FemaleNameTable {
    private final Grid<DogName> femaleNameGrid;

    public FemaleNameTable(DogNameRepository dogNameRepository) {
        this.femaleNameGrid = new Grid<>(DogName.class);
        updateMaleNameTable(dogNameRepository);
    }

    public void updateMaleNameTable(DogNameRepository dogNameRepository) {
        // Ververs de inhoud van de tabel
    }

    public Grid<DogName> getComponent() {
        return femaleNameGrid;
    }
}