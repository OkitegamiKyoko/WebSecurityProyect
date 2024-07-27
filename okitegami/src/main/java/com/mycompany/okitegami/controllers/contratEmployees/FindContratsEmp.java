/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers.contratEmployees;

import com.mycompany.okitegami.controllers.ContratosEmpleadosJpaController;
import com.mycompany.okitegami.models.ContratosEmpleados;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 *
 * @author meli_
 */
public class FindContratsEmp {
    private ContratosEmpleadosJpaController empCCont;
    
    public FindContratsEmp() {
        try {
            this.empCCont=new ContratosEmpleadosJpaController();
        } catch (NamingException ex) {
            Logger.getLogger(FindContratsEmp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<ContratosEmpleados> all(){
        return empCCont.findContratosEmpleadosEntities();
    }
    public List<ContratosEmpleados> byIdEmployee(int id){
        try{
            return empCCont.getEntityManager().createQuery("SELECT ce FROM ContratosEmpleados ce WHERE ce.idEmpleado.id = :idEmpleado", ContratosEmpleados.class).setParameter("idEmpleado", id).getResultList();
        }catch(Exception e){
            System.out.println(e.toString());
            System.out.println("Error byIdEmployee FindContratsEmp");
            return null;
        }
    }
    public ContratosEmpleados byIdEmployeeAndActive(int id){
        try{
            return empCCont.getEntityManager().createQuery("SELECT ce FROM ContratosEmpleados ce WHERE ce.idEmpleado.id = :idEmpleado AND ce.status = 1", ContratosEmpleados.class).setParameter("idEmpleado", id).getSingleResult();
        }catch(Exception e){
            System.out.println(e.toString());
            System.out.println("Error byIdEmployee FindContratsEmp");
            return null;
        }
    }
}
