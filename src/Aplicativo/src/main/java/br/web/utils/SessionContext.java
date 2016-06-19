/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.web.utils;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author hideki
 * fonte: http://www.devmedia.com.br/trabalhando-com-sessao-e-filter-em-jsf/32358
 */
public class SessionContext {

    private static SessionContext instance;

    public static SessionContext getInstance() {
        if (instance == null) {
            instance = new SessionContext();
        }

        return instance;
    }

    private SessionContext() {

    }

    private ExternalContext currentExternalContext() {
        if (FacesContext.getCurrentInstance() == null) {
            throw new RuntimeException("Erro! O FacesContext deve ser chamado dentro de uma requisição HTTP.");
        } else {
            return FacesContext.getCurrentInstance().getExternalContext();
        }
    }

    public void invalidateSession() {
        currentExternalContext().invalidateSession();
    }

    public Object getSessionAttribute(String nome) {
        return currentExternalContext().getSessionMap().get(nome);
    }

    public void setSessionAttribute(String nome, Object valor) {
        currentExternalContext().getSessionMap().put(nome, valor);
    }
}
