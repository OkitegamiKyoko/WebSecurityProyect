/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

import com.mycompany.okitegami.controllers.DepartamentosJpaController;
import com.mycompany.okitegami.models.Departamentos;
import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
//import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author meli_
 */
@Named("detailsDepart")
@ApplicationScoped
public class DetailsDepartment implements Serializable{
    
    @Inject
    DepartamentosJpaController depCont;
    
    private Departamentos dep;
    
    public DetailsDepartment(){
    }
    
    //public ()

    public Departamentos getDep() {
        return dep;
    }
    
    public void setDep(Departamentos dep) {
        this.dep = dep;
    }
    
    public String goDep(Departamentos dep) {
        this.dep = dep;
        return "details";
    }
    
}
