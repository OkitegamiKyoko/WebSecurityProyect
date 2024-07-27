/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers.employees;

import com.mycompany.okitegami.controllers.ClientesJpaController;
import com.mycompany.okitegami.controllers.DepartamentosJpaController;
import com.mycompany.okitegami.controllers.DireccionesJpaController;
import com.mycompany.okitegami.controllers.EmpleadosJpaController;
import com.mycompany.okitegami.controllers.EstadosJpaController;
import com.mycompany.okitegami.controllers.GruposJpaController;
import com.mycompany.okitegami.controllers.LocalidadesJpaController;
import com.mycompany.okitegami.controllers.MunicipiosJpaController;
import com.mycompany.okitegami.controllers.PuestosJpaController;
import com.mycompany.okitegami.controllers.RolesJpaController;
import com.mycompany.okitegami.controllers.UsersJpaController;
import com.mycompany.okitegami.dao.Dates;
import com.mycompany.okitegami.dao.Password;
import com.mycompany.okitegami.models.Clientes;
import com.mycompany.okitegami.models.Departamentos;
import com.mycompany.okitegami.models.Direcciones;
import com.mycompany.okitegami.models.Empleados;
import com.mycompany.okitegami.models.Estados;
import com.mycompany.okitegami.models.Localidades;
import com.mycompany.okitegami.models.Municipios;
import com.mycompany.okitegami.models.Puestos;
import com.mycompany.okitegami.models.Users;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.servlet.http.Part;

/**
 *
 * @author meli_
 */
@Named("addEmployee")
@SessionScoped
public class AddEmployee implements Serializable{
    @Inject
    UsersJpaController users;
    @Inject
    DepartamentosJpaController depart;
    @Inject
    PuestosJpaController jobCont;
    @Inject
    EstadosJpaController estCont;
    @Inject
    MunicipiosJpaController munCont;
    @Inject
    LocalidadesJpaController localCont;
    @Inject
    RolesJpaController rolCont;
    @Inject
    GruposJpaController groupCont;
    @Inject
    DireccionesJpaController directCont;
    Empleados emp = new Empleados();
    Users userNew = new Users();
    String passwordConfirm;
    Part imageFile,pdfFile;
    Dates date=new Dates();
    Departamentos dep = new Departamentos();
    Direcciones direct=new Direcciones();
    Puestos job = new Puestos();
    List<Puestos> listJobs;
    List<Departamentos> listDeps;
    List<Estados> listEstados;
    List<Municipios> listMunicipio;
    List<Localidades> listLocalidades;
    Estados estado=new Estados();
    Municipios municipio=new Municipios();
    Localidades localidad=new Localidades();
    String cp;
    int type;

    public Empleados getEmp() {
        return emp;
    }

    public void setEmp(Empleados emp) {
        this.emp = emp;
    }

    public Users getUserNew() {
        return userNew;
    }

    public void setUserNew(Users userNew) {
        this.userNew = userNew;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public Part getImageFile() {
        return imageFile;
    }

    public void setImageFile(Part imageFile) {
        this.imageFile = imageFile;
    }

    public Part getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(Part pdfFile) {
        this.pdfFile = pdfFile;
    }

    public Dates getDate() {
        return date;
    }

    public void setDate(Dates date) {
        this.date = date;
    }

    public Departamentos getDep() {
        return dep;
    }

    public void setDep(Departamentos dep) {
        this.dep = dep;
    }

    public Direcciones getDirect() {
        return direct;
    }

    public void setDirect(Direcciones direct) {
        this.direct = direct;
    }

    public Puestos getJob() {
        return job;
    }

    public void setJob(Puestos job) {
        this.job = job;
    }

    public List<Puestos> getListJobs() {
        return listJobs;
    }

    public void setListJobs(List<Puestos> listJobs) {
        this.listJobs = listJobs;
    }

    public List<Departamentos> getListDeps() {
        return listDeps;
    }

    public void setListDeps(List<Departamentos> listDeps) {
        this.listDeps = listDeps;
    }

    public List<Estados> getListEstados() {
        return listEstados;
    }

    public void setListEstados(List<Estados> listEstados) {
        this.listEstados = listEstados;
    }

    public List<Municipios> getListMunicipio() {
        return listMunicipio;
    }

    public void setListMunicipio(List<Municipios> listMunicipio) {
        this.listMunicipio = listMunicipio;
    }

    public List<Localidades> getListLocalidades() {
        return listLocalidades;
    }

    public void setListLocalidades(List<Localidades> listLocalidades) {
        this.listLocalidades = listLocalidades;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    public Municipios getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipios municipio) {
        this.municipio = municipio;
    }

    public Localidades getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidades localidad) {
        this.localidad = localidad;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public void addEmployee(){
        /*try {
            EmpleadosJpaController empCont=new EmpleadosJpaController();
            UsersJpaController userCont=new UsersJpaController();
            Password pass=new Password();
            Users user=new Users();
            user.setEmail();
            user.setPassword();
            user.setStatus(1);
            userCont.create(user);
            
        } catch (NamingException ex) {
            Logger.getLogger(AddEmployee.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de rregistro", "Error al registrar empleado"));
        }*/
        
    }
}
