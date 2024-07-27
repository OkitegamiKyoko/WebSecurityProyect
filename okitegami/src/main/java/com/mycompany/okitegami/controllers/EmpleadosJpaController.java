<<<<<<< HEAD
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
import com.mycompany.okitegami.models.Cv;
import com.mycompany.okitegami.models.Direcciones;
import com.mycompany.okitegami.models.Puestos;
import com.mycompany.okitegami.models.Users;
import com.mycompany.okitegami.models.Bonos;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.okitegami.models.ContratosEmpleados;
import com.mycompany.okitegami.models.Empleados;
import com.mycompany.okitegami.models.Envios;
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
public class EmpleadosJpaController implements Serializable {

    public EmpleadosJpaController() throws NamingException {
        this.utx = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        this.emf = Persistence.createEntityManagerFactory("okitegami_db_pool");
    }
    private UserTransaction utx;
    private EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleados empleados) throws IllegalOrphanException, RollbackFailureException, Exception {
        if (empleados.getBonosList() == null) {
            empleados.setBonosList(new ArrayList<Bonos>());
        }
        if (empleados.getContratosEmpleadosList() == null) {
            empleados.setContratosEmpleadosList(new ArrayList<ContratosEmpleados>());
        }
        if (empleados.getEnviosList() == null) {
            empleados.setEnviosList(new ArrayList<Envios>());
        }
        List<String> illegalOrphanMessages = null;
        Users idUserOrphanCheck = empleados.getIdUser();
        if (idUserOrphanCheck != null) {
            Empleados oldEmpleadosOfIdUser = idUserOrphanCheck.getEmpleados();
            if (oldEmpleadosOfIdUser != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Users " + idUserOrphanCheck + " already has an item of type Empleados whose idUser column cannot be null. Please make another selection for the idUser field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cv idCv = empleados.getIdCv();
            if (idCv != null) {
                idCv = em.getReference(idCv.getClass(), idCv.getId());
                empleados.setIdCv(idCv);
            }
            Direcciones idDirect = empleados.getIdDirect();
            if (idDirect != null) {
                idDirect = em.getReference(idDirect.getClass(), idDirect.getId());
                empleados.setIdDirect(idDirect);
            }
            Puestos idPuesto = empleados.getIdPuesto();
            if (idPuesto != null) {
                idPuesto = em.getReference(idPuesto.getClass(), idPuesto.getId());
                empleados.setIdPuesto(idPuesto);
            }
            Users idUser = empleados.getIdUser();
            if (idUser != null) {
                idUser = em.getReference(idUser.getClass(), idUser.getId());
                empleados.setIdUser(idUser);
            }
            List<Bonos> attachedBonosList = new ArrayList<Bonos>();
            for (Bonos bonosListBonosToAttach : empleados.getBonosList()) {
                bonosListBonosToAttach = em.getReference(bonosListBonosToAttach.getClass(), bonosListBonosToAttach.getId());
                attachedBonosList.add(bonosListBonosToAttach);
            }
            empleados.setBonosList(attachedBonosList);
            List<ContratosEmpleados> attachedContratosEmpleadosList = new ArrayList<ContratosEmpleados>();
            for (ContratosEmpleados contratosEmpleadosListContratosEmpleadosToAttach : empleados.getContratosEmpleadosList()) {
                contratosEmpleadosListContratosEmpleadosToAttach = em.getReference(contratosEmpleadosListContratosEmpleadosToAttach.getClass(), contratosEmpleadosListContratosEmpleadosToAttach.getId());
                attachedContratosEmpleadosList.add(contratosEmpleadosListContratosEmpleadosToAttach);
            }
            empleados.setContratosEmpleadosList(attachedContratosEmpleadosList);
            List<Envios> attachedEnviosList = new ArrayList<Envios>();
            for (Envios enviosListEnviosToAttach : empleados.getEnviosList()) {
                enviosListEnviosToAttach = em.getReference(enviosListEnviosToAttach.getClass(), enviosListEnviosToAttach.getId());
                attachedEnviosList.add(enviosListEnviosToAttach);
            }
            empleados.setEnviosList(attachedEnviosList);
            em.persist(empleados);
            if (idCv != null) {
                idCv.getEmpleadosList().add(empleados);
                idCv = em.merge(idCv);
            }
            if (idDirect != null) {
                idDirect.getEmpleadosList().add(empleados);
                idDirect = em.merge(idDirect);
            }
            if (idPuesto != null) {
                idPuesto.getEmpleadosList().add(empleados);
                idPuesto = em.merge(idPuesto);
            }
            if (idUser != null) {
                idUser.setEmpleados(empleados);
                idUser = em.merge(idUser);
            }
            for (Bonos bonosListBonos : empleados.getBonosList()) {
                bonosListBonos.getEmpleadosList().add(empleados);
                bonosListBonos = em.merge(bonosListBonos);
            }
            for (ContratosEmpleados contratosEmpleadosListContratosEmpleados : empleados.getContratosEmpleadosList()) {
                Empleados oldIdEmpleadoOfContratosEmpleadosListContratosEmpleados = contratosEmpleadosListContratosEmpleados.getIdEmpleado();
                contratosEmpleadosListContratosEmpleados.setIdEmpleado(empleados);
                contratosEmpleadosListContratosEmpleados = em.merge(contratosEmpleadosListContratosEmpleados);
                if (oldIdEmpleadoOfContratosEmpleadosListContratosEmpleados != null) {
                    oldIdEmpleadoOfContratosEmpleadosListContratosEmpleados.getContratosEmpleadosList().remove(contratosEmpleadosListContratosEmpleados);
                    oldIdEmpleadoOfContratosEmpleadosListContratosEmpleados = em.merge(oldIdEmpleadoOfContratosEmpleadosListContratosEmpleados);
                }
            }
            for (Envios enviosListEnvios : empleados.getEnviosList()) {
                Empleados oldIdEmpleadoOfEnviosListEnvios = enviosListEnvios.getIdEmpleado();
                enviosListEnvios.setIdEmpleado(empleados);
                enviosListEnvios = em.merge(enviosListEnvios);
                if (oldIdEmpleadoOfEnviosListEnvios != null) {
                    oldIdEmpleadoOfEnviosListEnvios.getEnviosList().remove(enviosListEnvios);
                    oldIdEmpleadoOfEnviosListEnvios = em.merge(oldIdEmpleadoOfEnviosListEnvios);
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

    public void edit(Empleados empleados) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Empleados persistentEmpleados = em.find(Empleados.class, empleados.getId());
            Cv idCvOld = persistentEmpleados.getIdCv();
            Cv idCvNew = empleados.getIdCv();
            Direcciones idDirectOld = persistentEmpleados.getIdDirect();
            Direcciones idDirectNew = empleados.getIdDirect();
            Puestos idPuestoOld = persistentEmpleados.getIdPuesto();
            Puestos idPuestoNew = empleados.getIdPuesto();
            Users idUserOld = persistentEmpleados.getIdUser();
            Users idUserNew = empleados.getIdUser();
            List<Bonos> bonosListOld = persistentEmpleados.getBonosList();
            List<Bonos> bonosListNew = empleados.getBonosList();
            List<ContratosEmpleados> contratosEmpleadosListOld = persistentEmpleados.getContratosEmpleadosList();
            List<ContratosEmpleados> contratosEmpleadosListNew = empleados.getContratosEmpleadosList();
            List<Envios> enviosListOld = persistentEmpleados.getEnviosList();
            List<Envios> enviosListNew = empleados.getEnviosList();
            List<String> illegalOrphanMessages = null;
            if (idUserNew != null && !idUserNew.equals(idUserOld)) {
                Empleados oldEmpleadosOfIdUser = idUserNew.getEmpleados();
                if (oldEmpleadosOfIdUser != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Users " + idUserNew + " already has an item of type Empleados whose idUser column cannot be null. Please make another selection for the idUser field.");
                }
            }
            for (ContratosEmpleados contratosEmpleadosListOldContratosEmpleados : contratosEmpleadosListOld) {
                if (!contratosEmpleadosListNew.contains(contratosEmpleadosListOldContratosEmpleados)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ContratosEmpleados " + contratosEmpleadosListOldContratosEmpleados + " since its idEmpleado field is not nullable.");
                }
            }
            for (Envios enviosListOldEnvios : enviosListOld) {
                if (!enviosListNew.contains(enviosListOldEnvios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Envios " + enviosListOldEnvios + " since its idEmpleado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCvNew != null) {
                idCvNew = em.getReference(idCvNew.getClass(), idCvNew.getId());
                empleados.setIdCv(idCvNew);
            }
            if (idDirectNew != null) {
                idDirectNew = em.getReference(idDirectNew.getClass(), idDirectNew.getId());
                empleados.setIdDirect(idDirectNew);
            }
            if (idPuestoNew != null) {
                idPuestoNew = em.getReference(idPuestoNew.getClass(), idPuestoNew.getId());
                empleados.setIdPuesto(idPuestoNew);
            }
            if (idUserNew != null) {
                idUserNew = em.getReference(idUserNew.getClass(), idUserNew.getId());
                empleados.setIdUser(idUserNew);
            }
            List<Bonos> attachedBonosListNew = new ArrayList<Bonos>();
            for (Bonos bonosListNewBonosToAttach : bonosListNew) {
                bonosListNewBonosToAttach = em.getReference(bonosListNewBonosToAttach.getClass(), bonosListNewBonosToAttach.getId());
                attachedBonosListNew.add(bonosListNewBonosToAttach);
            }
            bonosListNew = attachedBonosListNew;
            empleados.setBonosList(bonosListNew);
            List<ContratosEmpleados> attachedContratosEmpleadosListNew = new ArrayList<ContratosEmpleados>();
            for (ContratosEmpleados contratosEmpleadosListNewContratosEmpleadosToAttach : contratosEmpleadosListNew) {
                contratosEmpleadosListNewContratosEmpleadosToAttach = em.getReference(contratosEmpleadosListNewContratosEmpleadosToAttach.getClass(), contratosEmpleadosListNewContratosEmpleadosToAttach.getId());
                attachedContratosEmpleadosListNew.add(contratosEmpleadosListNewContratosEmpleadosToAttach);
            }
            contratosEmpleadosListNew = attachedContratosEmpleadosListNew;
            empleados.setContratosEmpleadosList(contratosEmpleadosListNew);
            List<Envios> attachedEnviosListNew = new ArrayList<Envios>();
            for (Envios enviosListNewEnviosToAttach : enviosListNew) {
                enviosListNewEnviosToAttach = em.getReference(enviosListNewEnviosToAttach.getClass(), enviosListNewEnviosToAttach.getId());
                attachedEnviosListNew.add(enviosListNewEnviosToAttach);
            }
            enviosListNew = attachedEnviosListNew;
            empleados.setEnviosList(enviosListNew);
            empleados = em.merge(empleados);
            if (idCvOld != null && !idCvOld.equals(idCvNew)) {
                idCvOld.getEmpleadosList().remove(empleados);
                idCvOld = em.merge(idCvOld);
            }
            if (idCvNew != null && !idCvNew.equals(idCvOld)) {
                idCvNew.getEmpleadosList().add(empleados);
                idCvNew = em.merge(idCvNew);
            }
            if (idDirectOld != null && !idDirectOld.equals(idDirectNew)) {
                idDirectOld.getEmpleadosList().remove(empleados);
                idDirectOld = em.merge(idDirectOld);
            }
            if (idDirectNew != null && !idDirectNew.equals(idDirectOld)) {
                idDirectNew.getEmpleadosList().add(empleados);
                idDirectNew = em.merge(idDirectNew);
            }
            if (idPuestoOld != null && !idPuestoOld.equals(idPuestoNew)) {
                idPuestoOld.getEmpleadosList().remove(empleados);
                idPuestoOld = em.merge(idPuestoOld);
            }
            if (idPuestoNew != null && !idPuestoNew.equals(idPuestoOld)) {
                idPuestoNew.getEmpleadosList().add(empleados);
                idPuestoNew = em.merge(idPuestoNew);
            }
            if (idUserOld != null && !idUserOld.equals(idUserNew)) {
                idUserOld.setEmpleados(null);
                idUserOld = em.merge(idUserOld);
            }
            if (idUserNew != null && !idUserNew.equals(idUserOld)) {
                idUserNew.setEmpleados(empleados);
                idUserNew = em.merge(idUserNew);
            }
            for (Bonos bonosListOldBonos : bonosListOld) {
                if (!bonosListNew.contains(bonosListOldBonos)) {
                    bonosListOldBonos.getEmpleadosList().remove(empleados);
                    bonosListOldBonos = em.merge(bonosListOldBonos);
                }
            }
            for (Bonos bonosListNewBonos : bonosListNew) {
                if (!bonosListOld.contains(bonosListNewBonos)) {
                    bonosListNewBonos.getEmpleadosList().add(empleados);
                    bonosListNewBonos = em.merge(bonosListNewBonos);
                }
            }
            for (ContratosEmpleados contratosEmpleadosListNewContratosEmpleados : contratosEmpleadosListNew) {
                if (!contratosEmpleadosListOld.contains(contratosEmpleadosListNewContratosEmpleados)) {
                    Empleados oldIdEmpleadoOfContratosEmpleadosListNewContratosEmpleados = contratosEmpleadosListNewContratosEmpleados.getIdEmpleado();
                    contratosEmpleadosListNewContratosEmpleados.setIdEmpleado(empleados);
                    contratosEmpleadosListNewContratosEmpleados = em.merge(contratosEmpleadosListNewContratosEmpleados);
                    if (oldIdEmpleadoOfContratosEmpleadosListNewContratosEmpleados != null && !oldIdEmpleadoOfContratosEmpleadosListNewContratosEmpleados.equals(empleados)) {
                        oldIdEmpleadoOfContratosEmpleadosListNewContratosEmpleados.getContratosEmpleadosList().remove(contratosEmpleadosListNewContratosEmpleados);
                        oldIdEmpleadoOfContratosEmpleadosListNewContratosEmpleados = em.merge(oldIdEmpleadoOfContratosEmpleadosListNewContratosEmpleados);
                    }
                }
            }
            for (Envios enviosListNewEnvios : enviosListNew) {
                if (!enviosListOld.contains(enviosListNewEnvios)) {
                    Empleados oldIdEmpleadoOfEnviosListNewEnvios = enviosListNewEnvios.getIdEmpleado();
                    enviosListNewEnvios.setIdEmpleado(empleados);
                    enviosListNewEnvios = em.merge(enviosListNewEnvios);
                    if (oldIdEmpleadoOfEnviosListNewEnvios != null && !oldIdEmpleadoOfEnviosListNewEnvios.equals(empleados)) {
                        oldIdEmpleadoOfEnviosListNewEnvios.getEnviosList().remove(enviosListNewEnvios);
                        oldIdEmpleadoOfEnviosListNewEnvios = em.merge(oldIdEmpleadoOfEnviosListNewEnvios);
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
                Integer id = empleados.getId();
                if (findEmpleados(id) == null) {
                    throw new NonexistentEntityException("The empleados with id " + id + " no longer exists.");
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
            Empleados empleados;
            try {
                empleados = em.getReference(Empleados.class, id);
                empleados.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleados with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ContratosEmpleados> contratosEmpleadosListOrphanCheck = empleados.getContratosEmpleadosList();
            for (ContratosEmpleados contratosEmpleadosListOrphanCheckContratosEmpleados : contratosEmpleadosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleados (" + empleados + ") cannot be destroyed since the ContratosEmpleados " + contratosEmpleadosListOrphanCheckContratosEmpleados + " in its contratosEmpleadosList field has a non-nullable idEmpleado field.");
            }
            List<Envios> enviosListOrphanCheck = empleados.getEnviosList();
            for (Envios enviosListOrphanCheckEnvios : enviosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleados (" + empleados + ") cannot be destroyed since the Envios " + enviosListOrphanCheckEnvios + " in its enviosList field has a non-nullable idEmpleado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cv idCv = empleados.getIdCv();
            if (idCv != null) {
                idCv.getEmpleadosList().remove(empleados);
                idCv = em.merge(idCv);
            }
            Direcciones idDirect = empleados.getIdDirect();
            if (idDirect != null) {
                idDirect.getEmpleadosList().remove(empleados);
                idDirect = em.merge(idDirect);
            }
            Puestos idPuesto = empleados.getIdPuesto();
            if (idPuesto != null) {
                idPuesto.getEmpleadosList().remove(empleados);
                idPuesto = em.merge(idPuesto);
            }
            Users idUser = empleados.getIdUser();
            if (idUser != null) {
                idUser.setEmpleados(null);
                idUser = em.merge(idUser);
            }
            List<Bonos> bonosList = empleados.getBonosList();
            for (Bonos bonosListBonos : bonosList) {
                bonosListBonos.getEmpleadosList().remove(empleados);
                bonosListBonos = em.merge(bonosListBonos);
            }
            em.remove(empleados);
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

    public List<Empleados> findEmpleadosEntities() {
        return findEmpleadosEntities(true, -1, -1);
    }

    public List<Empleados> findEmpleadosEntities(int maxResults, int firstResult) {
        return findEmpleadosEntities(false, maxResults, firstResult);
    }

    private List<Empleados> findEmpleadosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleados.class));
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

    public Empleados findEmpleados(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleados.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleados> rt = cq.from(Empleados.class);
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

import com.mycompany.okitegami.controllers.exceptions.IllegalOrphanException;
import com.mycompany.okitegami.controllers.exceptions.NonexistentEntityException;
import com.mycompany.okitegami.controllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.okitegami.models.Cv;
import com.mycompany.okitegami.models.Direcciones;
import com.mycompany.okitegami.models.Puestos;
import com.mycompany.okitegami.models.Users;
import com.mycompany.okitegami.models.Bonos;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.okitegami.models.ContratosEmpleados;
import com.mycompany.okitegami.models.Empleados;
import com.mycompany.okitegami.models.Envios;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author meli_
 */
public class EmpleadosJpaController implements Serializable {

    public EmpleadosJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleados empleados) throws RollbackFailureException, Exception {
        if (empleados.getBonosList() == null) {
            empleados.setBonosList(new ArrayList<Bonos>());
        }
        if (empleados.getContratosEmpleadosList() == null) {
            empleados.setContratosEmpleadosList(new ArrayList<ContratosEmpleados>());
        }
        if (empleados.getEnviosList() == null) {
            empleados.setEnviosList(new ArrayList<Envios>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cv idCv = empleados.getIdCv();
            if (idCv != null) {
                idCv = em.getReference(idCv.getClass(), idCv.getId());
                empleados.setIdCv(idCv);
            }
            Direcciones idDirect = empleados.getIdDirect();
            if (idDirect != null) {
                idDirect = em.getReference(idDirect.getClass(), idDirect.getId());
                empleados.setIdDirect(idDirect);
            }
            Puestos idPuesto = empleados.getIdPuesto();
            if (idPuesto != null) {
                idPuesto = em.getReference(idPuesto.getClass(), idPuesto.getId());
                empleados.setIdPuesto(idPuesto);
            }
            Users idUser = empleados.getIdUser();
            if (idUser != null) {
                idUser = em.getReference(idUser.getClass(), idUser.getId());
                empleados.setIdUser(idUser);
            }
            List<Bonos> attachedBonosList = new ArrayList<Bonos>();
            for (Bonos bonosListBonosToAttach : empleados.getBonosList()) {
                bonosListBonosToAttach = em.getReference(bonosListBonosToAttach.getClass(), bonosListBonosToAttach.getId());
                attachedBonosList.add(bonosListBonosToAttach);
            }
            empleados.setBonosList(attachedBonosList);
            List<ContratosEmpleados> attachedContratosEmpleadosList = new ArrayList<ContratosEmpleados>();
            for (ContratosEmpleados contratosEmpleadosListContratosEmpleadosToAttach : empleados.getContratosEmpleadosList()) {
                contratosEmpleadosListContratosEmpleadosToAttach = em.getReference(contratosEmpleadosListContratosEmpleadosToAttach.getClass(), contratosEmpleadosListContratosEmpleadosToAttach.getId());
                attachedContratosEmpleadosList.add(contratosEmpleadosListContratosEmpleadosToAttach);
            }
            empleados.setContratosEmpleadosList(attachedContratosEmpleadosList);
            List<Envios> attachedEnviosList = new ArrayList<Envios>();
            for (Envios enviosListEnviosToAttach : empleados.getEnviosList()) {
                enviosListEnviosToAttach = em.getReference(enviosListEnviosToAttach.getClass(), enviosListEnviosToAttach.getId());
                attachedEnviosList.add(enviosListEnviosToAttach);
            }
            empleados.setEnviosList(attachedEnviosList);
            em.persist(empleados);
            if (idCv != null) {
                idCv.getEmpleadosList().add(empleados);
                idCv = em.merge(idCv);
            }
            if (idDirect != null) {
                idDirect.getEmpleadosList().add(empleados);
                idDirect = em.merge(idDirect);
            }
            if (idPuesto != null) {
                idPuesto.getEmpleadosList().add(empleados);
                idPuesto = em.merge(idPuesto);
            }
            if (idUser != null) {
                idUser.getEmpleadosList().add(empleados);
                idUser = em.merge(idUser);
            }
            for (Bonos bonosListBonos : empleados.getBonosList()) {
                bonosListBonos.getEmpleadosList().add(empleados);
                bonosListBonos = em.merge(bonosListBonos);
            }
            for (ContratosEmpleados contratosEmpleadosListContratosEmpleados : empleados.getContratosEmpleadosList()) {
                Empleados oldIdEmpleadoOfContratosEmpleadosListContratosEmpleados = contratosEmpleadosListContratosEmpleados.getIdEmpleado();
                contratosEmpleadosListContratosEmpleados.setIdEmpleado(empleados);
                contratosEmpleadosListContratosEmpleados = em.merge(contratosEmpleadosListContratosEmpleados);
                if (oldIdEmpleadoOfContratosEmpleadosListContratosEmpleados != null) {
                    oldIdEmpleadoOfContratosEmpleadosListContratosEmpleados.getContratosEmpleadosList().remove(contratosEmpleadosListContratosEmpleados);
                    oldIdEmpleadoOfContratosEmpleadosListContratosEmpleados = em.merge(oldIdEmpleadoOfContratosEmpleadosListContratosEmpleados);
                }
            }
            for (Envios enviosListEnvios : empleados.getEnviosList()) {
                Empleados oldIdEmpleadoOfEnviosListEnvios = enviosListEnvios.getIdEmpleado();
                enviosListEnvios.setIdEmpleado(empleados);
                enviosListEnvios = em.merge(enviosListEnvios);
                if (oldIdEmpleadoOfEnviosListEnvios != null) {
                    oldIdEmpleadoOfEnviosListEnvios.getEnviosList().remove(enviosListEnvios);
                    oldIdEmpleadoOfEnviosListEnvios = em.merge(oldIdEmpleadoOfEnviosListEnvios);
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

    public void edit(Empleados empleados) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Empleados persistentEmpleados = em.find(Empleados.class, empleados.getId());
            Cv idCvOld = persistentEmpleados.getIdCv();
            Cv idCvNew = empleados.getIdCv();
            Direcciones idDirectOld = persistentEmpleados.getIdDirect();
            Direcciones idDirectNew = empleados.getIdDirect();
            Puestos idPuestoOld = persistentEmpleados.getIdPuesto();
            Puestos idPuestoNew = empleados.getIdPuesto();
            Users idUserOld = persistentEmpleados.getIdUser();
            Users idUserNew = empleados.getIdUser();
            List<Bonos> bonosListOld = persistentEmpleados.getBonosList();
            List<Bonos> bonosListNew = empleados.getBonosList();
            List<ContratosEmpleados> contratosEmpleadosListOld = persistentEmpleados.getContratosEmpleadosList();
            List<ContratosEmpleados> contratosEmpleadosListNew = empleados.getContratosEmpleadosList();
            List<Envios> enviosListOld = persistentEmpleados.getEnviosList();
            List<Envios> enviosListNew = empleados.getEnviosList();
            List<String> illegalOrphanMessages = null;
            for (ContratosEmpleados contratosEmpleadosListOldContratosEmpleados : contratosEmpleadosListOld) {
                if (!contratosEmpleadosListNew.contains(contratosEmpleadosListOldContratosEmpleados)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ContratosEmpleados " + contratosEmpleadosListOldContratosEmpleados + " since its idEmpleado field is not nullable.");
                }
            }
            for (Envios enviosListOldEnvios : enviosListOld) {
                if (!enviosListNew.contains(enviosListOldEnvios)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Envios " + enviosListOldEnvios + " since its idEmpleado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idCvNew != null) {
                idCvNew = em.getReference(idCvNew.getClass(), idCvNew.getId());
                empleados.setIdCv(idCvNew);
            }
            if (idDirectNew != null) {
                idDirectNew = em.getReference(idDirectNew.getClass(), idDirectNew.getId());
                empleados.setIdDirect(idDirectNew);
            }
            if (idPuestoNew != null) {
                idPuestoNew = em.getReference(idPuestoNew.getClass(), idPuestoNew.getId());
                empleados.setIdPuesto(idPuestoNew);
            }
            if (idUserNew != null) {
                idUserNew = em.getReference(idUserNew.getClass(), idUserNew.getId());
                empleados.setIdUser(idUserNew);
            }
            List<Bonos> attachedBonosListNew = new ArrayList<Bonos>();
            for (Bonos bonosListNewBonosToAttach : bonosListNew) {
                bonosListNewBonosToAttach = em.getReference(bonosListNewBonosToAttach.getClass(), bonosListNewBonosToAttach.getId());
                attachedBonosListNew.add(bonosListNewBonosToAttach);
            }
            bonosListNew = attachedBonosListNew;
            empleados.setBonosList(bonosListNew);
            List<ContratosEmpleados> attachedContratosEmpleadosListNew = new ArrayList<ContratosEmpleados>();
            for (ContratosEmpleados contratosEmpleadosListNewContratosEmpleadosToAttach : contratosEmpleadosListNew) {
                contratosEmpleadosListNewContratosEmpleadosToAttach = em.getReference(contratosEmpleadosListNewContratosEmpleadosToAttach.getClass(), contratosEmpleadosListNewContratosEmpleadosToAttach.getId());
                attachedContratosEmpleadosListNew.add(contratosEmpleadosListNewContratosEmpleadosToAttach);
            }
            contratosEmpleadosListNew = attachedContratosEmpleadosListNew;
            empleados.setContratosEmpleadosList(contratosEmpleadosListNew);
            List<Envios> attachedEnviosListNew = new ArrayList<Envios>();
            for (Envios enviosListNewEnviosToAttach : enviosListNew) {
                enviosListNewEnviosToAttach = em.getReference(enviosListNewEnviosToAttach.getClass(), enviosListNewEnviosToAttach.getId());
                attachedEnviosListNew.add(enviosListNewEnviosToAttach);
            }
            enviosListNew = attachedEnviosListNew;
            empleados.setEnviosList(enviosListNew);
            empleados = em.merge(empleados);
            if (idCvOld != null && !idCvOld.equals(idCvNew)) {
                idCvOld.getEmpleadosList().remove(empleados);
                idCvOld = em.merge(idCvOld);
            }
            if (idCvNew != null && !idCvNew.equals(idCvOld)) {
                idCvNew.getEmpleadosList().add(empleados);
                idCvNew = em.merge(idCvNew);
            }
            if (idDirectOld != null && !idDirectOld.equals(idDirectNew)) {
                idDirectOld.getEmpleadosList().remove(empleados);
                idDirectOld = em.merge(idDirectOld);
            }
            if (idDirectNew != null && !idDirectNew.equals(idDirectOld)) {
                idDirectNew.getEmpleadosList().add(empleados);
                idDirectNew = em.merge(idDirectNew);
            }
            if (idPuestoOld != null && !idPuestoOld.equals(idPuestoNew)) {
                idPuestoOld.getEmpleadosList().remove(empleados);
                idPuestoOld = em.merge(idPuestoOld);
            }
            if (idPuestoNew != null && !idPuestoNew.equals(idPuestoOld)) {
                idPuestoNew.getEmpleadosList().add(empleados);
                idPuestoNew = em.merge(idPuestoNew);
            }
            if (idUserOld != null && !idUserOld.equals(idUserNew)) {
                idUserOld.getEmpleadosList().remove(empleados);
                idUserOld = em.merge(idUserOld);
            }
            if (idUserNew != null && !idUserNew.equals(idUserOld)) {
                idUserNew.getEmpleadosList().add(empleados);
                idUserNew = em.merge(idUserNew);
            }
            for (Bonos bonosListOldBonos : bonosListOld) {
                if (!bonosListNew.contains(bonosListOldBonos)) {
                    bonosListOldBonos.getEmpleadosList().remove(empleados);
                    bonosListOldBonos = em.merge(bonosListOldBonos);
                }
            }
            for (Bonos bonosListNewBonos : bonosListNew) {
                if (!bonosListOld.contains(bonosListNewBonos)) {
                    bonosListNewBonos.getEmpleadosList().add(empleados);
                    bonosListNewBonos = em.merge(bonosListNewBonos);
                }
            }
            for (ContratosEmpleados contratosEmpleadosListNewContratosEmpleados : contratosEmpleadosListNew) {
                if (!contratosEmpleadosListOld.contains(contratosEmpleadosListNewContratosEmpleados)) {
                    Empleados oldIdEmpleadoOfContratosEmpleadosListNewContratosEmpleados = contratosEmpleadosListNewContratosEmpleados.getIdEmpleado();
                    contratosEmpleadosListNewContratosEmpleados.setIdEmpleado(empleados);
                    contratosEmpleadosListNewContratosEmpleados = em.merge(contratosEmpleadosListNewContratosEmpleados);
                    if (oldIdEmpleadoOfContratosEmpleadosListNewContratosEmpleados != null && !oldIdEmpleadoOfContratosEmpleadosListNewContratosEmpleados.equals(empleados)) {
                        oldIdEmpleadoOfContratosEmpleadosListNewContratosEmpleados.getContratosEmpleadosList().remove(contratosEmpleadosListNewContratosEmpleados);
                        oldIdEmpleadoOfContratosEmpleadosListNewContratosEmpleados = em.merge(oldIdEmpleadoOfContratosEmpleadosListNewContratosEmpleados);
                    }
                }
            }
            for (Envios enviosListNewEnvios : enviosListNew) {
                if (!enviosListOld.contains(enviosListNewEnvios)) {
                    Empleados oldIdEmpleadoOfEnviosListNewEnvios = enviosListNewEnvios.getIdEmpleado();
                    enviosListNewEnvios.setIdEmpleado(empleados);
                    enviosListNewEnvios = em.merge(enviosListNewEnvios);
                    if (oldIdEmpleadoOfEnviosListNewEnvios != null && !oldIdEmpleadoOfEnviosListNewEnvios.equals(empleados)) {
                        oldIdEmpleadoOfEnviosListNewEnvios.getEnviosList().remove(enviosListNewEnvios);
                        oldIdEmpleadoOfEnviosListNewEnvios = em.merge(oldIdEmpleadoOfEnviosListNewEnvios);
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
                Integer id = empleados.getId();
                if (findEmpleados(id) == null) {
                    throw new NonexistentEntityException("The empleados with id " + id + " no longer exists.");
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
            Empleados empleados;
            try {
                empleados = em.getReference(Empleados.class, id);
                empleados.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleados with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ContratosEmpleados> contratosEmpleadosListOrphanCheck = empleados.getContratosEmpleadosList();
            for (ContratosEmpleados contratosEmpleadosListOrphanCheckContratosEmpleados : contratosEmpleadosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleados (" + empleados + ") cannot be destroyed since the ContratosEmpleados " + contratosEmpleadosListOrphanCheckContratosEmpleados + " in its contratosEmpleadosList field has a non-nullable idEmpleado field.");
            }
            List<Envios> enviosListOrphanCheck = empleados.getEnviosList();
            for (Envios enviosListOrphanCheckEnvios : enviosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Empleados (" + empleados + ") cannot be destroyed since the Envios " + enviosListOrphanCheckEnvios + " in its enviosList field has a non-nullable idEmpleado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Cv idCv = empleados.getIdCv();
            if (idCv != null) {
                idCv.getEmpleadosList().remove(empleados);
                idCv = em.merge(idCv);
            }
            Direcciones idDirect = empleados.getIdDirect();
            if (idDirect != null) {
                idDirect.getEmpleadosList().remove(empleados);
                idDirect = em.merge(idDirect);
            }
            Puestos idPuesto = empleados.getIdPuesto();
            if (idPuesto != null) {
                idPuesto.getEmpleadosList().remove(empleados);
                idPuesto = em.merge(idPuesto);
            }
            Users idUser = empleados.getIdUser();
            if (idUser != null) {
                idUser.getEmpleadosList().remove(empleados);
                idUser = em.merge(idUser);
            }
            List<Bonos> bonosList = empleados.getBonosList();
            for (Bonos bonosListBonos : bonosList) {
                bonosListBonos.getEmpleadosList().remove(empleados);
                bonosListBonos = em.merge(bonosListBonos);
            }
            em.remove(empleados);
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

    public List<Empleados> findEmpleadosEntities() {
        return findEmpleadosEntities(true, -1, -1);
    }

    public List<Empleados> findEmpleadosEntities(int maxResults, int firstResult) {
        return findEmpleadosEntities(false, maxResults, firstResult);
    }

    private List<Empleados> findEmpleadosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleados.class));
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

    public Empleados findEmpleados(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleados.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleados> rt = cq.from(Empleados.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
>>>>>>> 3ec3023e315bb2dc3952b0d72da17fb39fbf9c91
