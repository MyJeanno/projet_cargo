package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Colis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "libelle_collis")
    private String libelleColis;
    @Column(name = "prix_colis")
    private double prixColis;
    private double poids;

    @ManyToOne
    @JoinColumn(name = "commandeid", insertable = false, updatable = false)
    private Commande commande;
    private Long commandeid;

    @Transient
    @ManyToOne
    @JoinColumn(name = "paysid", insertable = false, updatable = false)
    private Pays pays;
    @Transient
    private Long paysid;

    @ManyToOne
    @JoinColumn(name = "cartonid", insertable = false, updatable = false)
    private Carton carton;
    private Long cartonid;

}
