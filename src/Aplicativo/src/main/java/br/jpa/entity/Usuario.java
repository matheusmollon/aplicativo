/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpa.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hideki
 */
@Entity
@Table(name = "usuario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u"),
    @NamedQuery(name = "Usuario.findAllUNomeLike", query = "SELECT u FROM Usuario u WHERE u.uNome like :uNome"),
    @NamedQuery(name = "Usuario.findByUNome", query = "SELECT u FROM Usuario u WHERE u.uNome = :uNome"),
    @NamedQuery(name = "Usuario.findByUSenha", query = "SELECT u FROM Usuario u WHERE u.uSenha = :uSenha"),
    @NamedQuery(name = "Usuario.findByUCelular", query = "SELECT u FROM Usuario u WHERE u.uCelular = :uCelular")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "u_nome")
    private String uNome;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "u_senha")
    private String uSenha;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "u_celular")
    private String uCelular;

    public Usuario() {
    }

    public Usuario(String uNome) {
        this.uNome = uNome;
    }

    public Usuario(String uNome, String uSenha, String uCelular) {
        this.uNome = uNome;
        this.uSenha = uSenha;
        this.uCelular = uCelular;
    }

    public String getUNome() {
        return uNome;
    }

    public void setUNome(String uNome) {
        this.uNome = uNome;
    }

    public String getUSenha() {
        return uSenha;
    }

    public void setUSenha(String uSenha) {
        this.uSenha = uSenha;
    }

    public String getUCelular() {
        return uCelular;
    }

    public void setUCelular(String uCelular) {
        this.uCelular = uCelular;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (uNome != null ? uNome.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.uNome == null && other.uNome != null) || (this.uNome != null && !this.uNome.equals(other.uNome))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.jpa.entity.Usuario[ uNome=" + uNome + " ]";
    }
    
}
