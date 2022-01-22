package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Etat {

    @Id
    private Long code;
    @Column(name = "libelle_etat")
    private String libelleEtat;

    @ManyToOne
    @JoinColumn(name = "paysid", insertable = false, updatable = false)
    private Pays pays;
    private Long paysid;
}
