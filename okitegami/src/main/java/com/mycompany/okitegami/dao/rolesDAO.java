/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

import com.mycompany.okitegami.controllers.RolesJpaController;
import com.mycompany.okitegami.models.Roles;
import java.util.List;
import javax.inject.Named;
import javax.naming.NamingException;

/**
 *
 * @author meli_
 */
@Named
public class rolesDAO{
    
    private final RolesJpaController userJpa;
    
    public rolesDAO() throws NamingException{
        this.userJpa = new RolesJpaController();
    }
    
    public List<Roles> allRoles(){
        return userJpa.findRolesEntities();
    }
}
