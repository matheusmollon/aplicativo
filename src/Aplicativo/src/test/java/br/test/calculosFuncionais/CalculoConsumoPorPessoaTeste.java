/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.test.calculosFuncionais;

import br.jpa.calculos.CalculosFuncionais;
import br.jpa.controller.ContaJpaController;
import br.jpa.controller.UsuarioContaJpaController;
import br.jpa.controller.exceptions.NonexistentEntityException;
import br.jpa.entity.Conta;
import br.jpa.entity.Produto;
import br.jpa.entity.UsuarioConta;
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
public class CalculoConsumoPorPessoaTeste {

    private ContaJpaController cjc;
    private List<UsuarioConta> uc;

    public CalculoConsumoPorPessoaTeste() {
    }

    @Test
    public void CalculoConsumoTesteRoteiro1() {
        setUpRoteiro(101);
        Conta conta = cjc.findConta(101);
        CalculosFuncionais cf = new CalculosFuncionais(conta, 10);
        cf.FecharConta();

        UsuarioContaJpaController ucjc = UsuarioContaJpaController.getInstance();
        List<UsuarioConta> ucs = ucjc.getUsuarioContaPorId(conta.getCId());
        for (UsuarioConta uc : ucs) {

            assertEquals(19.25, uc.getUCValor(), 0.01);
        }

    }

    @Test
    public void CalculoConsumoRoteiro2User1() {
        CalculoConsumoTesteRoteiro2Cenario();
        for (UsuarioConta u : uc) {
            if (u.getUsuario().getUNome().equals("User1")) {
                assertEquals(31.83, u.getUCValor(), 0.01);
            }
        }
    }

    @Test
    public void CalculoConsumoRoteiro2User2() {
        uc = getUsuarioConta(102);
        for (UsuarioConta u : uc) {
            if (u.getUsuario().getUNome().equals("User2")) {
                assertEquals(34.33, u.getUCValor(), 0.01);
            }
        }
    }

    @Test
    public void CalculoConsumoRoteiro2User3() {
        uc = getUsuarioConta(102);
        for (UsuarioConta u : uc) {
            if (u.getUsuario().getUNome().equals("User3")) {
                assertEquals(49.34, u.getUCValor(), 0.01);
            }
        }
    }

    public List<UsuarioConta> getUsuarioConta(int id) {
        return UsuarioContaJpaController.getInstance().getUsuarioContaPorId(id);
    }

    public void CalculoConsumoTesteRoteiro2Cenario() {
        setUpRoteiro(102);
        Conta conta = cjc.findConta(102);
        CalculosFuncionais cf = new CalculosFuncionais(conta, 10);
        cf.FecharConta();
        uc = getUsuarioConta(conta.getCId());
    }

    public void setUpRoteiro(int id_conta) {
        UsuarioContaJpaController ucjc = UsuarioContaJpaController.getInstance();
        cjc = ContaJpaController.getInstance();
        Conta conta = cjc.findConta(id_conta);
        conta.setCFechada(false);
        try {
            cjc.edit(conta);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CalculoConsumoPorPessoaTeste.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CalculoConsumoPorPessoaTeste.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<UsuarioConta> ucs = ucjc.getUsuarioContaPorId(conta.getCId());
        for (UsuarioConta csa : ucs) {
            try {
                csa.setUCValor(0.0);
                ucjc.edit(csa);
            } catch (Exception ex) {
                Logger.getLogger(CalculoConsumoPorPessoaTeste.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
