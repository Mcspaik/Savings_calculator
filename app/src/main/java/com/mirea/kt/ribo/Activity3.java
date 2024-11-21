package com.mirea.kt.ribo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity3 extends AppCompatActivity {
    TextView start_price,article_start_price,spinner_result,article_f_price,finish_price;
    TextView article_t_amount,total_amount,article_f_payment,first_payment,article_sum_percents;
    TextView sum_of_percents,article_replenishment_1,article_replenishment_2,article_replenishment_3;
    TextView replenishment_1,replenishment_2,replenishment_3;
    Button share_button;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        article_start_price = findViewById(R.id.article_start_price);
        start_price = findViewById(R.id.start_price);
        article_f_price = findViewById(R.id.article_f_price);
        finish_price = findViewById(R.id.finish_price);
        article_t_amount = findViewById(R.id.article_t_amount);
        total_amount = findViewById(R.id.total_amount);
        spinner_result = findViewById(R.id.spinner_result);
        article_f_payment = findViewById(R.id.article_f_payment);
        first_payment = findViewById(R.id.first_payment);
        article_sum_percents = findViewById(R.id.article_sum_percents);
        sum_of_percents = findViewById(R.id.sum_of_percents);
        article_replenishment_1 = findViewById(R.id.article_replenishment_1);
        article_replenishment_2 = findViewById(R.id.article_replenishment_2);
        article_replenishment_3 = findViewById(R.id.article_replenishment_3);
        replenishment_1 = findViewById(R.id.replenishment_1);
        replenishment_2 = findViewById(R.id.replenishment_2);
        replenishment_3 = findViewById(R.id.replenishment_3);
        share_button = findViewById(R.id.share_button);
        Intent intent = getIntent();
        float result_0 = intent.getFloatExtra("START PRICE",0);
        float result_1 = intent.getFloatExtra("FINAL START PRICE",0);
        float result_2 = intent.getFloatExtra("FINAL AVAILABLE PRICE",0);
        float result_3 = intent.getFloatExtra("AVAILABLE PRICE",0);
        float result_4 = intent.getFloatExtra("SUM OF PERCENTS",0);
        float result_5 = intent.getFloatExtra("SUM OF REPLENISHMENTS",0);
        float result_6 = intent.getFloatExtra("NUMBER OF REPLENISHMENTS",0);
        int i = (int)(result_6/12);
        int x = (int) result_6;
        start_price.setText(String.format("%.2f",result_0).toString());
        finish_price.setText(String.format("%.2f",result_1).toString());
        total_amount.setText(String.format("%.2f",result_2).toString());
        first_payment.setText(String.format("%.2f",result_3).toString());
        sum_of_percents.setText(String.format("%.2f",result_4).toString());
        replenishment_1.setText(String.valueOf(i));
        replenishment_2.setText(String.valueOf(x));
        replenishment_3.setText(String.format("%.2f",result_5).toString());
        String spinner = intent.getStringExtra("SPINNER");
        if (spinner.equals("рубль/ruble")){
            spinner_result.setText("₽");
        }
        if (spinner.equals("доллар/dollar")){
            spinner_result.setText("$");
        }
        if (spinner.equals("евро/euro")){
            spinner_result.setText("€");
        }
        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("savings_calc","Click share_button");
                shareResult(article_start_price.getText().toString() + String.format("%.2f",result_0) + " " +
                        article_f_price.getText().toString() + String.format("%.2f",result_1) + " " + article_t_amount.getText().toString()+
                        String.format("%.2f",result_2) + " " + article_f_payment.getText().toString() + String.format("%.2f",result_3) +
                        " " + article_sum_percents.getText().toString() + String.format("%.2f",result_4) + " " + article_replenishment_1.getText().toString() +" "+
                        i + " " + article_replenishment_2.getText().toString() + " " + x + " " + article_replenishment_3.getText().toString() + " " + String.format("%.2f",result_5) ) ;
            }
        });
    }
    private void shareResult(String result){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT,result);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent,null));
    }
}