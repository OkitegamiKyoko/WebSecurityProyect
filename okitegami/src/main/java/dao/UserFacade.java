/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modelos.Users;

/**
 *
 * @author meli_
 */
@Stateless
public class UserFacade extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "com.mycompany_okitegami_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFacade() {
        super(Users.class);
    }
    
    public Users findByEmail(String email){
        try{
            return getEntityManager().createNamedQuery("Users.findByEmail",Users.class).setParameter("email",email).getSingleResult();
        }catch(Exception e){
            System.out.println("Error: "+e.toString());
            return null;
        }
    }
    
}
