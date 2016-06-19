/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.web.bean;

import br.jpa.controller.CrudProduto;
import br.jpa.entity.Produto;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Matheus Mollon
 */
@ManagedBean
@RequestScoped
public class ProdutoBean {

    private int id;
    private String produto;
    private double preco;

    public ProdutoBean() {
    }

    public void adicionar() {
        Produto p = new Produto();
        CrudProduto cp = new CrudProduto();
        p.setIdproduto(cp.getAllProdutos().size() + 1);
        p.setNomeproduto(produto);
        p.setPrecoproduto(preco);
        cp.persist(p);
        System.out.println("done");

    }

    public void remove(Produto produto) {
        new CrudProduto().remove(produto);

    }

    public void clean() {
        this.setId(-1);
        this.produto = "";
        this.preco = 0.0;
    }

    public void edit() {
        System.out.println("EDIT");
        Produto p = new Produto();
        p.setIdproduto(this.id);
        p.setNomeproduto(this.produto);
        p.setPrecoproduto(this.preco);
        System.out.println(p.toString());
        new CrudProduto().edit(p);

    }

    public void loadData(Produto prod) {
        this.id = prod.getIdproduto();
        this.produto = prod.getNomeproduto();
        this.preco = prod.getPrecoproduto();
        System.out.println("loading..." + prod.toString());

    }

    public List<Produto> getAllProdutos() {
        return new CrudProduto().getAllProdutos();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

}
