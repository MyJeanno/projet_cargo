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
    private int quantite;

    @ManyToOne
    @JoinColumn(name = "cargotypeid", insertable = false, updatable = false)
    private CargoType cargoType;
    private Long cargotypeid;

    @ManyToOne
    @JoinColumn(name = "commandeid", insertable = false, updatable = false)
    private Commande commande;
    private Long commandeid;

}
