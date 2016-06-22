/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.web.bean;

import br.jpa.controller.ContaJpaController;
import br.jpa.controller.UsuarioContaJpaController;
import br.jpa.controller.UsuarioJpaController;
import br.jpa.controller.exceptions.IllegalOrphanException;
import br.jpa.controller.exceptions.NonexistentEntityException;
import br.jpa.entity.Conta;
import br.jpa.entity.Usuario;
import br.jpa.entity.UsuarioConta;
import br.web.utils.SessionContext;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author hideki
 */
@ManagedBean
@RequestScoped
public class ContaBean {

    private Conta conta;
    private UsuarioConta usuarioConta;

    public ContaBean() {
        this.conta = new Conta();
        this.usuarioConta = new UsuarioConta();
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public UsuarioConta getUsuarioConta() {
        return usuarioConta;
    }

    public void setUsuarioConta(UsuarioConta usuarioConta) {
        this.usuarioConta = usuarioConta;
    }

    public Usuario getUsuarioSession() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AplicativoPU");
        UsuarioJpaController ujc = new UsuarioJpaController(emf);

        return ujc.findUsuario(SessionContext.getInstance().getSessionAttribute("uNome").toString());
    }

    public void criarConta() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AplicativoPU");
        ContaJpaController cjc = new ContaJpaController(emf);
        UsuarioContaJpaController ucjc = new UsuarioContaJpaController(emf);

        this.conta.setCValor(0.00);
        cjc.create(conta);

        this.usuarioConta.setUsuario(this.getUsuarioSession());
        this.usuarioConta.setConta(conta);
        this.usuarioConta.setUCGerente(this.getUsuarioSession().getUNome());
        this.usuarioConta.setUCValor(0.00);
        try {
            ucjc.create(usuarioConta);
        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha na criação de nova conta!", "Falha na criação de nova conta!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            System.out.println(ex.toString());
        }
    }

    public Conta getContaSession() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AplicativoPU");
        ContaJpaController cjc = new ContaJpaController(emf);

        return cjc.findConta((int) SessionContext.getInstance().getSessionAttribute("cId"));
    }

    public void atualizarConta() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AplicativoPU");
        ContaJpaController cjc = new ContaJpaController(emf);

        Conta contaAtualizada = this.getContaSession();
        contaAtualizada.setCNome(this.conta.getCNome());

        try {
            cjc.edit(contaAtualizada);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Aplicativo/faces/gerenciar_conta.xhtml");
        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha na atualização da conta!", "Falha na atualização da conta!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            System.out.println(ex.toString());
        }
    }

    public void excluirConta() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AplicativoPU");
        ContaJpaController cjc = new ContaJpaController(emf);
        
        try {
            cjc.destroy((int) SessionContext.getInstance().getSessionAttribute("cId"));
        } catch (IllegalOrphanException | NonexistentEntityException ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha na exclusão da conta!", "Falha na exclusão da conta!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

}
