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
import com.mycompany.okitegami.models.MetodosPago;
import com.mycompany.okitegami.models.Tarjetas;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author meli_
 */
public class TarjetasJpaController implements Serializable {

    public TarjetasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tarjetas tarjetas) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MetodosPago idMetPago = tarjetas.getIdMetPago();
            if (idMetPago != null) {
                idMetPago = em.getReference(idMetPago.getClass(), idMetPago.getId());
                tarjetas.setIdMetPago(idMetPago);
            }
            em.persist(tarjetas);
            if (idMetPago != null) {
                idMetPago.getTarjetasList().add(tarjetas);
                idMetPago = em.merge(idMetPago);
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

    public void edit(Tarjetas tarjetas) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tarjetas persistentTarjetas = em.find(Tarjetas.class, tarjetas.getId());
            MetodosPago idMetPagoOld = persistentTarjetas.getIdMetPago();
            MetodosPago idMetPagoNew = tarjetas.getIdMetPago();
            if (idMetPagoNew != null) {
                idMetPagoNew = em.getReference(idMetPagoNew.getClass(), idMetPagoNew.getId());
                tarjetas.setIdMetPago(idMetPagoNew);
            }
            tarjetas = em.merge(tarjetas);
            if (idMetPagoOld != null && !idMetPagoOld.equals(idMetPagoNew)) {
                idMetPagoOld.getTarjetasList().remove(tarjetas);
                idMetPagoOld = em.merge(idMetPagoOld);
            }
            if (idMetPagoNew != null && !idMetPagoNew.equals(idMetPagoOld)) {
                idMetPagoNew.getTarjetasList().add(tarjetas);
                idMetPagoNew = em.merge(idMetPagoNew);
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
                Integer id = tarjetas.getId();
                if (findTarjetas(id) == null) {
                    throw new NonexistentEntityException("The tarjetas with id " + id + " no longer exists.");
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
            Tarjetas tarjetas;
            try {
                tarjetas = em.getReference(Tarjetas.class, id);
                tarjetas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tarjetas with id " + id + " no longer exists.", enfe);
            }
            MetodosPago idMetPago = tarjetas.getIdMetPago();
            if (idMetPago != null) {
                idMetPago.getTarjetasList().remove(tarjetas);
                idMetPago = em.merge(idMetPago);
            }
            em.remove(tarjetas);
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

    public List<Tarjetas> findTarjetasEntities() {
        return findTarjetasEntities(true, -1, -1);
    }

    public List<Tarjetas> findTarjetasEntities(int maxResults, int firstResult) {
        return findTarjetasEntities(false, maxResults, firstResult);
    }

    private List<Tarjetas> findTarjetasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tarjetas.class));
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

    public Tarjetas findTarjetas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tarjetas.class, id);
        } finally {
            em.close();
        }
    }

    public int getTarjetasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tarjetas> rt = cq.from(Tarjetas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
