/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author meli_
 */
public class emailValidator implements Validator{

    @Override
    public void validate(FacesContext fc, UIComponent uic, Object t) throws ValidatorException {
        /*context = FacesContext.getCurrentInstance();
        String text=(String) t;
        System.out.println("Correo: "+userNew.getEmail()+" "+text);
        if (users.findByEmail(text)==null) {
            ((UIInput)toValidate).setValid(true);
        }else{
            ((UIInput)toValidate).setValid(false);
            context.addMessage(toValidate.getClientId(context), new FacesMessage("Correo no valido, correo ya registrado"));
        }*/
    }
    
}
