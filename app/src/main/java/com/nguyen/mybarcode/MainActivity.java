package com.nguyen.mybarcode;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.zxing.BarcodeFormat;

public class MainActivity extends AppCompatActivity {
    BarcodeFormat mFormat = BarcodeFormat.CODE_128;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText textInput = (EditText)findViewById(R.id.text_input);
        Spinner formatSpinner = (Spinner)findViewById(R.id.format_spinner);
        Button generateQR = (Button)findViewById(R.id.generate_qr);

        ArrayAdapter<CharSequence> formatAdapter = ArrayAdapter.createFromResource(this,
                R.array.format_array, android.R.layout.simple_spinner_item);
        formatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        formatSpinner.setAdapter(formatAdapter);
        formatSpinner.setSelection(formatAdapter.getPosition("100%"));
        formatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mFormat = BarcodeFormat.CODE_128;
                        break;
                    case 1:
                        mFormat = BarcodeFormat.QR_CODE;
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textInput.getText().toString();
                BarcodeFragment fragment = BarcodeFragment.newInstance(text, mFormat);
                FragmentManager manager = getSupportFragmentManager();
                fragment.show(manager, "BarcodeFragment");
            }
        });
    }
}
