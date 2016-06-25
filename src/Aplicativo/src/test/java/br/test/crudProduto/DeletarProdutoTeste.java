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
public class DeletarProdutoTeste extends CRUDTestStrategyModificado {

    private Produto p = new Produto();
    private ProdutoJpaController pjc;

    public DeletarProdutoTeste() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        criarProdutoTeste("TesteDeleteCRUD",30.0);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void DeletarTeste() {
        boolean deletou = false;
        try {
            p = getProduto("TesteDeleteCRUD");
            pjc = ProdutoJpaController.getInstance();
            deletou = pjc.destroy(p.getPId());
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(DeletarProdutoTeste.class.getName()).log(Level.SEVERE, null, ex);
        }

        assertTrue(deletou);

    }

}
