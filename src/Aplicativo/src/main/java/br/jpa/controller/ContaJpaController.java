/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpa.controller;

import br.jpa.controller.exceptions.IllegalOrphanException;
import br.jpa.controller.exceptions.NonexistentEntityException;
import br.jpa.entity.Conta;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.jpa.entity.UsuarioConta;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author hideki
 */
public class ContaJpaController implements Serializable {

    public ContaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Conta conta) {
        if (conta.getUsuarioContaCollection() == null) {
            conta.setUsuarioContaCollection(new ArrayList<UsuarioConta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<UsuarioConta> attachedUsuarioContaCollection = new ArrayList<UsuarioConta>();
            for (UsuarioConta usuarioContaCollectionUsuarioContaToAttach : conta.getUsuarioContaCollection()) {
                usuarioContaCollectionUsuarioContaToAttach = em.getReference(usuarioContaCollectionUsuarioContaToAttach.getClass(), usuarioContaCollectionUsuarioContaToAttach.getUsuarioContaPK());
                attachedUsuarioContaCollection.add(usuarioContaCollectionUsuarioContaToAttach);
            }
            conta.setUsuarioContaCollection(attachedUsuarioContaCollection);
            em.persist(conta);
            for (UsuarioConta usuarioContaCollectionUsuarioConta : conta.getUsuarioContaCollection()) {
                Conta oldContaOfUsuarioContaCollectionUsuarioConta = usuarioContaCollectionUsuarioConta.getConta();
                usuarioContaCollectionUsuarioConta.setConta(conta);
                usuarioContaCollectionUsuarioConta = em.merge(usuarioContaCollectionUsuarioConta);
                if (oldContaOfUsuarioContaCollectionUsuarioConta != null) {
                    oldContaOfUsuarioContaCollectionUsuarioConta.getUsuarioContaCollection().remove(usuarioContaCollectionUsuarioConta);
                    oldContaOfUsuarioContaCollectionUsuarioConta = em.merge(oldContaOfUsuarioContaCollectionUsuarioConta);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Conta conta) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Conta persistentConta = em.find(Conta.class, conta.getCId());
            Collection<UsuarioConta> usuarioContaCollectionOld = persistentConta.getUsuarioContaCollection();
            Collection<UsuarioConta> usuarioContaCollectionNew = conta.getUsuarioContaCollection();
            List<String> illegalOrphanMessages = null;
            for (UsuarioConta usuarioContaCollectionOldUsuarioConta : usuarioContaCollectionOld) {
                if (!usuarioContaCollectionNew.contains(usuarioContaCollectionOldUsuarioConta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioConta " + usuarioContaCollectionOldUsuarioConta + " since its conta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<UsuarioConta> attachedUsuarioContaCollectionNew = new ArrayList<UsuarioConta>();
            for (UsuarioConta usuarioContaCollectionNewUsuarioContaToAttach : usuarioContaCollectionNew) {
                usuarioContaCollectionNewUsuarioContaToAttach = em.getReference(usuarioContaCollectionNewUsuarioContaToAttach.getClass(), usuarioContaCollectionNewUsuarioContaToAttach.getUsuarioContaPK());
                attachedUsuarioContaCollectionNew.add(usuarioContaCollectionNewUsuarioContaToAttach);
            }
            usuarioContaCollectionNew = attachedUsuarioContaCollectionNew;
            conta.setUsuarioContaCollection(usuarioContaCollectionNew);
            conta = em.merge(conta);
            for (UsuarioConta usuarioContaCollectionNewUsuarioConta : usuarioContaCollectionNew) {
                if (!usuarioContaCollectionOld.contains(usuarioContaCollectionNewUsuarioConta)) {
                    Conta oldContaOfUsuarioContaCollectionNewUsuarioConta = usuarioContaCollectionNewUsuarioConta.getConta();
                    usuarioContaCollectionNewUsuarioConta.setConta(conta);
                    usuarioContaCollectionNewUsuarioConta = em.merge(usuarioContaCollectionNewUsuarioConta);
                    if (oldContaOfUsuarioContaCollectionNewUsuarioConta != null && !oldContaOfUsuarioContaCollectionNewUsuarioConta.equals(conta)) {
                        oldContaOfUsuarioContaCollectionNewUsuarioConta.getUsuarioContaCollection().remove(usuarioContaCollectionNewUsuarioConta);
                        oldContaOfUsuarioContaCollectionNewUsuarioConta = em.merge(oldContaOfUsuarioContaCollectionNewUsuarioConta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = conta.getCId();
                if (findConta(id) == null) {
                    throw new NonexistentEntityException("The conta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Conta conta;
            try {
                conta = em.getReference(Conta.class, id);
                conta.getCId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The conta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UsuarioConta> usuarioContaCollectionOrphanCheck = conta.getUsuarioContaCollection();
            for (UsuarioConta usuarioContaCollectionOrphanCheckUsuarioConta : usuarioContaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Conta (" + conta + ") cannot be destroyed since the UsuarioConta " + usuarioContaCollectionOrphanCheckUsuarioConta + " in its usuarioContaCollection field has a non-nullable conta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(conta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Conta> findContaEntities() {
        return findContaEntities(true, -1, -1);
    }

    public List<Conta> findContaEntities(int maxResults, int firstResult) {
        return findContaEntities(false, maxResults, firstResult);
    }

    private List<Conta> findContaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Conta.class));
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

    public Conta findConta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Conta.class, id);
        } finally {
            em.close();
        }
    }

    public int getContaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Conta> rt = cq.from(Conta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
