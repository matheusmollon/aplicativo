/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.web.bean;

import br.jpa.controller.UsuarioJpaController;
import br.jpa.entity.Usuario;
import br.web.utils.SessionContext;
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
public class UsuarioBean {

    private Usuario usuario;
    private String confirmaSenha;

    /**
     * Creates a new instance of UsuarioBean
     */
    public UsuarioBean() {
        this.usuario = new Usuario();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setConfirmaSenha(String confirmaSenha) {
        this.confirmaSenha = confirmaSenha;
    }

    public String getConfirmaSenha() {
        return confirmaSenha;
    }

    public void cadastrarUsuario() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AplicativoPU");
        UsuarioJpaController ujc = new UsuarioJpaController(emf);

        if (usuario.getUSenha().equals(confirmaSenha)) {
            try {
                ujc.create(usuario);
                FacesContext.getCurrentInstance().getExternalContext().redirect("/Aplicativo/faces/login.xhtml");
            } catch (Exception ex) {
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha no cadastro!", "Falha no cadastro!");
                FacesContext.getCurrentInstance().addMessage(null, msg);
                System.out.println(ex.toString());
            }
        } else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senhas não coincidem!", "Falha no cadastro!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public Usuario getUsuarioSession() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AplicativoPU");
        UsuarioJpaController ujc = new UsuarioJpaController(emf);

        return ujc.findUsuario(SessionContext.getInstance().getSessionAttribute("uNome").toString());
    }

    public void atualizarUsuario() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AplicativoPU");
        UsuarioJpaController ujc = new UsuarioJpaController(emf);

        Usuario usuarioAtualizado = ujc.findUsuario(SessionContext.getInstance().getSessionAttribute("uNome").toString());
        usuarioAtualizado.setUCelular(this.usuario.getUCelular());

        try {
            ujc.edit(usuarioAtualizado);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Aplicativo/faces/sistema.xhtml");
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }
    
}
