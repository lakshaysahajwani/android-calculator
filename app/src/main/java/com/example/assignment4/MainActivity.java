package com.example.assignment4;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;


public class MainActivity extends AppCompatActivity {
    private TextView input, output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        input = findViewById(R.id.tvFormula);
        output = findViewById(R.id.tvResult);


        findViewById(R.id.btn_ce).setOnClickListener(v -> {
            input.setText("");
            output.setText("");
        });


        findViewById(R.id.btn_c).setOnClickListener(v -> {
            String text = input.getText().toString();
            if (!text.isEmpty()) {
                input.setText(text.substring(0, text.length() - 1));
            }
        });


        findViewById(R.id.btn_equal).setOnClickListener(v -> {
            String data = input.getText().toString();
            if (data.isEmpty()) return;

            Context context = Context.enter();
            try {
                context.setOptimizationLevel(-1);
                Scriptable scriptable = context.initStandardObjects();
                Object result = context.evaluateString(scriptable, data, "javascript", 1, null);
                String finalResult = Context.toString(result);
                if (finalResult.endsWith(".0")) {
                    finalResult = finalResult.replace(".0", "");
                }
                output.setText(finalResult);
            } catch (Exception e) {
                output.setText(R.string.error_text);
            } finally {
                Context.exit();
            }
        });

        int[] buttonIds = {
                R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9,
                R.id.btn_plus, R.id.btn_minus, R.id.btn_multiply, R.id.btn_divide,
                R.id.btn_dot, R.id.btn_plus_minus, R.id.btn_backspace
        };

        View.OnClickListener numberClickListener = view -> {
            Button btn = (Button) view;
            String btnText = btn.getText().toString();
            String currentText = input.getText().toString();
            input.setText(String.format("%s%s", currentText, btnText));
        };

        for (int id : buttonIds) {
            View button = findViewById(id);
            if (button != null) {
                button.setOnClickListener(numberClickListener);
            }
        }
    }
}
