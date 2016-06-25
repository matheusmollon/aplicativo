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
public class CriarProdutoTeste {

    public CriarProdutoTeste() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        ProdutoJpaController pjc = ProdutoJpaController.getInstance();
        List<Produto> produtos = pjc.findProdutoEntities();

        for (Produto p : produtos) {
            if (p.getPNome().equals("TesteJUnitCRUD")) {
                try {
                    pjc.destroy(p.getPId());
                } catch (NonexistentEntityException ex) {
                    Logger.getLogger(CriarProdutoTeste.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void CriarTeste() {
        boolean armazenou;
        Produto produto = new Produto();
        Conta conta = ContaJpaController.getInstance().findConta(100);
        List<UsuarioConta> ucs = (List<UsuarioConta>) conta.getUsuarioContaCollection();
        List<Usuario> users = new ArrayList<>();

        for (UsuarioConta uc : ucs) {
            users.add(uc.getUsuario());
        }

        produto.setPNome("TesteJUnitCRUD");
        produto.setPValor(100.00);
        produto.setCId(conta);
        produto.setUsuarioCollection(users);

        armazenou = ProdutoJpaController.getInstance().create(produto);
        
        assertTrue(armazenou);

    }
}
