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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Matheus Mollon
 */
public class EditarProdutoTeste {

    private Produto p = new Produto();
    private ProdutoJpaController pjc;

    public EditarProdutoTeste() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        verificarExistencia();
        criarProdutoTeste();

    }

    @After
    public void tearDown() {
    }

    @Test
    public void AtualizarTeste() {
        boolean atualizou = false;
        Produto produto = getInstanciaProduto();
        produto.setPNome("TesteJUnitCRUDEdited");
        produto.setPValor(30.00);

        try {
            atualizou = ProdutoJpaController.getInstance().edit(produto);
        } catch (Exception ex) {
            Logger.getLogger(EditarProdutoTeste.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("atualizou: " + atualizou);
        assertTrue(atualizou);
    }

    private void verificarExistencia() {
        pjc = ProdutoJpaController.getInstance();
        Conta conta = ContaJpaController.getInstance().findConta(100);
        List<Produto> produtos = (List<Produto>) conta.getProdutoCollection();
        for (Produto p1 : produtos) {
            if (p1.getPNome().equals("TesteJUnitCRUDEdited")) {
                try {
                    pjc.destroy(p1.getPId());
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(EditarProdutoTeste.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private Produto getInstanciaProduto() {

        pjc = ProdutoJpaController.getInstance();
        Conta conta = ContaJpaController.getInstance().findConta(100);
        List<Produto> produtos = (List<Produto>) conta.getProdutoCollection();
        for (Produto p1 : produtos) {
            if (p1.getPNome().equals("TesteEditCRUD")) {

                return p1;
            }
        }

        return null;
    }

    private void criarProdutoTeste() {
        Produto produto = new Produto();
        Conta conta = ContaJpaController.getInstance().findConta(100);
        List<UsuarioConta> ucs = (List<UsuarioConta>) conta.getUsuarioContaCollection();
        List<Usuario> users = new ArrayList<>();

        for (UsuarioConta uc : ucs) {
            users.add(uc.getUsuario());
        }

        produto.setPNome("TesteEditCRUD");
        produto.setPValor(60.00);
        produto.setCId(conta);
        produto.setUsuarioCollection(users);
        pjc = ProdutoJpaController.getInstance();
        pjc.create(produto);

    }
}
