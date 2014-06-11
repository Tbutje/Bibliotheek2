/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.calco.bibliotheek2;

import java.io.Serializable;
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
public class BoekEditBean implements Serializable {

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
        FacesContext context = FacesContext.getCurrentInstance();

        // check titel
        if (this.boek.getTitel().isEmpty()) {
            //  FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Titel mag niet leeg zijn"));
        }

        // check auteur
        if (this.boek.getAuteur().isEmpty()) {
            //    FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Auteur mag niet leeg zijn"));
        }

        //check categorie
        if (this.boek.getCategorie_ID() == null) {
            //          FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Categorie mag niet leeg zijn"));
        }

        //check locatie
        if (this.boek.getLocatie().isEmpty()) {
            //          FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Locatie mag niet leeg zijn"));
        }

        // exemplaren verplicht, numeriek, integer niet negatief
        // integer.parse geeft error bij float en text
        try {

            // check niet negatief
            if (Integer.parseInt(exemplaren) < 0) {
                //              FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Aantal exemplaren mag niet lager dan 0 zijn"));
            }

        } catch (NumberFormatException ex) {
            //          FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Aantal exemplaren moet een positief heel getal zijn, geen text of float"));
        }

        //check isbn
        if (!this.boek.getIsbn().isEmpty()) {
            String isbn = this.boek.getIsbn().trim();
            isbn = isbn.replace("-", "");

            try {
                //isbn 10
                if (isbn.length() == 10) {

                    int sum = 0;
                    for (int idx = 0; idx < 10; ++idx) {
                        int nummer = Integer.parseInt(isbn.substring(idx, idx + 1));
                        sum += (nummer * (10 - idx));
                    }

                    //sum should be multiple 11
                    if (sum % 11 != 0) {
                        //                   FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, new FacesMessage("ISBN incorrect"));
                    }

                } // isbn 13 
                else if (isbn.length() == 13) {
                    int sum = 0;

                    for (int idx = 0; idx < 13; ++idx) {
                        int nummer = Integer.parseInt(isbn.substring(idx, idx + 1));
                        if (idx % 2 == 0) {
                            //even
                            sum += nummer;
                        } else {
                            //oneven
                            sum += (nummer * 3);
                        }

                    }
                    //sum should be multiple 10
                    if (sum % 10 != 0) {
                        //                       FacesContext context = FacesContext.getCurrentInstance();
                        context.addMessage(null, new FacesMessage("ISBN incorrect"));
                    }

                } //voldoet niet aan isbn lengtes
                else {
                    //                   FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage("ISBN moet lengte 10 of 13 zijn"));

                }
            } catch (NumberFormatException ex) {
                //            FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("ISBN moet numeriek zijn"));
            }

        } else {
            //          FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("ISBN mag niet leeg zijn"));
        }

        //als er geen fouten zijn vooer het dan door :)
        // beetje hackie, maar dit checken door te kijken of er geen messages zijn
        if (context.getMessageList().isEmpty()) {

            // sla het boek op en krijg boek_id terug
            try {
                Database database = new Database();
                this.boek = database.insertBoek(this.boek);
            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());
            }

            // voeg de exemplaren toe
            try {
                Database database = new Database();
                database.insertExemplaar(this.boek.getBoek_ID(), Integer.parseInt(exemplaren));
            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());
            }

            // clear scherm
        }

    }

    public void annuleren() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage("Werkt nog niet"));
    }

}
