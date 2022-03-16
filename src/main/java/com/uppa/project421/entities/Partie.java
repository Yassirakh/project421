package com.uppa.project421.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "PARTIE")
public class Partie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="partie_seq_gen")
    @SequenceGenerator(name="partie_seq_gen", sequenceName="partie_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_PARTIE")
    private Long id_partie;

    @Temporal(TemporalType.TIMESTAMP)
    private Date DATE_DEBUT;

    @Temporal(TemporalType.TIMESTAMP)
    private Date DATE_FIN;

    @JoinColumn(name = "ID_GAGNANT")
    @ManyToOne(optional = false)
    private Joueur joueurGagnant;

    @ManyToMany(mappedBy = "JOUEUR")
    private Set<Joueur> joueursSet;
}
