  /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import modelos.Session;
import modelos.Users;

/**
 *
 * @author meli_
 */
@Named("sessionController")
@SessionScoped
public class SessionController extends UserController implements Serializable {
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
    public String validarSession(){
        this.user=findByEmail(session.getEmail());
        if (user!=null) {
            if (user.getPassword().equals(session.getPassword())) {
                session.setNivel(user.getStatus());                  //Cambiar status por nivel
                return "index";
            }else{
                return "error_401";
            }
        }else{
            session=null;
            return "error_403";
        }
    }
    public boolean exist(){
        if (session!=null) {
            return session.getEmail() != null;
        }else{
            return false;
        }
    }
    
    public String close(){
        session=null;
        user=null;
        return "index";
    }
}
