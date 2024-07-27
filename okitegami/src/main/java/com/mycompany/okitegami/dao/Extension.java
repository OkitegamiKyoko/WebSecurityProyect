/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.servlet.http.Part;

/**
 *
 * @author meli_
 */
@Named("validarFiles")
public class Extension {
    public void validarImage(FacesContext context, UIComponent toValidate, Object value){
        try{
            Part image=(Part) value;
            String fileName=getFileName(image),extension="",type="";
            int j=0;
            int i=fileName.lastIndexOf(".");
            if (i>=0) {
                type=fileName.substring(i);
                extension=fileName.substring(i);
            }
            if(!extension.equals(".png")){
                ((UIInput)toValidate).setValid(false);
                context.addMessage(toValidate.getClientId(context), new FacesMessage("Error de formato:","Archivo no valido")); 
            }
        }catch(Exception ex){
            System.out.println("Error de archivo: "+ex.toString());
        }
    }
    
    public void validarPDF(FacesContext context, UIComponent toValidate, Object value){
        try{
            Part image=(Part) value;
            String fileName=getFileName(image),extension="",type="";
            int j=0;
            int i=fileName.lastIndexOf(".");
            if (i>=0) {
                type=fileName.substring(i);
                extension=fileName.substring(i);
            }
            if(!extension.equals(".pdf")){
                ((UIInput)toValidate).setValid(false);
                context.addMessage(toValidate.getClientId(context), new FacesMessage("Archivo no valido")); 
            }
        }catch(Exception ex){
            System.out.println("Error archivo: "+ex.toString());
        }
    }
    
    private String getFileName(Part imageFile){
        String name;
        if (imageFile!=null) {
            for (String cd : imageFile.getHeader("content-disposition").split(";")) {
                if (cd.trim().startsWith("filename")) {
                    name=cd.substring(cd.indexOf('=')+1).trim().replace("\"", "");
                    return name.substring(name.lastIndexOf('/')+1).substring(name.lastIndexOf('\\')+1);
                }
            }
        }
        return null;
    }
}
