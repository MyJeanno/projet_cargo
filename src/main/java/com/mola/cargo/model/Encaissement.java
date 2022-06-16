package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Encaissement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double montant;
    @DateTimeFormat(pattern = "yyyy-dd-MM")
    private LocalDate dateEncaissement;

    @ManyToOne
    @JoinColumn(name = "commande_id", insertable = false, updatable = false)
    private Commande commande;
    private Long commande_id;


}
