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
import com.mycompany.okitegami.models.Empleados;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.okitegami.models.Clientes;
import com.mycompany.okitegami.models.Direcciones;
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
public class DireccionesJpaController implements Serializable {

    public DireccionesJpaController() throws NamingException {
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

    public void create(Direcciones direcciones) throws RollbackFailureException, Exception {
        if (direcciones.getEmpleadosList() == null) {
            direcciones.setEmpleadosList(new ArrayList<Empleados>());
        }
        if (direcciones.getClientesList() == null) {
            direcciones.setClientesList(new ArrayList<Clientes>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Empleados> attachedEmpleadosList = new ArrayList<Empleados>();
            for (Empleados empleadosListEmpleadosToAttach : direcciones.getEmpleadosList()) {
                empleadosListEmpleadosToAttach = em.getReference(empleadosListEmpleadosToAttach.getClass(), empleadosListEmpleadosToAttach.getId());
                attachedEmpleadosList.add(empleadosListEmpleadosToAttach);
            }
            direcciones.setEmpleadosList(attachedEmpleadosList);
            List<Clientes> attachedClientesList = new ArrayList<Clientes>();
            for (Clientes clientesListClientesToAttach : direcciones.getClientesList()) {
                clientesListClientesToAttach = em.getReference(clientesListClientesToAttach.getClass(), clientesListClientesToAttach.getId());
                attachedClientesList.add(clientesListClientesToAttach);
            }
            direcciones.setClientesList(attachedClientesList);
            em.persist(direcciones);
            for (Empleados empleadosListEmpleados : direcciones.getEmpleadosList()) {
                Direcciones oldIdDirectOfEmpleadosListEmpleados = empleadosListEmpleados.getIdDirect();
                empleadosListEmpleados.setIdDirect(direcciones);
                empleadosListEmpleados = em.merge(empleadosListEmpleados);
                if (oldIdDirectOfEmpleadosListEmpleados != null) {
                    oldIdDirectOfEmpleadosListEmpleados.getEmpleadosList().remove(empleadosListEmpleados);
                    oldIdDirectOfEmpleadosListEmpleados = em.merge(oldIdDirectOfEmpleadosListEmpleados);
                }
            }
            for (Clientes clientesListClientes : direcciones.getClientesList()) {
                Direcciones oldIdDirectOfClientesListClientes = clientesListClientes.getIdDirect();
                clientesListClientes.setIdDirect(direcciones);
                clientesListClientes = em.merge(clientesListClientes);
                if (oldIdDirectOfClientesListClientes != null) {
                    oldIdDirectOfClientesListClientes.getClientesList().remove(clientesListClientes);
                    oldIdDirectOfClientesListClientes = em.merge(oldIdDirectOfClientesListClientes);
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

    public void edit(Direcciones direcciones) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Direcciones persistentDirecciones = em.find(Direcciones.class, direcciones.getId());
            List<Empleados> empleadosListOld = persistentDirecciones.getEmpleadosList();
            List<Empleados> empleadosListNew = direcciones.getEmpleadosList();
            List<Clientes> clientesListOld = persistentDirecciones.getClientesList();
            List<Clientes> clientesListNew = direcciones.getClientesList();
            List<String> illegalOrphanMessages = null;
            for (Empleados empleadosListOldEmpleados : empleadosListOld) {
                if (!empleadosListNew.contains(empleadosListOldEmpleados)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Empleados " + empleadosListOldEmpleados + " since its idDirect field is not nullable.");
                }
            }
            for (Clientes clientesListOldClientes : clientesListOld) {
                if (!clientesListNew.contains(clientesListOldClientes)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Clientes " + clientesListOldClientes + " since its idDirect field is not nullable.");
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
            direcciones.setEmpleadosList(empleadosListNew);
            List<Clientes> attachedClientesListNew = new ArrayList<Clientes>();
            for (Clientes clientesListNewClientesToAttach : clientesListNew) {
                clientesListNewClientesToAttach = em.getReference(clientesListNewClientesToAttach.getClass(), clientesListNewClientesToAttach.getId());
                attachedClientesListNew.add(clientesListNewClientesToAttach);
            }
            clientesListNew = attachedClientesListNew;
            direcciones.setClientesList(clientesListNew);
            direcciones = em.merge(direcciones);
            for (Empleados empleadosListNewEmpleados : empleadosListNew) {
                if (!empleadosListOld.contains(empleadosListNewEmpleados)) {
                    Direcciones oldIdDirectOfEmpleadosListNewEmpleados = empleadosListNewEmpleados.getIdDirect();
                    empleadosListNewEmpleados.setIdDirect(direcciones);
                    empleadosListNewEmpleados = em.merge(empleadosListNewEmpleados);
                    if (oldIdDirectOfEmpleadosListNewEmpleados != null && !oldIdDirectOfEmpleadosListNewEmpleados.equals(direcciones)) {
                        oldIdDirectOfEmpleadosListNewEmpleados.getEmpleadosList().remove(empleadosListNewEmpleados);
                        oldIdDirectOfEmpleadosListNewEmpleados = em.merge(oldIdDirectOfEmpleadosListNewEmpleados);
                    }
                }
            }
            for (Clientes clientesListNewClientes : clientesListNew) {
                if (!clientesListOld.contains(clientesListNewClientes)) {
                    Direcciones oldIdDirectOfClientesListNewClientes = clientesListNewClientes.getIdDirect();
                    clientesListNewClientes.setIdDirect(direcciones);
                    clientesListNewClientes = em.merge(clientesListNewClientes);
                    if (oldIdDirectOfClientesListNewClientes != null && !oldIdDirectOfClientesListNewClientes.equals(direcciones)) {
                        oldIdDirectOfClientesListNewClientes.getClientesList().remove(clientesListNewClientes);
                        oldIdDirectOfClientesListNewClientes = em.merge(oldIdDirectOfClientesListNewClientes);
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
                Integer id = direcciones.getId();
                if (findDirecciones(id) == null) {
                    throw new NonexistentEntityException("The direcciones with id " + id + " no longer exists.");
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
            Direcciones direcciones;
            try {
                direcciones = em.getReference(Direcciones.class, id);
                direcciones.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The direcciones with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Empleados> empleadosListOrphanCheck = direcciones.getEmpleadosList();
            for (Empleados empleadosListOrphanCheckEmpleados : empleadosListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Direcciones (" + direcciones + ") cannot be destroyed since the Empleados " + empleadosListOrphanCheckEmpleados + " in its empleadosList field has a non-nullable idDirect field.");
            }
            List<Clientes> clientesListOrphanCheck = direcciones.getClientesList();
            for (Clientes clientesListOrphanCheckClientes : clientesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Direcciones (" + direcciones + ") cannot be destroyed since the Clientes " + clientesListOrphanCheckClientes + " in its clientesList field has a non-nullable idDirect field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(direcciones);
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

    public List<Direcciones> findDireccionesEntities() {
        return findDireccionesEntities(true, -1, -1);
    }

    public List<Direcciones> findDireccionesEntities(int maxResults, int firstResult) {
        return findDireccionesEntities(false, maxResults, firstResult);
    }

    private List<Direcciones> findDireccionesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Direcciones.class));
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

    public Direcciones findDirecciones(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Direcciones.class, id);
        } finally {
            em.close();
        }
    }

    public int getDireccionesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Direcciones> rt = cq.from(Direcciones.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
