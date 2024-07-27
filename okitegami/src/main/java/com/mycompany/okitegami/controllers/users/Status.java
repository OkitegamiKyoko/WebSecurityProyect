/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers.users;

import com.mycompany.okitegami.controllers.ClientesJpaController;
import com.mycompany.okitegami.controllers.ContratosClientesJpaController;
import com.mycompany.okitegami.controllers.ContratosEmpleadosJpaController;
import com.mycompany.okitegami.controllers.EmpleadosJpaController;
import com.mycompany.okitegami.controllers.EnviosJpaController;
import com.mycompany.okitegami.controllers.UsersJpaController;
import com.mycompany.okitegami.controllers.clients.FindClients;
import com.mycompany.okitegami.controllers.contratClients.FindContrats;
import com.mycompany.okitegami.controllers.contratEmployees.FindContratsEmp;
import com.mycompany.okitegami.controllers.employees.FindEmployees;
import com.mycompany.okitegami.controllers.envios.FindEnvios;
import com.mycompany.okitegami.controllers.exceptions.RollbackFailureException;
import com.mycompany.okitegami.dao.Dates;
import com.mycompany.okitegami.models.Clientes;
import com.mycompany.okitegami.models.ContratosClientes;
import com.mycompany.okitegami.models.ContratosEmpleados;
import com.mycompany.okitegami.models.Empleados;
import com.mycompany.okitegami.models.Envios;
import com.mycompany.okitegami.models.Users;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author meli_
 * @author Okitegami
 */
@Named("statusUser")
public class Status {

    @Inject
    UsersJpaController userCont;
    @Inject
    EmpleadosJpaController empCont;
    @Inject
    ClientesJpaController clientCont;
    @Inject
    ContratosEmpleadosJpaController empCCont;
    @Inject
    ContratosClientesJpaController clientCCont;

    /**
     * Mediante el id(usuario) verifica si es de un cliente o de un empleado y
     * realiza la desactivacion del mismo llamando a el metodo userStatus de
     * esta clase
     *
     * @param id int
     */
    public void desactive(int id) {
        FindEnvios envF = new FindEnvios();
        Empleados emp;
        Clientes client;
        try {
            empCont = new EmpleadosJpaController();
            FindEmployees fEmp = new FindEmployees();
            emp = fEmp.byIdUser(id);
            if (emp != null) {
                FindEnvios fe = new FindEnvios();
                Envios env = fe.byIdEmployeeAndStatus(emp.getId(), 1);
                System.out.println("env " + env);
                if (env != null) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Error al desactivar usuario, se encuentra en envio"));
                } else {
                    ContratosEmpleadosJpaController empCCont = new ContratosEmpleadosJpaController();
                    FindContratsEmp fce = new FindContratsEmp();
                    ContratosEmpleados cont = fce.byIdEmployeeAndActive(emp.getId());
                    cont.setStatus(0);
                    Dates date = new Dates();
                    cont.setFin(date.now());
                    empCCont.edit(cont);
                    userStatus(id, 0);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario eliminado:", "Usuario eliminado con exito"));
                }
                System.out.println("Valida empleado");
            } else {
                FindClients fcl = new FindClients();
                FindContrats fc = new FindContrats();
                client = fcl.byIdUser(id);
                List<ContratosClientes> listClient = fc.byIdClienteAndStatus(client.getId(), 1);
                System.out.println("Valida cliente");
                if (listClient.isEmpty() || listClient == null) {
                    userStatus(id, 0);
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario eliminado:", "Usuario eliminado con exito"));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al desactivar usuario", "Ususario con contratos activos"));
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al desactivar usuario", ex.toString()));
        }
    }

    public void active(int id) {
        Empleados emp;
        Clientes client;
        try {
            FindClients fcl = new FindClients();
            FindContrats fc = new FindContrats();
            client = fcl.byIdUser(id);
            UsersJpaController userCont = new UsersJpaController();
            if (client != null) {
                Users user = userCont.findUsers(id);
                user.setStatus(1);
                userCont.edit(user);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario recativado:", "Usuario recativado con exito"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Error:", "Usuario empleado, recontratar para reactivar"));
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public void userStatus(int id, int status) {
        try {
            System.out.println("id user " + id);
            userCont = new UsersJpaController();
            Users user = userCont.findUsers(id);
            user.setStatus(status);
            userCont.edit(user);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
}
