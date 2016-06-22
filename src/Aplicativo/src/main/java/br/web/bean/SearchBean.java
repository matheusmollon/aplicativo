/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.web.bean;

import br.jpa.controller.UsuarioJpaController;
import br.jpa.entity.Usuario;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author hideki
 */
@ManagedBean
@ViewScoped
public class SearchBean {

    private String uNome;
    /**
     * Creates a new instance of SearchBean
     */
    public SearchBean() {
    }   

    public String getUNome() {
        return uNome;
    }

    public void setUNome(String uNome) {
        this.uNome = uNome;
    }

    public List<Usuario> searchResults() {
        return UsuarioJpaController.getInstance().findUsuarioLike(this.uNome);
    }

    public String searchNumbers() {
        return "Total de resultados: " + UsuarioJpaController.getInstance().findUsuarioLikeCount(this.uNome);
    }
    
}
