package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoRecepteur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate dateInfo;
    private String motifInfo;

    @ManyToOne
    @JoinColumn(name = "recepteurId", insertable = false, updatable = false)
    private Recepteur recepteur;
    private Long recepteurId;
}
