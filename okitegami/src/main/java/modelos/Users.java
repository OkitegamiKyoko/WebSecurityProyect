/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelos;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;

/**
 *
 * @author meli_
 */
@Entity
@Table (name="users")
@NamedQueries(value={
    @NamedQuery(name="Users.findByEmail",query="SELECT u FROM Users u WHERE u.email = :email")})
public class Users implements Serializable{
    
    public static final long serialVersionUID=1L;
    
    @Id
    @GeneratedValue (strategy=GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    private int id;
    @Column(nullable=false)
    private String name,password;
    @Email(message="Correo no valido")
    @Column(unique=true, nullable=false)
    private String email;
    private int status=1;
    @Column(nullable=false)
    @Temporal(TemporalType.DATE)
    private Date date_ingreso = new Date();
    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getStatus() {
        return status;
    }

    public Date getDate_ingreso() {
        return date_ingreso;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDate_ingreso(Date date_ingreso) {
        try{
            this.date_ingreso = date_ingreso;
        }catch(Exception e){
            System.out.println("Error: "+e.getMessage());
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Users other = (Users) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Users{" + "id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", status=" + status + ", date_ingreso=" + date_ingreso + '}';
    }
    public void datos(){
        /*Calendar c = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fecha=sdf.format(date_ingreso);
        c.get(Calendar.DAY_OF_MONTH);
        System.out.println("[31mfecha= "+fecha);*/
        System.out.println(toString());
    }
    public String convertirFechas(){
        Calendar c = new GregorianCalendar();
        c.setTime(date_ingreso);
        String date_ingreso;
        return date_ingreso=""+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR);
    }
}