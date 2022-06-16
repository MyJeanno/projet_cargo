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
    @JoinColumn(name = "inventaire_id", insertable = false, updatable = false)
    private Inventaire inventaire;
    private Long inventaire_id;
}
