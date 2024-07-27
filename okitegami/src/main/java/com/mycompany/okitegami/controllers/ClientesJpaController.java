<<<<<<< HEAD
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.okitegami.controllers;

import com.mycompany.okitegami.controllers.exceptions.IllegalOrphanException;
import com.mycompany.okitegami.controllers.exceptions.NonexistentEntityException;
import com.mycompany.okitegami.controllers.exceptions.RollbackFailureException;
import com.mycompany.okitegami.models.Clientes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.okitegami.models.Direcciones;
import com.mycompany.okitegami.models.Users;
import com.mycompany.okitegami.models.ContratosClientes;
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
public class ClientesJpaController implements Serializable {

    public ClientesJpaController() throws NamingException {
        this.utx = (UserTransaction) new InitialContext().lookup("java:comp/UserTransaction");
        this.emf = Persistence.createEntityManagerFactory("okitegami_db_pool");
    }
    private UserTransaction utx;
    private EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clientes clientes) throws IllegalOrphanException, RollbackFailureException, Exception {
        if (clientes.getContratosClientesList() == null) {
            clientes.setContratosClientesList(new ArrayList<ContratosClientes>());
        }
        List<String> illegalOrphanMessages = null;
        Users idUserOrphanCheck = clientes.getIdUser();
        if (idUserOrphanCheck != null) {
            Clientes oldClientesOfIdUser = idUserOrphanCheck.getClientes();
            if (oldClientesOfIdUser != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Users " + idUserOrphanCheck + " already has an item of type Clientes whose idUser column cannot be null. Please make another selection for the idUser field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Direcciones idDirect = clientes.getIdDirect();
            if (idDirect != null) {
                idDirect = em.getReference(idDirect.getClass(), idDirect.getId());
                clientes.setIdDirect(idDirect);
            }
            Users idUser = clientes.getIdUser();
            if (idUser != null) {
                idUser = em.getReference(idUser.getClass(), idUser.getId());
                clientes.setIdUser(idUser);
            }
            List<ContratosClientes> attachedContratosClientesList = new ArrayList<ContratosClientes>();
            for (ContratosClientes contratosClientesListContratosClientesToAttach : clientes.getContratosClientesList()) {
                contratosClientesListContratosClientesToAttach = em.getReference(contratosClientesListContratosClientesToAttach.getClass(), contratosClientesListContratosClientesToAttach.getId());
                attachedContratosClientesList.add(contratosClientesListContratosClientesToAttach);
            }
            clientes.setContratosClientesList(attachedContratosClientesList);
            em.persist(clientes);
            if (idDirect != null) {
                idDirect.getClientesList().add(clientes);
                idDirect = em.merge(idDirect);
            }
            if (idUser != null) {
                idUser.setClientes(clientes);
                idUser = em.merge(idUser);
            }
            for (ContratosClientes contratosClientesListContratosClientes : clientes.getContratosClientesList()) {
                Clientes oldIdClienteOfContratosClientesListContratosClientes = contratosClientesListContratosClientes.getIdCliente();
                contratosClientesListContratosClientes.setIdCliente(clientes);
                contratosClientesListContratosClientes = em.merge(contratosClientesListContratosClientes);
                if (oldIdClienteOfContratosClientesListContratosClientes != null) {
                    oldIdClienteOfContratosClientesListContratosClientes.getContratosClientesList().remove(contratosClientesListContratosClientes);
                    oldIdClienteOfContratosClientesListContratosClientes = em.merge(oldIdClienteOfContratosClientesListContratosClientes);
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

    public void edit(Clientes clientes) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Clientes persistentClientes = em.find(Clientes.class, clientes.getId());
            Direcciones idDirectOld = persistentClientes.getIdDirect();
            Direcciones idDirectNew = clientes.getIdDirect();
            Users idUserOld = persistentClientes.getIdUser();
            Users idUserNew = clientes.getIdUser();
            List<ContratosClientes> contratosClientesListOld = persistentClientes.getContratosClientesList();
            List<ContratosClientes> contratosClientesListNew = clientes.getContratosClientesList();
            List<String> illegalOrphanMessages = null;
            if (idUserNew != null && !idUserNew.equals(idUserOld)) {
                Clientes oldClientesOfIdUser = idUserNew.getClientes();
                if (oldClientesOfIdUser != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Users " + idUserNew + " already has an item of type Clientes whose idUser column cannot be null. Please make another selection for the idUser field.");
                }
            }
            for (ContratosClientes contratosClientesListOldContratosClientes : contratosClientesListOld) {
                if (!contratosClientesListNew.contains(contratosClientesListOldContratosClientes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ContratosClientes " + contratosClientesListOldContratosClientes + " since its idCliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idDirectNew != null) {
                idDirectNew = em.getReference(idDirectNew.getClass(), idDirectNew.getId());
                clientes.setIdDirect(idDirectNew);
            }
            if (idUserNew != null) {
                idUserNew = em.getReference(idUserNew.getClass(), idUserNew.getId());
                clientes.setIdUser(idUserNew);
            }
            List<ContratosClientes> attachedContratosClientesListNew = new ArrayList<ContratosClientes>();
            for (ContratosClientes contratosClientesListNewContratosClientesToAttach : contratosClientesListNew) {
                contratosClientesListNewContratosClientesToAttach = em.getReference(contratosClientesListNewContratosClientesToAttach.getClass(), contratosClientesListNewContratosClientesToAttach.getId());
                attachedContratosClientesListNew.add(contratosClientesListNewContratosClientesToAttach);
            }
            contratosClientesListNew = attachedContratosClientesListNew;
            clientes.setContratosClientesList(contratosClientesListNew);
            clientes = em.merge(clientes);
            if (idDirectOld != null && !idDirectOld.equals(idDirectNew)) {
                idDirectOld.getClientesList().remove(clientes);
                idDirectOld = em.merge(idDirectOld);
            }
            if (idDirectNew != null && !idDirectNew.equals(idDirectOld)) {
                idDirectNew.getClientesList().add(clientes);
                idDirectNew = em.merge(idDirectNew);
            }
            if (idUserOld != null && !idUserOld.equals(idUserNew)) {
                idUserOld.setClientes(null);
                idUserOld = em.merge(idUserOld);
            }
            if (idUserNew != null && !idUserNew.equals(idUserOld)) {
                idUserNew.setClientes(clientes);
                idUserNew = em.merge(idUserNew);
            }
            for (ContratosClientes contratosClientesListNewContratosClientes : contratosClientesListNew) {
                if (!contratosClientesListOld.contains(contratosClientesListNewContratosClientes)) {
                    Clientes oldIdClienteOfContratosClientesListNewContratosClientes = contratosClientesListNewContratosClientes.getIdCliente();
                    contratosClientesListNewContratosClientes.setIdCliente(clientes);
                    contratosClientesListNewContratosClientes = em.merge(contratosClientesListNewContratosClientes);
                    if (oldIdClienteOfContratosClientesListNewContratosClientes != null && !oldIdClienteOfContratosClientesListNewContratosClientes.equals(clientes)) {
                        oldIdClienteOfContratosClientesListNewContratosClientes.getContratosClientesList().remove(contratosClientesListNewContratosClientes);
                        oldIdClienteOfContratosClientesListNewContratosClientes = em.merge(oldIdClienteOfContratosClientesListNewContratosClientes);
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
                Integer id = clientes.getId();
                if (findClientes(id) == null) {
                    throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.");
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
            Clientes clientes;
            try {
                clientes = em.getReference(Clientes.class, id);
                clientes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ContratosClientes> contratosClientesListOrphanCheck = clientes.getContratosClientesList();
            for (ContratosClientes contratosClientesListOrphanCheckContratosClientes : contratosClientesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clientes (" + clientes + ") cannot be destroyed since the ContratosClientes " + contratosClientesListOrphanCheckContratosClientes + " in its contratosClientesList field has a non-nullable idCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Direcciones idDirect = clientes.getIdDirect();
            if (idDirect != null) {
                idDirect.getClientesList().remove(clientes);
                idDirect = em.merge(idDirect);
            }
            Users idUser = clientes.getIdUser();
            if (idUser != null) {
                idUser.setClientes(null);
                idUser = em.merge(idUser);
            }
            em.remove(clientes);
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

    public List<Clientes> findClientesEntities() {
        return findClientesEntities(true, -1, -1);
    }

    public List<Clientes> findClientesEntities(int maxResults, int firstResult) {
        return findClientesEntities(false, maxResults, firstResult);
    }

    private List<Clientes> findClientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clientes.class));
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

    public Clientes findClientes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clientes> rt = cq.from(Clientes.class);
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
import com.mycompany.okitegami.models.Clientes;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.okitegami.models.Direcciones;
import com.mycompany.okitegami.models.Users;
import com.mycompany.okitegami.models.ContratosClientes;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author meli_
 */
public class ClientesJpaController implements Serializable {

    public ClientesJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clientes clientes) throws RollbackFailureException, Exception {
        if (clientes.getContratosClientesList() == null) {
            clientes.setContratosClientesList(new ArrayList<ContratosClientes>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Direcciones idDirect = clientes.getIdDirect();
            if (idDirect != null) {
                idDirect = em.getReference(idDirect.getClass(), idDirect.getId());
                clientes.setIdDirect(idDirect);
            }
            Users idUser = clientes.getIdUser();
            if (idUser != null) {
                idUser = em.getReference(idUser.getClass(), idUser.getId());
                clientes.setIdUser(idUser);
            }
            List<ContratosClientes> attachedContratosClientesList = new ArrayList<ContratosClientes>();
            for (ContratosClientes contratosClientesListContratosClientesToAttach : clientes.getContratosClientesList()) {
                contratosClientesListContratosClientesToAttach = em.getReference(contratosClientesListContratosClientesToAttach.getClass(), contratosClientesListContratosClientesToAttach.getId());
                attachedContratosClientesList.add(contratosClientesListContratosClientesToAttach);
            }
            clientes.setContratosClientesList(attachedContratosClientesList);
            em.persist(clientes);
            if (idDirect != null) {
                idDirect.getClientesList().add(clientes);
                idDirect = em.merge(idDirect);
            }
            if (idUser != null) {
                idUser.getClientesList().add(clientes);
                idUser = em.merge(idUser);
            }
            for (ContratosClientes contratosClientesListContratosClientes : clientes.getContratosClientesList()) {
                Clientes oldIdClienteOfContratosClientesListContratosClientes = contratosClientesListContratosClientes.getIdCliente();
                contratosClientesListContratosClientes.setIdCliente(clientes);
                contratosClientesListContratosClientes = em.merge(contratosClientesListContratosClientes);
                if (oldIdClienteOfContratosClientesListContratosClientes != null) {
                    oldIdClienteOfContratosClientesListContratosClientes.getContratosClientesList().remove(contratosClientesListContratosClientes);
                    oldIdClienteOfContratosClientesListContratosClientes = em.merge(oldIdClienteOfContratosClientesListContratosClientes);
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

    public void edit(Clientes clientes) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Clientes persistentClientes = em.find(Clientes.class, clientes.getId());
            Direcciones idDirectOld = persistentClientes.getIdDirect();
            Direcciones idDirectNew = clientes.getIdDirect();
            Users idUserOld = persistentClientes.getIdUser();
            Users idUserNew = clientes.getIdUser();
            List<ContratosClientes> contratosClientesListOld = persistentClientes.getContratosClientesList();
            List<ContratosClientes> contratosClientesListNew = clientes.getContratosClientesList();
            List<String> illegalOrphanMessages = null;
            for (ContratosClientes contratosClientesListOldContratosClientes : contratosClientesListOld) {
                if (!contratosClientesListNew.contains(contratosClientesListOldContratosClientes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ContratosClientes " + contratosClientesListOldContratosClientes + " since its idCliente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idDirectNew != null) {
                idDirectNew = em.getReference(idDirectNew.getClass(), idDirectNew.getId());
                clientes.setIdDirect(idDirectNew);
            }
            if (idUserNew != null) {
                idUserNew = em.getReference(idUserNew.getClass(), idUserNew.getId());
                clientes.setIdUser(idUserNew);
            }
            List<ContratosClientes> attachedContratosClientesListNew = new ArrayList<ContratosClientes>();
            for (ContratosClientes contratosClientesListNewContratosClientesToAttach : contratosClientesListNew) {
                contratosClientesListNewContratosClientesToAttach = em.getReference(contratosClientesListNewContratosClientesToAttach.getClass(), contratosClientesListNewContratosClientesToAttach.getId());
                attachedContratosClientesListNew.add(contratosClientesListNewContratosClientesToAttach);
            }
            contratosClientesListNew = attachedContratosClientesListNew;
            clientes.setContratosClientesList(contratosClientesListNew);
            clientes = em.merge(clientes);
            if (idDirectOld != null && !idDirectOld.equals(idDirectNew)) {
                idDirectOld.getClientesList().remove(clientes);
                idDirectOld = em.merge(idDirectOld);
            }
            if (idDirectNew != null && !idDirectNew.equals(idDirectOld)) {
                idDirectNew.getClientesList().add(clientes);
                idDirectNew = em.merge(idDirectNew);
            }
            if (idUserOld != null && !idUserOld.equals(idUserNew)) {
                idUserOld.getClientesList().remove(clientes);
                idUserOld = em.merge(idUserOld);
            }
            if (idUserNew != null && !idUserNew.equals(idUserOld)) {
                idUserNew.getClientesList().add(clientes);
                idUserNew = em.merge(idUserNew);
            }
            for (ContratosClientes contratosClientesListNewContratosClientes : contratosClientesListNew) {
                if (!contratosClientesListOld.contains(contratosClientesListNewContratosClientes)) {
                    Clientes oldIdClienteOfContratosClientesListNewContratosClientes = contratosClientesListNewContratosClientes.getIdCliente();
                    contratosClientesListNewContratosClientes.setIdCliente(clientes);
                    contratosClientesListNewContratosClientes = em.merge(contratosClientesListNewContratosClientes);
                    if (oldIdClienteOfContratosClientesListNewContratosClientes != null && !oldIdClienteOfContratosClientesListNewContratosClientes.equals(clientes)) {
                        oldIdClienteOfContratosClientesListNewContratosClientes.getContratosClientesList().remove(contratosClientesListNewContratosClientes);
                        oldIdClienteOfContratosClientesListNewContratosClientes = em.merge(oldIdClienteOfContratosClientesListNewContratosClientes);
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
                Integer id = clientes.getId();
                if (findClientes(id) == null) {
                    throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.");
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
            Clientes clientes;
            try {
                clientes = em.getReference(Clientes.class, id);
                clientes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clientes with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ContratosClientes> contratosClientesListOrphanCheck = clientes.getContratosClientesList();
            for (ContratosClientes contratosClientesListOrphanCheckContratosClientes : contratosClientesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clientes (" + clientes + ") cannot be destroyed since the ContratosClientes " + contratosClientesListOrphanCheckContratosClientes + " in its contratosClientesList field has a non-nullable idCliente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Direcciones idDirect = clientes.getIdDirect();
            if (idDirect != null) {
                idDirect.getClientesList().remove(clientes);
                idDirect = em.merge(idDirect);
            }
            Users idUser = clientes.getIdUser();
            if (idUser != null) {
                idUser.getClientesList().remove(clientes);
                idUser = em.merge(idUser);
            }
            em.remove(clientes);
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

    public List<Clientes> findClientesEntities() {
        return findClientesEntities(true, -1, -1);
    }

    public List<Clientes> findClientesEntities(int maxResults, int firstResult) {
        return findClientesEntities(false, maxResults, firstResult);
    }

    private List<Clientes> findClientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clientes.class));
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

    public Clientes findClientes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getClientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clientes> rt = cq.from(Clientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
>>>>>>> 3ec3023e315bb2dc3952b0d72da17fb39fbf9c91
