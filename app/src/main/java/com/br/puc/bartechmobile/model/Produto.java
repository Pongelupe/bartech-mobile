package com.br.puc.bartechmobile.model;

public class Produto {

    private String id;
    private String nome;
    private double preco;
    private int quantidadeEstoque;
    private String codigoDeBarras;
    private int codigo;
    private boolean temControleEstoque;


    public Produto(String id, String nome, double preco, int quantidadeEstoque, String codigoDeBarras, int codigo, boolean temControleEstoque) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.codigoDeBarras = codigoDeBarras;
        this.codigo = codigo;
        this.temControleEstoque = temControleEstoque;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(int quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public String getCodigoDeBarras() {
        return codigoDeBarras;
    }

    public void setCodigoDeBarras(String codigoDeBarras) {
        this.codigoDeBarras = codigoDeBarras;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public boolean isTemControleEstoque() {
        return temControleEstoque;
    }

    public void setTemControleEstoque(boolean temControleEstoque) {
        this.temControleEstoque = temControleEstoque;
    }
}
