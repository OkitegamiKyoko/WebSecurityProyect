/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.dao;

import com.mycompany.okitegami.controllers.UsersJpaController;
import com.mycompany.okitegami.models.Users;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author meli_
 */

public class UsersDAO {
    @Inject
    UsersJpaController users;
    public void create(Users user) throws Exception{
        users.create(user);
    }
}
