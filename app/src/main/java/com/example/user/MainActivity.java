package com.example.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.user.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;
    private int kg;
    private int g;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b.input.length()==0)
                    Toast.makeText(MainActivity.this, "Enter min qty", Toast.LENGTH_SHORT).show();
                else
                    setUpNumberPicker(b.input.getText().toString());
            }
        });

        b.kg.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(newVal>kg)
                    b.gram.setMinValue(0);
                else if(newVal==kg)
                    b.gram.setMinValue(g/50);
            }
        });
    }

    private void setUpNumberPicker(String input) {

        float quantity = Float.parseFloat(input) * 1000;
        kg = (int) (quantity/1000);
        g = (int) (quantity % 1000);
        b.kg.setMinValue(kg);
        b.kg.setMaxValue(kg+20);
        b.gram.setMinValue(g/50);
        b.gram.setMaxValue(19);


        b.kg.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value+"kg";
            }
        });

        b.gram.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value*50+"g";
            }
        });

        View firstItemKg = b.kg.getChildAt(0);
        if (firstItemKg != null) {
            firstItemKg.setVisibility(View.INVISIBLE);
        }
        View firstItemg = b.gram.getChildAt(0);
        if (firstItemg != null) {
            firstItemg.setVisibility(View.INVISIBLE);
        }
    }
}