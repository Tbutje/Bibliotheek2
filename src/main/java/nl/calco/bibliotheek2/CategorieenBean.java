/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.calco.bibliotheek2;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.naming.NamingException;

/**
 *
 * @author TKoole
 */
@Named
@ViewScoped
public class CategorieenBean implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(
    Thread.currentThread().getStackTrace()[1].getClassName());
    
    private List<Categorie> categorieen = null ;
    private Categorie geselecteerdeCategorie;

    public Categorie getGeselecteerdeCategorie() {
        return geselecteerdeCategorie;
    }
    

    public List<Categorie> getCategorieen() {
       // check if empty, if it is then fill with database
        if(categorieen == null)
        {
            try {
                Database database = new Database();
                this.categorieen = database.getCategorieen();
            } catch (SQLException | NamingException ex) {
                LOGGER.log(Level.SEVERE,"Error {0}",ex);
                System.out.println(ex.getMessage());
            }
        }
        
        return categorieen;
    }

    public void selecteer(Integer categorieID){
        // gebruik dit om een omschrijving te selecteren
        
     for(Categorie categorie:categorieen){
         if(categorie.getCategorieID().equals(categorieID)){
             this.geselecteerdeCategorie = categorie;
             break;
         }    
     }
    }
    
    public void verwijderCategorie(Integer categorieID){
        
          try {
                Database database = new Database();
                database.verwijderCategorie(this.geselecteerdeCategorie);
            } catch (SQLException | NamingException ex){
                 LOGGER.log(Level.SEVERE,"Error {0}",ex);
                System.out.println(ex.getMessage());
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("De database kan niet worden benaderd"));
            }
          //zet categorieen weer op nulll zodat deze weer opnieuw uit de db gehaald worden
          this.categorieen = null;
    }
    
    public void wijzigCategorie(){
        
    }
    
    public void nieuweCategorie(){
        
    }
}
