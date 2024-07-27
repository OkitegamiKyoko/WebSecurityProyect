package com.mycompany.okitegami.dao;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author meli_
 */
public class Session{
    
    private String email,password;
    private int nivel;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getNivel() {
        return nivel;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
    
    
}
