/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.web.bean;

import br.jpa.calculos.CalculosFuncionais;
import br.jpa.controller.ContaJpaController;
import br.jpa.controller.ProdutoJpaController;
import br.jpa.controller.UsuarioJpaController;
import br.jpa.controller.exceptions.NonexistentEntityException;
import br.jpa.entity.Conta;
import br.jpa.entity.Produto;
import br.jpa.entity.Usuario;
import br.web.utils.SessionContext;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Matheus Mollon
 */
@ManagedBean
@RequestScoped
public class ProdutoBean {

    private int taxaServico = 0;
    private int id;
    private String produto;
    private double preco;
    private String[] selecionados;
    private List<Usuario> users_por_produto;

    public ProdutoBean() {
        this.selecionados = new String[UsuarioJpaController.getInstance().findUsuarioEntities().size()];
        this.users_por_produto = new ArrayList<>();

    }

    public int getTaxaServico() {
        return taxaServico;
    }

    public void setTaxaServico(int taxaServico) {
        this.taxaServico = taxaServico;
    }

    public String[] getSelecionados() {
        return selecionados;
    }

    public void setSelecionados(String[] selecionados) {
        this.selecionados = selecionados;
    }

    public void adicionar() {
        ProdutoJpaController pjc = ProdutoJpaController.getInstance();
        UsuarioJpaController ujc = UsuarioJpaController.getInstance();
        List<Usuario> users = new ArrayList<Usuario>();

        for (int i = 0; i < selecionados.length; i++) {
            System.out.println(ujc.findUsuario(selecionados[i]).toString());
            users.add(ujc.findUsuario(selecionados[i]));
        }

        Produto p = new Produto();
        //p.setPId(pjc.findProdutoEntities().size() + 1);
        p.setPNome(produto);
        p.setPValor(preco);
        p.setCId(recuperarConta());
        for (Usuario u : users) {
            System.out.println(u.toString());
        }
        p.setUsuarioCollection(users);
        boolean resposta = pjc.create(p);
        if (resposta) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Produto armazenado com sucesso", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Produto não armazenado", ""));
        }
        System.out.println("done");
        clean();
    }

    public void remove(Produto produto) {
        try {
            ProdutoJpaController pjc = ProdutoJpaController.getInstance();
            boolean resposta = pjc.destroy(produto.getPId());
            if (resposta) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Produto removido com sucesso", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Produto não foi removido", ""));
            }
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(ProdutoBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void clean() {

        this.id = -1;
        this.produto = "";
        this.preco = 0.0;
        this.selecionados = new String[ProdutoJpaController.getInstance().findProdutoEntities().size()];
    }

    public void loadData(Produto prod) {
        int produto_id = prod.getPId();
        SessionContext.getInstance().setSessionAttribute("produto_edit", produto_id);
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Aplicativo/faces/editar_produto.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProdutoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("loading..." + prod.toString());

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public void getUsers_por_produto(int id) {
        ProdutoJpaController pjc = ProdutoJpaController.getInstance();
        this.users_por_produto = pjc.getAllUsersFromProduct(id);

    }

    public List<Usuario> getTodosUsuariosNaConta() {
        ProdutoJpaController pjc = ProdutoJpaController.getInstance();
        return pjc.getAllUsersFromAccount(recuperarConta().getCId());
    }

    public List<Usuario> getUsers_por_produto() {
        return users_por_produto;
    }

    public void limparDetalhes() {
        users_por_produto = new ArrayList<>();
    }

    public void fecharConta() {
        try {
            CalculosFuncionais cf = new CalculosFuncionais(recuperarConta(), this.taxaServico);
            cf.FecharConta();
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Aplicativo/faces/visualizar_conta.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ProdutoBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private Conta recuperarConta() {
        Conta contaSession = ContaJpaController.getInstance().findConta((int) SessionContext.getInstance().getSessionAttribute("cId"));
        return contaSession;
    }

}
