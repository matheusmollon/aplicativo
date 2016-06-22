/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.web.bean;

import br.jpa.controller.UsuarioJpaController;
import br.jpa.entity.Usuario;
import java.util.ArrayList;
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
    private List<Usuario> results;
    
    /**
     * Creates a new instance of SearchBean
     */
    public SearchBean() {
        this.uNome = "";
        this.results = new ArrayList<>();
    }   

    public String getUNome() {
        return uNome;
    }

    public void setUNome(String uNome) {
        this.uNome = uNome;
    }
    
    public void clearSearch() {
        this.uNome = "";
        this.results = new ArrayList<>();
    }

    public List<Usuario> searchResults() {
        if(!this.uNome.equals(""))
            this.results = UsuarioJpaController.getInstance().findUsuarioLike(this.uNome);
        else
            this.clearSearch();
        
        return results;
    }

    public String searchNumbers() {
        return "Total de resultados: " + results.size();
    }
    
}
