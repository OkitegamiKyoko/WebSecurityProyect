<<<<<<< HEAD
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author meli_
 */
@Entity
@Table(name = "empleados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empleados.findAll", query = "SELECT e FROM Empleados e"),
    @NamedQuery(name = "Empleados.findById", query = "SELECT e FROM Empleados e WHERE e.id = :id"),
    @NamedQuery(name = "Empleados.findByNombre", query = "SELECT e FROM Empleados e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Empleados.findByApMat", query = "SELECT e FROM Empleados e WHERE e.apMat = :apMat"),
    @NamedQuery(name = "Empleados.findByApPat", query = "SELECT e FROM Empleados e WHERE e.apPat = :apPat"),
    @NamedQuery(name = "Empleados.findByNacimiento", query = "SELECT e FROM Empleados e WHERE e.nacimiento = :nacimiento"),
    @NamedQuery(name = "Empleados.findByCurp", query = "SELECT e FROM Empleados e WHERE e.curp = :curp"),
    @NamedQuery(name = "Empleados.findByTelefono", query = "SELECT e FROM Empleados e WHERE e.telefono = :telefono"),
    @NamedQuery(name = "Empleados.findByTelEmerg", query = "SELECT e FROM Empleados e WHERE e.telEmerg = :telEmerg"),
    @NamedQuery(name = "Empleados.findByRfc", query = "SELECT e FROM Empleados e WHERE e.rfc = :rfc")})
