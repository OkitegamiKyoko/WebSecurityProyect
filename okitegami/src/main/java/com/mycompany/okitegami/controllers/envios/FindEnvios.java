/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers.envios;

import com.mycompany.okitegami.controllers.EnviosJpaController;
import com.mycompany.okitegami.models.Envios;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author meli_
 */
public class FindEnvios {
    @Inject EnviosJpaController envCont;
    public List<Envios> all(){
        return envCont.findEnviosEntities();
    }
    public Envios byId(int id){
        return envCont.findEnvios(id);
    }
    public List<Envios> byIdContrat(int id){
        EntityManager em=envCont.getEntityManager();
        try{
            List<Envios> env=new ArrayList();
            return env=em.createNamedQuery("Envios.findByContrato", Envios.class).setParameter("idContrato", id).getResultList();
        }catch(Exception e){
            return null;
        }
    }
    public List<Envios> byIdEmployee(int id){
        EntityManager em=envCont.getEntityManager();
        try{
            List<Envios> env=new ArrayList();
            return env=em.createNamedQuery("Envios.findByEmpleado", Envios.class).setParameter("idEmpleado", id).getResultList();
        }catch(Exception e){
            return null;
        }
    }
    public Envios byIdEmployeeAndStatus(int id, int status){
        try{
            EnviosJpaController envCont=new EnviosJpaController();
            EntityManager em=envCont.getEntityManager();
            return em.createQuery("SELECT e FROM Envios e WHERE e.idEmpleado.id = :idEmpleado and e.idContrato.status = :status", Envios.class).setParameter("idEmpleado", id).setParameter("status", status).getSingleResult();
        }catch(Exception e){
            return null;
        }
    }
}
