/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers;

import com.mycompany.okitegami.controllers.exceptions.IllegalOrphanException;
import com.mycompany.okitegami.controllers.exceptions.NonexistentEntityException;
import com.mycompany.okitegami.controllers.exceptions.PreexistingEntityException;
import com.mycompany.okitegami.controllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.okitegami.models.Estados;
import com.mycompany.okitegami.models.Localidades;
import com.mycompany.okitegami.models.Municipios;
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
public class MunicipiosJpaController implements Serializable {

    public MunicipiosJpaController() throws NamingException {
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

    public void create(Municipios municipios) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (municipios.getLocalidadesList() == null) {
            municipios.setLocalidadesList(new ArrayList<Localidades>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Estados idEstado = municipios.getIdEstado();
            if (idEstado != null) {
                idEstado = em.getReference(idEstado.getClass(), idEstado.getId());
                municipios.setIdEstado(idEstado);
            }
            List<Localidades> attachedLocalidadesList = new ArrayList<Localidades>();
            for (Localidades localidadesListLocalidadesToAttach : municipios.getLocalidadesList()) {
                localidadesListLocalidadesToAttach = em.getReference(localidadesListLocalidadesToAttach.getClass(), localidadesListLocalidadesToAttach.getId());
                attachedLocalidadesList.add(localidadesListLocalidadesToAttach);
            }
            municipios.setLocalidadesList(attachedLocalidadesList);
            em.persist(municipios);
            if (idEstado != null) {
                idEstado.getMunicipiosList().add(municipios);
                idEstado = em.merge(idEstado);
            }
            for (Localidades localidadesListLocalidades : municipios.getLocalidadesList()) {
                Municipios oldIdMunicipioOfLocalidadesListLocalidades = localidadesListLocalidades.getIdMunicipio();
                localidadesListLocalidades.setIdMunicipio(municipios);
                localidadesListLocalidades = em.merge(localidadesListLocalidades);
                if (oldIdMunicipioOfLocalidadesListLocalidades != null) {
                    oldIdMunicipioOfLocalidadesListLocalidades.getLocalidadesList().remove(localidadesListLocalidades);
                    oldIdMunicipioOfLocalidadesListLocalidades = em.merge(oldIdMunicipioOfLocalidadesListLocalidades);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMunicipios(municipios.getId()) != null) {
                throw new PreexistingEntityException("Municipios " + municipios + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Municipios municipios) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Municipios persistentMunicipios = em.find(Municipios.class, municipios.getId());
            Estados idEstadoOld = persistentMunicipios.getIdEstado();
            Estados idEstadoNew = municipios.getIdEstado();
            List<Localidades> localidadesListOld = persistentMunicipios.getLocalidadesList();
            List<Localidades> localidadesListNew = municipios.getLocalidadesList();
            List<String> illegalOrphanMessages = null;
            for (Localidades localidadesListOldLocalidades : localidadesListOld) {
                if (!localidadesListNew.contains(localidadesListOldLocalidades)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Localidades " + localidadesListOldLocalidades + " since its idMunicipio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idEstadoNew != null) {
                idEstadoNew = em.getReference(idEstadoNew.getClass(), idEstadoNew.getId());
                municipios.setIdEstado(idEstadoNew);
            }
            List<Localidades> attachedLocalidadesListNew = new ArrayList<Localidades>();
            for (Localidades localidadesListNewLocalidadesToAttach : localidadesListNew) {
                localidadesListNewLocalidadesToAttach = em.getReference(localidadesListNewLocalidadesToAttach.getClass(), localidadesListNewLocalidadesToAttach.getId());
                attachedLocalidadesListNew.add(localidadesListNewLocalidadesToAttach);
            }
            localidadesListNew = attachedLocalidadesListNew;
            municipios.setLocalidadesList(localidadesListNew);
            municipios = em.merge(municipios);
            if (idEstadoOld != null && !idEstadoOld.equals(idEstadoNew)) {
                idEstadoOld.getMunicipiosList().remove(municipios);
                idEstadoOld = em.merge(idEstadoOld);
            }
            if (idEstadoNew != null && !idEstadoNew.equals(idEstadoOld)) {
                idEstadoNew.getMunicipiosList().add(municipios);
                idEstadoNew = em.merge(idEstadoNew);
            }
            for (Localidades localidadesListNewLocalidades : localidadesListNew) {
                if (!localidadesListOld.contains(localidadesListNewLocalidades)) {
                    Municipios oldIdMunicipioOfLocalidadesListNewLocalidades = localidadesListNewLocalidades.getIdMunicipio();
                    localidadesListNewLocalidades.setIdMunicipio(municipios);
                    localidadesListNewLocalidades = em.merge(localidadesListNewLocalidades);
                    if (oldIdMunicipioOfLocalidadesListNewLocalidades != null && !oldIdMunicipioOfLocalidadesListNewLocalidades.equals(municipios)) {
                        oldIdMunicipioOfLocalidadesListNewLocalidades.getLocalidadesList().remove(localidadesListNewLocalidades);
                        oldIdMunicipioOfLocalidadesListNewLocalidades = em.merge(oldIdMunicipioOfLocalidadesListNewLocalidades);
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
                Integer id = municipios.getId();
                if (findMunicipios(id) == null) {
                    throw new NonexistentEntityException("The municipios with id " + id + " no longer exists.");
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
            Municipios municipios;
            try {
                municipios = em.getReference(Municipios.class, id);
                municipios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The municipios with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Localidades> localidadesListOrphanCheck = municipios.getLocalidadesList();
            for (Localidades localidadesListOrphanCheckLocalidades : localidadesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Municipios (" + municipios + ") cannot be destroyed since the Localidades " + localidadesListOrphanCheckLocalidades + " in its localidadesList field has a non-nullable idMunicipio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estados idEstado = municipios.getIdEstado();
            if (idEstado != null) {
                idEstado.getMunicipiosList().remove(municipios);
                idEstado = em.merge(idEstado);
            }
            em.remove(municipios);
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

    public List<Municipios> findMunicipiosEntities() {
        return findMunicipiosEntities(true, -1, -1);
    }

    public List<Municipios> findMunicipiosEntities(int maxResults, int firstResult) {
        return findMunicipiosEntities(false, maxResults, firstResult);
    }

    private List<Municipios> findMunicipiosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Municipios.class));
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

    public Municipios findMunicipios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Municipios.class, id);
        } finally {
            em.close();
        }
    }

    public int getMunicipiosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Municipios> rt = cq.from(Municipios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
