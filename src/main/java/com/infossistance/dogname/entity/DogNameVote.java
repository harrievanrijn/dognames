package com.infossistance.dogname.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dognamevote")
public class DogNameVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_dogname", nullable = false)
    private DogName dogName;

    @ManyToOne
    @JoinColumn(name = "id_person", nullable = false)
    private Voter voter;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    // Getters en Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DogName getName() {
        return dogName;
    }

    public void setName(DogName dogName) {
        this.dogName = dogName;
    }

    public Voter getPerson() {
        return voter;
    }

    public void setPerson(Voter voter) {
        this.voter = voter;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}