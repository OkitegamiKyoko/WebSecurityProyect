/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers.employees;

import com.mycompany.okitegami.controllers.UsersJpaController;
import com.mycompany.okitegami.models.Empleados;
import com.mycompany.okitegami.models.Users;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author meli_
 */
@Named("detailsEmployee")
public class DetailsEmployee {
    Users user;
    @Inject UsersJpaController userCont;
    Empleados emp;

    public Empleados getEmp() {
        return emp;
    }

    public void setEmp(Empleados emp) {
        this.emp = emp;
    }
    
    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
