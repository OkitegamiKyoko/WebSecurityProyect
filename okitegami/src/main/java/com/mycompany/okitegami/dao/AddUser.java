/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

import com.mycompany.okitegami.controllers.ClientesJpaController;
import com.mycompany.okitegami.controllers.DepartamentosJpaController;
import com.mycompany.okitegami.controllers.DireccionesJpaController;
import com.mycompany.okitegami.controllers.EstadosJpaController;
import com.mycompany.okitegami.controllers.LocalidadesJpaController;
import com.mycompany.okitegami.controllers.MunicipiosJpaController;
import com.mycompany.okitegami.controllers.PuestosJpaController;
import com.mycompany.okitegami.controllers.RolesJpaController;
import com.mycompany.okitegami.controllers.UsersJpaController;
import com.mycompany.okitegami.models.Clientes;
import com.mycompany.okitegami.models.Departamentos;
import com.mycompany.okitegami.models.Direcciones;
import com.mycompany.okitegami.models.Empleados;
import com.mycompany.okitegami.models.Estados;
import com.mycompany.okitegami.models.Localidades;
import com.mycompany.okitegami.models.Municipios;
import com.mycompany.okitegami.models.Puestos;
import com.mycompany.okitegami.models.Roles;
import com.mycompany.okitegami.models.Users;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author meli_
 */

@Named("createUser")
@SessionScoped
@MultipartConfig(location="/tmp", fileSizeThreshold=1024*1024, 
    maxFileSize=1024*1024*5, maxRequestSize=1024*1024*5*5)
public class AddUser implements Serializable {
    
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
    ClientesJpaController clientCont;
    @Inject
    DireccionesJpaController directCont;
    Empleados emp;
    Clientes client;
    Users userNew = new Users();
    String passwordConfirm;
    Part imageFile;
    Dates date=new Dates();
    Departamentos dep = new Departamentos();
    Direcciones direct=new Direcciones();
    Puestos job = new Puestos();
    List<Puestos> listJobs;
    List<Estados> listEstados;
    List<Municipios> listMunicipio;
    List<Localidades> listLocalidades;
    Estados estado=new Estados();
    Municipios municipio=new Municipios();
    Localidades localidad=new Localidades();
    String cp;
    int type;
    UploadedFile file;
    
    public Users getUserNew() {
        return userNew;
    }

    public Empleados getEmp() {
        return emp;
    }

    public Departamentos getDep() {
        return dep;
    }

    public Puestos getJob() {
        return job;
    }

    public List<Puestos> getListJobs() {
        return listJobs;
    }

    public int getType() {
        return type;
    }

    public Clientes getClient() {
        if (client==null) {
            client=new Clientes();
        }
        return client;
    }

    public void setEmp(Empleados emp) {
        this.emp = emp;
    }
    
