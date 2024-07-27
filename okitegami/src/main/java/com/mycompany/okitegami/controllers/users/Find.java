/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers.users;

import com.mycompany.okitegami.controllers.UsersJpaController;
import com.mycompany.okitegami.dao.StatusName;
import com.mycompany.okitegami.models.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.naming.NamingException;
import org.primefaces.util.LangUtils;

/**
 *
 * @author meli_
 */
@Named("findUser")
@ApplicationScoped
public class Find implements Serializable{
    private List<Users> list;
    private List<Users> listFilter;
    private UsersJpaController uCont;
    public Find(){
        list= all();
        listFilter=list;
        try{
            uCont=new UsersJpaController();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }

    public List<Users> getList() {
        return list;
    }

    public void setList(List<Users> list) {
        this.list = list;
    }

    public List<Users> getListFilter() {
        return listFilter;
    }

    public void setListFilter(List<Users> listFilter) {
        this.listFilter = listFilter;
    }
    
    public List<Users> all(){
        try {
            uCont=new UsersJpaController();
            return uCont.findUsersEntities();
        } catch (NamingException ex) {
            Logger.getLogger(Find.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    /**
     * Desactiva al usuario en la base de datos y en la lista list de esta clase
     * @param u Users
     */
    public void desactive(Users u){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(u)) {
                Status s=new Status();
                try{
                    s.desactive(u.getId());
                    if (uCont.findUsers(u.getId()).getStatus()==0) {
                        list.get(i).setStatus(0);
                    }
                }catch(Exception e){
                    System.out.println(e.toString());
                }
            }
        }
    }
    public void active(Users u){
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(u)) {
                Status s=new Status();
                try{
                    s.active(u.getId());
                    if (uCont.findUsers(u.getId()).getStatus()==1) {
                        list.get(i).setStatus(1);
                    }
                }catch(Exception e){
                    System.out.println(e.toString());
                }
            }
        }
    }
    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        System.out.println(filterText);
        if (LangUtils.isBlank(filterText)) {
            return true;
        }
        int filterInt = getInteger(filterText);
        StatusName s=new StatusName();
        Users customer = (Users) value;
        return customer.getName().toLowerCase().contains(filterText)
                || customer.getId()==filterInt
                || customer.getEmail().toLowerCase().contains(filterText)
                || customer.getName().toLowerCase().contains(filterText)
                || s.status(customer.getStatus()).toLowerCase().contains(filterText);
    }
    private int getInteger(String string) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException ex) {
            return 0;
        }
    }
    public void conf(){
        System.out.println("hola");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario eliminado:", "Usuario eliminado con exito"));
    }
}
