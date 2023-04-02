/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers;

import com.mycompany.okitegami.controllers.exceptions.NonexistentEntityException;
import com.mycompany.okitegami.controllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.okitegami.models.Roles;
import com.mycompany.okitegami.models.Users;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

/**
 *
 * @author meli_
 */
@Named("userController")
@ApplicationScoped
public class UsersJpaController implements Serializable {
    
    private UserTransaction utx;
    private EntityManagerFactory emf;
    private Users userEdit,userDetails,userNew;
    private String passwordConfirm,image;
    //private final RolesJpaController rolJpa;

    public UsersJpaController() throws NamingException {
        this.utx = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        this.emf = Persistence.createEntityManagerFactory("okitegami_db_pool");
        //this.rolJpa= new RolesJpaController();
        //emf.createEntityManager().isOpen();
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) throws RollbackFailureException, Exception {
        EntityManager em = null;
        System.out.println("Creando");
        users.getDateIngreso();
        
        try {
            utx.begin();
            em = getEntityManager();
            Roles idRol = users.getIdRol();
            if (idRol != null) {
                idRol = em.getReference(idRol.getClass(), idRol.getId());
                users.setIdRol(idRol);
            }
            System.out.println(users.toString());
            em.persist(users);
            if (idRol != null) {
                idRol.getUsersList().add(users);
                idRol = em.merge(idRol);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                System.out.println("Creando fallo");
                System.out.println("Error "+ex.getCause().getMessage());
                utx.rollback();
            } catch (Exception re) {
                System.out.println("Creando fallo otra vez");
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            System.out.println("Editando");
            em = getEntityManager();
            Users persistentUsers = em.find(Users.class, users.getId());
            Roles idRolOld = persistentUsers.getIdRol();
            Roles idRolNew = users.getIdRol();
            if (idRolNew != null) {
                idRolNew = em.getReference(idRolNew.getClass(), idRolNew.getId());
                users.setIdRol(idRolNew);
            }
            users = em.merge(users);
            if (idRolOld != null && !idRolOld.equals(idRolNew)) {
                idRolOld.getUsersList().remove(users);
                idRolOld = em.merge(idRolOld);
            }
            if (idRolNew != null && !idRolNew.equals(idRolOld)) {
                idRolNew.getUsersList().add(users);
                idRolNew = em.merge(idRolNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = users.getId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            Roles idRol = users.getIdRol();
            if (idRol != null) {
                idRol.getUsersList().remove(users);
                idRol = em.merge(idRol);
            }
            em.remove(users);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Users findUsers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    public Users findByEmail(String email){
        try{
            return getEntityManager().createNamedQuery("Users.findByEmail",Users.class).setParameter("email",email).getSingleResult();
        }catch(Exception e){
            System.out.println("Error: "+e.toString());
            return null;
        }
    }

    public Users getUserEdit() {
        return userEdit;
    }

    public void setUserEdit(Users userEdit) {
        this.userEdit = userEdit;
    }
    
    public Users getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(Users userDetails) {
        this.userDetails = userDetails;
    }

    public Users getUserNew() {
        if(userNew==null){
            userNew= new Users();
        }
        return userNew;
    }

    public void setUserNew(Users userNew) {
        this.userNew = userNew;
    }
    
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
    
    public String getImage() {
        if(image==null){
            image="http://localhost:8080/okitegami/vlcsnap-2023-03-11-14h00m51s589.png";
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    public String editUser(Users user){
        passwordConfirm=user.getPassword();
        this.userEdit=user;
        return "userEdit";
    }
    
    public String details(Users user){
        this.userDetails=user;
        return "detailsUser";
    }
    
    public void pruebas(){
        System.out.println("Found");
    }
    
    public void desactive(Users user) throws RollbackFailureException, Exception{
        user.setStatus(0);
        edit(user);
    }
    
    public void reactive(Users user) throws RollbackFailureException, Exception{
        user.setStatus(1);
        edit(user);
    }
}
