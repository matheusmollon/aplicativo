/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpa.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author hideki
 */
@Entity
@Table(name = "conta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Conta.findAll", query = "SELECT c FROM Conta c"),
    @NamedQuery(name = "Conta.findByCId", query = "SELECT c FROM Conta c WHERE c.cId = :cId"),
    @NamedQuery(name = "Conta.findByCNome", query = "SELECT c FROM Conta c WHERE c.cNome = :cNome"),
    @NamedQuery(name = "Conta.findByCValor", query = "SELECT c FROM Conta c WHERE c.cValor = :cValor"),
    @NamedQuery(name = "Conta.findByCGerente", query = "SELECT c FROM Conta c WHERE c.cGerente = :cGerente")})
public class Conta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "c_id")
    private Integer cId;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "c_nome")
    private String cNome;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "c_valor")
    private double cValor;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "c_gerente")
    private String cGerente;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "conta")
    private Collection<UsuarioConta> usuarioContaCollection;

    public Conta() {
    }

    public Conta(Integer cId) {
        this.cId = cId;
    }

    public Conta(Integer cId, String cNome, double cValor, String cGerente) {
        this.cId = cId;
        this.cNome = cNome;
        this.cValor = cValor;
        this.cGerente = cGerente;
    }

    public Integer getCId() {
        return cId;
    }

    public void setCId(Integer cId) {
        this.cId = cId;
    }

    public String getCNome() {
        return cNome;
    }

    public void setCNome(String cNome) {
        this.cNome = cNome;
    }

    public double getCValor() {
        return cValor;
    }

    public void setCValor(double cValor) {
        this.cValor = cValor;
    }

    public String getCGerente() {
        return cGerente;
    }

    public void setCGerente(String cGerente) {
        this.cGerente = cGerente;
    }

    @XmlTransient
    public Collection<UsuarioConta> getUsuarioContaCollection() {
        return usuarioContaCollection;
    }

    public void setUsuarioContaCollection(Collection<UsuarioConta> usuarioContaCollection) {
        this.usuarioContaCollection = usuarioContaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cId != null ? cId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Conta)) {
            return false;
        }
        Conta other = (Conta) object;
        if ((this.cId == null && other.cId != null) || (this.cId != null && !this.cId.equals(other.cId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.jpa.entity.Conta[ cId=" + cId + " ]";
    }
    
}
