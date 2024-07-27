/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

import com.mycompany.okitegami.controllers.UsersJpaController;
import com.mycompany.okitegami.controllers.exceptions.RollbackFailureException;
import com.mycompany.okitegami.models.Users;
import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author meli_
 */
@Named("changePass")
@SessionScoped
public class ChangePassword implements Serializable{
    
    @Inject UsersJpaController userCont;
    Users user;
    String newPassword,oldPassword;

    public Users getUser() {
        return user;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    
    public void change(Users user){
        if (userCont.findUsers(user.getId())!=null) {
            Password pass=new Password();
            try {
                user.setPassword(pass.encrypt(newPassword));
                userCont.edit(user);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackFailureException ex) {
                Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void validatePass(FacesContext context, UIComponent toValidate, Object value){
        context = FacesContext.getCurrentInstance();
        String text=(String) value;
        if (text.equals(newPassword)) {
            ((UIInput)toValidate).setValid(true);
        }else{
            ((UIInput)toValidate).setValid(false);
        }
    }
}
