/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpa.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author hideki
 */
@Embeddable
public class UsuarioContaPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "u_nome")
    private String uNome;
    @Basic(optional = false)
    @NotNull
    @Column(name = "c_id")
    private int cId;

    public UsuarioContaPK() {
    }

    public UsuarioContaPK(String uNome, int cId) {
        this.uNome = uNome;
        this.cId = cId;
    }

    public String getUNome() {
        return uNome;
    }

    public void setUNome(String uNome) {
        this.uNome = uNome;
    }

    public int getCId() {
        return cId;
    }

    public void setCId(int cId) {
        this.cId = cId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uNome != null ? uNome.hashCode() : 0);
        hash += (int) cId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioContaPK)) {
            return false;
        }
        UsuarioContaPK other = (UsuarioContaPK) object;
        if ((this.uNome == null && other.uNome != null) || (this.uNome != null && !this.uNome.equals(other.uNome))) {
            return false;
        }
        if (this.cId != other.cId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.web.aplicativo.UsuarioContaPK[ uNome=" + uNome + ", cId=" + cId + " ]";
    }
    
}
