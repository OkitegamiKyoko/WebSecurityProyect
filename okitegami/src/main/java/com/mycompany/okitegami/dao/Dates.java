/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

/**
 *
 * @author meli_
 */
public class Dates {
    public Date now(){
        //Date now= Date.from(datetime.atZone(ZoneId.systemDefault()).toInstant());
        Date input = new Date();
        Instant instant = input.toInstant();
        Date output = Date.from(instant);
        //LocalDateTime today = LocalDateTime.now();
        return output;
    };
}
