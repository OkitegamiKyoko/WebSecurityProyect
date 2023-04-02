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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author meli_
 */
@Entity
@Table(name = "contratos_clientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContratosClientes.findAll", query = "SELECT c FROM ContratosClientes c"),
    @NamedQuery(name = "ContratosClientes.findById", query = "SELECT c FROM ContratosClientes c WHERE c.id = :id"),
    @NamedQuery(name = "ContratosClientes.findByCreacion", query = "SELECT c FROM ContratosClientes c WHERE c.creacion = :creacion"),
    @NamedQuery(name = "ContratosClientes.findByInicio", query = "SELECT c FROM ContratosClientes c WHERE c.inicio = :inicio"),
    @NamedQuery(name = "ContratosClientes.findByFin", query = "SELECT c FROM ContratosClientes c WHERE c.fin = :fin"),
    @NamedQuery(name = "ContratosClientes.findByStatus", query = "SELECT c FROM ContratosClientes c WHERE c.status = :status")})
public class ContratosClientes implements Serializable {

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
    @JoinColumn(name = "id_cliente", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Clientes idCliente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idContrato")
    private List<Envios> enviosList;

    public ContratosClientes() {
    }

    public ContratosClientes(Integer id) {
        this.id = id;
    }

    public ContratosClientes(Integer id, Date creacion, Date inicio, Date fin, int status) {
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

    public Clientes getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Clientes idCliente) {
        this.idCliente = idCliente;
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
        if (!(object instanceof ContratosClientes)) {
            return false;
        }
        ContratosClientes other = (ContratosClientes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.okitegami.models.ContratosClientes[ id=" + id + " ]";
    }
    
}
