/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author meli_
 */
@Entity
@Table(name = "contratos_empleados")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContratosEmpleados.findAll", query = "SELECT c FROM ContratosEmpleados c"),
    @NamedQuery(name = "ContratosEmpleados.findById", query = "SELECT c FROM ContratosEmpleados c WHERE c.id = :id"),
    @NamedQuery(name = "ContratosEmpleados.findByCreacion", query = "SELECT c FROM ContratosEmpleados c WHERE c.creacion = :creacion"),
    @NamedQuery(name = "ContratosEmpleados.findByInicio", query = "SELECT c FROM ContratosEmpleados c WHERE c.inicio = :inicio"),
    @NamedQuery(name = "ContratosEmpleados.findByFin", query = "SELECT c FROM ContratosEmpleados c WHERE c.fin = :fin"),
    @NamedQuery(name = "ContratosEmpleados.findByStatus", query = "SELECT c FROM ContratosEmpleados c WHERE c.status = :status")})
public class ContratosEmpleados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creacion")
    @Temporal(TemporalType.DATE)
    private Date creacion;
    @Basic(optional = false)
    @NotNull
    @Column(name = "inicio")
    @Temporal(TemporalType.DATE)
    private Date inicio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fin")
    @Temporal(TemporalType.DATE)
    private Date fin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private int status;
    @JoinColumn(name = "id_empleado", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Empleados idEmpleado;

    public ContratosEmpleados() {
    }

    public ContratosEmpleados(Integer id) {
        this.id = id;
    }

    public ContratosEmpleados(Integer id, Date creacion, Date inicio, Date fin, int status) {
        this.id = id;
        this.creacion = creacion;
        this.inicio = inicio;
        this.fin = fin;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreacion() {
        return creacion;
    }

    public void setCreacion(Date creacion) {
        this.creacion = creacion;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Empleados getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleados idEmpleado) {
        this.idEmpleado = idEmpleado;
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
        if (!(object instanceof ContratosEmpleados)) {
            return false;
        }
        ContratosEmpleados other = (ContratosEmpleados) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.okitegami.models.ContratosEmpleados[ id=" + id + " ]";
    }
    
}
