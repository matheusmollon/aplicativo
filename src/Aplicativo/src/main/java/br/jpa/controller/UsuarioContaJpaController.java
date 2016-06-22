/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpa.controller;

import br.jpa.controller.exceptions.NonexistentEntityException;
import br.jpa.controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.jpa.entity.Conta;
import br.jpa.entity.Usuario;
import br.jpa.entity.UsuarioConta;
import br.jpa.entity.UsuarioContaPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author hideki
 */
public class UsuarioContaJpaController implements Serializable {

    private static UsuarioContaJpaController ucjc;
    private EntityManagerFactory emf = null;

    private UsuarioContaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("AplicativoPU");
    }
    
    public static UsuarioContaJpaController getInstance() {
        if(ucjc == null) {
            ucjc = new UsuarioContaJpaController();
        }
        
        return ucjc;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UsuarioConta usuarioConta) throws PreexistingEntityException, Exception {
        if (usuarioConta.getUsuarioContaPK() == null) {
            usuarioConta.setUsuarioContaPK(new UsuarioContaPK());
        }
        usuarioConta.getUsuarioContaPK().setUNome(usuarioConta.getUsuario().getUNome());
        usuarioConta.getUsuarioContaPK().setCId(usuarioConta.getConta().getCId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Conta conta = usuarioConta.getConta();
            if (conta != null) {
                conta = em.getReference(conta.getClass(), conta.getCId());
                usuarioConta.setConta(conta);
            }
            Usuario usuario = usuarioConta.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getUNome());
                usuarioConta.setUsuario(usuario);
            }
            em.persist(usuarioConta);
            if (conta != null) {
                conta.getUsuarioContaCollection().add(usuarioConta);
                conta = em.merge(conta);
            }
            if (usuario != null) {
                usuario.getUsuarioContaCollection().add(usuarioConta);
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuarioConta(usuarioConta.getUsuarioContaPK()) != null) {
                throw new PreexistingEntityException("UsuarioConta " + usuarioConta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UsuarioConta usuarioConta) throws NonexistentEntityException, Exception {
        usuarioConta.getUsuarioContaPK().setUNome(usuarioConta.getUsuario().getUNome());
        usuarioConta.getUsuarioContaPK().setCId(usuarioConta.getConta().getCId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioConta persistentUsuarioConta = em.find(UsuarioConta.class, usuarioConta.getUsuarioContaPK());
            Conta contaOld = persistentUsuarioConta.getConta();
            Conta contaNew = usuarioConta.getConta();
            Usuario usuarioOld = persistentUsuarioConta.getUsuario();
            Usuario usuarioNew = usuarioConta.getUsuario();
            if (contaNew != null) {
                contaNew = em.getReference(contaNew.getClass(), contaNew.getCId());
                usuarioConta.setConta(contaNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getUNome());
                usuarioConta.setUsuario(usuarioNew);
            }
            usuarioConta = em.merge(usuarioConta);
            if (contaOld != null && !contaOld.equals(contaNew)) {
                contaOld.getUsuarioContaCollection().remove(usuarioConta);
                contaOld = em.merge(contaOld);
            }
            if (contaNew != null && !contaNew.equals(contaOld)) {
                contaNew.getUsuarioContaCollection().add(usuarioConta);
                contaNew = em.merge(contaNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getUsuarioContaCollection().remove(usuarioConta);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getUsuarioContaCollection().add(usuarioConta);
                usuarioNew = em.merge(usuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                UsuarioContaPK id = usuarioConta.getUsuarioContaPK();
                if (findUsuarioConta(id) == null) {
                    throw new NonexistentEntityException("The usuarioConta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(UsuarioContaPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            UsuarioConta usuarioConta;
            try {
                usuarioConta = em.getReference(UsuarioConta.class, id);
                usuarioConta.getUsuarioContaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarioConta with id " + id + " no longer exists.", enfe);
            }
            Conta conta = usuarioConta.getConta();
            if (conta != null) {
                conta.getUsuarioContaCollection().remove(usuarioConta);
                conta = em.merge(conta);
            }
            Usuario usuario = usuarioConta.getUsuario();
            if (usuario != null) {
                usuario.getUsuarioContaCollection().remove(usuarioConta);
                usuario = em.merge(usuario);
            }
            em.remove(usuarioConta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<UsuarioConta> findUsuarioContaEntities() {
        return findUsuarioContaEntities(true, -1, -1);
    }

    public List<UsuarioConta> findUsuarioContaEntities(int maxResults, int firstResult) {
        return findUsuarioContaEntities(false, maxResults, firstResult);
    }

    private List<UsuarioConta> findUsuarioContaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UsuarioConta.class));
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

    public UsuarioConta findUsuarioConta(UsuarioContaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UsuarioConta.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioContaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UsuarioConta> rt = cq.from(UsuarioConta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
