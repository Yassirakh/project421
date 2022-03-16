package com.uppa.project421.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
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
            name = "JOUEUR_PARTIE",
            joinColumns = { @JoinColumn(name = "ID_JOUEUR") },
            inverseJoinColumns = { @JoinColumn(name = "ID_PARTIE") }
    )
    @ManyToMany
    private Set<Partie> partiesSet;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "JOUEUR")
    private Set<Tour> tours;
}
