package com.uppa.project421.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TOUR")
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="tour_seq_gen")
    @SequenceGenerator(name="tour_seq_gen", sequenceName="tour_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TOUR")
    private Long id_tour;

    @Column(name = "JETONS")
    private int jetons = 0;

    @Column(name = "NBRLANCER")
    private int nbrelance;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_FIN_TOUR")
    private Date DATE_FIN;

    @Column(name = "PHASE")
    private int phase;

    @JoinColumn(name = "ID_JOUEUR")
    @ManyToOne(optional = false)
    private Joueur joueur;

    @JoinColumn(name = "ID_PARTIE")
    @ManyToOne(optional = false)
    private Partie partie;

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            mappedBy = "tour")
    private Collection<Lance> lancesCollection  = new ArrayList<>();


    public void addLance(Lance lance) {
        if (this.lancesCollection == null)
            lancesCollection = new ArrayList<>();
        this.lancesCollection.add(lance);
    }

    public void removeLance(Lance lance) {
        if (this.lancesCollection != null)
            this.lancesCollection.remove(lance);
    }
}
