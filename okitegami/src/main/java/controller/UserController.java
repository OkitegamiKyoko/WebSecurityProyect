/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UserFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
//import javax.enterprise.context.RequestScoped;
//import javax.enterprise.context.SessionScoped;
//import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modelos.Users;

/**
 *
 * @author meli_
 */
@Named("userController")
@ApplicationScoped
public class UserController implements Serializable {
    public static final long serialVersionUID=1L;
    private Users user, userEdit;
    @EJB
    private UserFacade uf;
    
    public List<Users> getRead(){
        return this.uf.findAll();
    }
    
    public void create(){
        this.uf.create(user);
        this.user =new Users();
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Users getUser() {
        if (user==null) {
            return this.user=new Users();
        }
        return user;
    }
    
     public void setUserEdit(Users user) {
        this.user = user;
    }

    public Users getUserEdit() {
        return user;
    }
    public void delete(Users user){
        user.setStatus(0);
        uf.edit(user);
    }
    public void active(Users user){
        user.setStatus(1);
        uf.edit(user);
    }
    public int count(){
        return this.uf.count();
    }
    public Users find(Users user){
        return this.user=this.uf.find(user);
    }
    
    public Users findByEmail(String email){
        return this.uf.findByEmail(email);
    }
    
    public String edit(Users user){
        this.user=user;
        return "userEdit";
    }
}
