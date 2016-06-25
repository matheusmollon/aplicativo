/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.jpa.entity;

import br.jpa.controller.ProdutoJpaController;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Matheus Mollon
 */
@Entity
@Table(name = "produto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Produto.findAll", query = "SELECT p FROM Produto p"),
    @NamedQuery(name = "Produto.findByPId", query = "SELECT p FROM Produto p WHERE p.pId = :pId"),
    @NamedQuery(name = "Produto.findByPNome", query = "SELECT p FROM Produto p WHERE p.pNome = :pNome"),
    @NamedQuery(name = "Produto.findByPValor", query = "SELECT p FROM Produto p WHERE p.pValor = :pValor")})
@NamedNativeQueries({
    @NamedNativeQuery(
            name = "getAllUsersFromProduct",
            query = "select * from usuario u, produto_usuario pu where (u.u_nome = pu.u_nome) and pu.p_id=?;",
            resultClass = Usuario.class
    ),
    @NamedNativeQuery(
            name = "getAllUsersFromAccount",
            query = "select * from usuario u, usuario_conta uc where (u.u_nome = uc.u_nome) and uc.c_id=?;",
            resultClass = Usuario.class
    )

})
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "p_id")
    private Integer pId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "p_nome")
    private String pNome;
    @Basic(optional = false)
    @NotNull
    @Column(name = "p_valor")
    private double pValor;
    @JoinTable(name = "produto_usuario", joinColumns = {
        @JoinColumn(name = "p_id", referencedColumnName = "p_id")}, inverseJoinColumns = {
        @JoinColumn(name = "u_nome", referencedColumnName = "u_nome")})
    @ManyToMany
    private Collection<Usuario> usuarioCollection;
    @JoinColumn(name = "c_id", referencedColumnName = "c_id")
    @ManyToOne(optional = false)
    private Conta cId;

    public Produto() {
    }

    public Produto(Integer pId) {
        this.pId = pId;
    }

    public Produto(Integer pId, String pNome, double pValor) {
        this.pId = pId;
        this.pNome = pNome;
        this.pValor = pValor;
    }

    public Integer getPId() {
        return pId;
    }

    public void setPId(Integer pId) {
        this.pId = pId;
    }

    public String getPNome() {
        return pNome;
    }

    public void setPNome(String pNome) {
        this.pNome = pNome;
    }

    public double getPValor() {
        return pValor;
    }

    public void setPValor(double pValor) {
        this.pValor = pValor;
    }

    @XmlTransient
    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    public Conta getCId() {
        return cId;
    }

    public int getQtdUsersProduct(int id) {
        return ProdutoJpaController.getInstance().getAllUsersFromProduct(id).size();
    }

    public void setCId(Conta cId) {
        this.cId = cId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pId != null ? pId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Produto)) {
            return false;
        }
        Produto other = (Produto) object;
        if ((this.pId == null && other.pId != null) || (this.pId != null && !this.pId.equals(other.pId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.jpa.entity.Produto[ pId=" + pId + " ]";
    }

}
