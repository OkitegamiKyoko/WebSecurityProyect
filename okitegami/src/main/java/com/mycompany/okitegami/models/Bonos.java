/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@Table(name = "bonos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bonos.findAll", query = "SELECT b FROM Bonos b"),
    @NamedQuery(name = "Bonos.findById", query = "SELECT b FROM Bonos b WHERE b.id = :id"),
    @NamedQuery(name = "Bonos.findByNombre", query = "SELECT b FROM Bonos b WHERE b.nombre = :nombre"),
    @NamedQuery(name = "Bonos.findByDescripcion", query = "SELECT b FROM Bonos b WHERE b.descripcion = :descripcion"),
    @NamedQuery(name = "Bonos.findByAumento", query = "SELECT b FROM Bonos b WHERE b.aumento = :aumento"),
    @NamedQuery(name = "Bonos.findByStatus", query = "SELECT b FROM Bonos b WHERE b.status = :status")})
public class Bonos implements Serializable {

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
    @Size(min = 1, max = 255)
    @Column(name = "descripcion")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "aumento")
    private BigDecimal aumento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @JoinTable(name = "bonos_empleados", joinColumns = {
        @JoinColumn(name = "id_bono", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "id_empleado", referencedColumnName = "id")})
    @ManyToMany
    private List<Empleados> empleadosList;

    public Bonos() {
    }

    public Bonos(Integer id) {
        this.id = id;
    }

    public Bonos(Integer id, String nombre, String descripcion, BigDecimal aumento, int status) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.aumento = aumento;
        this.status = status;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getAumento() {
        return aumento;
    }

    public void setAumento(BigDecimal aumento) {
        this.aumento = aumento;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bonos)) {
            return false;
        }
        Bonos other = (Bonos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.okitegami.models.Bonos[ id=" + id + " ]";
    }
    
}
