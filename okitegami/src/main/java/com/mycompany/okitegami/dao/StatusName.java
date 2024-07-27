/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author meli_
 */
@Named("status")
@RequestScoped
public class StatusName {
    public String status(int status){
        String s="";
        switch (status){
                case 0:
                    s="Inactivo";
                    break;
                case 1:
                    s="Activo";
                    break;
                case 2:
                    s="Asignado";
                    break;
        }
        return s;
    }
}
