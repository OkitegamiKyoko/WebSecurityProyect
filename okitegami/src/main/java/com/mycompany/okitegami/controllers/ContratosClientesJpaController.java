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
import com.mycompany.okitegami.models.Clientes;
import com.mycompany.okitegami.models.ContratosClientes;
import com.mycompany.okitegami.models.Envios;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author meli_
 */
public class ContratosClientesJpaController implements Serializable {

    public ContratosClientesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ContratosClientes contratosClientes) throws RollbackFailureException, Exception {
        if (contratosClientes.getEnviosList() == null) {
            contratosClientes.setEnviosList(new ArrayList<Envios>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Clientes idCliente = contratosClientes.getIdCliente();
            if (idCliente != null) {
                idCliente = em.getReference(idCliente.getClass(), idCliente.getId());
                contratosClientes.setIdCliente(idCliente);
            }
            List<Envios> attachedEnviosList = new ArrayList<Envios>();
            for (Envios enviosListEnviosToAttach : contratosClientes.getEnviosList()) {
                enviosListEnviosToAttach = em.getReference(enviosListEnviosToAttach.getClass(), enviosListEnviosToAttach.getId());
                attachedEnviosList.add(enviosListEnviosToAttach);
            }
            contratosClientes.setEnviosList(attachedEnviosList);
            em.persist(contratosClientes);
            if (idCliente != null) {
                idCliente.getContratosClientesList().add(contratosClientes);
                idCliente = em.merge(idCliente);
            }
            for (Envios enviosListEnvios : contratosClientes.getEnviosList()) {
                ContratosClientes oldIdContratoOfEnviosListEnvios = enviosListEnvios.getIdContrato();
                enviosListEnvios.setIdContrato(contratosClientes);
                enviosListEnvios = em.merge(enviosListEnvios);
                if (oldIdContratoOfEnviosListEnvios != null) {
                    oldIdContratoOfEnviosListEnvios.getEnviosList().remove(enviosListEnvios);
                    oldIdContratoOfEnviosListEnvios = em.merge(oldIdContratoOfEnviosListEnvios);
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

    public void edit(ContratosClientes contratosClientes) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ContratosClientes persistentContratosClientes = em.find(ContratosClientes.class, contratosClientes.getId());
            Clientes idClienteOld = persistentContratosClientes.getIdCliente();
            Clientes idClienteNew = contratosClientes.getIdCliente();
            List<Envios> enviosListOld = persistentContratosClientes.getEnviosList();
            List<Envios> enviosListNew = contratosClientes.getEnviosList();
            List<String> illegalOrphanMessages = null;
            for (Envios enviosListOldEnvios : enviosListOld) {
                if (!enviosListNew.contains(enviosListOldEnvios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Envios " + enviosListOldEnvios + " since its idContrato field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idClienteNew != null) {
                idClienteNew = em.getReference(idClienteNew.getClass(), idClienteNew.getId());
                contratosClientes.setIdCliente(idClienteNew);
            }
            List<Envios> attachedEnviosListNew = new ArrayList<Envios>();
            for (Envios enviosListNewEnviosToAttach : enviosListNew) {
                enviosListNewEnviosToAttach = em.getReference(enviosListNewEnviosToAttach.getClass(), enviosListNewEnviosToAttach.getId());
                attachedEnviosListNew.add(enviosListNewEnviosToAttach);
            }
            enviosListNew = attachedEnviosListNew;
            contratosClientes.setEnviosList(enviosListNew);
            contratosClientes = em.merge(contratosClientes);
            if (idClienteOld != null && !idClienteOld.equals(idClienteNew)) {
                idClienteOld.getContratosClientesList().remove(contratosClientes);
                idClienteOld = em.merge(idClienteOld);
            }
            if (idClienteNew != null && !idClienteNew.equals(idClienteOld)) {
                idClienteNew.getContratosClientesList().add(contratosClientes);
                idClienteNew = em.merge(idClienteNew);
            }
            for (Envios enviosListNewEnvios : enviosListNew) {
                if (!enviosListOld.contains(enviosListNewEnvios)) {
                    ContratosClientes oldIdContratoOfEnviosListNewEnvios = enviosListNewEnvios.getIdContrato();
                    enviosListNewEnvios.setIdContrato(contratosClientes);
                    enviosListNewEnvios = em.merge(enviosListNewEnvios);
                    if (oldIdContratoOfEnviosListNewEnvios != null && !oldIdContratoOfEnviosListNewEnvios.equals(contratosClientes)) {
                        oldIdContratoOfEnviosListNewEnvios.getEnviosList().remove(enviosListNewEnvios);
                        oldIdContratoOfEnviosListNewEnvios = em.merge(oldIdContratoOfEnviosListNewEnvios);
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
                Integer id = contratosClientes.getId();
                if (findContratosClientes(id) == null) {
                    throw new NonexistentEntityException("The contratosClientes with id " + id + " no longer exists.");
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
            ContratosClientes contratosClientes;
            try {
                contratosClientes = em.getReference(ContratosClientes.class, id);
                contratosClientes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contratosClientes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Envios> enviosListOrphanCheck = contratosClientes.getEnviosList();
            for (Envios enviosListOrphanCheckEnvios : enviosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ContratosClientes (" + contratosClientes + ") cannot be destroyed since the Envios " + enviosListOrphanCheckEnvios + " in its enviosList field has a non-nullable idContrato field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Clientes idCliente = contratosClientes.getIdCliente();
            if (idCliente != null) {
                idCliente.getContratosClientesList().remove(contratosClientes);
                idCliente = em.merge(idCliente);
            }
            em.remove(contratosClientes);
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

    public List<ContratosClientes> findContratosClientesEntities() {
        return findContratosClientesEntities(true, -1, -1);
    }

    public List<ContratosClientes> findContratosClientesEntities(int maxResults, int firstResult) {
        return findContratosClientesEntities(false, maxResults, firstResult);
    }

    private List<ContratosClientes> findContratosClientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ContratosClientes.class));
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

    public ContratosClientes findContratosClientes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ContratosClientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getContratosClientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ContratosClientes> rt = cq.from(ContratosClientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
