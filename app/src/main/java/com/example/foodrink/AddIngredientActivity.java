package com.example.foodrink;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddIngredientActivity extends AppCompatActivity {

    private EditText etName, etQuantity;
    private Spinner spUnit;
    private Button btnSave;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        etName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etIngredientQuantity);
        spUnit = findViewById(R.id.spUnit);
        btnSave = findViewById(R.id.btnSaveIngredient);

        dbHelper = new DBHelper(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.unit_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUnit.setAdapter(adapter);

        btnSave.setOnClickListener(v -> saveIngredient());
    }

    private void saveIngredient() {
        String name = etName.getText().toString();
        String quantityStr = etQuantity.getText().toString();
        String unit = spUnit.getSelectedItem().toString();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (name.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "Isi semua field", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(quantityStr);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("added_date", date);
        db.insert("ingredients", null, values);
        db.close();

        Toast.makeText(this, "Bahan berhasil ditambahkan", Toast.LENGTH_SHORT).show();
        finish();
    }
}
