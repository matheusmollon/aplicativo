/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.web.bean;

import br.jpa.controller.UsuarioJpaController;
import br.jpa.entity.Usuario;
import br.web.utils.SessionContext;
import java.io.IOException;
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
public class LoginBean {
    
    private Usuario usuario;

    /**
     * Creates a new instance of AcessoBean
     */
    public LoginBean() {
        this.usuario = new Usuario();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public void login() {
        boolean valid = true;

        Usuario usuarioBanco = UsuarioJpaController.getInstance().findUsuario(usuario.getUNome());

        if (usuarioBanco == null || !usuarioBanco.getUSenha().equals(usuario.getUSenha())) {
            valid = false;
        }

        if (valid) {
            SessionContext.getInstance().setSessionAttribute("uNome", usuario.getUNome());
            
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/Aplicativo/faces/sistema.xhtml");
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        } else {
            this.usuario.setUNome("");
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Nome de usuário ou senha incorretos",
                            "Erro de acesso!"));
        }
    }
    
    public String logout() {  
        SessionContext.getInstance().invalidateSession();
        return "login?faces-redirect=true";
    }
    
}
