/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.nio.file.Files;
import java.util.Random;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author meli_
 */
@Named("video")
@WebServlet("/videos")
public class videoDao extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Random ran=new Random();
        int random;
        do{
            random=ran.nextInt(7);
            System.out.println(random);
        }while(random==0);
        try{
            BufferedOutputStream bufferedOutputStream;
            OutputStream outputStream = response.getOutputStream();
                bufferedOutputStream = new BufferedOutputStream(outputStream);
                String path="C:\\Users\\meli_\\Videos\\about"+random+".webm";
                File file=new File(path);
                InputStream inputStream = new FileInputStream(file);
                    response.setContentType("video/mp4");
                    response.setHeader("Content-Disposition", "inline; filename=video_about.mp4");
                    byte[] buffer = new byte[40960];
                    int bytesRead;
                    while((bytesRead=inputStream.read(buffer))!=-1){
                        bufferedOutputStream.write(buffer,0,bytesRead);
                        if(!isConnectionActive(request)){
                            System.out.println("El usuario se a desconectado de la pagina about");
                            System.out.println("Bytes enviados: "+bytesRead);
                            break;
                        }
                    }
                
                outputStream.flush();
            
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    private boolean isConnectionActive(HttpServletRequest request) {
        try {
            // Obtener la dirección IP del cliente
            String clientIpAddress = request.getRemoteAddr();

            // Realizar un ping al cliente
            InetAddress clientAddress = InetAddress.getByName(clientIpAddress);
            boolean isReachable = clientAddress.isReachable(300); // 3000 ms de tiempo de espera

            // La conexión está activa si el ping tuvo éxito
            return isReachable;
        } catch (IOException e) {
            // Ocurrió un error al realizar el ping, asumir que la conexión está cerrada
            return false;
        }
    }
}
