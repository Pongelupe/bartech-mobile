package com.br.puc.bartechmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.br.puc.bartechmobile.service.ScanService;
import com.br.puc.bartechmobile.util.JsonPathReader;
import com.br.puc.bartechmobile.util.rest.OnTaskFinished;
import com.br.puc.bartechmobile.util.rest.RequestEnum;

import org.json.JSONArray;
import org.json.JSONException;


public class MainActivity extends AppCompatActivity implements OnTaskFinished {

    private Button btScanner;
    private ScanService scanService;
    private static final String SEPARATOR = "#";
    private String idVenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.scanService = new ScanService(this);
        btScanner = findViewById(R.id.button);
        btScanner.setOnClickListener(v -> {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SAVE_HISTORY", false);
            startActivityForResult(intent, 0);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 0) {
                if (resultCode == RESULT_OK) {
                    evaluateScan(data.getStringExtra("SCAN_RESULT"));
                } else if (resultCode == RESULT_CANCELED) {
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
        String[] splitted = content.split(SEPARATOR);
        this.idVenda = splitted[1];
        this.scanService.findProdutoIdByCodigoDeBarra(splitted[0]);
    }

    @Override
    public <T> void taskfinished(T response, RequestEnum request) {
        try {
            switch (request) {
                case ALL_PRODUTOS:
                    System.out.println(((JSONArray) response).get(0));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
