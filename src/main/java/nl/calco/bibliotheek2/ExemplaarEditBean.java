/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.calco.bibliotheek2;

import java.util.Map;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 *
 * @author TKoole
 */
@Named
@ViewScoped
public class ExemplaarEditBean {
    
    private static final Logger LOGGER = Logger.getLogger(
            Thread.currentThread().getStackTrace()[1].getClassName());
    private String exemplaren;

    public String getExemplaren() {
        return exemplaren;
    }
    
    private Boek boek = null;

    public Boek getBoek() {
        
           if (boek == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
            this.boek = (Boek) sessionMap.get("boek");
            sessionMap.remove("boek");
        }
        return boek;
    }
    
    public void exemplaarToevoegen(){
        
    }

    

}
