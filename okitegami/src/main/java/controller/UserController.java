/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.UserFacade;
//import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
//import javax.enterprise.context.Dependent;
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
    private List<Users> list;
    private Users user,userNew,userDetails;
    private String passwordConfirm;
    private String image;
    @EJB
    private UserFacade uf;
    
    public List<Users> getRead(){
        list=this.uf.findAll();
        return list;
    }
    
    public boolean isPopulated(){
        return !list.isEmpty();
    }
    
    public void create(){
        try{
            this.uf.create(userNew);
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }
        this.userNew =new Users();
        //this.user =new Users();
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setUserNew(Users userNew) {
        this.userNew = userNew;
    }
    
    public Users getUserNew() {
        if (userNew==null) {
            return this.userNew=new Users();
        }
        userNew.setDate_ingreso(null);
        return userNew;
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
    public void reactive(Users user){
        user.setStatus(1);
        uf.edit(user);
    }
    
    public void desactive(Users user){
        user.setStatus(0);
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
        passwordConfirm=user.getPassword();
        this.user=user;
        return "userEdit";
    }
    
    public String actualizar(Users user){
        this.uf.edit(user);
        return "users";
    }
    
    public boolean userStatus(int status){
        return status==1;
    }
    
    public boolean validarName(){
        return userNew.getName()!=null;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
    
    public boolean validarPasword(){
        return passwordConfirm.equals(userNew.getPassword());
    }
    
    public boolean validarNewUser(){
        getUserNew();
        return userNew.getDate_ingreso()!=null & userNew.getName()!=null & userNew.getEmail()!=null & userNew.getPassword()!=null & !userNew.getName().isEmpty() & !userNew.getEmail().isEmpty() & !userNew.getPassword().isEmpty();
    }

    public String getImage() {
        if (image==null) {
            return this.image="http://localhost:8080/okitegami/vlcsnap-2023-03-11-14h00m51s589.png";
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Users getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(Users userDetails) {
        this.userDetails = userDetails;
    }
    
    public String details(Users user){
        this.user=user;
        System.out.println(userNew.toString());
        return "details";
    }
}
