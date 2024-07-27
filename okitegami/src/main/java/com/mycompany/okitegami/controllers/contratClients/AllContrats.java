/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers.contratClients;

import com.mycompany.okitegami.controllers.ContratosClientesJpaController;
import com.mycompany.okitegami.models.ContratosClientes;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author meli_
 */
@Named("contratsClie")
@ApplicationScoped
public class AllContrats {
    @Inject ContratosClientesJpaController clieContraCont;
    public List<ContratosClientes> all(){
        return clieContraCont.findContratosClientesEntities();
    }
}
