/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpa.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "usuario_conta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuarioConta.findAll", query = "SELECT u FROM UsuarioConta u"),
    @NamedQuery(name = "UsuarioConta.findByUNome", query = "SELECT u FROM UsuarioConta u WHERE u.usuarioContaPK.uNome = :uNome"),
    @NamedQuery(name = "UsuarioConta.findByCId", query = "SELECT u FROM UsuarioConta u WHERE u.usuarioContaPK.cId = :cId"),
    @NamedQuery(name = "UsuarioConta.findByUCGerente", query = "SELECT u FROM UsuarioConta u WHERE u.uCGerente = :uCGerente"),
    @NamedQuery(name = "UsuarioConta.findByUCValorUsuario", query = "SELECT u FROM UsuarioConta u WHERE u.uCValorUsuario = :uCValorUsuario")})
public class UsuarioConta implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UsuarioContaPK usuarioContaPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "u_c_gerente")
    private String uCGerente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "u_c_valor_usuario")
    private double uCValorUsuario;
    @JoinColumn(name = "c_id", referencedColumnName = "c_id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Conta conta;
    @JoinColumn(name = "u_nome", referencedColumnName = "u_nome", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;

    public UsuarioConta() {
    }

    public UsuarioConta(UsuarioContaPK usuarioContaPK) {
        this.usuarioContaPK = usuarioContaPK;
    }

    public UsuarioConta(UsuarioContaPK usuarioContaPK, String uCGerente, double uCValorUsuario) {
        this.usuarioContaPK = usuarioContaPK;
        this.uCGerente = uCGerente;
        this.uCValorUsuario = uCValorUsuario;
    }

    public UsuarioConta(String uNome, int cId) {
        this.usuarioContaPK = new UsuarioContaPK(uNome, cId);
    }

    public UsuarioContaPK getUsuarioContaPK() {
        return usuarioContaPK;
    }

    public void setUsuarioContaPK(UsuarioContaPK usuarioContaPK) {
        this.usuarioContaPK = usuarioContaPK;
    }

    public String getUCGerente() {
        return uCGerente;
    }

    public void setUCGerente(String uCGerente) {
        this.uCGerente = uCGerente;
    }

    public double getUCValorUsuario() {
        return uCValorUsuario;
    }

    public void setUCValorUsuario(double uCValorUsuario) {
        this.uCValorUsuario = uCValorUsuario;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (usuarioContaPK != null ? usuarioContaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsuarioConta)) {
            return false;
        }
        UsuarioConta other = (UsuarioConta) object;
        if ((this.usuarioContaPK == null && other.usuarioContaPK != null) || (this.usuarioContaPK != null && !this.usuarioContaPK.equals(other.usuarioContaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.web.aplicativo.UsuarioConta[ usuarioContaPK=" + usuarioContaPK + " ]";
    }
    
}
