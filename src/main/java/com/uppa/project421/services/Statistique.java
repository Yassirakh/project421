package com.uppa.project421.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Statistique {
    private int partieJouees;
    private int partieGagnees;
    private double partieGagneesPrct;
    private double jetonsChargePrct;
    private double jetonsDechargePrct;
}
