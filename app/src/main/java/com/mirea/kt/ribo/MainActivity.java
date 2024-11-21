package com.mirea.kt.ribo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText hint_1;
    private EditText hint_2;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String server = "https://android-for-students.ru";
        String serverPath = "/coursework/login.php";
        setContentView(R.layout.activity_main);
        hint_1 = findViewById(R.id.login);
        hint_2 = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("savings_calc", "Click btn_login");
                HashMap<String,String> map = new HashMap<>();
                map.put("lgn",hint_1.getText().toString());
                map.put("pwd",hint_2.getText().toString());
                map.put("g","RIBO-02-22");
                HTTPRunnable httpRunnable_1 = new HTTPRunnable(server + serverPath,map);
                Thread th = new Thread(httpRunnable_1);
                th.start();
                try{
                    th.join();
                } catch (InterruptedException ex){
                    ex.getMessage();
                } finally {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(httpRunnable_1.getResponseBody());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    int result = 0;
                    try {
                        result = jsonObject.getInt("result_code");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    switch (result){
                        case 1:
                            Intent intent = new Intent(MainActivity.this,Activity2.class);
                            MainActivity.this.startActivity(intent);
                            showToast("Correct");
                            break;
                        case 0:
                            showToast("Incorrect");
                            break;
                        default:
                            showToast("Situation becomes worse");
                            break;
                    }
                }
            }
        });
    }
    public class HTTPRunnable implements Runnable {
        private String address;
        private HashMap<String,String> requestBody;
        private String responseBody;
        public HTTPRunnable(String address,HashMap<String,String> requestBody){
            this.address = address;
            this.requestBody = requestBody;
        }
        public String getResponseBody() {
            return responseBody;
        }
        @Override
        public void run(){
            if(this.address != null && !this.address.isEmpty()) {
                try {
                    URL url = new URL(this.address);
                    URLConnection connection = url.openConnection();
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    httpConnection.setRequestMethod("POST");
                    httpConnection.setDoOutput(true);
                    OutputStreamWriter osw = new OutputStreamWriter(httpConnection.getOutputStream());
                    osw.write(generateStringBody());
                    osw.flush();
                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode == 200) {
                        InputStreamReader isr = new InputStreamReader(httpConnection.getInputStream());
                        BufferedReader br = new BufferedReader(isr);
                        String currentLine;
                        StringBuilder sbResponse = new StringBuilder();
                        while ((currentLine = br.readLine()) != null) {
                            sbResponse.append(currentLine);
                        }
                        responseBody = sbResponse.toString();
                    } else {
                        showToast("Error! Bad response code!");
                    }
                } catch (IOException ex) {
                    showToast("Error! Please try again!");
                }
            }
            }
            private String generateStringBody(){
            StringBuilder sbParams = new StringBuilder();
            if(this.requestBody != null && !requestBody.isEmpty()){
                int i = 0;
                for (String key: this.requestBody.keySet()){
                    try{
                        if(i != 0){
                            sbParams.append("&");
                        }
                        sbParams.append(key).append("=")
                                .append(URLEncoder.encode(this.requestBody.get(key),"UTF-8"));
                    } catch (UnsupportedEncodingException e){
                        e.printStackTrace();
                    }
                    i++;
                }
            }
            return sbParams.toString();
        }
    }
    private void showToast(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
