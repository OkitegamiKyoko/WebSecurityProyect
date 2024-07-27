/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers;

import com.mycompany.okitegami.controllers.exceptions.IllegalOrphanException;
import com.mycompany.okitegami.controllers.exceptions.NonexistentEntityException;
import com.mycompany.okitegami.controllers.exceptions.RollbackFailureException;
import com.mycompany.okitegami.models.Grupos;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.okitegami.models.GruposRoles;
import java.util.ArrayList;
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
public class GruposJpaController implements Serializable {

    public GruposJpaController() throws NamingException {
        this.utx = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        this.emf = Persistence.createEntityManagerFactory("okitegami_db_pool");
    }
    private UserTransaction utx;
    private EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Grupos grupos) throws RollbackFailureException, Exception {
        if (grupos.getGruposRolesList() == null) {
            grupos.setGruposRolesList(new ArrayList<GruposRoles>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<GruposRoles> attachedGruposRolesList = new ArrayList<GruposRoles>();
            for (GruposRoles gruposRolesListGruposRolesToAttach : grupos.getGruposRolesList()) {
                gruposRolesListGruposRolesToAttach = em.getReference(gruposRolesListGruposRolesToAttach.getClass(), gruposRolesListGruposRolesToAttach.getId());
                attachedGruposRolesList.add(gruposRolesListGruposRolesToAttach);
            }
            grupos.setGruposRolesList(attachedGruposRolesList);
            em.persist(grupos);
            for (GruposRoles gruposRolesListGruposRoles : grupos.getGruposRolesList()) {
                Grupos oldIdGrupoOfGruposRolesListGruposRoles = gruposRolesListGruposRoles.getIdGrupo();
                gruposRolesListGruposRoles.setIdGrupo(grupos);
                gruposRolesListGruposRoles = em.merge(gruposRolesListGruposRoles);
                if (oldIdGrupoOfGruposRolesListGruposRoles != null) {
                    oldIdGrupoOfGruposRolesListGruposRoles.getGruposRolesList().remove(gruposRolesListGruposRoles);
                    oldIdGrupoOfGruposRolesListGruposRoles = em.merge(oldIdGrupoOfGruposRolesListGruposRoles);
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

    public void edit(Grupos grupos) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Grupos persistentGrupos = em.find(Grupos.class, grupos.getId());
            List<GruposRoles> gruposRolesListOld = persistentGrupos.getGruposRolesList();
            List<GruposRoles> gruposRolesListNew = grupos.getGruposRolesList();
            List<String> illegalOrphanMessages = null;
            for (GruposRoles gruposRolesListOldGruposRoles : gruposRolesListOld) {
                if (!gruposRolesListNew.contains(gruposRolesListOldGruposRoles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain GruposRoles " + gruposRolesListOldGruposRoles + " since its idGrupo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<GruposRoles> attachedGruposRolesListNew = new ArrayList<GruposRoles>();
            for (GruposRoles gruposRolesListNewGruposRolesToAttach : gruposRolesListNew) {
                gruposRolesListNewGruposRolesToAttach = em.getReference(gruposRolesListNewGruposRolesToAttach.getClass(), gruposRolesListNewGruposRolesToAttach.getId());
                attachedGruposRolesListNew.add(gruposRolesListNewGruposRolesToAttach);
            }
            gruposRolesListNew = attachedGruposRolesListNew;
            grupos.setGruposRolesList(gruposRolesListNew);
            grupos = em.merge(grupos);
            for (GruposRoles gruposRolesListNewGruposRoles : gruposRolesListNew) {
                if (!gruposRolesListOld.contains(gruposRolesListNewGruposRoles)) {
                    Grupos oldIdGrupoOfGruposRolesListNewGruposRoles = gruposRolesListNewGruposRoles.getIdGrupo();
                    gruposRolesListNewGruposRoles.setIdGrupo(grupos);
                    gruposRolesListNewGruposRoles = em.merge(gruposRolesListNewGruposRoles);
                    if (oldIdGrupoOfGruposRolesListNewGruposRoles != null && !oldIdGrupoOfGruposRolesListNewGruposRoles.equals(grupos)) {
                        oldIdGrupoOfGruposRolesListNewGruposRoles.getGruposRolesList().remove(gruposRolesListNewGruposRoles);
                        oldIdGrupoOfGruposRolesListNewGruposRoles = em.merge(oldIdGrupoOfGruposRolesListNewGruposRoles);
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
                Integer id = grupos.getId();
                if (findGrupos(id) == null) {
                    throw new NonexistentEntityException("The grupos with id " + id + " no longer exists.");
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
            Grupos grupos;
            try {
                grupos = em.getReference(Grupos.class, id);
                grupos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupos with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<GruposRoles> gruposRolesListOrphanCheck = grupos.getGruposRolesList();
            for (GruposRoles gruposRolesListOrphanCheckGruposRoles : gruposRolesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Grupos (" + grupos + ") cannot be destroyed since the GruposRoles " + gruposRolesListOrphanCheckGruposRoles + " in its gruposRolesList field has a non-nullable idGrupo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(grupos);
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

    public List<Grupos> findGruposEntities() {
        return findGruposEntities(true, -1, -1);
    }

    public List<Grupos> findGruposEntities(int maxResults, int firstResult) {
        return findGruposEntities(false, maxResults, firstResult);
    }

    private List<Grupos> findGruposEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Grupos.class));
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

    public Grupos findGrupos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Grupos.class, id);
        } finally {
            em.close();
        }
    }

    public int getGruposCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Grupos> rt = cq.from(Grupos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
