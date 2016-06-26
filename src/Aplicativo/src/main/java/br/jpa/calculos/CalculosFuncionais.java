/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpa.calculos;

import br.jpa.controller.ContaJpaController;
import br.jpa.controller.UsuarioContaJpaController;
import br.jpa.controller.exceptions.NonexistentEntityException;
import br.jpa.entity.Conta;
import br.jpa.entity.Produto;
import br.jpa.entity.Usuario;
import br.jpa.entity.UsuarioConta;
import br.web.bean.ProdutoBean;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Matheus Mollon
 */
public class CalculosFuncionais {

    private Conta c;
    private double porcentagem = 0.0;

    public CalculosFuncionais(Conta c, int porcentagem) {
        this.c = c;
        this.porcentagem = Double.valueOf(porcentagem);
        System.out.println("PORCENTAGEM: " + porcentagem);
    }

    public double calcularPorcentagem(double somaConta) {

        return (somaConta * (porcentagem / 100));

    }

    public double calcularSomaTotalSemPorcetagem() {
        List<Produto> produtos = (List<Produto>) c.getProdutoCollection();
        double soma_aux = 0.0;
        for (Produto p : produtos) {
            soma_aux += p.getPValor();
        }
        System.out.println("Soma_aux: " + soma_aux);
        return soma_aux;
    }

    public double calcularValorTotalConta() {
        return formatar(calcularSomaTotalSemPorcetagem() + calcularPorcentagem(calcularSomaTotalSemPorcetagem()));

    }

    public void peristirValorTotalConta() {
        try {
            this.c.setCValor(calcularValorTotalConta());
            ContaJpaController.getInstance().edit(c);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CalculosFuncionais.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CalculosFuncionais.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void calcularQuantiaPorPessoa() {
        UsuarioContaJpaController ucjc = UsuarioContaJpaController.getInstance();
        List<UsuarioConta> ucs = ucjc.getUsuarioContaPorId(c.getCId());
        List<Produto> produtos = (List<Produto>) c.getProdutoCollection();
        double valorTaxaServico = (calcularPorcentagem(calcularSomaTotalSemPorcetagem()) / c.getUsuarioContaCollection().size());
        System.out.println("Taxa: " + valorTaxaServico);
        for (UsuarioConta csa : ucs) {
            
            csa.setUCValor(formatar(valorTaxaServico));
        }

        for (Produto p : produtos) {
            List<Usuario> usuarios = (List<Usuario>) p.getUsuarioCollection();
            double valor = (p.getPValor() / usuarios.size());

            for (UsuarioConta uc : ucs) {
                if (usuarios.contains(uc.getUsuario())) {
                    uc.setUCValor(formatar(uc.getUCValor() + valor));
                }
            }

        }
        for (UsuarioConta csa : ucs) {
            try {
                ucjc.edit(csa);
            } catch (Exception ex) {
                Logger.getLogger(ProdutoBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void EncerrarConta() {
        c.setCFechada(true);

        try {
            ContaJpaController.getInstance().edit(c);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CalculosFuncionais.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CalculosFuncionais.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public final void FecharConta() {
        this.peristirValorTotalConta();
        this.calcularQuantiaPorPessoa();
        this.EncerrarConta();

    }

    public double formatar(double valor) {
        DecimalFormat fmt = new DecimalFormat("0.00");
        String stringFormatada = fmt.format(valor);
        System.out.println(stringFormatada);
        String[] partes = stringFormatada.split(",");
        String stringFormatacao = partes[0] + "." + partes[1];
        return Double.parseDouble(stringFormatacao);

    }

}