    public void setUserNew(Users userNew) {
        this.userNew = userNew;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public String getCp() {
        return cp;
    }

    public Estados getEstado() {
        return estado;
    }

    public Municipios getMunicipio() {
        return municipio;
    }

    public Localidades getLocalidad() {
        return localidad;
    }

    public Direcciones getDirect() {
        return direct;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
    
    public Part getImageFile() {
        return imageFile;
    }

    public List<Estados> getListEstados() {
        return listEstados;
    }

    public List<Municipios> getListMunicipio() {
        return listMunicipio;
    }

    public List<Localidades> getListLocalidades() {
        return listLocalidades;
    }

    public void setImageFile(Part imageFile) {
        this.imageFile = imageFile;
    }

    public void setDep(Departamentos dep) {
        this.dep = dep;
    }

    public void setJob(Puestos job) {
        this.job = job;
    }

    public void setListJobs(List<Puestos> listJobs) {
        this.listJobs = listJobs;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setClient(Clientes client) {
        this.client = client;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    public void setMunicipio(Municipios municipio) {
        this.municipio = municipio;
    }

    public void setLocalidad(Localidades localidad) {
        this.localidad = localidad;
    }

    public void setListEstados(List<Estados> listEstados) {
        this.listEstados = listEstados;
    }

    public void setListMunicipio(List<Municipios> listMunicipio) {
        this.listMunicipio = listMunicipio;
    }

    public void setListLocalidades(List<Localidades> listLocalidades) {
        this.listLocalidades = listLocalidades;
    }

    public void setDirect(Direcciones direct) {
        this.direct = direct;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public String createUser(){
        userNew.setDateIngreso(date.now());
        
        userNew.setStatus(1);
        String cliente="Cliente";
        List<Roles> rol=rolCont.findRolesEntities();
        Password pass=new Password();
        for (int i = 0; i < rol.size(); i++) {
            if (rol.get(i).getRol().equals(cliente)) {
                userNew.setIdRol(rol.get(i));
                break;
            }
        }
        System.out.println("Creando");
            if (file==null) {
                userNew.setImage("default.png");
            }else{
                userNew.setImage(file.getFileName());
            }
            try{
                userNew.setPassword(pass.encrypt(userNew.getPassword()));
                users.create(userNew);
                System.out.println("Usuario creado");
                if(file!=null){
                    try{
                        uploadFile();
                        System.out.println("Imagen guardada");
                    }catch(Exception e){
                        //Durante pruebas se eliminara al usuario
                        //users.destroy(users.findByEmail(userNew.getEmail()).getId());
                        //En producción se usara este
                        userNew=users.findByEmail(userNew.getEmail());
                        userNew.setImage("default.png");
                        users.edit(userNew);
                        System.out.println("Error de guardado: "+e.toString());
                    }
                }
                try{
                    direct.setIdLocalidad(localCont.findLocalidades(localidad.getId()));
                    direct.setStatus((short)1);
                    directCont.create(direct);
                    System.out.println("Direccion creada");
                    try{
                        List<Direcciones> diList=directCont.findDireccionesEntities();
                        Direcciones di;
                        for (int i = 0; i < diList.size(); i++) {
                            di=diList.get(i);
                            if (di.getCalle().equals(direct.getCalle())&&di.getNoExt()==direct.getNoExt()&&di.getIdLocalidad().equals(direct.getIdLocalidad())) {
                                client.setIdDirect(di);
                                System.out.println("Find");
                                break;
                            }
                        }
                        if (users.findByEmail(userNew.getEmail())==null) {
                            System.out.println("No exist");
                        }
                        client.setIdUser(users.findByEmail(userNew.getEmail()));
                        System.out.println(client.getIdUser().getEmail()+" "+client.getIdDirect().getCalle()+"");
                        clientCont.create(client);
                        System.out.println("Cliente creado");
                    }catch(Exception e){
                        List<Direcciones> diList=directCont.findDireccionesEntities();
                        for (int i = 0; i < diList.size(); i++) {
                            Direcciones di=diList.get(i);
                            if (di.getCalle().equals(direct.getCalle())&&di.getNoExt()==direct.getNoExt()&&di.getIdLocalidad().equals(direct.getIdLocalidad())) {
                                directCont.destroy(di.getId());
                                break;
                            }
                        }
                        users.destroy(users.findByEmail(userNew.getEmail()).getId());
                        System.out.println("Error de creacion de cliente: "+e.toString());
                    }
                }catch(Exception e){
                    users.destroy(users.findByEmail(userNew.getEmail()).getId());
                    System.out.println("Error de creacion de direccion: "+e.toString());
                }
            }catch(Exception e){
                System.out.println("Error de creacion de usuario: "+e.toString());
            }
        return "index";
    }
    
    public String toComplite(){
        String go="continue";
        return go;
    }
    
    private void uploadFile(){
        //String fileName=getFileName();
        String fileName=file.getFileName();
        String path = "C:\\Users\\meli_\\Okitegami\\Users\\Images\\"+fileName;
        try{
            File uploads = new File(path);
            String extension="",aux="",type="";
            int j=0;
            int i=fileName.lastIndexOf(".");
            if (i>=0) {
                type=fileName.substring(i);
                extension=fileName.substring(i);
            }
            System.out.println(extension);
            while(uploads.exists()){
                aux=fileName.replaceAll(extension, "");
                aux=aux+" - "+j+extension;
                path = "C:\\Users\\meli_\\Okitegami\\Users\\Images\\"+aux;
                uploads = new File(path);
                System.out.println(path);
                j++;
            }
            userNew.setImage(aux);
            try (FileOutputStream fos = new FileOutputStream(path)) {
                InputStream is=file.getInputStream();
                byte[] data=new byte[is.available()];
                is.read(data);
                fos.write(data);
                /*File uploads = new File(path);
                Path pathFile=Paths.get(imageFile.getSubmittedFileName());
                String fileName=pathFile.getFileName().toString();
                InputStream input=imageFile.getInputStream();
                File file=new File(uploads,fileName);
                Files.copy(input, file.toPath());*/
                /*if (fileName!=null) {
                imageFile.write(path+fileName);
                }*/
            }
        }catch(IOException e){
            System.out.println("Error de guardado: "+e.toString());
            e.getStackTrace();
        }catch(Exception e){
            System.out.println("Error de archivo en sistema: "+e.toString());
            e.getStackTrace();
        }
    }
    
    private String getFileName(){
        String name;
        if (imageFile!=null) {
            for (String cd : imageFile.getHeader("content-disposition").split(";")) {
                if (cd.trim().startsWith("filename")) {
                    name=cd.substring(cd.indexOf('=')+1).trim().replace("\"", "");
                    return name.substring(name.lastIndexOf('/')+1).substring(name.lastIndexOf('\\')+1);
                }
            }
        }
        return null;
    }
    
    public void updateJobs(AjaxBehaviorEvent e){
        try{
            System.out.println("depId: "+dep.getId());
            if (dep.getId()==null) {
                dep.setId(0);
                listJobs=null;
            }else{
                dep=depart.findDepartamentos(dep.getId());
                listJobs=getDep().getPuestosList();
            }
            System.out.println("depId: "+dep.getId());
        }catch(Exception ex){
            System.out.println("Error jobs: "+ex.toString());
        }
    }
    
    public void updateMunicipios(AjaxBehaviorEvent e){
        try{
            if (estado.getId()==0) {
                estado=new Estados();
                listMunicipio=null;
                listLocalidades=null;
                cp=null;
            }else{
                estado=estCont.findEstados(estado.getId());
                listMunicipio=estado.getMunicipiosList();
                listLocalidades=null;
                cp=null;
            }
        }catch(Exception ex){
            System.out.println("Error Municipios: "+ex.toString());
        }
    }
    public void updateLocalidades(AjaxBehaviorEvent e){
        try{
            if (municipio.getId()==0) {
                municipio.setId(0);
                cp=null;
            }else{
                municipio=munCont.findMunicipios(municipio.getId());
                listLocalidades=municipio.getLocalidadesList();
                cp=null;
            }
        }catch(Exception ex){
            System.out.println("Error Localidades: "+ex.toString());
        }
    }
    public void updateCP(AjaxBehaviorEvent e){
        try{
            if (localidad.getId()==0) {
                localidad.setId(0);
                cp=null;
            }else{
                localidad=localCont.findLocalidades(localidad.getId());
                cp=""+localidad.getCp();
            }
        }catch(Exception ex){
            System.out.println("Error CP: "+ex.toString());
        }
    }
    
    String input1,input2;
    boolean set;
    
    public void validatePass(FacesContext context, UIComponent toValidate, Object value){
        //context = FacesContext.getCurrentInstance();
        //String text=(String) value;
        if (set) {
            input2=(String) value;
            if (input2.equals(input1)==false) {
                ((UIInput)toValidate).setValid(false);
                context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error contraseña: ","Contraseña no coinciden")); 
            }
        }else{
            set=true;
            input1=(String) value;
        }
        /*UIInput pas=(UIInput)toValidate.getAttributes().get("password");
        String pass=(String) pas.getValue();
        if (text.equals((String)pass)) {
            ((UIInput)toValidate).setValid(true);
        }else{
            ((UIInput)toValidate).setValid(false);
            context.addMessage(toValidate.getClientId(context), new FacesMessage("Contraseña no coinciden"));
        }*/
    }
    public void validateEmail(FacesContext context, UIComponent toValidate, Object value){
        context = FacesContext.getCurrentInstance();
        String text=(String) value;
        if (users.findByEmail(text)==null) {
            ((UIInput)toValidate).setValid(true);
        }else{
            ((UIInput)toValidate).setValid(false);
            context.addMessage(toValidate.getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error correo: ","Correo no valido, correo ya registrado"));
        }
    }
    public void validateLocalidad(FacesContext context, UIComponent toValidate, Object value){
        context = FacesContext.getCurrentInstance();
        int text=(int) value;
        if (text!=0) {
            ((UIInput)toValidate).setValid(true);
        }else{
            ((UIInput)toValidate).setValid(false);
            context.addMessage(toValidate.getClientId(context), new FacesMessage("Seleccione una localidad valida"));
        }
    }
}