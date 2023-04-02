/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers;

import com.mycompany.okitegami.controllers.exceptions.IllegalOrphanException;
import com.mycompany.okitegami.controllers.exceptions.NonexistentEntityException;
import com.mycompany.okitegami.controllers.exceptions.RollbackFailureException;
import com.mycompany.okitegami.models.Departamentos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.okitegami.models.Puestos;
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
@Named("departmentController")
@ApplicationScoped
public class DepartamentosJpaController implements Serializable {

    public DepartamentosJpaController() throws NamingException {
        this.utx = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        this.emf = Persistence.createEntityManagerFactory("okitegami_db_pool");
    }
    private UserTransaction utx;
    private EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departamentos departamentos) throws RollbackFailureException, Exception {
        if (departamentos.getPuestosList() == null) {
            departamentos.setPuestosList(new ArrayList<Puestos>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Puestos> attachedPuestosList = new ArrayList<Puestos>();
            for (Puestos puestosListPuestosToAttach : departamentos.getPuestosList()) {
                puestosListPuestosToAttach = em.getReference(puestosListPuestosToAttach.getClass(), puestosListPuestosToAttach.getId());
                attachedPuestosList.add(puestosListPuestosToAttach);
            }
            departamentos.setPuestosList(attachedPuestosList);
            em.persist(departamentos);
            for (Puestos puestosListPuestos : departamentos.getPuestosList()) {
                Departamentos oldIdDepartOfPuestosListPuestos = puestosListPuestos.getIdDepart();
                puestosListPuestos.setIdDepart(departamentos);
                puestosListPuestos = em.merge(puestosListPuestos);
                if (oldIdDepartOfPuestosListPuestos != null) {
                    oldIdDepartOfPuestosListPuestos.getPuestosList().remove(puestosListPuestos);
                    oldIdDepartOfPuestosListPuestos = em.merge(oldIdDepartOfPuestosListPuestos);
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

    public void edit(Departamentos departamentos) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Departamentos persistentDepartamentos = em.find(Departamentos.class, departamentos.getId());
            List<Puestos> puestosListOld = persistentDepartamentos.getPuestosList();
            List<Puestos> puestosListNew = departamentos.getPuestosList();
            List<String> illegalOrphanMessages = null;
            for (Puestos puestosListOldPuestos : puestosListOld) {
                if (!puestosListNew.contains(puestosListOldPuestos)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Puestos " + puestosListOldPuestos + " since its idDepart field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Puestos> attachedPuestosListNew = new ArrayList<Puestos>();
            for (Puestos puestosListNewPuestosToAttach : puestosListNew) {
                puestosListNewPuestosToAttach = em.getReference(puestosListNewPuestosToAttach.getClass(), puestosListNewPuestosToAttach.getId());
                attachedPuestosListNew.add(puestosListNewPuestosToAttach);
            }
            puestosListNew = attachedPuestosListNew;
            departamentos.setPuestosList(puestosListNew);
            departamentos = em.merge(departamentos);
            for (Puestos puestosListNewPuestos : puestosListNew) {
                if (!puestosListOld.contains(puestosListNewPuestos)) {
                    Departamentos oldIdDepartOfPuestosListNewPuestos = puestosListNewPuestos.getIdDepart();
                    puestosListNewPuestos.setIdDepart(departamentos);
                    puestosListNewPuestos = em.merge(puestosListNewPuestos);
                    if (oldIdDepartOfPuestosListNewPuestos != null && !oldIdDepartOfPuestosListNewPuestos.equals(departamentos)) {
                        oldIdDepartOfPuestosListNewPuestos.getPuestosList().remove(puestosListNewPuestos);
                        oldIdDepartOfPuestosListNewPuestos = em.merge(oldIdDepartOfPuestosListNewPuestos);
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
                Integer id = departamentos.getId();
                if (findDepartamentos(id) == null) {
                    throw new NonexistentEntityException("The departamentos with id " + id + " no longer exists.");
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
            Departamentos departamentos;
            try {
                departamentos = em.getReference(Departamentos.class, id);
                departamentos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamentos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Puestos> puestosListOrphanCheck = departamentos.getPuestosList();
            for (Puestos puestosListOrphanCheckPuestos : puestosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamentos (" + departamentos + ") cannot be destroyed since the Puestos " + puestosListOrphanCheckPuestos + " in its puestosList field has a non-nullable idDepart field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(departamentos);
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

    public List<Departamentos> findDepartamentosEntities() {
        return findDepartamentosEntities(true, -1, -1);
    }

    public List<Departamentos> findDepartamentosEntities(int maxResults, int firstResult) {
        return findDepartamentosEntities(false, maxResults, firstResult);
    }

    private List<Departamentos> findDepartamentosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departamentos.class));
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

    public Departamentos findDepartamentos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departamentos.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departamentos> rt = cq.from(Departamentos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
