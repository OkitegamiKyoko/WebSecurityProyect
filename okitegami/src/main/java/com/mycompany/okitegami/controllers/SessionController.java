  /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import com.mycompany.okitegami.models.Session;
import com.mycompany.okitegami.models.Users;
import javax.inject.Inject;

/**
 *
 * @author meli_
 */
@Named("sessionController")
@SessionScoped
public class SessionController implements Serializable {
    @Inject
    UsersJpaController users;
    private Session session;
    private Users user;

    public Session getSession() {
        if (session==null) {
            return this.session=new Session();
        }
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    
    public String validarSession(){
        this.user=users.findByEmail(session.getEmail());
        if (user!=null) {
            if (user.getPassword().equals(session.getPassword())) {
                session.setNivel(user.getStatus());                  //Cambiar status por nivel
                return "index";
            }else{
                session=null;
                return "error_401";
            }
        }else{
            user=null;
            session=null;
            return "error_403";
        }
    }
    public boolean exist(){
        if (session!=null) {
            HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            System.out.println(req.getSession().getId());
            return session.getEmail() != null;
        }else{
            return false;
        }
    }
    
    public String close(){
        HttpServletRequest req = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println(req.getSession().getId());
        req.getSession().invalidate();
        session=null;
        user=null;
        return "index";
    }
    public void clentAddress(){
        String remoteAddr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
        System.out.println(remoteAddr);
    }
    public String details(Users user){
        this.user=user;
        return "detailsUser";
    }
}
