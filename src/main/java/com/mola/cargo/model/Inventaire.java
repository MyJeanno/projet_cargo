package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double prixTotal;
    private String status;
    private int nombreColis;
    private String commercial;

    @ManyToOne
    @JoinColumn(name = "commandeid", insertable = false, updatable = false)
    private Commande commande;
    private Long commandeid;
}
