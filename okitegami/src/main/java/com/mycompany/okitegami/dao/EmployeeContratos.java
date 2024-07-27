/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

import com.mycompany.okitegami.controllers.ContratosEmpleadosJpaController;
import com.mycompany.okitegami.controllers.EnviosJpaController;
import com.mycompany.okitegami.controllers.UsersJpaController;
import com.mycompany.okitegami.models.ContratosEmpleados;
import com.mycompany.okitegami.models.Empleados;
import com.mycompany.okitegami.models.Envios;
import com.mycompany.okitegami.models.Users;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author meli_
 */
@Named("detailsContratEmp")
@SessionScoped
public class EmployeeContratos implements Serializable{
    @Inject ContratosEmpleadosJpaController empCont;
    @Inject EnviosJpaController enviosCont;
    @Inject UsersJpaController userCont;
    ContratosEmpleados cont;
    Envios envio;

    public ContratosEmpleados getCont() {
        return cont;
    }

    public Envios getEnvio() {
        return envio;
    }

    public void setCont(ContratosEmpleados cont) {
        this.cont = cont;
    }

    public void setEnvio(Envios envio) {
        this.envio = envio;
    }
    
    public String verContratos(Empleados emp, Users user){
        String go="detailsContEmp";
        if (user!=null) {
            emp=encontrarEmpleado(user);
        }
        if (emp!=null) {
            List<ContratosEmpleados> listCont=empCont.findContratosEmpleadosEntities();
            for (int i = 0; i < listCont.size(); i++) {
                if (listCont.get(i).getIdEmpleado().equals(emp)) {
                    cont=new ContratosEmpleados();
                    this.cont=listCont.get(i);
                    break;
                }
            }
            List<Envios> listEnv=enviosCont.findEnviosEntities();
            for (int i = 0; i < listEnv.size(); i++) {
                if (listEnv.get(i).getIdEmpleado().equals(emp)&&listEnv.get(i).getIdContrato().getStatus()==1) {
                    envio=new Envios();
                    envio=listEnv.get(i);
                    System.out.println(envio.getIdContrato().getFin());
                    break;
                }
            }
            go="detailsContEmp";
        }
        return go;
    }
    public Empleados encontrarEmpleado(Users user){
        /*List<Empleados> list=user.getEmpleadosList();
        Empleados emp=new Empleados();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getIdUser().equals(user)) {
                emp=list.get(i);
            }
        }
        return emp;*/
        return user.getEmpleados();
    }
    public String verEnvio(Empleados emp, Users user){
        String go="detailsEnvio";
        if (user!=null) {
            emp=encontrarEmpleado(user);
        }
        if (emp!=null) {
            List<Envios> listEnv=enviosCont.findEnviosEntities();
            for (int i = 0; i < listEnv.size(); i++) {
                if (listEnv.get(i).getIdEmpleado().equals(emp)&&listEnv.get(i).getIdContrato().getStatus()==1) {
                    envio=new Envios();
                    envio=listEnv.get(i);
                    System.out.println(envio.getIdContrato().getFin());
                    break;
                }
            }
            go="detailsEnvio";
        }
        return go;
    }
}
