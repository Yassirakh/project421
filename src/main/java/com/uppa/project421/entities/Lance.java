package com.uppa.project421.entities;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Lance")
public class Lance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="lance_seq_gen")
    @SequenceGenerator(name="lance_seq_gen", sequenceName="lance_SEQ", allocationSize = 1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_LANCE")
    private Long id_lance;

    @Column(name = "DES_UN")
    private int desUn;

    @Column(name = "DES_DEUX")
    private int desDeux;

    @Column(name = "DES_TROIS")
    private int desTrois;

    @JoinColumn(name = "ID_TOUR")
    @ManyToOne(optional = false)
    private Tour tour;
}
