package com.br.puc.bartechmobile.util.rest;

public enum RequestEnum {

    ALL_PRODUTOS("$.*.allProdutoes[0]", String.class),
    ADD_ITEM_VENDA("$.*.createItemVenda.produto.nome", String.class);

    String identifier;
    Class<?> clazz;

    RequestEnum(String identifier, Class<?> clazz) {
        this.identifier = identifier;
        this.clazz = clazz;
    }
}
