package com.infossistance.dogname.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "dogname")
public class DogName {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Enum wordt opgeslagen als STRING in database
    private GenderType gender;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String creator;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @OneToMany(mappedBy = "dogName", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DogNameVote> dogNameVotes;

    // Getters en Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public List<DogNameVote> getDogNameVotes() {
        return dogNameVotes;
    }

    public void setDogNameVotes(List<DogNameVote> votes) {
        this.dogNameVotes = votes;
    }


    public enum GenderType {
        MALE("M"),
        FEMALE("F");

        private final String code;

        GenderType(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static GenderType fromCode(String code) {
            for (GenderType gender : GenderType.values()) {
                if (gender.code.equals(code)) {
                    return gender;
                }
            }
            throw new IllegalArgumentException("Onbekende code: " + code);
        }
    }
}