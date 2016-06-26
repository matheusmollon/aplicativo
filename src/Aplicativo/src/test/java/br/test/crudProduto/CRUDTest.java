/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.test.crudProduto;

import br.jpa.controller.ContaJpaController;
import br.jpa.controller.ProdutoJpaController;
import br.jpa.controller.exceptions.NonexistentEntityException;
import br.jpa.entity.Conta;
import br.jpa.entity.Produto;
import br.jpa.entity.Usuario;
import br.jpa.entity.UsuarioConta;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matheus Mollon
 */
public abstract class CRUDTest {

    private ProdutoJpaController pjc;
    protected final int id_conta = 100;

    public void criarProdutoTeste(String produtoNome, double Produtopreco) {
        pjc = ProdutoJpaController.getInstance();
        Produto produto = new Produto();
        Conta conta = ContaJpaController.getInstance().findConta(id_conta);
        List<UsuarioConta> ucs = (List<UsuarioConta>) conta.getUsuarioContaCollection();
        List<Usuario> users = new ArrayList<>();

        for (UsuarioConta uc : ucs) {
            users.add(uc.getUsuario());
        }

        produto.setPNome(produtoNome);
        produto.setPValor(Produtopreco);
        produto.setCId(conta);
        produto.setUsuarioCollection(users);
        pjc = ProdutoJpaController.getInstance();
        pjc.create(produto);

    }
    
    public Produto getProduto(String produtoNome) {

        pjc = ProdutoJpaController.getInstance();
        Conta conta = ContaJpaController.getInstance().findConta(id_conta);
        List<Produto> produtos = (List<Produto>) conta.getProdutoCollection();
        for (Produto p1 : produtos) {
            if (p1.getPNome().equals(produtoNome)) {

                return p1;
            }
        }

        return null;
    }
    
     public void deletarProduto(String produtoNome) {
        pjc = ProdutoJpaController.getInstance();
        Conta conta = ContaJpaController.getInstance().findConta(id_conta);
        List<Produto> produtos = (List<Produto>) conta.getProdutoCollection();
        for (Produto p1 : produtos) {
            if (p1.getPNome().equals(produtoNome)) {
                try {
                    pjc.destroy(p1.getPId());
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(EditarProdutoTeste.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    

}
