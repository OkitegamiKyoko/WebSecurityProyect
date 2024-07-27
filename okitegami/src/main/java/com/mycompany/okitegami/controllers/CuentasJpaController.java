<<<<<<< HEAD
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers;

import com.mycompany.okitegami.controllers.exceptions.NonexistentEntityException;
import com.mycompany.okitegami.controllers.exceptions.RollbackFailureException;
import com.mycompany.okitegami.models.Cuentas;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.okitegami.models.MetodosPago;
import java.util.List;
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
public class CuentasJpaController implements Serializable {

    public CuentasJpaController() throws NamingException {
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

    public void create(Cuentas cuentas) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MetodosPago idMetPago = cuentas.getIdMetPago();
            if (idMetPago != null) {
                idMetPago = em.getReference(idMetPago.getClass(), idMetPago.getId());
                cuentas.setIdMetPago(idMetPago);
            }
            em.persist(cuentas);
            if (idMetPago != null) {
                idMetPago.getCuentasList().add(cuentas);
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

    public void edit(Cuentas cuentas) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cuentas persistentCuentas = em.find(Cuentas.class, cuentas.getId());
            MetodosPago idMetPagoOld = persistentCuentas.getIdMetPago();
            MetodosPago idMetPagoNew = cuentas.getIdMetPago();
            if (idMetPagoNew != null) {
                idMetPagoNew = em.getReference(idMetPagoNew.getClass(), idMetPagoNew.getId());
                cuentas.setIdMetPago(idMetPagoNew);
            }
            cuentas = em.merge(cuentas);
            if (idMetPagoOld != null && !idMetPagoOld.equals(idMetPagoNew)) {
                idMetPagoOld.getCuentasList().remove(cuentas);
                idMetPagoOld = em.merge(idMetPagoOld);
            }
            if (idMetPagoNew != null && !idMetPagoNew.equals(idMetPagoOld)) {
                idMetPagoNew.getCuentasList().add(cuentas);
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
                Integer id = cuentas.getId();
                if (findCuentas(id) == null) {
                    throw new NonexistentEntityException("The cuentas with id " + id + " no longer exists.");
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
            Cuentas cuentas;
            try {
                cuentas = em.getReference(Cuentas.class, id);
                cuentas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cuentas with id " + id + " no longer exists.", enfe);
            }
            MetodosPago idMetPago = cuentas.getIdMetPago();
            if (idMetPago != null) {
                idMetPago.getCuentasList().remove(cuentas);
                idMetPago = em.merge(idMetPago);
            }
            em.remove(cuentas);
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

    public List<Cuentas> findCuentasEntities() {
        return findCuentasEntities(true, -1, -1);
    }

    public List<Cuentas> findCuentasEntities(int maxResults, int firstResult) {
        return findCuentasEntities(false, maxResults, firstResult);
    }

    private List<Cuentas> findCuentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cuentas.class));
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

    public Cuentas findCuentas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cuentas.class, id);
        } finally {
            em.close();
        }
    }

    public int getCuentasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cuentas> rt = cq.from(Cuentas.class);
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
import com.mycompany.okitegami.models.Cuentas;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.okitegami.models.MetodosPago;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author meli_
 */
public class CuentasJpaController implements Serializable {

    public CuentasJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cuentas cuentas) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MetodosPago idMetPago = cuentas.getIdMetPago();
            if (idMetPago != null) {
                idMetPago = em.getReference(idMetPago.getClass(), idMetPago.getId());
                cuentas.setIdMetPago(idMetPago);
            }
            em.persist(cuentas);
            if (idMetPago != null) {
                idMetPago.getCuentasList().add(cuentas);
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

    public void edit(Cuentas cuentas) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cuentas persistentCuentas = em.find(Cuentas.class, cuentas.getId());
            MetodosPago idMetPagoOld = persistentCuentas.getIdMetPago();
            MetodosPago idMetPagoNew = cuentas.getIdMetPago();
            if (idMetPagoNew != null) {
                idMetPagoNew = em.getReference(idMetPagoNew.getClass(), idMetPagoNew.getId());
                cuentas.setIdMetPago(idMetPagoNew);
            }
            cuentas = em.merge(cuentas);
            if (idMetPagoOld != null && !idMetPagoOld.equals(idMetPagoNew)) {
                idMetPagoOld.getCuentasList().remove(cuentas);
                idMetPagoOld = em.merge(idMetPagoOld);
            }
            if (idMetPagoNew != null && !idMetPagoNew.equals(idMetPagoOld)) {
                idMetPagoNew.getCuentasList().add(cuentas);
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
                Integer id = cuentas.getId();
                if (findCuentas(id) == null) {
                    throw new NonexistentEntityException("The cuentas with id " + id + " no longer exists.");
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
            Cuentas cuentas;
            try {
                cuentas = em.getReference(Cuentas.class, id);
                cuentas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cuentas with id " + id + " no longer exists.", enfe);
            }
            MetodosPago idMetPago = cuentas.getIdMetPago();
            if (idMetPago != null) {
                idMetPago.getCuentasList().remove(cuentas);
                idMetPago = em.merge(idMetPago);
            }
            em.remove(cuentas);
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

    public List<Cuentas> findCuentasEntities() {
        return findCuentasEntities(true, -1, -1);
    }

    public List<Cuentas> findCuentasEntities(int maxResults, int firstResult) {
        return findCuentasEntities(false, maxResults, firstResult);
    }

    private List<Cuentas> findCuentasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cuentas.class));
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

    public Cuentas findCuentas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cuentas.class, id);
        } finally {
            em.close();
        }
    }

    public int getCuentasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cuentas> rt = cq.from(Cuentas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
>>>>>>> 3ec3023e315bb2dc3952b0d72da17fb39fbf9c91
