/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

import com.mycompany.okitegami.controllers.session.SessionCont;
import com.mycompany.okitegami.models.Users;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author meli_
 */
@Named("adminRedirect")
public class UsersRedirect {
    public void redirect(int rol){
        if (rol!=1) {
            System.out.println("Rol: "+rol);
            FacesContext face=FacesContext.getCurrentInstance();
            ExternalContext external=face.getExternalContext();
            try {
                external.redirect("/pages/error/error_403.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(SessionCont.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
