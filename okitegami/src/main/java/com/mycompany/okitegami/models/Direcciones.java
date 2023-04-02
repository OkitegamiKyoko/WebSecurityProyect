/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author meli_
 */
@Entity
@Table(name = "direcciones")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Direcciones.findAll", query = "SELECT d FROM Direcciones d"),
    @NamedQuery(name = "Direcciones.findById", query = "SELECT d FROM Direcciones d WHERE d.id = :id"),
    @NamedQuery(name = "Direcciones.findByEstado", query = "SELECT d FROM Direcciones d WHERE d.estado = :estado"),
    @NamedQuery(name = "Direcciones.findByMunicipio", query = "SELECT d FROM Direcciones d WHERE d.municipio = :municipio"),
    @NamedQuery(name = "Direcciones.findByLocalidad", query = "SELECT d FROM Direcciones d WHERE d.localidad = :localidad"),
    @NamedQuery(name = "Direcciones.findByCalle", query = "SELECT d FROM Direcciones d WHERE d.calle = :calle"),
    @NamedQuery(name = "Direcciones.findByNoExt", query = "SELECT d FROM Direcciones d WHERE d.noExt = :noExt"),
    @NamedQuery(name = "Direcciones.findByNoInt", query = "SELECT d FROM Direcciones d WHERE d.noInt = :noInt"),
    @NamedQuery(name = "Direcciones.findByCp", query = "SELECT d FROM Direcciones d WHERE d.cp = :cp"),
    @NamedQuery(name = "Direcciones.findByStatus", query = "SELECT d FROM Direcciones d WHERE d.status = :status")})
public class Direcciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "estado")
    private String estado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "municipio")
    private String municipio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "localidad")
    private String localidad;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "calle")
    private String calle;
    @Basic(optional = false)
    @NotNull
    @Column(name = "no_ext")
    private int noExt;
    @Column(name = "no_int")
    private Integer noInt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cp")
    private int cp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDirect")
    private List<Empleados> empleadosList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDirect")
    private List<Clientes> clientesList;

    public Direcciones() {
    }

    public Direcciones(Integer id) {
        this.id = id;
    }

    public Direcciones(Integer id, String estado, String municipio, String localidad, String calle, int noExt, int cp, int status) {
        this.id = id;
        this.estado = estado;
        this.municipio = municipio;
        this.localidad = localidad;
        this.calle = calle;
        this.noExt = noExt;
        this.cp = cp;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNoExt() {
        return noExt;
    }

    public void setNoExt(int noExt) {
        this.noExt = noExt;
    }

    public Integer getNoInt() {
        return noInt;
    }

    public void setNoInt(Integer noInt) {
        this.noInt = noInt;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @XmlTransient
    public List<Empleados> getEmpleadosList() {
        return empleadosList;
    }

    public void setEmpleadosList(List<Empleados> empleadosList) {
        this.empleadosList = empleadosList;
    }

    @XmlTransient
    public List<Clientes> getClientesList() {
        return clientesList;
    }

    public void setClientesList(List<Clientes> clientesList) {
        this.clientesList = clientesList;
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
        if (!(object instanceof Direcciones)) {
            return false;
        }
        Direcciones other = (Direcciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.okitegami.models.Direcciones[ id=" + id + " ]";
    }
    
}
