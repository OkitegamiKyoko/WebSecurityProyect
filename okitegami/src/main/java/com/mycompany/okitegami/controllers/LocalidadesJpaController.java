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
import com.mycompany.okitegami.models.Municipios;
import com.mycompany.okitegami.models.Direcciones;
import com.mycompany.okitegami.models.Localidades;
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
public class LocalidadesJpaController implements Serializable {

    public LocalidadesJpaController() throws NamingException {
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

    public void create(Localidades localidades) throws RollbackFailureException, Exception {
        if (localidades.getDireccionesList() == null) {
            localidades.setDireccionesList(new ArrayList<Direcciones>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Municipios idMunicipio = localidades.getIdMunicipio();
            if (idMunicipio != null) {
                idMunicipio = em.getReference(idMunicipio.getClass(), idMunicipio.getId());
                localidades.setIdMunicipio(idMunicipio);
            }
            List<Direcciones> attachedDireccionesList = new ArrayList<Direcciones>();
            for (Direcciones direccionesListDireccionesToAttach : localidades.getDireccionesList()) {
                direccionesListDireccionesToAttach = em.getReference(direccionesListDireccionesToAttach.getClass(), direccionesListDireccionesToAttach.getId());
                attachedDireccionesList.add(direccionesListDireccionesToAttach);
            }
            localidades.setDireccionesList(attachedDireccionesList);
            em.persist(localidades);
            if (idMunicipio != null) {
                idMunicipio.getLocalidadesList().add(localidades);
                idMunicipio = em.merge(idMunicipio);
            }
            for (Direcciones direccionesListDirecciones : localidades.getDireccionesList()) {
                Localidades oldIdLocalidadOfDireccionesListDirecciones = direccionesListDirecciones.getIdLocalidad();
                direccionesListDirecciones.setIdLocalidad(localidades);
                direccionesListDirecciones = em.merge(direccionesListDirecciones);
                if (oldIdLocalidadOfDireccionesListDirecciones != null) {
                    oldIdLocalidadOfDireccionesListDirecciones.getDireccionesList().remove(direccionesListDirecciones);
                    oldIdLocalidadOfDireccionesListDirecciones = em.merge(oldIdLocalidadOfDireccionesListDirecciones);
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

    public void edit(Localidades localidades) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Localidades persistentLocalidades = em.find(Localidades.class, localidades.getId());
            Municipios idMunicipioOld = persistentLocalidades.getIdMunicipio();
            Municipios idMunicipioNew = localidades.getIdMunicipio();
            List<Direcciones> direccionesListOld = persistentLocalidades.getDireccionesList();
            List<Direcciones> direccionesListNew = localidades.getDireccionesList();
            if (idMunicipioNew != null) {
                idMunicipioNew = em.getReference(idMunicipioNew.getClass(), idMunicipioNew.getId());
                localidades.setIdMunicipio(idMunicipioNew);
            }
            List<Direcciones> attachedDireccionesListNew = new ArrayList<Direcciones>();
            for (Direcciones direccionesListNewDireccionesToAttach : direccionesListNew) {
                direccionesListNewDireccionesToAttach = em.getReference(direccionesListNewDireccionesToAttach.getClass(), direccionesListNewDireccionesToAttach.getId());
                attachedDireccionesListNew.add(direccionesListNewDireccionesToAttach);
            }
            direccionesListNew = attachedDireccionesListNew;
            localidades.setDireccionesList(direccionesListNew);
            localidades = em.merge(localidades);
            if (idMunicipioOld != null && !idMunicipioOld.equals(idMunicipioNew)) {
                idMunicipioOld.getLocalidadesList().remove(localidades);
                idMunicipioOld = em.merge(idMunicipioOld);
            }
            if (idMunicipioNew != null && !idMunicipioNew.equals(idMunicipioOld)) {
                idMunicipioNew.getLocalidadesList().add(localidades);
                idMunicipioNew = em.merge(idMunicipioNew);
            }
            for (Direcciones direccionesListOldDirecciones : direccionesListOld) {
                if (!direccionesListNew.contains(direccionesListOldDirecciones)) {
                    direccionesListOldDirecciones.setIdLocalidad(null);
                    direccionesListOldDirecciones = em.merge(direccionesListOldDirecciones);
                }
            }
            for (Direcciones direccionesListNewDirecciones : direccionesListNew) {
                if (!direccionesListOld.contains(direccionesListNewDirecciones)) {
                    Localidades oldIdLocalidadOfDireccionesListNewDirecciones = direccionesListNewDirecciones.getIdLocalidad();
                    direccionesListNewDirecciones.setIdLocalidad(localidades);
                    direccionesListNewDirecciones = em.merge(direccionesListNewDirecciones);
                    if (oldIdLocalidadOfDireccionesListNewDirecciones != null && !oldIdLocalidadOfDireccionesListNewDirecciones.equals(localidades)) {
                        oldIdLocalidadOfDireccionesListNewDirecciones.getDireccionesList().remove(direccionesListNewDirecciones);
                        oldIdLocalidadOfDireccionesListNewDirecciones = em.merge(oldIdLocalidadOfDireccionesListNewDirecciones);
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
                Integer id = localidades.getId();
                if (findLocalidades(id) == null) {
                    throw new NonexistentEntityException("The localidades with id " + id + " no longer exists.");
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
            Localidades localidades;
            try {
                localidades = em.getReference(Localidades.class, id);
                localidades.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The localidades with id " + id + " no longer exists.", enfe);
            }
            Municipios idMunicipio = localidades.getIdMunicipio();
            if (idMunicipio != null) {
                idMunicipio.getLocalidadesList().remove(localidades);
                idMunicipio = em.merge(idMunicipio);
            }
            List<Direcciones> direccionesList = localidades.getDireccionesList();
            for (Direcciones direccionesListDirecciones : direccionesList) {
                direccionesListDirecciones.setIdLocalidad(null);
                direccionesListDirecciones = em.merge(direccionesListDirecciones);
            }
            em.remove(localidades);
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

    public List<Localidades> findLocalidadesEntities() {
        return findLocalidadesEntities(true, -1, -1);
    }

    public List<Localidades> findLocalidadesEntities(int maxResults, int firstResult) {
        return findLocalidadesEntities(false, maxResults, firstResult);
    }

    private List<Localidades> findLocalidadesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Localidades.class));
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

    public Localidades findLocalidades(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Localidades.class, id);
        } finally {
            em.close();
        }
    }

    public int getLocalidadesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Localidades> rt = cq.from(Localidades.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
