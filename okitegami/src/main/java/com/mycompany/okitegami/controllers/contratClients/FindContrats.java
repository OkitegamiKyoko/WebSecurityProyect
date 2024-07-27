/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers.contratClients;

import com.mycompany.okitegami.controllers.ContratosClientesJpaController;
import com.mycompany.okitegami.models.ContratosClientes;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * @author meli_
 */
public class FindContrats {
    @Inject ContratosClientesJpaController clientCCont;
    public List<ContratosClientes> all(){
        return clientCCont.findContratosClientesEntities();
    }
    public List<ContratosClientes> byIdClienteAndStatus(int id, int status){
        List<ContratosClientes> list=new ArrayList();
        try{
            ContratosClientesJpaController clientCCont=new ContratosClientesJpaController();
            return clientCCont.getEntityManager().createQuery("SELECT e FROM ContratosClientes e WHERE e.idCliente.id = :idCliente AND e.status = :status", ContratosClientes.class).setParameter("idCliente", id).setParameter("status", status).getResultList();
        }catch(Exception e){
            System.out.println("Error de busqueda byIdClienteAndStatus with id "+id);
            return list;
        }
    }
}
