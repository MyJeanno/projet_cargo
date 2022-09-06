package com.mola.cargo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VueCommandeStat {
    private String libelleCom;
    private int qteCom;
    private int qteVendue;
    private int qteRestante;
    private double montantCom;
    private double totalVendu;
    private double benefice;
}
