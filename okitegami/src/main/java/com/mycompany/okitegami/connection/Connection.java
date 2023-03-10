/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.connection;

/**
 *
 * @author meli_
 */
public class Connection {
    private Connection conect;
    private final String user = "admin",password="okitegami",db="okitegami",host="localhost",port="5432",cadena="jdbc:postgresql://"+host+":"+port+"/"+db;
    public Connection(){
        try{
            
        }catch(Exception e){
            System.out.println("Error en la coneccion con la base de datos, error: "+e.toString());
        }
    }
    public void closeConnection(){
        
    }
}
