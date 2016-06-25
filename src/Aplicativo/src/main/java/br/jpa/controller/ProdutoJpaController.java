/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpa.controller;

import br.jpa.controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.jpa.entity.Conta;
import br.jpa.entity.Produto;
import br.jpa.entity.Usuario;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Matheus Mollon
 */
public class ProdutoJpaController implements Serializable {

    private static ProdutoJpaController pjc;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    private ProdutoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("AplicativoPU");
    }

    public static ProdutoJpaController getInstance() {
        if (pjc == null) {
            pjc = new ProdutoJpaController();
        }
        return pjc;
    }

    public boolean create(Produto produto) {
        boolean armazenou = false;
        if (produto.getUsuarioCollection() == null) {
            produto.setUsuarioCollection(new ArrayList<Usuario>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Conta CId = produto.getCId();
            if (CId != null) {
                CId = em.getReference(CId.getClass(), CId.getCId());
                produto.setCId(CId);
            }
            Collection<Usuario> attachedUsuarioCollection = new ArrayList<Usuario>();
            for (Usuario usuarioCollectionUsuarioToAttach : produto.getUsuarioCollection()) {
                usuarioCollectionUsuarioToAttach = em.getReference(usuarioCollectionUsuarioToAttach.getClass(), usuarioCollectionUsuarioToAttach.getUNome());
                attachedUsuarioCollection.add(usuarioCollectionUsuarioToAttach);
            }
            produto.setUsuarioCollection(attachedUsuarioCollection);
            em.persist(produto);
            if (CId != null) {
                CId.getProdutoCollection().add(produto);
                CId = em.merge(CId);
            }
            for (Usuario usuarioCollectionUsuario : produto.getUsuarioCollection()) {
                usuarioCollectionUsuario.getProdutoCollection().add(produto);
                usuarioCollectionUsuario = em.merge(usuarioCollectionUsuario);
            }
            em.getTransaction().commit();
            armazenou = true;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return armazenou;
    }

    public boolean edit(Produto produto) throws NonexistentEntityException, Exception {
        boolean editado = false;
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produto persistentProduto = em.find(Produto.class, produto.getPId());
            Conta CIdOld = persistentProduto.getCId();
            Conta CIdNew = produto.getCId();
            Collection<Usuario> usuarioCollectionOld = persistentProduto.getUsuarioCollection();
            Collection<Usuario> usuarioCollectionNew = produto.getUsuarioCollection();
            if (CIdNew != null) {
                CIdNew = em.getReference(CIdNew.getClass(), CIdNew.getCId());
                produto.setCId(CIdNew);
            }
            Collection<Usuario> attachedUsuarioCollectionNew = new ArrayList<Usuario>();
            for (Usuario usuarioCollectionNewUsuarioToAttach : usuarioCollectionNew) {
                usuarioCollectionNewUsuarioToAttach = em.getReference(usuarioCollectionNewUsuarioToAttach.getClass(), usuarioCollectionNewUsuarioToAttach.getUNome());
                attachedUsuarioCollectionNew.add(usuarioCollectionNewUsuarioToAttach);
            }
            usuarioCollectionNew = attachedUsuarioCollectionNew;
            produto.setUsuarioCollection(usuarioCollectionNew);
            produto = em.merge(produto);
            if (CIdOld != null && !CIdOld.equals(CIdNew)) {
                CIdOld.getProdutoCollection().remove(produto);
                CIdOld = em.merge(CIdOld);
            }
            if (CIdNew != null && !CIdNew.equals(CIdOld)) {
                CIdNew.getProdutoCollection().add(produto);
                CIdNew = em.merge(CIdNew);
            }
            for (Usuario usuarioCollectionOldUsuario : usuarioCollectionOld) {
                if (!usuarioCollectionNew.contains(usuarioCollectionOldUsuario)) {
                    usuarioCollectionOldUsuario.getProdutoCollection().remove(produto);
                    usuarioCollectionOldUsuario = em.merge(usuarioCollectionOldUsuario);
                }
            }
            for (Usuario usuarioCollectionNewUsuario : usuarioCollectionNew) {
                if (!usuarioCollectionOld.contains(usuarioCollectionNewUsuario)) {
                    usuarioCollectionNewUsuario.getProdutoCollection().add(produto);
                    usuarioCollectionNewUsuario = em.merge(usuarioCollectionNewUsuario);
                }
            }
            em.getTransaction().commit();
            editado = true;
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = produto.getPId();
                if (findProduto(id) == null) {
                    throw new NonexistentEntityException("The produto with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return editado;
    }

    public List<Usuario> getAllUsersFromProduct(int id) {
        EntityManager em = emf.createEntityManager();
        List<Usuario> users = em.createNamedQuery("getAllUsersFromProduct", Usuario.class).setParameter(1, id).getResultList();
        return users;
    }

    public List<Usuario> getAllUsersFromAccount(int id) {
        EntityManager em = emf.createEntityManager();
        List<Usuario> users = em.createNamedQuery("getAllUsersFromAccount", Usuario.class).setParameter(1, id).getResultList();
        return users;
    }

    public boolean destroy(Integer id) throws NonexistentEntityException {
        boolean apagado = false;
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produto produto;
            try {
                produto = em.getReference(Produto.class, id);
                produto.getPId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produto with id " + id + " no longer exists.", enfe);
            }
            Conta CId = produto.getCId();
            if (CId != null) {
                CId.getProdutoCollection().remove(produto);
                CId = em.merge(CId);
            }
            Collection<Usuario> usuarioCollection = produto.getUsuarioCollection();
            for (Usuario usuarioCollectionUsuario : usuarioCollection) {
                usuarioCollectionUsuario.getProdutoCollection().remove(produto);
                usuarioCollectionUsuario = em.merge(usuarioCollectionUsuario);
            }
            em.remove(produto);
            em.getTransaction().commit();
            apagado = true;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return apagado;
    }

    public List<Produto> findProdutoEntities() {
        return findProdutoEntities(true, -1, -1);
    }

    public List<Produto> findProdutoEntities(int maxResults, int firstResult) {
        return findProdutoEntities(false, maxResults, firstResult);
    }

    private List<Produto> findProdutoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Produto.class));
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

    public Produto findProduto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Produto.class, id);
        } finally {
            em.close();
        }
    }

    public int getProdutoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produto> rt = cq.from(Produto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
