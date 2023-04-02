/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.models;

import java.io.Serializable;
import java.math.BigInteger;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author meli_
 */
@Entity
@Table(name = "cuentas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cuentas.findAll", query = "SELECT c FROM Cuentas c"),
    @NamedQuery(name = "Cuentas.findById", query = "SELECT c FROM Cuentas c WHERE c.id = :id"),
    @NamedQuery(name = "Cuentas.findByCuenta", query = "SELECT c FROM Cuentas c WHERE c.cuenta = :cuenta")})
public class Cuentas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cuenta")
    private BigInteger cuenta;
    @JoinColumn(name = "id_met_pago", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MetodosPago idMetPago;

    public Cuentas() {
    }

    public Cuentas(Integer id) {
        this.id = id;
    }

    public Cuentas(Integer id, BigInteger cuenta) {
        this.id = id;
        this.cuenta = cuenta;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigInteger getCuenta() {
        return cuenta;
    }

    public void setCuenta(BigInteger cuenta) {
        this.cuenta = cuenta;
    }

    public MetodosPago getIdMetPago() {
        return idMetPago;
    }

    public void setIdMetPago(MetodosPago idMetPago) {
        this.idMetPago = idMetPago;
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
        if (!(object instanceof Cuentas)) {
            return false;
        }
        Cuentas other = (Cuentas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.okitegami.models.Cuentas[ id=" + id + " ]";
    }
    
}
