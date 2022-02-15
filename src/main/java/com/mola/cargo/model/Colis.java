package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
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

}
