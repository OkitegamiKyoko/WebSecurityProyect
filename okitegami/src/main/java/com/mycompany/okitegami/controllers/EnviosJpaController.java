<<<<<<< HEAD
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
import com.mycompany.okitegami.models.ContratosClientes;
import com.mycompany.okitegami.models.Envios;
import java.util.List;
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
@Named("enviosCont")
public class EnviosJpaController implements Serializable {

    public EnviosJpaController() throws NamingException {
        this.utx = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        this.emf = Persistence.createEntityManagerFactory("okitegami_db_pool");
        //this.rolJpa= new RolesJpaController();
        //emf.createEntityManager().isOpen();
    }
    private UserTransaction utx;
    private EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Envios envios) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ContratosClientes idContrato = envios.getIdContrato();
            if (idContrato != null) {
                idContrato = em.getReference(idContrato.getClass(), idContrato.getId());
                envios.setIdContrato(idContrato);
            }
            em.persist(envios);
            if (idContrato != null) {
                idContrato.getEnviosList().add(envios);
                idContrato = em.merge(idContrato);
            }
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

    public void edit(Envios envios) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Envios persistentEnvios = em.find(Envios.class, envios.getId());
            ContratosClientes idContratoOld = persistentEnvios.getIdContrato();
            ContratosClientes idContratoNew = envios.getIdContrato();
            if (idContratoNew != null) {
                idContratoNew = em.getReference(idContratoNew.getClass(), idContratoNew.getId());
                envios.setIdContrato(idContratoNew);
            }
            envios = em.merge(envios);
            if (idContratoOld != null && !idContratoOld.equals(idContratoNew)) {
                idContratoOld.getEnviosList().remove(envios);
                idContratoOld = em.merge(idContratoOld);
            }
            if (idContratoNew != null && !idContratoNew.equals(idContratoOld)) {
                idContratoNew.getEnviosList().add(envios);
                idContratoNew = em.merge(idContratoNew);
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
                Integer id = envios.getId();
                if (findEnvios(id) == null) {
                    throw new NonexistentEntityException("The envios with id " + id + " no longer exists.");
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
            Envios envios;
            try {
                envios = em.getReference(Envios.class, id);
                envios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The envios with id " + id + " no longer exists.", enfe);
            }
            ContratosClientes idContrato = envios.getIdContrato();
            if (idContrato != null) {
                idContrato.getEnviosList().remove(envios);
                idContrato = em.merge(idContrato);
            }
            em.remove(envios);
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

    public List<Envios> findEnviosEntities() {
        return findEnviosEntities(true, -1, -1);
    }

    public List<Envios> findEnviosEntities(int maxResults, int firstResult) {
        return findEnviosEntities(false, maxResults, firstResult);
    }

    private List<Envios> findEnviosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Envios.class));
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

    public Envios findEnvios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Envios.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnviosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Envios> rt = cq.from(Envios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
=======
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
import com.mycompany.okitegami.models.ContratosClientes;
import com.mycompany.okitegami.models.Empleados;
import com.mycompany.okitegami.models.Envios;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author meli_
 */
public class EnviosJpaController implements Serializable {

    public EnviosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Envios envios) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ContratosClientes idContrato = envios.getIdContrato();
            if (idContrato != null) {
                idContrato = em.getReference(idContrato.getClass(), idContrato.getId());
                envios.setIdContrato(idContrato);
            }
            Empleados idEmpleado = envios.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getId());
                envios.setIdEmpleado(idEmpleado);
            }
            em.persist(envios);
            if (idContrato != null) {
                idContrato.getEnviosList().add(envios);
                idContrato = em.merge(idContrato);
            }
            if (idEmpleado != null) {
                idEmpleado.getEnviosList().add(envios);
                idEmpleado = em.merge(idEmpleado);
            }
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

    public void edit(Envios envios) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Envios persistentEnvios = em.find(Envios.class, envios.getId());
            ContratosClientes idContratoOld = persistentEnvios.getIdContrato();
            ContratosClientes idContratoNew = envios.getIdContrato();
            Empleados idEmpleadoOld = persistentEnvios.getIdEmpleado();
            Empleados idEmpleadoNew = envios.getIdEmpleado();
            if (idContratoNew != null) {
                idContratoNew = em.getReference(idContratoNew.getClass(), idContratoNew.getId());
                envios.setIdContrato(idContratoNew);
            }
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getId());
                envios.setIdEmpleado(idEmpleadoNew);
            }
            envios = em.merge(envios);
            if (idContratoOld != null && !idContratoOld.equals(idContratoNew)) {
                idContratoOld.getEnviosList().remove(envios);
                idContratoOld = em.merge(idContratoOld);
            }
            if (idContratoNew != null && !idContratoNew.equals(idContratoOld)) {
                idContratoNew.getEnviosList().add(envios);
                idContratoNew = em.merge(idContratoNew);
            }
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getEnviosList().remove(envios);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getEnviosList().add(envios);
                idEmpleadoNew = em.merge(idEmpleadoNew);
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
                Integer id = envios.getId();
                if (findEnvios(id) == null) {
                    throw new NonexistentEntityException("The envios with id " + id + " no longer exists.");
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
            Envios envios;
            try {
                envios = em.getReference(Envios.class, id);
                envios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The envios with id " + id + " no longer exists.", enfe);
            }
            ContratosClientes idContrato = envios.getIdContrato();
            if (idContrato != null) {
                idContrato.getEnviosList().remove(envios);
                idContrato = em.merge(idContrato);
            }
            Empleados idEmpleado = envios.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getEnviosList().remove(envios);
                idEmpleado = em.merge(idEmpleado);
            }
            em.remove(envios);
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

    public List<Envios> findEnviosEntities() {
        return findEnviosEntities(true, -1, -1);
    }

    public List<Envios> findEnviosEntities(int maxResults, int firstResult) {
        return findEnviosEntities(false, maxResults, firstResult);
    }

    private List<Envios> findEnviosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Envios.class));
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

    public Envios findEnvios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Envios.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnviosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Envios> rt = cq.from(Envios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
>>>>>>> 3ec3023e315bb2dc3952b0d72da17fb39fbf9c91
