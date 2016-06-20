/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpa.controller;

import br.jpa.controller.exceptions.IllegalOrphanException;
import br.jpa.controller.exceptions.NonexistentEntityException;
import br.jpa.controller.exceptions.PreexistingEntityException;
import br.jpa.entity.Usuario;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.jpa.entity.UsuarioConta;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author hideki
 */
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws PreexistingEntityException, Exception {
        if (usuario.getUsuarioContaCollection() == null) {
            usuario.setUsuarioContaCollection(new ArrayList<UsuarioConta>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<UsuarioConta> attachedUsuarioContaCollection = new ArrayList<UsuarioConta>();
            for (UsuarioConta usuarioContaCollectionUsuarioContaToAttach : usuario.getUsuarioContaCollection()) {
                usuarioContaCollectionUsuarioContaToAttach = em.getReference(usuarioContaCollectionUsuarioContaToAttach.getClass(), usuarioContaCollectionUsuarioContaToAttach.getUsuarioContaPK());
                attachedUsuarioContaCollection.add(usuarioContaCollectionUsuarioContaToAttach);
            }
            usuario.setUsuarioContaCollection(attachedUsuarioContaCollection);
            em.persist(usuario);
            for (UsuarioConta usuarioContaCollectionUsuarioConta : usuario.getUsuarioContaCollection()) {
                Usuario oldUsuarioOfUsuarioContaCollectionUsuarioConta = usuarioContaCollectionUsuarioConta.getUsuario();
                usuarioContaCollectionUsuarioConta.setUsuario(usuario);
                usuarioContaCollectionUsuarioConta = em.merge(usuarioContaCollectionUsuarioConta);
                if (oldUsuarioOfUsuarioContaCollectionUsuarioConta != null) {
                    oldUsuarioOfUsuarioContaCollectionUsuarioConta.getUsuarioContaCollection().remove(usuarioContaCollectionUsuarioConta);
                    oldUsuarioOfUsuarioContaCollectionUsuarioConta = em.merge(oldUsuarioOfUsuarioContaCollectionUsuarioConta);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findUsuario(usuario.getUNome()) != null) {
                throw new PreexistingEntityException("Usuario " + usuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getUNome());
            Collection<UsuarioConta> usuarioContaCollectionOld = persistentUsuario.getUsuarioContaCollection();
            Collection<UsuarioConta> usuarioContaCollectionNew = usuario.getUsuarioContaCollection();
            List<String> illegalOrphanMessages = null;
            for (UsuarioConta usuarioContaCollectionOldUsuarioConta : usuarioContaCollectionOld) {
                if (!usuarioContaCollectionNew.contains(usuarioContaCollectionOldUsuarioConta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain UsuarioConta " + usuarioContaCollectionOldUsuarioConta + " since its usuario field is not nullable.");
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
            usuario.setUsuarioContaCollection(usuarioContaCollectionNew);
            usuario = em.merge(usuario);
            for (UsuarioConta usuarioContaCollectionNewUsuarioConta : usuarioContaCollectionNew) {
                if (!usuarioContaCollectionOld.contains(usuarioContaCollectionNewUsuarioConta)) {
                    Usuario oldUsuarioOfUsuarioContaCollectionNewUsuarioConta = usuarioContaCollectionNewUsuarioConta.getUsuario();
                    usuarioContaCollectionNewUsuarioConta.setUsuario(usuario);
                    usuarioContaCollectionNewUsuarioConta = em.merge(usuarioContaCollectionNewUsuarioConta);
                    if (oldUsuarioOfUsuarioContaCollectionNewUsuarioConta != null && !oldUsuarioOfUsuarioContaCollectionNewUsuarioConta.equals(usuario)) {
                        oldUsuarioOfUsuarioContaCollectionNewUsuarioConta.getUsuarioContaCollection().remove(usuarioContaCollectionNewUsuarioConta);
                        oldUsuarioOfUsuarioContaCollectionNewUsuarioConta = em.merge(oldUsuarioOfUsuarioContaCollectionNewUsuarioConta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = usuario.getUNome();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getUNome();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<UsuarioConta> usuarioContaCollectionOrphanCheck = usuario.getUsuarioContaCollection();
            for (UsuarioConta usuarioContaCollectionOrphanCheckUsuarioConta : usuarioContaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the UsuarioConta " + usuarioContaCollectionOrphanCheckUsuarioConta + " in its usuarioContaCollection field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public List<Usuario> findUsuarioLike(String parameter) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("Usuario.findAllUNomeLike");
            query.setParameter("uNome", parameter + "%");
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
    public int findUsuarioLikeCount(String parameter) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            Query query = em.createNamedQuery("Usuario.findAllUNomeLikeCount");
            query.setParameter("uNome", parameter + "%");
            return ((Long) query.getSingleResult()).intValue();
        } catch (Exception ex) {
            return 0;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Usuario findUsuario(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
