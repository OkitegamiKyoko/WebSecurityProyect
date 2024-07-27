/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers;

import com.mycompany.okitegami.controllers.exceptions.IllegalOrphanException;
import com.mycompany.okitegami.controllers.exceptions.NonexistentEntityException;
import com.mycompany.okitegami.controllers.exceptions.RollbackFailureException;
import com.mycompany.okitegami.models.Estados;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.okitegami.models.Municipios;
import java.util.ArrayList;
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
@Named("estadosController")
@ApplicationScoped
public class EstadosJpaController implements Serializable {

    public EstadosJpaController() throws NamingException {
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

    public void create(Estados estados) throws RollbackFailureException, Exception {
        if (estados.getMunicipiosList() == null) {
            estados.setMunicipiosList(new ArrayList<Municipios>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Municipios> attachedMunicipiosList = new ArrayList<Municipios>();
            for (Municipios municipiosListMunicipiosToAttach : estados.getMunicipiosList()) {
                municipiosListMunicipiosToAttach = em.getReference(municipiosListMunicipiosToAttach.getClass(), municipiosListMunicipiosToAttach.getId());
                attachedMunicipiosList.add(municipiosListMunicipiosToAttach);
            }
            estados.setMunicipiosList(attachedMunicipiosList);
            em.persist(estados);
            for (Municipios municipiosListMunicipios : estados.getMunicipiosList()) {
                Estados oldIdEstadoOfMunicipiosListMunicipios = municipiosListMunicipios.getIdEstado();
                municipiosListMunicipios.setIdEstado(estados);
                municipiosListMunicipios = em.merge(municipiosListMunicipios);
                if (oldIdEstadoOfMunicipiosListMunicipios != null) {
                    oldIdEstadoOfMunicipiosListMunicipios.getMunicipiosList().remove(municipiosListMunicipios);
                    oldIdEstadoOfMunicipiosListMunicipios = em.merge(oldIdEstadoOfMunicipiosListMunicipios);
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

    public void edit(Estados estados) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Estados persistentEstados = em.find(Estados.class, estados.getId());
            List<Municipios> municipiosListOld = persistentEstados.getMunicipiosList();
            List<Municipios> municipiosListNew = estados.getMunicipiosList();
            List<String> illegalOrphanMessages = null;
            for (Municipios municipiosListOldMunicipios : municipiosListOld) {
                if (!municipiosListNew.contains(municipiosListOldMunicipios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Municipios " + municipiosListOldMunicipios + " since its idEstado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Municipios> attachedMunicipiosListNew = new ArrayList<Municipios>();
            for (Municipios municipiosListNewMunicipiosToAttach : municipiosListNew) {
                municipiosListNewMunicipiosToAttach = em.getReference(municipiosListNewMunicipiosToAttach.getClass(), municipiosListNewMunicipiosToAttach.getId());
                attachedMunicipiosListNew.add(municipiosListNewMunicipiosToAttach);
            }
            municipiosListNew = attachedMunicipiosListNew;
            estados.setMunicipiosList(municipiosListNew);
            estados = em.merge(estados);
            for (Municipios municipiosListNewMunicipios : municipiosListNew) {
                if (!municipiosListOld.contains(municipiosListNewMunicipios)) {
                    Estados oldIdEstadoOfMunicipiosListNewMunicipios = municipiosListNewMunicipios.getIdEstado();
                    municipiosListNewMunicipios.setIdEstado(estados);
                    municipiosListNewMunicipios = em.merge(municipiosListNewMunicipios);
                    if (oldIdEstadoOfMunicipiosListNewMunicipios != null && !oldIdEstadoOfMunicipiosListNewMunicipios.equals(estados)) {
                        oldIdEstadoOfMunicipiosListNewMunicipios.getMunicipiosList().remove(municipiosListNewMunicipios);
                        oldIdEstadoOfMunicipiosListNewMunicipios = em.merge(oldIdEstadoOfMunicipiosListNewMunicipios);
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
                Integer id = estados.getId();
                if (findEstados(id) == null) {
                    throw new NonexistentEntityException("The estados with id " + id + " no longer exists.");
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
            Estados estados;
            try {
                estados = em.getReference(Estados.class, id);
                estados.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estados with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Municipios> municipiosListOrphanCheck = estados.getMunicipiosList();
            for (Municipios municipiosListOrphanCheckMunicipios : municipiosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estados (" + estados + ") cannot be destroyed since the Municipios " + municipiosListOrphanCheckMunicipios + " in its municipiosList field has a non-nullable idEstado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estados);
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

    public List<Estados> findEstadosEntities() {
        return findEstadosEntities(true, -1, -1);
    }

    public List<Estados> findEstadosEntities(int maxResults, int firstResult) {
        return findEstadosEntities(false, maxResults, firstResult);
    }

    private List<Estados> findEstadosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estados.class));
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

    public Estados findEstados(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estados.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estados> rt = cq.from(Estados.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