public class Empleados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ap_mat")
    private String apMat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ap_pat")
    private String apPat;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nacimiento")
    @Temporal(TemporalType.DATE)
    private Date nacimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "curp")
    private String curp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "telefono")
    private long telefono;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tel_emerg")
    private long telEmerg;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "rfc")
    private String rfc;
    @ManyToMany(mappedBy = "empleadosList")
    private List<Bonos> bonosList;
    @JoinColumn(name = "id_cv", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cv idCv;
    @JoinColumn(name = "id_direct", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Direcciones idDirect;
    @JoinColumn(name = "id_puesto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Puestos idPuesto;
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Users idUser;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleado")
    private List<ContratosEmpleados> contratosEmpleadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleado")
    private List<Envios> enviosList;

    public Empleados() {
    }

    public Empleados(Integer id) {
        this.id = id;
    }

    public Empleados(Integer id, String nombre, String apMat, String apPat, Date nacimiento, String curp, long telefono, long telEmerg, String rfc) {
        this.id = id;
        this.nombre = nombre;
        this.apMat = apMat;
        this.apPat = apPat;
        this.nacimiento = nacimiento;
        this.curp = curp;
        this.telefono = telefono;
        this.telEmerg = telEmerg;
        this.rfc = rfc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApMat() {
        return apMat;
    }

    public void setApMat(String apMat) {
        this.apMat = apMat;
    }

    public String getApPat() {
        return apPat;
    }

    public void setApPat(String apPat) {
        this.apPat = apPat;
    }

    public Date getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(Date nacimiento) {
        this.nacimiento = nacimiento;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    public long getTelEmerg() {
        return telEmerg;
    }

    public void setTelEmerg(long telEmerg) {
        this.telEmerg = telEmerg;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    @XmlTransient
    public List<Bonos> getBonosList() {
        return bonosList;
    }

    public void setBonosList(List<Bonos> bonosList) {
        this.bonosList = bonosList;
    }

    public Cv getIdCv() {
        return idCv;
    }

    public void setIdCv(Cv idCv) {
        this.idCv = idCv;
    }

    public Direcciones getIdDirect() {
        return idDirect;
    }

    public void setIdDirect(Direcciones idDirect) {
        this.idDirect = idDirect;
    }

    public Puestos getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(Puestos idPuesto) {
        this.idPuesto = idPuesto;
    }

    public Users getIdUser() {
        return idUser;
    }

    public void setIdUser(Users idUser) {
        this.idUser = idUser;
    }

    @XmlTransient
    public List<ContratosEmpleados> getContratosEmpleadosList() {
        return contratosEmpleadosList;
    }

    public void setContratosEmpleadosList(List<ContratosEmpleados> contratosEmpleadosList) {
        this.contratosEmpleadosList = contratosEmpleadosList;
    }

    @XmlTransient
    public List<Envios> getEnviosList() {
        return enviosList;
    }

    public void setEnviosList(List<Envios> enviosList) {
        this.enviosList = enviosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empleados)) {
            return false;
        }
        Empleados other = (Empleados) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.okitegami.models.Empleados[ id=" + id + " ]";
    }
    
}
=======
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author meli_
 */
@Entity
@Table(name = "empleados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empleados.findAll", query = "SELECT e FROM Empleados e"),
    @NamedQuery(name = "Empleados.findById", query = "SELECT e FROM Empleados e WHERE e.id = :id"),
    @NamedQuery(name = "Empleados.findByNombre", query = "SELECT e FROM Empleados e WHERE e.nombre = :nombre"),
    @NamedQuery(name = "Empleados.findByApMat", query = "SELECT e FROM Empleados e WHERE e.apMat = :apMat"),
    @NamedQuery(name = "Empleados.findByApPat", query = "SELECT e FROM Empleados e WHERE e.apPat = :apPat"),
    @NamedQuery(name = "Empleados.findByNacimiento", query = "SELECT e FROM Empleados e WHERE e.nacimiento = :nacimiento"),
    @NamedQuery(name = "Empleados.findByCurp", query = "SELECT e FROM Empleados e WHERE e.curp = :curp"),
    @NamedQuery(name = "Empleados.findByTelefono", query = "SELECT e FROM Empleados e WHERE e.telefono = :telefono"),
    @NamedQuery(name = "Empleados.findByTelEmerg", query = "SELECT e FROM Empleados e WHERE e.telEmerg = :telEmerg"),
    @NamedQuery(name = "Empleados.findByRfc", query = "SELECT e FROM Empleados e WHERE e.rfc = :rfc")})
public class Empleados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ap_mat")
    private String apMat;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ap_pat")
    private String apPat;
    @Basic(optional = false)
    @NotNull
    @Column(name = "nacimiento")
    @Temporal(TemporalType.DATE)
    private Date nacimiento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "curp")
    private String curp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "telefono")
    private long telefono;
    @Basic(optional = false)
    @NotNull
    @Column(name = "tel_emerg")
    private long telEmerg;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "rfc")
    private String rfc;
    @ManyToMany(mappedBy = "empleadosList")
    private List<Bonos> bonosList;
    @JoinColumn(name = "id_cv", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cv idCv;
    @JoinColumn(name = "id_direct", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Direcciones idDirect;
    @JoinColumn(name = "id_puesto", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Puestos idPuesto;
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users idUser;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleado")
    private List<ContratosEmpleados> contratosEmpleadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpleado")
    private List<Envios> enviosList;

    public Empleados() {
    }

    public Empleados(Integer id) {
        this.id = id;
    }

    public Empleados(Integer id, String nombre, String apMat, String apPat, Date nacimiento, String curp, long telefono, long telEmerg, String rfc) {
        this.id = id;
        this.nombre = nombre;
        this.apMat = apMat;
        this.apPat = apPat;
        this.nacimiento = nacimiento;
        this.curp = curp;
        this.telefono = telefono;
        this.telEmerg = telEmerg;
        this.rfc = rfc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApMat() {
        return apMat;
    }

    public void setApMat(String apMat) {
        this.apMat = apMat;
    }

    public String getApPat() {
        return apPat;
    }

    public void setApPat(String apPat) {
        this.apPat = apPat;
    }

    public Date getNacimiento() {
        return nacimiento;
    }

    public void setNacimiento(Date nacimiento) {
        this.nacimiento = nacimiento;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public long getTelefono() {
        return telefono;
    }

    public void setTelefono(long telefono) {
        this.telefono = telefono;
    }

    public long getTelEmerg() {
        return telEmerg;
    }

    public void setTelEmerg(long telEmerg) {
        this.telEmerg = telEmerg;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    @XmlTransient
    public List<Bonos> getBonosList() {
        return bonosList;
    }

    public void setBonosList(List<Bonos> bonosList) {
        this.bonosList = bonosList;
    }

    public Cv getIdCv() {
        return idCv;
    }

    public void setIdCv(Cv idCv) {
        this.idCv = idCv;
    }

    public Direcciones getIdDirect() {
        return idDirect;
    }

    public void setIdDirect(Direcciones idDirect) {
        this.idDirect = idDirect;
    }

    public Puestos getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(Puestos idPuesto) {
        this.idPuesto = idPuesto;
    }

    public Users getIdUser() {
        return idUser;
    }

    public void setIdUser(Users idUser) {
        this.idUser = idUser;
    }

    @XmlTransient
    public List<ContratosEmpleados> getContratosEmpleadosList() {
        return contratosEmpleadosList;
    }

    public void setContratosEmpleadosList(List<ContratosEmpleados> contratosEmpleadosList) {
        this.contratosEmpleadosList = contratosEmpleadosList;
    }

    @XmlTransient
    public List<Envios> getEnviosList() {
        return enviosList;
    }

    public void setEnviosList(List<Envios> enviosList) {
        this.enviosList = enviosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Empleados)) {
            return false;
        }
        Empleados other = (Empleados) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.okitegami.models.Empleados[ id=" + id + " ]";
    }
    
}
>>>>>>> 3ec3023e315bb2dc3952b0d72da17fb39fbf9c91
