/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.web.bean;

import br.jpa.controller.ContaJpaController;
import br.jpa.controller.UsuarioContaJpaController;
import br.jpa.controller.UsuarioJpaController;
import br.jpa.entity.Usuario;
import br.jpa.entity.UsuarioConta;
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
public class UsuarioContaBean {
    
    private UsuarioConta usuarioConta;
    
    /**
     * Creates a new instance of UsuarioContaBean
     */
    public UsuarioContaBean() {
        this.usuarioConta = new UsuarioConta();
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
    
    public void acessarConta(UsuarioConta usuarioConta) {
        SessionContext.getInstance().setSessionAttribute("cId", usuarioConta.getUsuarioContaPK().getCId());
        
        if(SessionContext.getInstance().getSessionAttribute("uNome").equals(usuarioConta.getConta().getCGerente())) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/Aplicativo/faces/gerenciar_conta.xhtml");
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        } else {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/Aplicativo/faces/visualizar_conta.xhtml");
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }
    }
    
    public void cadastrarUsuarioConta(String uNome) {
        this.usuarioConta.setUsuario(UsuarioJpaController.getInstance().findUsuario(uNome));
        this.usuarioConta.setConta(ContaJpaController.getInstance().findConta((int) SessionContext.getInstance().getSessionAttribute("cId")));
        this.usuarioConta.setUCValor(0.00);
        try {
            UsuarioContaJpaController.getInstance().create(usuarioConta);
        } catch (Exception ex) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao adicionar usuário na conta!", "Falha ao adicionar usuário na conta!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            System.out.println(ex.toString());
        }        
    }
    
}
