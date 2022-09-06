package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartonVente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "qte_vente")
    private int qteVente;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateVente;

    @ManyToOne
    @JoinColumn(name = "cartonid", insertable = false, updatable = false)
    private Carton carton;
    private Long cartonid;

    @ManyToOne
    @JoinColumn(name = "commandeCartonid", insertable = false, updatable = false)
    private CommandeCarton commandeCarton;
    private Long commandeCartonid;


}
