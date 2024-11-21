package com.mirea.kt.ribo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class Activity2 extends AppCompatActivity {
    private Button btn_result;
    private EditText num_of_meters, price_of_one_meter,price_change_rate,available_price;
    private EditText interest_rate, replenishment;
    private final Handler handler = new Handler(Looper.getMainLooper());
    String item;
    TextView meters,article_price_of_one_meter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        num_of_meters = findViewById(R.id.number_of_meters);
        meters = findViewById(R.id.meters);
        article_price_of_one_meter = findViewById(R.id.article_price_of_one_meter);
        price_of_one_meter = findViewById(R.id.price_of_one_meter);
        price_change_rate = findViewById(R.id.price_change_rate);
        available_price = findViewById(R.id.available_price);
        interest_rate = findViewById(R.id.interest_rate);
        replenishment = findViewById(R.id.replenishment);
        btn_result = findViewById(R.id.button_result);
        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("savings calc", "Click btn_result");
                if (!num_of_meters.getText().toString().isEmpty()
                        &&!price_of_one_meter.getText().toString().isEmpty()&&!price_change_rate.getText().toString().isEmpty()
                        &&!available_price.getText().toString().isEmpty()&&!interest_rate.getText().toString().isEmpty()
                        &&!replenishment.getText().toString().isEmpty()&&Float.parseFloat(num_of_meters.getText().toString())>0
                        &&Float.parseFloat(price_of_one_meter.getText().toString())>0&&Float.parseFloat(price_change_rate.getText().toString())>0
                        &&Float.parseFloat(available_price.getText().toString())>0&&Float.parseFloat(interest_rate.getText().toString())>0
                        &&Float.parseFloat(replenishment.getText().toString())>0){
                    Thread thread = new Thread(()->{
                        float start_price = Float.parseFloat(num_of_meters.getText().toString())*Float.parseFloat(price_of_one_meter.getText().toString());
                        float start_price_f = Float.parseFloat(num_of_meters.getText().toString())*Float.parseFloat(price_of_one_meter.getText().toString());
                        float available_price_1 = Float.parseFloat(available_price.getText().toString());
                        float available_price_f = Float.parseFloat(available_price.getText().toString());
                        float interest_rate_r = Float.parseFloat(interest_rate.getText().toString());
                        float replenishment_r = Float.parseFloat(replenishment.getText().toString());
                        float price_change_rate_r = Float.parseFloat(price_change_rate.getText().toString());
                        float available_price_final = 1;
                        float start_price_final = 1;
                        float n = 0;
                        float sum_of_replenishments = 0;
                        float sum_of_percents = 0;
                        while (available_price_f < start_price_f){
                            available_price_final = available_price_f + (available_price_f*(interest_rate_r/100)/12);
                            sum_of_percents += available_price_f*(interest_rate_r/100)/12;
                            available_price_f = available_price_final + replenishment_r;
                            sum_of_replenishments += replenishment_r;
                            n+=1;
                            if (n%12==0){
                                start_price_final = start_price_f + (start_price_f*(price_change_rate_r/100));
                                start_price_f = start_price_final;
                            }
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        float finalStart_price_f = start_price_f;
                        float finalAvailable_price_f = available_price_f;
                        float finalN = n;
                        float finalSum_of_replenishments = sum_of_replenishments;
                        float finalSum_of_percents = sum_of_percents;
                        Log.d("savings_calc","Result: " + finalStart_price_f + "," + finalAvailable_price_f + "," +
                                finalN + "," + finalSum_of_replenishments + "," + finalSum_of_percents);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(Activity2.this,Activity3.class);
                                intent.putExtra("SPINNER",item);
                                intent.putExtra("START PRICE",start_price);
                                intent.putExtra("FINAL START PRICE",finalStart_price_f);
                                intent.putExtra("FINAL AVAILABLE PRICE",finalAvailable_price_f);
                                intent.putExtra("AVAILABLE PRICE",available_price_1);
                                intent.putExtra("SUM OF REPLENISHMENTS",finalSum_of_replenishments);
                                intent.putExtra("NUMBER OF REPLENISHMENTS",finalN);
                                intent.putExtra("SUM OF PERCENTS",finalSum_of_percents);
                                Activity2.this.startActivity(intent);
                            }
                        });
                    });
                    thread.start();
                } else {
                    Toast.makeText(Activity2.this,"Fill in all required fields correctly",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Spinner spinner = findViewById(R.id.spinner_currency);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("savings_calc","Spinner item selected");
                item = parent.getItemAtPosition(position).toString();
                Toast.makeText(Activity2.this,"Selected currency: " + item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("рубль/ruble");
        arrayList.add("доллар/dollar");
        arrayList.add("евро/euro");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);
        }
    }
