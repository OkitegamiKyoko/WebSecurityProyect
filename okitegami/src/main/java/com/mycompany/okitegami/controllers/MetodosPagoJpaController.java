/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers;

import com.mycompany.okitegami.controllers.exceptions.IllegalOrphanException;
import com.mycompany.okitegami.controllers.exceptions.NonexistentEntityException;
import com.mycompany.okitegami.controllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.okitegami.models.Users;
import com.mycompany.okitegami.models.Tarjetas;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.okitegami.models.Cuentas;
import com.mycompany.okitegami.models.MetodosPago;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author meli_
 */
public class MetodosPagoJpaController implements Serializable {

    public MetodosPagoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MetodosPago metodosPago) throws RollbackFailureException, Exception {
        if (metodosPago.getTarjetasList() == null) {
            metodosPago.setTarjetasList(new ArrayList<Tarjetas>());
        }
        if (metodosPago.getCuentasList() == null) {
            metodosPago.setCuentasList(new ArrayList<Cuentas>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Users idUser = metodosPago.getIdUser();
            if (idUser != null) {
                idUser = em.getReference(idUser.getClass(), idUser.getId());
                metodosPago.setIdUser(idUser);
            }
            List<Tarjetas> attachedTarjetasList = new ArrayList<Tarjetas>();
            for (Tarjetas tarjetasListTarjetasToAttach : metodosPago.getTarjetasList()) {
                tarjetasListTarjetasToAttach = em.getReference(tarjetasListTarjetasToAttach.getClass(), tarjetasListTarjetasToAttach.getId());
                attachedTarjetasList.add(tarjetasListTarjetasToAttach);
            }
            metodosPago.setTarjetasList(attachedTarjetasList);
            List<Cuentas> attachedCuentasList = new ArrayList<Cuentas>();
            for (Cuentas cuentasListCuentasToAttach : metodosPago.getCuentasList()) {
                cuentasListCuentasToAttach = em.getReference(cuentasListCuentasToAttach.getClass(), cuentasListCuentasToAttach.getId());
                attachedCuentasList.add(cuentasListCuentasToAttach);
            }
            metodosPago.setCuentasList(attachedCuentasList);
            em.persist(metodosPago);
            if (idUser != null) {
                idUser.getMetodosPagoList().add(metodosPago);
                idUser = em.merge(idUser);
            }
            for (Tarjetas tarjetasListTarjetas : metodosPago.getTarjetasList()) {
                MetodosPago oldIdMetPagoOfTarjetasListTarjetas = tarjetasListTarjetas.getIdMetPago();
                tarjetasListTarjetas.setIdMetPago(metodosPago);
                tarjetasListTarjetas = em.merge(tarjetasListTarjetas);
                if (oldIdMetPagoOfTarjetasListTarjetas != null) {
                    oldIdMetPagoOfTarjetasListTarjetas.getTarjetasList().remove(tarjetasListTarjetas);
                    oldIdMetPagoOfTarjetasListTarjetas = em.merge(oldIdMetPagoOfTarjetasListTarjetas);
                }
            }
            for (Cuentas cuentasListCuentas : metodosPago.getCuentasList()) {
                MetodosPago oldIdMetPagoOfCuentasListCuentas = cuentasListCuentas.getIdMetPago();
                cuentasListCuentas.setIdMetPago(metodosPago);
                cuentasListCuentas = em.merge(cuentasListCuentas);
                if (oldIdMetPagoOfCuentasListCuentas != null) {
                    oldIdMetPagoOfCuentasListCuentas.getCuentasList().remove(cuentasListCuentas);
                    oldIdMetPagoOfCuentasListCuentas = em.merge(oldIdMetPagoOfCuentasListCuentas);
                }
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

    public void edit(MetodosPago metodosPago) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MetodosPago persistentMetodosPago = em.find(MetodosPago.class, metodosPago.getId());
            Users idUserOld = persistentMetodosPago.getIdUser();
            Users idUserNew = metodosPago.getIdUser();
            List<Tarjetas> tarjetasListOld = persistentMetodosPago.getTarjetasList();
            List<Tarjetas> tarjetasListNew = metodosPago.getTarjetasList();
            List<Cuentas> cuentasListOld = persistentMetodosPago.getCuentasList();
            List<Cuentas> cuentasListNew = metodosPago.getCuentasList();
            List<String> illegalOrphanMessages = null;
            for (Tarjetas tarjetasListOldTarjetas : tarjetasListOld) {
                if (!tarjetasListNew.contains(tarjetasListOldTarjetas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Tarjetas " + tarjetasListOldTarjetas + " since its idMetPago field is not nullable.");
                }
            }
            for (Cuentas cuentasListOldCuentas : cuentasListOld) {
                if (!cuentasListNew.contains(cuentasListOldCuentas)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cuentas " + cuentasListOldCuentas + " since its idMetPago field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idUserNew != null) {
                idUserNew = em.getReference(idUserNew.getClass(), idUserNew.getId());
                metodosPago.setIdUser(idUserNew);
            }
            List<Tarjetas> attachedTarjetasListNew = new ArrayList<Tarjetas>();
            for (Tarjetas tarjetasListNewTarjetasToAttach : tarjetasListNew) {
                tarjetasListNewTarjetasToAttach = em.getReference(tarjetasListNewTarjetasToAttach.getClass(), tarjetasListNewTarjetasToAttach.getId());
                attachedTarjetasListNew.add(tarjetasListNewTarjetasToAttach);
            }
            tarjetasListNew = attachedTarjetasListNew;
            metodosPago.setTarjetasList(tarjetasListNew);
            List<Cuentas> attachedCuentasListNew = new ArrayList<Cuentas>();
            for (Cuentas cuentasListNewCuentasToAttach : cuentasListNew) {
                cuentasListNewCuentasToAttach = em.getReference(cuentasListNewCuentasToAttach.getClass(), cuentasListNewCuentasToAttach.getId());
                attachedCuentasListNew.add(cuentasListNewCuentasToAttach);
            }
            cuentasListNew = attachedCuentasListNew;
            metodosPago.setCuentasList(cuentasListNew);
            metodosPago = em.merge(metodosPago);
            if (idUserOld != null && !idUserOld.equals(idUserNew)) {
                idUserOld.getMetodosPagoList().remove(metodosPago);
                idUserOld = em.merge(idUserOld);
            }
            if (idUserNew != null && !idUserNew.equals(idUserOld)) {
                idUserNew.getMetodosPagoList().add(metodosPago);
                idUserNew = em.merge(idUserNew);
            }
            for (Tarjetas tarjetasListNewTarjetas : tarjetasListNew) {
                if (!tarjetasListOld.contains(tarjetasListNewTarjetas)) {
                    MetodosPago oldIdMetPagoOfTarjetasListNewTarjetas = tarjetasListNewTarjetas.getIdMetPago();
                    tarjetasListNewTarjetas.setIdMetPago(metodosPago);
                    tarjetasListNewTarjetas = em.merge(tarjetasListNewTarjetas);
                    if (oldIdMetPagoOfTarjetasListNewTarjetas != null && !oldIdMetPagoOfTarjetasListNewTarjetas.equals(metodosPago)) {
                        oldIdMetPagoOfTarjetasListNewTarjetas.getTarjetasList().remove(tarjetasListNewTarjetas);
                        oldIdMetPagoOfTarjetasListNewTarjetas = em.merge(oldIdMetPagoOfTarjetasListNewTarjetas);
                    }
                }
            }
            for (Cuentas cuentasListNewCuentas : cuentasListNew) {
                if (!cuentasListOld.contains(cuentasListNewCuentas)) {
                    MetodosPago oldIdMetPagoOfCuentasListNewCuentas = cuentasListNewCuentas.getIdMetPago();
                    cuentasListNewCuentas.setIdMetPago(metodosPago);
                    cuentasListNewCuentas = em.merge(cuentasListNewCuentas);
                    if (oldIdMetPagoOfCuentasListNewCuentas != null && !oldIdMetPagoOfCuentasListNewCuentas.equals(metodosPago)) {
                        oldIdMetPagoOfCuentasListNewCuentas.getCuentasList().remove(cuentasListNewCuentas);
                        oldIdMetPagoOfCuentasListNewCuentas = em.merge(oldIdMetPagoOfCuentasListNewCuentas);
                    }
                }
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
                Integer id = metodosPago.getId();
                if (findMetodosPago(id) == null) {
                    throw new NonexistentEntityException("The metodosPago with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MetodosPago metodosPago;
            try {
                metodosPago = em.getReference(MetodosPago.class, id);
                metodosPago.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The metodosPago with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Tarjetas> tarjetasListOrphanCheck = metodosPago.getTarjetasList();
            for (Tarjetas tarjetasListOrphanCheckTarjetas : tarjetasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MetodosPago (" + metodosPago + ") cannot be destroyed since the Tarjetas " + tarjetasListOrphanCheckTarjetas + " in its tarjetasList field has a non-nullable idMetPago field.");
            }
            List<Cuentas> cuentasListOrphanCheck = metodosPago.getCuentasList();
            for (Cuentas cuentasListOrphanCheckCuentas : cuentasListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MetodosPago (" + metodosPago + ") cannot be destroyed since the Cuentas " + cuentasListOrphanCheckCuentas + " in its cuentasList field has a non-nullable idMetPago field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Users idUser = metodosPago.getIdUser();
            if (idUser != null) {
                idUser.getMetodosPagoList().remove(metodosPago);
                idUser = em.merge(idUser);
            }
            em.remove(metodosPago);
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

    public List<MetodosPago> findMetodosPagoEntities() {
        return findMetodosPagoEntities(true, -1, -1);
    }

    public List<MetodosPago> findMetodosPagoEntities(int maxResults, int firstResult) {
        return findMetodosPagoEntities(false, maxResults, firstResult);
    }

    private List<MetodosPago> findMetodosPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MetodosPago.class));
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

    public MetodosPago findMetodosPago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MetodosPago.class, id);
        } finally {
            em.close();
        }
    }

    public int getMetodosPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MetodosPago> rt = cq.from(MetodosPago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
