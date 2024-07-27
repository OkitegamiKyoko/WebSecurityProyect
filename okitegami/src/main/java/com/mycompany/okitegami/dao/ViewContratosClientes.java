/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

/**
 *
 * @author meli_
 */
public class ViewContratosClientes{
    private Integer id;
    private String nombre;
    private String puesto;
    private double salario;
    private Integer meses;
    private double pago;

    public ViewContratosClientes() {
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPuesto() {
        return puesto;
    }

    public double getSalario() {
        return salario;
    }

    public Integer getMeses() {
        return meses;
    }

    public double getPago() {
        return pago;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public void setMeses(Integer meses) {
        this.meses = meses;
    }

    public void setPago(double pago) {
        this.pago = pago;
    }

    @Override
    public String toString() {
        return "ViewContratosClientes{" + "id=" + id + ", nombre=" + nombre + ", puesto=" + puesto + ", salario=" + salario + ", meses=" + meses + ", pago=" + pago + '}';
    }
    
    
}
