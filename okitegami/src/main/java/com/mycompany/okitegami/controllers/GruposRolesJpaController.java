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
import com.mycompany.okitegami.models.Grupos;
import com.mycompany.okitegami.models.GruposRoles;
import com.mycompany.okitegami.models.Roles;
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
public class GruposRolesJpaController implements Serializable {

    public GruposRolesJpaController() throws NamingException {
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

    public void create(GruposRoles gruposRoles) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Grupos idGrupo = gruposRoles.getIdGrupo();
            if (idGrupo != null) {
                idGrupo = em.getReference(idGrupo.getClass(), idGrupo.getId());
                gruposRoles.setIdGrupo(idGrupo);
            }
            Roles idRol = gruposRoles.getIdRol();
            if (idRol != null) {
                idRol = em.getReference(idRol.getClass(), idRol.getId());
                gruposRoles.setIdRol(idRol);
            }
            em.persist(gruposRoles);
            if (idGrupo != null) {
                idGrupo.getGruposRolesList().add(gruposRoles);
                idGrupo = em.merge(idGrupo);
            }
            if (idRol != null) {
                idRol.getGruposRolesList().add(gruposRoles);
                idRol = em.merge(idRol);
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

    public void edit(GruposRoles gruposRoles) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            GruposRoles persistentGruposRoles = em.find(GruposRoles.class, gruposRoles.getId());
            Grupos idGrupoOld = persistentGruposRoles.getIdGrupo();
            Grupos idGrupoNew = gruposRoles.getIdGrupo();
            Roles idRolOld = persistentGruposRoles.getIdRol();
            Roles idRolNew = gruposRoles.getIdRol();
            if (idGrupoNew != null) {
                idGrupoNew = em.getReference(idGrupoNew.getClass(), idGrupoNew.getId());
                gruposRoles.setIdGrupo(idGrupoNew);
            }
            if (idRolNew != null) {
                idRolNew = em.getReference(idRolNew.getClass(), idRolNew.getId());
                gruposRoles.setIdRol(idRolNew);
            }
            gruposRoles = em.merge(gruposRoles);
            if (idGrupoOld != null && !idGrupoOld.equals(idGrupoNew)) {
                idGrupoOld.getGruposRolesList().remove(gruposRoles);
                idGrupoOld = em.merge(idGrupoOld);
            }
            if (idGrupoNew != null && !idGrupoNew.equals(idGrupoOld)) {
                idGrupoNew.getGruposRolesList().add(gruposRoles);
                idGrupoNew = em.merge(idGrupoNew);
            }
            if (idRolOld != null && !idRolOld.equals(idRolNew)) {
                idRolOld.getGruposRolesList().remove(gruposRoles);
                idRolOld = em.merge(idRolOld);
            }
            if (idRolNew != null && !idRolNew.equals(idRolOld)) {
                idRolNew.getGruposRolesList().add(gruposRoles);
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
                Integer id = gruposRoles.getId();
                if (findGruposRoles(id) == null) {
                    throw new NonexistentEntityException("The gruposRoles with id " + id + " no longer exists.");
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
            GruposRoles gruposRoles;
            try {
                gruposRoles = em.getReference(GruposRoles.class, id);
                gruposRoles.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The gruposRoles with id " + id + " no longer exists.", enfe);
            }
            Grupos idGrupo = gruposRoles.getIdGrupo();
            if (idGrupo != null) {
                idGrupo.getGruposRolesList().remove(gruposRoles);
                idGrupo = em.merge(idGrupo);
            }
            Roles idRol = gruposRoles.getIdRol();
            if (idRol != null) {
                idRol.getGruposRolesList().remove(gruposRoles);
                idRol = em.merge(idRol);
            }
            em.remove(gruposRoles);
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

    public List<GruposRoles> findGruposRolesEntities() {
        return findGruposRolesEntities(true, -1, -1);
    }

    public List<GruposRoles> findGruposRolesEntities(int maxResults, int firstResult) {
        return findGruposRolesEntities(false, maxResults, firstResult);
    }

    private List<GruposRoles> findGruposRolesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GruposRoles.class));
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

    public GruposRoles findGruposRoles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GruposRoles.class, id);
        } finally {
            em.close();
        }
    }

    public int getGruposRolesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GruposRoles> rt = cq.from(GruposRoles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
