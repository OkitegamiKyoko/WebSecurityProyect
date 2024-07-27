/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

import com.mycompany.okitegami.controllers.ContratosClientesJpaController;
import com.mycompany.okitegami.models.ContratosClientes;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author meli_
 */
@Named("detailsCont")
@RequestScoped
public class DetailsContrat {
    @Inject
    ContratosClientesJpaController cont;
    ContratosClientes contDetails;

    public ContratosClientes getContDetails() {
        return contDetails;
    }

    public void setContDetails(ContratosClientes contDetails) {
        this.contDetails = contDetails;
    }
    
    public void doGet(){
        if (contDetails==null) {
            FacesContext face=FacesContext.getCurrentInstance();
            ExternalContext external=face.getExternalContext();
            HttpServletRequest request=(HttpServletRequest)external.getRequest();
            System.out.println("hola");
            String req=request.getParameter("id");
            System.out.println(req);
            try{
                int id=Integer.parseInt(req);
                System.out.println(id);
                System.out.println("hola");
                contDetails=cont.findContratosClientes(id);
            }catch(NumberFormatException e){
                try {
                    external.redirect("/pages/clients/clients.xhtml");
                    System.out.println("Error de busqueda: "+e.toString());
                } catch (IOException ex) {
                    Logger.getLogger(DetailsClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
