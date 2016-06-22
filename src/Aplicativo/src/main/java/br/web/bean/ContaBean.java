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
import br.jpa.entity.UsuarioContaPK;
import br.web.utils.SessionContext;
import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

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
        return UsuarioJpaController.getInstance().findUsuario(SessionContext.getInstance().getSessionAttribute("uNome").toString());
    }

    public void criarConta() {
        this.conta.setCValor(0.00);
        this.conta.setCGerente(SessionContext.getInstance().getSessionAttribute("uNome").toString());
        ContaJpaController.getInstance().create(conta);

        this.usuarioConta.setUsuario(this.getUsuarioSession());
        this.usuarioConta.setConta(conta);
        this.usuarioConta.setUCValor(0.00);
        try {
            UsuarioContaJpaController.getInstance().create(usuarioConta);
        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha na criação de nova conta!", "Falha na criação de nova conta!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            System.out.println(ex.toString());
        }
    }

    public Conta getContaSessionMenosGerente() {
        Conta contaSession = ContaJpaController.getInstance().findConta((int) SessionContext.getInstance().getSessionAttribute("cId"));
        UsuarioContaPK usuarioContaPK = new UsuarioContaPK(contaSession.getCGerente(), contaSession.getCId());
        contaSession.getUsuarioContaCollection().remove(UsuarioContaJpaController.getInstance().findUsuarioConta(usuarioContaPK));
        return contaSession;
    }

    public void atualizarConta() {
        Conta contaAtualizada = this.getContaSessionMenosGerente();
        contaAtualizada.setCNome(this.conta.getCNome());

        try {
            ContaJpaController.getInstance().edit(contaAtualizada);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Aplicativo/faces/gerenciar_conta.xhtml");
        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha na atualização da conta!", "Falha na atualização da conta!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            System.out.println(ex.toString());
        }
    }

    public void excluirConta() {
        Conta contaExcluida = ContaJpaController.getInstance().findConta((int) SessionContext.getInstance().getSessionAttribute("cId"));
        if (contaExcluida.getUsuarioContaCollection().size() == 1) {
            try {
                UsuarioContaPK usuarioContaPK = new UsuarioContaPK(contaExcluida.getCGerente(), contaExcluida.getCId());
                UsuarioContaJpaController.getInstance().destroy(usuarioContaPK);
                ContaJpaController.getInstance().destroy((int) SessionContext.getInstance().getSessionAttribute("cId"));
                FacesContext.getCurrentInstance().getExternalContext().redirect("/Aplicativo/faces/sistema.xhtml");
            } catch (IllegalOrphanException | NonexistentEntityException | IOException ex) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha na exclusão da conta!", "Falha na exclusão da conta!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } else {            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contas com outros usuários além do gerente não podem ser excluídas!", "Contas com outros usuários além do gerente não podem ser excluídas!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    public void tornarGerente(UsuarioConta usuarioConta) {
        Conta contaNovoGerente = ContaJpaController.getInstance().findConta((int) SessionContext.getInstance().getSessionAttribute("cId"));
        contaNovoGerente.setCGerente(usuarioConta.getUsuarioContaPK().getUNome());
        System.out.println(contaNovoGerente.getCGerente());
        try {
            ContaJpaController.getInstance().edit(contaNovoGerente);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Aplicativo/faces/visualizar_conta.xhtml");
        } catch (Exception ex) {            
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao substituir gerente!", "Falha ao substituir gerente!");
            FacesContext.getCurrentInstance().addMessage(null, msg);            
            System.out.println(ex.toString());
        }
    }

}
