/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.calco.bibliotheek2;

import java.io.Serializable;
import java.time.LocalDate;

/**
 *
 * @author TKoole
 */
public class Exemplaar implements Serializable {

    private Integer exemplaar_ID;
    private Integer boek_ID;
    private Integer exemplaarVolgnummer;
    private LocalDate datumAanschaf;
    private boolean vermist;

    public Integer getExemplaar_ID() {
        return exemplaar_ID;
    }

    public void setExemplaar_ID(Integer exemplaar_ID) {
        this.exemplaar_ID = exemplaar_ID;
    }

    public Integer getBoek_ID() {
        return boek_ID;
    }

    public void setBoek_ID(Integer boek_ID) {
        this.boek_ID = boek_ID;
    }

    public Integer getExemplaarVolgnummer() {
        return exemplaarVolgnummer;
    }

    public void setExemplaarVolgnummer(Integer exemplaarVolgnummer) {
        this.exemplaarVolgnummer = exemplaarVolgnummer;
    }

    public LocalDate getDatumAanschaf() {
        return datumAanschaf;
    }

    public void setDatumAanschaf(LocalDate datumAanschaf) {
        this.datumAanschaf = datumAanschaf;
    }

    public boolean isVermist() {
        return vermist;
    }

    public void setVermist(boolean vermist) {
        this.vermist = vermist;
    }

}
