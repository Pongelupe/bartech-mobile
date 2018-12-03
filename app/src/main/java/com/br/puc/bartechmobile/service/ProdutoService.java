package com.br.puc.bartechmobile.service;

import com.br.puc.bartechmobile.util.rest.HttpRequest;
import com.br.puc.bartechmobile.util.rest.OnTaskFinished;
import com.br.puc.bartechmobile.util.rest.RequestEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class ProdutoService {

    private final OnTaskFinished target;
    private static final ObjectMapper mapper = new ObjectMapper();

    public ProdutoService(OnTaskFinished target) {
        this.target = target;
    }

    public void addItemVendaOnVenda(String idProduto, String idVenda, int quantidadeEstoque, int quantidade) throws JsonProcessingException {
        new HttpRequest(getJsonAddItemVendaOnVenda(idProduto, idVenda, quantidadeEstoque, quantidade), this.target, RequestEnum.ADD_ITEM_VENDA).execute();
    }


    private String getJsonAddItemVendaOnVenda(String idProduto, String idVenda, int quantidadeEstoque, int quantidade) throws JsonProcessingException {
        Map<String, String> keys = new HashMap<>();
        keys.put("query", getQueryAddItemVendaOnVenda(idProduto, idVenda, quantidadeEstoque, quantidade));
        return mapper.writeValueAsString(keys);
    }

    private String getQueryAddItemVendaOnVenda(String idProduto, String idVenda, int quantidadeEstoque, int quantidade) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String data = df.format(new Date());

        StringBuilder sb = new StringBuilder();
        sb.append("mutation { ");
        sb.append("    createItemVenda( ");
        sb.append("         vendaId: \"").append(idVenda).append("\"");
        sb.append("         quantidade: ").append(quantidade);
        sb.append("         produtoId: \"").append(idProduto).append("\"");
        sb.append("         data: \"").append(data).append("\"");
        sb.append("       ) { ");
        sb.append("          id");
        sb.append("          produto { ");
        sb.append("             nome ");
        sb.append("          } ");
        sb.append("       }  ");
        sb.append("       updateProduto(  ");
        sb.append("       quantidadeEstoque: ").append(quantidadeEstoque - quantidade);
        sb.append("       id: \"").append(idProduto).append("\"");
        sb.append("       ) {  ");
        sb.append("          id  ");
        sb.append("       }  ");
        sb.append("}  ");

        return sb.toString();
    }
}

