/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers.employees;

import com.mycompany.okitegami.controllers.EmpleadosJpaController;
import com.mycompany.okitegami.models.Empleados;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author meli_
 */
@Named("editEmp")
@ApplicationScoped
public class EditEmployee {
    @Inject EmpleadosJpaController empCont;
    public void desactive(Empleados emp){
        
    }
}
