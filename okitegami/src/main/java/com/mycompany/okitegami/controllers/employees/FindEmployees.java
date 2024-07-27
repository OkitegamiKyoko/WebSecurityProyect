/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers.employees;

import com.mycompany.okitegami.controllers.EmpleadosJpaController;
import com.mycompany.okitegami.models.Empleados;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;

/**
 *
 * @author meli_
 */
public class FindEmployees {
    private EmpleadosJpaController empCont;

    public FindEmployees() {
        try {
            empCont= new EmpleadosJpaController();
        } catch (NamingException ex) {
            System.out.println(ex.toString());
        }
    }
    public Empleados byId(int id){
        try{
            return empCont.findEmpleados(id);
        }catch(Exception e){
            System.out.println(e.toString());
            System.out.println("Error byId en FindEmployees");
            return null;
        }
    }
    
    public Empleados byIdUser(int id){
        try{
            return empCont.getEntityManager().createQuery("SELECT e FROM Empleados e WHERE e.idUser.id = :idUser", Empleados.class).setParameter("idUser", id).getSingleResult();
        }catch(Exception e){
            System.out.println(e.toString());
            System.out.println("Error byIdUser en FindEmployees");
            return null;
        }
    }
}
