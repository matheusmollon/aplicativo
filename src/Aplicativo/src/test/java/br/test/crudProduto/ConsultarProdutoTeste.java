/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.test.crudProduto;

import br.jpa.controller.ProdutoJpaController;
import br.jpa.entity.Produto;
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
public class ConsultarProdutoTeste extends CRUDTestStrategyModificado {

    public ConsultarProdutoTeste() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        criarProdutoTeste("TesteConsultaCRUD", 40.0);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void ConsultarExistenteTeste() {
        Produto p1 = ProdutoJpaController.getInstance().findProduto(getProduto("TesteConsultaCRUD").getPId());
        assertNotNull(p1);
    }

    @Test
    public void ConsultarNaoExistenteTeste() {
        Produto p1 = ProdutoJpaController.getInstance().findProduto(-1);
        assertNull(p1);
    }

}
