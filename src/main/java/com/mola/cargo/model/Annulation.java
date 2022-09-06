package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Annulation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String motif;

    @ManyToOne
    @JoinColumn(name = "commande_id", insertable = false, updatable = false)
    private Commande commande;
    private Long commande_id;
}
