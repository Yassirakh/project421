package com.uppa.project421.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "JOUEUR")
public class Joueur {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="joueur_seq_gen")
    @SequenceGenerator(name="joueur_seq_gen", sequenceName="joueur_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_JOUEUR")
    private Long id_joueur;

    @Column(name = "PSEUDO")
        private String pseudo;

    @Column(name = "MOTDEPASSE")
    private String motdepasse;

    @Column(name = "AGE")
    private int age;

    @Column(name = "SEXE")
    private String sexe;

    @Column(name = "VILLE")
    private String ville;

    @JoinTable(
            name = "joueur_partie",
            joinColumns = { @JoinColumn(name = "ID_JOUEUR") },
            inverseJoinColumns = { @JoinColumn(name = "ID_PARTIE") }
    )
    @ManyToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
    private Collection<Partie> partiesCollection;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "joueur")
    private Collection<Tour> toursCollection = new ArrayList<>();

    public void addPartie(Partie partie) {
        if (this.partiesCollection == null)
            partiesCollection = new ArrayList<>();
        this.partiesCollection.add(partie);
    }

    public void removePartie(Partie partie) {
        if (this.partiesCollection != null)
            this.partiesCollection.remove(partie);
    }

    public void addTour(Tour tour) {
        if (this.toursCollection == null)
            toursCollection = new ArrayList<>();
        this.toursCollection.add(tour);
    }

    public void removeTour(Tour tour) {
        if (this.toursCollection != null)
            this.toursCollection.remove(tour);
    }
}
