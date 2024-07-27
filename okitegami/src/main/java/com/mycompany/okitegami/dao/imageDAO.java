/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

import com.mycompany.okitegami.controllers.session.SessionCont;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author meli_
 */
@WebServlet("/image")
public class imageDAO extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Inject
    SessionCont session;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ruta;
        System.out.println("RUTA IMAGE: "+request.getParameter("ruta"));
        ruta=request.getParameter("ruta");
        if (ruta.isEmpty()) {
            ruta="default.png";
        }
        //String ruta=session.getUser().getImage();
        String path="C:\\Users\\meli_\\Okitegami\\Users\\Images\\"+ruta;
        File file=new File(path);
        byte[] imageBytesArray = (byte[]) Files.readAllBytes(file.toPath());
        response.setContentType("application/images");
        response.setContentLength(imageBytesArray.length);
        response.getOutputStream().write(imageBytesArray);
    }
}
