/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers;

import com.mycompany.okitegami.controllers.exceptions.NonexistentEntityException;
import com.mycompany.okitegami.controllers.exceptions.PreexistingEntityException;
import com.mycompany.okitegami.controllers.exceptions.RollbackFailureException;
import com.mycompany.okitegami.models.ContratosEmpleados;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.okitegami.models.Empleados;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author meli_
 */
public class ContratosEmpleadosJpaController implements Serializable {

    public ContratosEmpleadosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ContratosEmpleados contratosEmpleados) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Empleados idEmpleado = contratosEmpleados.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getId());
                contratosEmpleados.setIdEmpleado(idEmpleado);
            }
            em.persist(contratosEmpleados);
            if (idEmpleado != null) {
                idEmpleado.getContratosEmpleadosList().add(contratosEmpleados);
                idEmpleado = em.merge(idEmpleado);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findContratosEmpleados(contratosEmpleados.getId()) != null) {
                throw new PreexistingEntityException("ContratosEmpleados " + contratosEmpleados + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ContratosEmpleados contratosEmpleados) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ContratosEmpleados persistentContratosEmpleados = em.find(ContratosEmpleados.class, contratosEmpleados.getId());
            Empleados idEmpleadoOld = persistentContratosEmpleados.getIdEmpleado();
            Empleados idEmpleadoNew = contratosEmpleados.getIdEmpleado();
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getId());
                contratosEmpleados.setIdEmpleado(idEmpleadoNew);
            }
            contratosEmpleados = em.merge(contratosEmpleados);
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getContratosEmpleadosList().remove(contratosEmpleados);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getContratosEmpleadosList().add(contratosEmpleados);
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
                Integer id = contratosEmpleados.getId();
                if (findContratosEmpleados(id) == null) {
                    throw new NonexistentEntityException("The contratosEmpleados with id " + id + " no longer exists.");
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
            ContratosEmpleados contratosEmpleados;
            try {
                contratosEmpleados = em.getReference(ContratosEmpleados.class, id);
                contratosEmpleados.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contratosEmpleados with id " + id + " no longer exists.", enfe);
            }
            Empleados idEmpleado = contratosEmpleados.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getContratosEmpleadosList().remove(contratosEmpleados);
                idEmpleado = em.merge(idEmpleado);
            }
            em.remove(contratosEmpleados);
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

    public List<ContratosEmpleados> findContratosEmpleadosEntities() {
        return findContratosEmpleadosEntities(true, -1, -1);
    }

    public List<ContratosEmpleados> findContratosEmpleadosEntities(int maxResults, int firstResult) {
        return findContratosEmpleadosEntities(false, maxResults, firstResult);
    }

    private List<ContratosEmpleados> findContratosEmpleadosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ContratosEmpleados.class));
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

    public ContratosEmpleados findContratosEmpleados(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ContratosEmpleados.class, id);
        } finally {
            em.close();
        }
    }

    public int getContratosEmpleadosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ContratosEmpleados> rt = cq.from(ContratosEmpleados.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
