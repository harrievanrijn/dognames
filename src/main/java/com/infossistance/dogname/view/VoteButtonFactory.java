package com.infossistance.dogname.view;

import com.infossistance.dogname.entity.DogName;
import com.vaadin.flow.component.button.Button;

import java.util.function.Consumer;

public class VoteButtonFactory {
    public static Button createVoteButton(DogName dogName, Consumer<DogName> onVoteClick) {
        Button button = new Button("Vote");
        button.addClickListener(event -> onVoteClick.accept(dogName));
        return button;
    }
}