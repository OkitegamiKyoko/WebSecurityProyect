/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dao.DepartmentFacade;
import dao.UserFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
//import javax.enterprise.context.RequestScoped;
//import javax.enterprise.context.SessionScoped;
//import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modelos.Departamentos;
import modelos.Users;

/**
 *
 * @author meli_
 */
@Named("departmentController")
@ApplicationScoped
public class DepartmentsController implements Serializable {
    public static final long serialVersionUID=1L;
    private List<Departamentos> list;
    private Departamentos department;
    private Departamentos departmentNew;
    @EJB
    private DepartmentFacade uf;
    
    public List<Departamentos> getRead(){
        list=this.uf.findAll();
        System.out.println(list.size());
        return list;
    }
    
    public boolean isPopulated(){
        return !list.isEmpty();
    }
    
    public void create(){
        this.uf.create(departmentNew);
        //this.user =new Users();
    }

    public void setUser(Departamentos department) {
        this.department = department;
    }

    public void setUserNew(Departamentos departmentNew) {
        this.departmentNew = departmentNew;
    }
    
    public Departamentos getDepartmentNew() {
        return departmentNew;
    }
    
    public Departamentos getDepartment() {
        if (department==null) {
            return this.department=new Departamentos();
        }
        return department;
    }
    
     public void setUserEdit(Departamentos department) {
        this.department = department;
    }
     
    public void delete(Departamentos department){
        department.setStatus(0);
        uf.edit(department);
    }
    public void reactive(Departamentos department){
        department.setStatus(1);
        uf.edit(department);
    }
    
    public void desactive(Departamentos department){
        department.setStatus(0);
        uf.edit(department);
    }
    
    public int count(){
        return this.uf.count();
    }
    public Departamentos find(Departamentos department){
        return this.department=this.uf.find(department);
    }
    
    public Departamentos findByNombre(String nombre){
        return this.uf.findByNombre(nombre);
    }
    
    public String edit(Departamentos department){
        this.department=department;
        return "edit";
    }
    
    public String actualizar(Departamentos department){
        this.uf.edit(department);
        return "departments";
    }
    
    public boolean departmentStatus(int status){
        System.out.println("Status: "+status);
        return status==1;
    }
}
