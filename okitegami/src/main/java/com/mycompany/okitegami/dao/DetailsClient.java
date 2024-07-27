/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

import com.mycompany.okitegami.controllers.ClientesJpaController;
import com.mycompany.okitegami.models.Clientes;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author meli_
 */
@Named("detailsClient")
@SessionScoped
public class DetailsClient implements Serializable {
    @Inject
    ClientesJpaController clientCont;
    
    private Clientes client;
    
    public Clientes getClient() {
        return client;
    }

    public void setClient(Clientes client) {
        this.client = client;
    }
    
    public String goClient(Clientes client) {
        this.client = client;
        return "details";
    }
    
    public void doGet() {
        if (client==null) {
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
                client=clientCont.findClientes(id);
            }catch(NumberFormatException e){
                try {
                    external.redirect("/pages/clients/clients.xhtml");
                    System.out.println("Error de busqueda: "+e.getLocalizedMessage());
                } catch (IOException ex) {
                    Logger.getLogger(DetailsClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
