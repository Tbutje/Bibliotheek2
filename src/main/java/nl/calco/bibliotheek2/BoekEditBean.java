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
import java.util.Map;
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
    private Boolean nieuw_boek = false;
    //extra toevoegingen om ook voor exemplaren te gebruiken

    private Boolean exemplaar_edit = false;
    private Exemplaar exemplaar = null;

    public Boolean isExemplaar_edit() {
        return exemplaar_edit;
    }

    public Exemplaar getExemplaar() {
        return exemplaar;
    }

    public Boolean isNieuw_boek() {
        return nieuw_boek;
    }

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

            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            if (sessionMap.containsKey("boek")) {
                this.boek = (Boek) sessionMap.get("boek");
                sessionMap.remove("boek");

                //controleer of er ook een exemplaar is
                // dat betekend dat we in exemplaar edit mode gaan
                if (sessionMap.containsKey("exemplaar")) {
                    this.exemplaar = (Exemplaar) sessionMap.get("exemplaar");
                    this.exemplaar_edit = true;
                    sessionMap.remove("exemplaar");
                }

            } else {
                nieuw_boek = true;
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

        }
        return boek;
    }

    public void titelToevoegen() {
        this.checkInput();
        FacesContext context = FacesContext.getCurrentInstance();

        //check dat titel niet bestaat en isbn uniek is
        List<Boek> boeken;
        try {
            Database database = new Database();
            boeken = database.getBoeken(this.boek.getTitel());
            if (boeken.size() > 0) {
                context.addMessage(null, new FacesMessage("Titel bestaat al"));
            }
            boeken = database.getBoeken(this.boek.getIsbn());
            if (boeken.size() > 0) {
                context.addMessage(null, new FacesMessage("ISBNF bestaat al"));
            }
        } catch (SQLException | NamingException ex) {
            LOGGER.log(Level.SEVERE, "Error {0}", ex);
            System.out.println(ex.getMessage());
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

            // voeg de exemplaren toe als exemplaar edit != true
            if (!this.isExemplaar_edit()) {
                try {
                    Database database = new Database();
                    database.insertExemplaar(this.boek.getBoek_ID(), Integer.parseInt(exemplaren));
                } catch (SQLException | NamingException ex) {
                    LOGGER.log(Level.SEVERE, "Error {0}", ex);
                    System.out.println(ex.getMessage());
                }
            }

            // clear scherm
        }

    }

    public String terug() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();

        if (sessionMap.containsKey("vanwaar")) {

            // kijk waar we vandan komen
            String vanwaar = (String) sessionMap.get("vanwaar");
            sessionMap.remove("vanwaar");

            // krijg een vers boek.
            try {
                Database database = new Database();
                this.boek = database.getBoek(this.boek.getBoek_ID());
            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());
            }

            if (vanwaar.equals("vanuitlenen")) {
                // stop alles weer in sessionmap
                sessionMap.put("boek", this.boek);
                return "uitleneninnemen";
            } else {
                // niks in session map als we terug naar boeken gaan
                return "naarboeken";
            }
        } else {
            // niks in session map als we terug naar boeken gaan
            return "naarboeken";
        }

        //      sessionMap.remove("boek");
    }

    public void exemplaarOpslaan() {
        // voeg boekwijzigingen door
        titelWijzigen();

        //voer exemplaar wijzigingen door. oftewel vermist status
        try {
            Database database = new Database();
            database.updateExemplaar(exemplaar);
        } catch (SQLException | NamingException ex) {
            LOGGER.log(Level.SEVERE, "Error {0}", ex);
            System.out.println(ex.getMessage());
        }

    }

    public void titelWijzigen() {
        this.checkInput();
        FacesContext context = FacesContext.getCurrentInstance();

        //als er geen fouten zijn vooer het dan door :)
        // beetje hackie, maar dit checken door te kijken of er geen messages zijn
        if (context.getMessageList().isEmpty()) {

            // update het boek op en krijg boek_id terug
            try {
                Database database = new Database();
                database.updateBoek(this.boek);
            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE, "Error {0}", ex);
                System.out.println(ex.getMessage());
            }

            // voeg de exemplaren toe als exemplaar edit != true
            if (!this.isExemplaar_edit()) {
                String exemplaar_aantal = getExemplaar_aantal();
                try {
                    Database database = new Database();
                    database.insertExemplaar(boek.getBoek_ID(),
                            Integer.parseInt(exemplaar_aantal) + 1,
                            Integer.parseInt(exemplaar_aantal) + 1 + Integer.parseInt(exemplaren));
                } catch (SQLException | NamingException ex) {
                    LOGGER.log(Level.SEVERE, "Error {0}", ex);
                    System.out.println(ex.getMessage());
                }
            }

            // clear scherm
        }

    }

    private void checkInput() {
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
        // niet doen als we in exemplaar edit mode zijn
        if (!this.exemplaar_edit) {
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

    }

    // helper functie om huidige hoogste exemplaar te vinden
    private String getExemplaar_aantal() {
        List<Exemplaar> exemplaren;
        String exemplaar_aantal = new String();

        try {
            Database database = new Database();
            exemplaren = database.getExemplaren(boek.getBoek_ID());
            // get last row of exemplaren to get highest number
            exemplaar_aantal = exemplaren.get(exemplaren.size() - 1).getExemplaarVolgnummer().toString();

        } catch (SQLException | NamingException ex) {
            LOGGER.log(Level.SEVERE, "Error {0}", ex);
            System.out.println(ex.getMessage());
        }

        return exemplaar_aantal;
    }
}
