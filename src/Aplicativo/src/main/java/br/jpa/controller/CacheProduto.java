/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpa.controller;


import br.jpa.entity.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Matheus Mollon
 */
public class CacheProduto {

    private static CacheProduto cp;
    private List<Usuario> users;

    private CacheProduto() {
        users = new ArrayList<>();
    }

    public static CacheProduto getInstance() {
        if (cp == null) {
            cp = new CacheProduto();
        }
        return cp;
    }

    public List<Usuario> getUsers() {
        return users;
    }

    public void setUsers(int id) {
        if (users.isEmpty()) {
            this.users = ProdutoJpaController.getInstance().getAllUsersFromProduct(id);
        }
    }

    public void removeItem(Usuario u) {
        if (users.size() <= 1) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Um produto deve estar atrelado a pelo menos uma pessoa", "Erro");
            FacesContext.getCurrentInstance().addMessage("msgs", msg);
        } else {
            this.users.remove(u);

        }
    }

    public void addItem(Usuario u) {
        users.add(u);
    }

    public void liberar() {
        users = new ArrayList<>();
    }

}
