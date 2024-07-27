/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers.session;

import com.mycompany.okitegami.controllers.GruposJpaController;
import com.mycompany.okitegami.controllers.UsersJpaController;
import com.mycompany.okitegami.dao.Session;
import com.mycompany.okitegami.models.Grupos;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import com.mycompany.okitegami.models.Users;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author meli_
 */
@Named("sessionController")
@SessionScoped
public class SessionCont implements Serializable {

    @Inject
    UsersJpaController users;
    @Inject
    GruposJpaController group;
    private Session session;
    private Users user;

    public SessionCont() {
        this.session = new Session();
    }

    public Session getSession() {
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

    public String validarSession() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        
        System.out.println(req.getContextPath());
        String out = "";
        try {
            req.login(session.getEmail(), session.getPassword());
            this.user = users.findByEmail(session.getEmail());
            if (user != null && user.getStatus() != 0) {
                System.out.println("Admin " + req.isUserInRole("Administradores"));
                System.out.println("Cliente " + req.isUserInRole("Clientes"));
                System.out.println("Emp " + req.isUserInRole("Empleados"));
                System.out.println("HR " + req.isUserInRole("Recursos Humanos"));
                this.user = users.findByEmail(session.getEmail());
                session.setNivel(user.getIdRol().getId());
                out = "index";
            } else {
                user = null;
                session = new Session();
                try {
                    res.sendError(HttpServletResponse.SC_FORBIDDEN);
                } catch (IOException ex) {
                    Logger.getLogger(SessionCont.class.getName()).log(Level.SEVERE, null, ex);
                }
                req.logout();
                out = "error_403";
                System.out.println("Rechazado");
            }
        } catch (ServletException ex) {
            try {
                if (!req.authenticate(res)) {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }else{
                    System.out.println("logout");
                    req.logout();
                    req.getSession().invalidate();
                    validarSession();
                }
            } catch (IOException | ServletException ex1) {
                Logger.getLogger(SessionCont.class.getName()).log(Level.SEVERE, null, ex1);
            }
            System.out.println(ex.getLocalizedMessage());
            System.out.println(session.getEmail());
            user = null;
            session = new Session();
            //req.getSession().invalidate();
            //out = "error_401";
        }
        return out;
    }

    public boolean exist() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (session != null) {
            //System.out.println(req.getSession().getId());
            return session.getEmail() != null;
        } else {
            return false;
        }
    }

    public String close() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println(req.getSession().getId());
        //req.getSession().invalidate();
        try {
            if (req.isRequestedSessionIdValid()) {
                req.getSession().invalidate();
                req.logout();
            }
        } catch (ServletException ex) {
            Logger.getLogger(SessionCont.class.getName()).log(Level.SEVERE, null, ex);
        }
        session = new Session();
        user = null;
        return "index";
    }

    public void clentAddress() {
        String remoteAddr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
        System.out.println(remoteAddr);
    }

    public String details(Users user) {
        this.user = user;
        return "detailsUser";
    }

    public List<String> groups() {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        List<String> listG = new ArrayList<>();
        List<Grupos> list = group.findGruposEntities();
        for (int i = 0; i < list.size(); i++) {
            if (req.isUserInRole(list.get(i).getNombre())) {
                listG.add(list.get(i).getNombre());
            }
        }
        return listG;
    }

    public boolean permisos(String group) {
        HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return req.isUserInRole(group);
    }
}
