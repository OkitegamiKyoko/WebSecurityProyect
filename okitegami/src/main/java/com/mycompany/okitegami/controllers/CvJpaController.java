<<<<<<< HEAD
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers;

import com.mycompany.okitegami.controllers.exceptions.IllegalOrphanException;
import com.mycompany.okitegami.controllers.exceptions.NonexistentEntityException;
import com.mycompany.okitegami.controllers.exceptions.RollbackFailureException;
import com.mycompany.okitegami.models.Cv;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.okitegami.models.Empleados;
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
public class CvJpaController implements Serializable {

    public CvJpaController() throws NamingException {
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

    public void create(Cv cv) throws RollbackFailureException, Exception {
        if (cv.getEmpleadosList() == null) {
            cv.setEmpleadosList(new ArrayList<Empleados>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Empleados> attachedEmpleadosList = new ArrayList<Empleados>();
            for (Empleados empleadosListEmpleadosToAttach : cv.getEmpleadosList()) {
                empleadosListEmpleadosToAttach = em.getReference(empleadosListEmpleadosToAttach.getClass(), empleadosListEmpleadosToAttach.getId());
                attachedEmpleadosList.add(empleadosListEmpleadosToAttach);
            }
            cv.setEmpleadosList(attachedEmpleadosList);
            em.persist(cv);
            for (Empleados empleadosListEmpleados : cv.getEmpleadosList()) {
                Cv oldIdCvOfEmpleadosListEmpleados = empleadosListEmpleados.getIdCv();
                empleadosListEmpleados.setIdCv(cv);
                empleadosListEmpleados = em.merge(empleadosListEmpleados);
                if (oldIdCvOfEmpleadosListEmpleados != null) {
                    oldIdCvOfEmpleadosListEmpleados.getEmpleadosList().remove(empleadosListEmpleados);
                    oldIdCvOfEmpleadosListEmpleados = em.merge(oldIdCvOfEmpleadosListEmpleados);
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

    public void edit(Cv cv) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cv persistentCv = em.find(Cv.class, cv.getId());
            List<Empleados> empleadosListOld = persistentCv.getEmpleadosList();
            List<Empleados> empleadosListNew = cv.getEmpleadosList();
            List<String> illegalOrphanMessages = null;
            for (Empleados empleadosListOldEmpleados : empleadosListOld) {
                if (!empleadosListNew.contains(empleadosListOldEmpleados)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleados " + empleadosListOldEmpleados + " since its idCv field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Empleados> attachedEmpleadosListNew = new ArrayList<Empleados>();
            for (Empleados empleadosListNewEmpleadosToAttach : empleadosListNew) {
                empleadosListNewEmpleadosToAttach = em.getReference(empleadosListNewEmpleadosToAttach.getClass(), empleadosListNewEmpleadosToAttach.getId());
                attachedEmpleadosListNew.add(empleadosListNewEmpleadosToAttach);
            }
            empleadosListNew = attachedEmpleadosListNew;
            cv.setEmpleadosList(empleadosListNew);
            cv = em.merge(cv);
            for (Empleados empleadosListNewEmpleados : empleadosListNew) {
                if (!empleadosListOld.contains(empleadosListNewEmpleados)) {
                    Cv oldIdCvOfEmpleadosListNewEmpleados = empleadosListNewEmpleados.getIdCv();
                    empleadosListNewEmpleados.setIdCv(cv);
                    empleadosListNewEmpleados = em.merge(empleadosListNewEmpleados);
                    if (oldIdCvOfEmpleadosListNewEmpleados != null && !oldIdCvOfEmpleadosListNewEmpleados.equals(cv)) {
                        oldIdCvOfEmpleadosListNewEmpleados.getEmpleadosList().remove(empleadosListNewEmpleados);
                        oldIdCvOfEmpleadosListNewEmpleados = em.merge(oldIdCvOfEmpleadosListNewEmpleados);
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
                Integer id = cv.getId();
                if (findCv(id) == null) {
                    throw new NonexistentEntityException("The cv with id " + id + " no longer exists.");
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
            Cv cv;
            try {
                cv = em.getReference(Cv.class, id);
                cv.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cv with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Empleados> empleadosListOrphanCheck = cv.getEmpleadosList();
            for (Empleados empleadosListOrphanCheckEmpleados : empleadosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cv (" + cv + ") cannot be destroyed since the Empleados " + empleadosListOrphanCheckEmpleados + " in its empleadosList field has a non-nullable idCv field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cv);
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

    public List<Cv> findCvEntities() {
        return findCvEntities(true, -1, -1);
    }

    public List<Cv> findCvEntities(int maxResults, int firstResult) {
        return findCvEntities(false, maxResults, firstResult);
    }

    private List<Cv> findCvEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cv.class));
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

    public Cv findCv(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cv.class, id);
        } finally {
            em.close();
        }
    }

    public int getCvCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cv> rt = cq.from(Cv.class);
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
import com.mycompany.okitegami.models.Cv;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.okitegami.models.Empleados;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author meli_
 */
public class CvJpaController implements Serializable {

    public CvJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cv cv) throws RollbackFailureException, Exception {
        if (cv.getEmpleadosList() == null) {
            cv.setEmpleadosList(new ArrayList<Empleados>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Empleados> attachedEmpleadosList = new ArrayList<Empleados>();
            for (Empleados empleadosListEmpleadosToAttach : cv.getEmpleadosList()) {
                empleadosListEmpleadosToAttach = em.getReference(empleadosListEmpleadosToAttach.getClass(), empleadosListEmpleadosToAttach.getId());
                attachedEmpleadosList.add(empleadosListEmpleadosToAttach);
            }
            cv.setEmpleadosList(attachedEmpleadosList);
            em.persist(cv);
            for (Empleados empleadosListEmpleados : cv.getEmpleadosList()) {
                Cv oldIdCvOfEmpleadosListEmpleados = empleadosListEmpleados.getIdCv();
                empleadosListEmpleados.setIdCv(cv);
                empleadosListEmpleados = em.merge(empleadosListEmpleados);
                if (oldIdCvOfEmpleadosListEmpleados != null) {
                    oldIdCvOfEmpleadosListEmpleados.getEmpleadosList().remove(empleadosListEmpleados);
                    oldIdCvOfEmpleadosListEmpleados = em.merge(oldIdCvOfEmpleadosListEmpleados);
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

    public void edit(Cv cv) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cv persistentCv = em.find(Cv.class, cv.getId());
            List<Empleados> empleadosListOld = persistentCv.getEmpleadosList();
            List<Empleados> empleadosListNew = cv.getEmpleadosList();
            List<String> illegalOrphanMessages = null;
            for (Empleados empleadosListOldEmpleados : empleadosListOld) {
                if (!empleadosListNew.contains(empleadosListOldEmpleados)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleados " + empleadosListOldEmpleados + " since its idCv field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Empleados> attachedEmpleadosListNew = new ArrayList<Empleados>();
            for (Empleados empleadosListNewEmpleadosToAttach : empleadosListNew) {
                empleadosListNewEmpleadosToAttach = em.getReference(empleadosListNewEmpleadosToAttach.getClass(), empleadosListNewEmpleadosToAttach.getId());
                attachedEmpleadosListNew.add(empleadosListNewEmpleadosToAttach);
            }
            empleadosListNew = attachedEmpleadosListNew;
            cv.setEmpleadosList(empleadosListNew);
            cv = em.merge(cv);
            for (Empleados empleadosListNewEmpleados : empleadosListNew) {
                if (!empleadosListOld.contains(empleadosListNewEmpleados)) {
                    Cv oldIdCvOfEmpleadosListNewEmpleados = empleadosListNewEmpleados.getIdCv();
                    empleadosListNewEmpleados.setIdCv(cv);
                    empleadosListNewEmpleados = em.merge(empleadosListNewEmpleados);
                    if (oldIdCvOfEmpleadosListNewEmpleados != null && !oldIdCvOfEmpleadosListNewEmpleados.equals(cv)) {
                        oldIdCvOfEmpleadosListNewEmpleados.getEmpleadosList().remove(empleadosListNewEmpleados);
                        oldIdCvOfEmpleadosListNewEmpleados = em.merge(oldIdCvOfEmpleadosListNewEmpleados);
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
                Integer id = cv.getId();
                if (findCv(id) == null) {
                    throw new NonexistentEntityException("The cv with id " + id + " no longer exists.");
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
            Cv cv;
            try {
                cv = em.getReference(Cv.class, id);
                cv.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cv with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Empleados> empleadosListOrphanCheck = cv.getEmpleadosList();
            for (Empleados empleadosListOrphanCheckEmpleados : empleadosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cv (" + cv + ") cannot be destroyed since the Empleados " + empleadosListOrphanCheckEmpleados + " in its empleadosList field has a non-nullable idCv field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cv);
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

    public List<Cv> findCvEntities() {
        return findCvEntities(true, -1, -1);
    }

    public List<Cv> findCvEntities(int maxResults, int firstResult) {
        return findCvEntities(false, maxResults, firstResult);
    }

    private List<Cv> findCvEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cv.class));
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

    public Cv findCv(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cv.class, id);
        } finally {
            em.close();
        }
    }

    public int getCvCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cv> rt = cq.from(Cv.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
>>>>>>> 3ec3023e315bb2dc3952b0d72da17fb39fbf9c91
