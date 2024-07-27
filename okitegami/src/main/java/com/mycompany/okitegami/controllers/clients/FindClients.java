/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers.clients;

import com.mycompany.okitegami.controllers.ClientesJpaController;
import com.mycompany.okitegami.models.Clientes;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

/**
 *
 * @author meli_
 */
@Named("findClients")
public class FindClients {
    @Inject ClientesJpaController clientCont;
    public List<Clientes> all(){
        return clientCont.findClientesEntities();
    }
    public Clientes byIdUser(int id){
        try{
            ClientesJpaController clientCont=new ClientesJpaController();
            Query q=clientCont.getEntityManager().createQuery("SELECT c FROM Clientes c WHERE c.idUser.id = :idUser", Clientes.class);
            q.setParameter("idUser", id);
            return (Clientes) q.getSingleResult();
        }catch(Exception e){
            System.out.println(e.toString());
            System.out.println("Error de busqueda byIdUser FindClients with id: "+id);
            //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error:",e.getMessage()));
            return null;
        }
    }
}
