/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers.departments;

import com.mycompany.okitegami.controllers.DepartamentosJpaController;
import com.mycompany.okitegami.models.Departamentos;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author meli_
 */
@Named("addDepart")
@ApplicationScoped
public class AddDepartment {
    @Inject
    DepartamentosJpaController depCont;
    Departamentos dep;
    
    
}
