/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

//import javax.enterprise.context.RequestScoped;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author meli_
 */
@Named("nombreCompleto")
@RequestScoped
public class nombreCompleto{
    public String nombre(String nombre,String apMat, String apPat){
       return nombre+" "+apMat+" "+apPat; 
    }
}
