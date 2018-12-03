package com.br.puc.bartechmobile.service;

import com.br.puc.bartechmobile.util.rest.HttpRequest;
import com.br.puc.bartechmobile.util.rest.OnTaskFinished;
import com.br.puc.bartechmobile.util.rest.RequestEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScanService {

    private final OnTaskFinished target;

    public ScanService(OnTaskFinished target) {
        this.target = target;
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    public void findProdutoIdByCodigoDeBarra(String codigoDeBarras) throws IOException {
        new HttpRequest(getJsonfindProdutoIdByCodigoDeBarra(codigoDeBarras), this.target, RequestEnum.ALL_PRODUTOS).execute();
    }

    private String getJsonfindProdutoIdByCodigoDeBarra(String codigoDeBarra) throws JsonProcessingException {
        Map<String, String> keys = new HashMap<>();
        keys.put("query", getQueryfindProdutoIdByCodigoDeBarra(codigoDeBarra));
        return mapper.writeValueAsString(keys);
    }

    private String getQueryfindProdutoIdByCodigoDeBarra(String codigoDeBarra) {
        StringBuilder sb = new StringBuilder();
        sb.append("query { ");
        sb.append("    allProdutoes( ");
        sb.append("         last: 1");
        sb.append("         filter: { ");
        sb.append("             codigoDeBarras: \"" + codigoDeBarra + "\"");
        sb.append("         } ");
        sb.append("       ) { ");
        sb.append("          id");
        sb.append("          nome");
        sb.append("          preco");
        sb.append("          quantidadeEstoque");
        sb.append("       }  ");
        sb.append("}  ");

        return sb.toString();
    }

}
