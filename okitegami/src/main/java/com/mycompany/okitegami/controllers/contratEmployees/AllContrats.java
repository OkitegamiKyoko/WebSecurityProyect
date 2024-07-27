/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers.contratEmployees;

import com.mycompany.okitegami.controllers.ContratosEmpleadosJpaController;
import com.mycompany.okitegami.models.ContratosEmpleados;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author meli_
 */
@Named("contratsEmp")
@ApplicationScoped
public class AllContrats {
    @Inject ContratosEmpleadosJpaController empContraCont;
    public List<ContratosEmpleados> all(){
        return empContraCont.findContratosEmpleadosEntities();
    }
}
