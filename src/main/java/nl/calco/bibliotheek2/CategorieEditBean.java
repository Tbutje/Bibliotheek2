/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.calco.bibliotheek2;

import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.naming.NamingException;

/**
 *
 * @author TKoole
 */
@Named
@ViewScoped
public class CategorieEditBean {

    private Categorie categorie = null;
    private static final Logger LOGGER = Logger.getLogger(
            Thread.currentThread().getStackTrace()[1].getClassName());

    public Categorie getCategorie() {

        if (categorie == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            this.categorie = (Categorie) sessionMap.get("categorie");
            sessionMap.remove("categorie");
        }
        return categorie;
    }

    public String annuleren() {
        // 
        return "terug";
    }

    public String opslaan() {

        // als er fout is null returned met foujtmelding
        // oomschrijving != leeg zijn + waarschuwing
        if (this.categorie.getOmschrijving() == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Omschrijving mag niet leeg zijn"));
            return null;
        } else {
            //if id == null dan is het een nieuwe categorie
            if (this.categorie.getCategorieID() == null) {
                try {
                    Database database = new Database();
                    database.insertCategorie(this.categorie);
                    return "terug";

                } catch (SQLException | NamingException ex) {
                    LOGGER.log(Level.SEVERE, "Error {0}", ex);
                    System.out.println(ex.getMessage());
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage("De database kan niet worden benaderd"));
                    return null;
                }
            } else {
                //if id != null dan beestaat die al
                try {
                    Database database = new Database();
                    database.updateCategorie(this.categorie);
                    return "terug";

                } catch (SQLException | NamingException ex) {
                    LOGGER.log(Level.SEVERE, "Error {0}", ex);
                    System.out.println(ex.getMessage());
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage("De database kan niet worden benaderd"));
                    return null;
                }

            }
        }
        //zet categorieen weer op nulll zodat deze weer opnieuw uit de db gehaald worden
    }
}
