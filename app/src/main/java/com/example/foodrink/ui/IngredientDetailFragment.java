package com.example.foodrink.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.foodrink.DBHelper;
import com.example.foodrink.R;
import com.google.android.material.textfield.TextInputEditText;

public class IngredientDetailFragment extends Fragment {

    private TextInputEditText etName, etCategory, etQuantity, etUnit, etLocation;
    private Button btnSave, btnDelete;
    private DBHelper dbHelper;
    private int ingredientId = -1;

    public static IngredientDetailFragment newInstance(int ingredientId) {
        IngredientDetailFragment fragment = new IngredientDetailFragment();
        Bundle args = new Bundle();
        args.putInt("INGREDIENT_ID", ingredientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ingredient_detail, container, false);

        etName = view.findViewById(R.id.etIngredientName);
        etCategory = view.findViewById(R.id.etIngredientCategory);
        etQuantity = view.findViewById(R.id.etIngredientQuantity);
        etUnit = view.findViewById(R.id.etIngredientUnit);
        etLocation = view.findViewById(R.id.etIngredientLocation);

        btnSave = view.findViewById(R.id.btnSaveIngredient);
        btnDelete = view.findViewById(R.id.btnDeleteIngredient);

        dbHelper = new DBHelper(requireContext());

        // Ambil ID bahan dari argumen
        if (getArguments() != null) {
            ingredientId = getArguments().getInt("INGREDIENT_ID", -1);
        }

        // Muat data bahan jika ini mode edit
        if (ingredientId != -1) {
            loadIngredientData();
        } else {
            // Mode tambah bahan baru
            btnDelete.setVisibility(View.GONE); // Sembunyikan tombol hapus
        }

        // Set listeners
        btnSave.setOnClickListener(v -> saveIngredient());
        btnDelete.setOnClickListener(v -> confirmDelete());

        return view;
    }

    private void loadIngredientData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(
                    "SELECT name, quantity, unit, storage_location, category FROM ingredients WHERE id = ?",
                    new String[]{String.valueOf(ingredientId)}
            );

            if (cursor.moveToFirst()) {
                etName.setText(cursor.getString(0));
                etQuantity.setText(String.valueOf(cursor.getInt(1)));
                etUnit.setText(cursor.getString(2));

                // Handle optional columns safely
                int storageIndex = cursor.getColumnIndex("storage_location");
                if (storageIndex != -1) {
                    etLocation.setText(cursor.getString(storageIndex));
                }

                int categoryIndex = cursor.getColumnIndex("category");
                if (categoryIndex != -1) {
                    etCategory.setText(cursor.getString(categoryIndex));
                }
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error loading ingredient data: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    private void saveIngredient() {
        String name = etName.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String category = etCategory.getText().toString().trim();

        // Validasi input
        if (name.isEmpty() || quantityStr.isEmpty() || unit.isEmpty()) {
            Toast.makeText(requireContext(), "Nama, jumlah, dan satuan harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Jumlah harus berupa angka", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buat content values
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("storage_location", location);
        values.put("category", category);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (ingredientId != -1) {
            // Update bahan yang sudah ada
            db.update("ingredients", values, "id = ?", new String[]{String.valueOf(ingredientId)});
            Toast.makeText(requireContext(), "Bahan berhasil diperbarui", Toast.LENGTH_SHORT).show();
        } else {
            // Tambahkan bahan baru
            db.insert("ingredients", null, values);
            Toast.makeText(requireContext(), "Bahan berhasil ditambahkan", Toast.LENGTH_SHORT).show();
        }

        db.close();

        // Kembali ke fragment sebelumnya
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void confirmDelete() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah Anda yakin ingin menghapus bahan ini?")
                .setPositiveButton("Ya", (dialog, which) -> deleteIngredient())
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void deleteIngredient() {
        if (ingredientId != -1) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("ingredients", "id = ?", new String[]{String.valueOf(ingredientId)});
            db.close();

            Toast.makeText(requireContext(), "Bahan berhasil dihapus", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }
}