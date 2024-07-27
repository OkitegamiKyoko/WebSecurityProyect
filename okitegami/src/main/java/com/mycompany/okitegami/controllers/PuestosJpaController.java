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
import com.mycompany.okitegami.models.Departamentos;
import com.mycompany.okitegami.models.Empleados;
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
@Named("jobsController")
@ApplicationScoped
public class PuestosJpaController implements Serializable {

    public PuestosJpaController()throws NamingException {
        this.utx = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        this.emf = Persistence.createEntityManagerFactory("okitegami_db_pool");
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Puestos puestos) throws RollbackFailureException, Exception {
        if (puestos.getEmpleadosList() == null) {
            puestos.setEmpleadosList(new ArrayList<Empleados>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Departamentos idDepart = puestos.getIdDepart();
            if (idDepart != null) {
                idDepart = em.getReference(idDepart.getClass(), idDepart.getId());
                puestos.setIdDepart(idDepart);
            }
            List<Empleados> attachedEmpleadosList = new ArrayList<Empleados>();
            for (Empleados empleadosListEmpleadosToAttach : puestos.getEmpleadosList()) {
                empleadosListEmpleadosToAttach = em.getReference(empleadosListEmpleadosToAttach.getClass(), empleadosListEmpleadosToAttach.getId());
                attachedEmpleadosList.add(empleadosListEmpleadosToAttach);
            }
            puestos.setEmpleadosList(attachedEmpleadosList);
            em.persist(puestos);
            if (idDepart != null) {
                idDepart.getPuestosList().add(puestos);
                idDepart = em.merge(idDepart);
            }
            for (Empleados empleadosListEmpleados : puestos.getEmpleadosList()) {
                Puestos oldIdPuestoOfEmpleadosListEmpleados = empleadosListEmpleados.getIdPuesto();
                empleadosListEmpleados.setIdPuesto(puestos);
                empleadosListEmpleados = em.merge(empleadosListEmpleados);
                if (oldIdPuestoOfEmpleadosListEmpleados != null) {
                    oldIdPuestoOfEmpleadosListEmpleados.getEmpleadosList().remove(empleadosListEmpleados);
                    oldIdPuestoOfEmpleadosListEmpleados = em.merge(oldIdPuestoOfEmpleadosListEmpleados);
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

    public void edit(Puestos puestos) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Puestos persistentPuestos = em.find(Puestos.class, puestos.getId());
            Departamentos idDepartOld = persistentPuestos.getIdDepart();
            Departamentos idDepartNew = puestos.getIdDepart();
            List<Empleados> empleadosListOld = persistentPuestos.getEmpleadosList();
            List<Empleados> empleadosListNew = puestos.getEmpleadosList();
            List<String> illegalOrphanMessages = null;
            for (Empleados empleadosListOldEmpleados : empleadosListOld) {
                if (!empleadosListNew.contains(empleadosListOldEmpleados)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleados " + empleadosListOldEmpleados + " since its idPuesto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idDepartNew != null) {
                idDepartNew = em.getReference(idDepartNew.getClass(), idDepartNew.getId());
                puestos.setIdDepart(idDepartNew);
            }
            List<Empleados> attachedEmpleadosListNew = new ArrayList<Empleados>();
            for (Empleados empleadosListNewEmpleadosToAttach : empleadosListNew) {
                empleadosListNewEmpleadosToAttach = em.getReference(empleadosListNewEmpleadosToAttach.getClass(), empleadosListNewEmpleadosToAttach.getId());
                attachedEmpleadosListNew.add(empleadosListNewEmpleadosToAttach);
            }
            empleadosListNew = attachedEmpleadosListNew;
            puestos.setEmpleadosList(empleadosListNew);
            puestos = em.merge(puestos);
            if (idDepartOld != null && !idDepartOld.equals(idDepartNew)) {
                idDepartOld.getPuestosList().remove(puestos);
                idDepartOld = em.merge(idDepartOld);
            }
            if (idDepartNew != null && !idDepartNew.equals(idDepartOld)) {
                idDepartNew.getPuestosList().add(puestos);
                idDepartNew = em.merge(idDepartNew);
            }
            for (Empleados empleadosListNewEmpleados : empleadosListNew) {
                if (!empleadosListOld.contains(empleadosListNewEmpleados)) {
                    Puestos oldIdPuestoOfEmpleadosListNewEmpleados = empleadosListNewEmpleados.getIdPuesto();
                    empleadosListNewEmpleados.setIdPuesto(puestos);
                    empleadosListNewEmpleados = em.merge(empleadosListNewEmpleados);
                    if (oldIdPuestoOfEmpleadosListNewEmpleados != null && !oldIdPuestoOfEmpleadosListNewEmpleados.equals(puestos)) {
                        oldIdPuestoOfEmpleadosListNewEmpleados.getEmpleadosList().remove(empleadosListNewEmpleados);
                        oldIdPuestoOfEmpleadosListNewEmpleados = em.merge(oldIdPuestoOfEmpleadosListNewEmpleados);
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
                Integer id = puestos.getId();
                if (findPuestos(id) == null) {
                    throw new NonexistentEntityException("The puestos with id " + id + " no longer exists.");
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
            Puestos puestos;
            try {
                puestos = em.getReference(Puestos.class, id);
                puestos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The puestos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Empleados> empleadosListOrphanCheck = puestos.getEmpleadosList();
            for (Empleados empleadosListOrphanCheckEmpleados : empleadosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Puestos (" + puestos + ") cannot be destroyed since the Empleados " + empleadosListOrphanCheckEmpleados + " in its empleadosList field has a non-nullable idPuesto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Departamentos idDepart = puestos.getIdDepart();
            if (idDepart != null) {
                idDepart.getPuestosList().remove(puestos);
                idDepart = em.merge(idDepart);
            }
            em.remove(puestos);
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

    public List<Puestos> findPuestosEntities() {
        return findPuestosEntities(true, -1, -1);
    }

    public List<Puestos> findPuestosEntities(int maxResults, int firstResult) {
        return findPuestosEntities(false, maxResults, firstResult);
    }

    private List<Puestos> findPuestosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Puestos.class));
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

    public Puestos findPuestos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Puestos.class, id);
        } finally {
            em.close();
        }
    }

    public int getPuestosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Puestos> rt = cq.from(Puestos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
