/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

/*import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.PDFContratCliente.PdfDocument;
import com.itextpdf.kernel.PDFContratCliente.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.text.DocumentException;
import com.mycompany.okitegami.controllers.ContratosClientesJpaController;
import org.xhtmlrenderer.PDFContratCliente.ITextRenderer;
*/
import com.mycompany.okitegami.controllers.ContratosClientesJpaController;
import com.mycompany.okitegami.controllers.EnviosJpaController;
import com.mycompany.okitegami.models.Clientes;
import com.mycompany.okitegami.models.ContratosClientes;
import com.mycompany.okitegami.models.Direcciones;
import com.mycompany.okitegami.models.Empleados;
import com.mycompany.okitegami.models.Envios;
//import com.mycompany.okitegami.models.views.ViewContratosClientes;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author meli_
 */
@Named("createPDF")
@WebServlet("/pdfContratCliente")
public class PDFContratCliente extends HttpServlet {
    //Document PDFContratCliente=new Document();
    //@PersistenceContext
    //private EntityManagerFactory emf=Persistence.createEntityManagerFactory("okitegami_db_pool");
    //private EntityManager entityManager=emf.createEntityManager();
    @Inject
    ContratosClientesJpaController cont;
    ContratosClientes contDetails;
    Clientes cliente;
    @Inject
    EnviosJpaController envios;

    public String id(int id) {
        return ""+id;
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //FacesContext face=FacesContext.getCurrentInstance();
        //ExternalContext external=face.getExternalContext();
        //HttpSession session=(HttpSession) external.getSession(true);
        //ServletResponse res=(ServletResponse) external.getResponse();
        System.out.println("Print PDF");
        String idCont=request.getParameter("id");
        try {
            ServletOutputStream out=response.getOutputStream();
            InputStream /*icono= this.getServletConfig().getServletContext().getResourceAsStream("/pages/formats/customer/icono.png"),*/
                    reporte= this.getServletConfig().getServletContext().getResourceAsStream("/pages/formats/customer/contrat.jasper");
            if(reporte!=null){
                int id=Integer.parseInt(idCont);
                contDetails=cont.findContratosClientes(id);
                Date inicio=contDetails.getInicio(),
                        fin=contDetails.getFin();
                cliente=contDetails.getIdCliente();
                String nombre=cliente.getNombre(),
                        rfc=cliente.getRfc();
                Direcciones direct=cliente.getIdDirect();
                String direccion=direct.getCalle()+", "+direct.getNoExt()+", "+direct.getIdLocalidad().getNombre()+", "+direct.getIdLocalidad().getIdMunicipio().getNombre()+", "+direct.getIdLocalidad().getIdMunicipio().getIdEstado().getNombre();
                List<ViewContratosClientes> list= new ArrayList<>();
                List<Envios> envioList=contDetails.getEnviosList();
                ViewContratosClientes view=new ViewContratosClientes();
                list.add(view);
                for (int i = 0; i < envioList.size(); i++) {
                    Empleados emp=envioList.get(i).getIdEmpleado();
                    view.setId(emp.getId());
                    view.setNombre(emp.getNombre()+" "+emp.getApMat()+" "+emp.getApPat());
                    view.setPuesto(emp.getIdPuesto().getNombre());
                    view.setSalario((double)emp.getIdPuesto().getPago());
                    long diff=fin.getTime()-inicio.getTime();
                    TimeUnit time = TimeUnit.DAYS; 
                    long diffrence = time.convert(diff, TimeUnit.MILLISECONDS);
                    int meses=(int) diffrence/30;
                    view.setMeses(meses);
                    view.setPago((double)(emp.getIdPuesto().getPago()*meses));
                    view.toString();
                    System.out.println(view.getNombre());
                    list.add(view);
                    System.out.println(list.get(0).getNombre());
                    view=new ViewContratosClientes();
                }
                int noEmpleados=contDetails.getEnviosList().size();
                JasperReport report=(JasperReport) JRLoader.loadObject(reporte);
                JRBeanArrayDataSource source=new JRBeanArrayDataSource(list.toArray(),false);
                Map<String, Object> parameters=new HashMap();
                //parameters.put("logo", logo);
                //parameters.put("logo", icono);
                parameters.put("clienteDirreccion", direccion);
                parameters.put("clienteName", nombre);
                parameters.put("clienteRFC", rfc);
                parameters.put("inicio", inicio);
                parameters.put("fin", fin);
                parameters.put("noEmpleados", noEmpleados);
                parameters.put("idContrato", id);
                parameters.put("tablaClienteContrato", source);
                response.setContentType("application/pdf");
                response.addHeader("Content-disposition", "inline; filename=reporte.pdf");
                JasperPrint print=JasperFillManager.fillReport(report, parameters,source);
                JasperExportManager.exportReportToPdfStream(print, out);
                out.flush();
                out.close();
                parameters.clear();
                //source.getData().clear();
            }else{
                System.out.println("Error de recursos para reporte");
            }
            //Para pruebas
            //String url="http://localhost/pages/formats/customer/contract.xhtml:j_username="+"okitegami@kyoko.com"+":j_password="+"okitegami"+"?id="+cont.getId();
            //Para produccion
            //String url="http://okitegami-kyoko.sytes.net:8080/pages/formats/customer/contract.xhtml?id="+cont.getId();
            /*try{
            ITextRenderer render=new ITextRenderer();
            URLConnection conect=new URL(url).openConnection();
            System.out.println("Permiso: "+conect.getPermission());
            render.setDocument(new URL(url).toString());
            render.layout();
            HttpServletResponse response=(HttpServletResponse) external.getResponse();
            response.reset();
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"contrato"+cont.getId()+cont.getIdCliente().getId()+".PDFContratCliente\"");
            OutputStream output=response.getOutputStream();
            render.createPDF(output);
            //HtmlConverter.convertToPdf(new URL(url).openStream(), output);
            System.out.println("PDF generado con exito");
            }catch(IOException e){
            System.out.println("Error de generacion: "+e.toString());
            } catch (DocumentException ex) {
            System.out.println("Error de generacion: "+ex.toString());
            }
            face.responseComplete();
        */} catch (IOException | JRException ex) {
            Logger.getLogger(PDFContratCliente.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error de creacion de reporte");
        }
    }
    /*public void getPdfContrato(ContratosClientes cont){
    File file=new File("contrato"+cont.getId()+cont.getIdCliente().getId()+".PDFContratCliente");
    try(PdfWriter writer = new PdfWriter(file)) {
    PdfDocument pdfDoc=new PdfDocument(writer);
    Document doc=new Document(pdfDoc);
    doc.
    pdfDoc.close();
    } catch (FileNotFoundException ex) {
    Logger.getLogger(PDFContratCliente.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
    Logger.getLogger(PDFContratCliente.class.getName()).log(Level.SEVERE, null, ex);
    }finally{
    }
    }*/
    
}
