/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.web.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author hideki
 */
@ManagedBean
@RequestScoped
public class NavigationBean {

    /**
     * Creates a new instance of NavigationBean
     */
    public NavigationBean() {
    }
    
    public String redirectCadastroUsuario() {
        return "cadastro_usuario?faces-redirect=true";
    }
    
    public String redirectAtualizacaoUsuario() {
        return "atualizacao_usuario?faces-redirect=true";
    }
    
    public String redirectLogin() {
        return "login?faces-redirect=true";
    }
    
    public String redirectSistema() {
        return "sistema?faces-redirect=true";
    }
    
}
