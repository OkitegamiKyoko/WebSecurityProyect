/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

import com.mycompany.okitegami.controllers.EnviosJpaController;
import com.mycompany.okitegami.models.ContratosClientes;
import com.mycompany.okitegami.models.Envios;
import javax.annotation.PostConstruct;  
import java.io.Serializable;  
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

/**
 *
 * @author meli_
 */

@Named("graficContratos")
@SessionScoped
public class GraficLineContratos implements Serializable {
    
    private LineChartModel drawArea;
    
    public LineChartModel getDrawArea() {  
        return drawArea;  
    }
    
    public LineChartModel createDrawArea(List<ContratosClientes> env) {
        drawArea = new LineChartModel();
        ChartData data= new ChartData();
        if (env!=null) {
            LineChartDataSet dataSet=new LineChartDataSet();
            
            data.setLabels(meses());
            
            dataSet.setData(datos(env));
            dataSet.setFill(false);
            dataSet.setLabel("Contratos");
            dataSet.setTension(0.1);
            dataSet.setBorderColor("rgb(75, 192, 192)");
            data.addChartDataSet(dataSet);
            
            Title title=new Title();
            title.setDisplay(true);
            title.setText("Contratos  totales en el año");
            
            LineChartOptions options = new LineChartOptions();
            options.setTitle(title);
            drawArea.setOptions(options);
            drawArea.setData(data);
        }
        return drawArea; 
    }
    
    public int limit(int envios){
        return envios;
    }
    
    public List<String> meses(){
        List<String> meses= new ArrayList();
        meses.add("Enero");
        meses.add("Febrero");
        meses.add("Marzo");
        meses.add("Abril");
        meses.add("Mayo");
        meses.add("Junio");
        meses.add("julio");
        meses.add("Agosto");
        meses.add("Septiembre");
        meses.add("Octubre");
        meses.add("Noviembre");
        meses.add("Diciembre");
        return meses;
    }
    public List<Object> datos(List<ContratosClientes> env){
        List<Object> list= new ArrayList();
        int num=0;
        list.add(num);
        list.add(num);
        list.add(num);
        list.add(num);
        list.add(num);
        list.add(num);
        list.add(num);
        list.add(num);
        list.add(num);
        list.add(num);
        list.add(num);
        list.add(num);
        int size=env.size();
        for (int i = 0; i < size; i++) {
                Calendar time=Calendar.getInstance();
                ContratosClientes envio=env.get(i);
                time.setTime(envio.getCreacion());
                int mouth=time.get(Calendar.MONTH)-1;
                String mes=""+list.get(mouth);
                int plusMouth=Integer.parseInt(mes)+1;
                list.set(mouth, plusMouth);
            }
        return list;
    }
}
