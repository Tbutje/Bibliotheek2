/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.calco.bibliotheek2;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.naming.NamingException;

/**
 *
 * @author TKoole
 */
@Named
@ViewScoped
public class BoekEditBean {

    private static final Logger LOGGER = Logger.getLogger(
            Thread.currentThread().getStackTrace()[1].getClassName());

    private Boek boek = null;
    private List<SelectItem> categorieSelectie = null;
    private String exemplaren;

    public void setExemplaren(String exemplaren) {
        this.exemplaren = exemplaren;
    }

    public String getExemplaren() {
        return exemplaren;
    }

    public List<SelectItem> getCategorieSelectie() {

        if (this.categorieSelectie == null) {
            List<Categorie> categorieen = new ArrayList<>();
            List<SelectItem> items = new ArrayList<>();

            try {
                Database database = new Database();
                categorieen = database.getCategorieen();
                for (Categorie categorie : categorieen) {
                    items.add(new SelectItem(categorie.getCategorieID(), categorie.getOmschrijving()));

                }
                this.categorieSelectie = items;
            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());
            }
        }

        return categorieSelectie;
    }

    public Boek getBoek() {
        //als boek leeg is maar dan nieuw boek aan en geef deze een boeknummer
        // namelijk huidige max boeknummer +1
        if (boek == null) {
            this.boek = new Boek();

            try {

                String text;
                Integer nummer;
                Database database = new Database();
                text = (database.getMaxBoeknr()).trim();
                nummer = Integer.parseInt(text.substring(2, 5));
                nummer++;

                this.boek.setBoekNummer(text.substring(0, 2) + nummer);
                // this.boek = boekTmp;
            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());
            }

        }
        return boek;
    }

    public void titelToevoegen() {

        // check titel
        if (this.boek.getTitel().isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Titel mag niet leeg zijn"));
        }

        // check auteur
        if (this.boek.getAuteur().isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Auteur mag niet leeg zijn"));
        }

        //check categorie
        if (this.boek.getCategorie_ID() == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Categorie mag niet leeg zijn"));
        }

        //check locatie
        if (this.boek.getLocatie().isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Locatie mag niet leeg zijn"));
        }

        // exemplaren verplicht, numeriek, integer niet negatief
        // integer.parse geeft error bij float en text
        try {
            Integer exempNum;
            exempNum = Integer.parseInt(exemplaren);
            
            // check niet negatief
            if (exempNum < 0) {
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Aantal exemplaren mag niet lager dan 0 zijn"));
            }
            
        } catch (NumberFormatException ex) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Aantal exemplaren moet een positief heel getal zijn, geen text of float"));
        }
        
        //check isbn

    }
    

    public void annuleren() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Werkt nog niet"));
    }

}
