/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import modelos.Departamentos;

/**
 *
 * @author meli_
 */
@Stateless
public class DepartmentFacade extends AbstractFacade<Departamentos> {

    @PersistenceContext(unitName = "com.mycompany_okitegami_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DepartmentFacade() {
        super(Departamentos.class);
    }
    
    public Departamentos findByNombre(String nombre){
        try{
            return getEntityManager().createNamedQuery("Departments.findByName",Departamentos.class).setParameter("nombre",nombre).getSingleResult();
        }catch(Exception e){
            System.out.println("Error: "+e.toString());
            return null;
        }
    }
}
