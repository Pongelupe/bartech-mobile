package com.br.puc.bartechmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.br.puc.bartechmobile.service.ProdutoService;
import com.br.puc.bartechmobile.service.ScanService;
import com.br.puc.bartechmobile.util.rest.OnTaskFinished;
import com.br.puc.bartechmobile.util.rest.RequestEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.zxing.integration.android.IntentIntegrator;

import net.minidev.json.JSONArray;

import java.util.LinkedHashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements OnTaskFinished {

    private ScanService scanService;
    private ProdutoService produtoService;
    private static final String SEPARATOR = "#";
    private String idVenda;
    private String idProduto;
    private int quantideEstoque;
    private int quantidade;

    // UI Elements
    private Button btScanner;
    private Button btCancelar;
    private EditText etNomeProduto;
    private EditText etPreco;
    private EditText etQuantidadeEstoque;
    private EditText etQuantidade;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.scanService = new ScanService(this);
        this.produtoService = new ProdutoService(this);
        btScanner = findViewById(R.id.button);
        btCancelar = findViewById(R.id.btCancelar);
        etPreco = findViewById(R.id.etPreco);
        etNomeProduto = findViewById(R.id.etNome);
        etQuantidadeEstoque = findViewById(R.id.etQuantidadeEstoque);
        etQuantidade = findViewById(R.id.etQuantidade);
        btCancelar.setOnClickListener(v -> {
            v.setVisibility(View.INVISIBLE);
            btScanner.setText("Scanner");
            btScanner.setOnClickListener(this.listenerBtScanner());
        });
        btScanner.setOnClickListener(this.listenerBtScanner());
    }

    private View.OnClickListener listenerBtScanner() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Leia o código de barras de seu produto");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.setRequestCode(1);
                integrator.initiateScan();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 1) {
                if (resultCode == -1) {
                    evaluateScan(data.getStringExtra("SCAN_RESULT"));
                } else if (resultCode == 0) {
                    Toast.makeText(getApplicationContext(), "Operação cancelada", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void evaluateScan(String content) throws Exception {
        try {
            String[] splitted = content.split(SEPARATOR);
            this.idVenda = splitted[1];
            this.scanService.findProdutoIdByCodigoDeBarra(splitted[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new Exception("Erro ao ler código de barras");
        }
    }

    private void onProdutoScaned(Map<String, ?> produto) {
        idProduto = (String) produto.get("id");
        this.etQuantidade.getText();
        this.etNomeProduto.setText((String) produto.get("nome"));
        this.etPreco.setText(("R$ " + produto.get("preco")).replace('.', ','));
        this.quantideEstoque = (Integer) produto.get("quantidadeEstoque");
        this.etQuantidadeEstoque.setText("" + this.quantideEstoque);
        this.btCancelar.setVisibility(View.VISIBLE);
        this.btScanner.setText("Confirmar");
        btScanner.setOnClickListener(v -> {
            try {
                this.quantidade = Integer.parseInt(this.etQuantidade.getText().toString());
                if (quantidade <= this.quantideEstoque && quantidade > 0) {
                    this.produtoService.addItemVendaOnVenda(idProduto, this.idVenda, this.quantideEstoque, quantidade);
                } else {
                    String text = quantidade > 0 ? "A quantidade nao pode ser maior que " + this.quantideEstoque :
                            "A quantidade deve ser maior que 0";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT)
                            .show();
                }

            } catch (JsonProcessingException e) {
            }
        });
    }

    @Override
    public <T> void taskfinished(T response, RequestEnum request) {
        try {
            switch (request) {
                case ALL_PRODUTOS:
                    Map<String, ?> obj = (LinkedHashMap) ((JSONArray) response).get(0);
                    onProdutoScaned(obj);
                    break;
                case ADD_ITEM_VENDA:
                    String quantidade = this.quantidade <= 1 ? "" : this.quantidade + " ";
                    Toast.makeText(getApplicationContext(), quantidade + ((JSONArray) response).get(0) + " foi adicionado com sucesso!", Toast.LENGTH_LONG)
                            .show();
                    btCancelar.callOnClick();
                    break;
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Erro ao requisitar o servidor\nTente mais tarde", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
