/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers.contratClients;

import com.mycompany.okitegami.models.ContratosClientes;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author meli_
 */
@Named("detailsContClie")
@SessionScoped
public class detailsContrat implements Serializable{
    ContratosClientes cont;

    public ContratosClientes getCont() {
        return cont;
    }

    public void setCont(ContratosClientes cont) {
        this.cont = cont;
    }
    
    public String details(ContratosClientes cont){
        this.cont=cont;
        return "detailsContClient";
    }
}
