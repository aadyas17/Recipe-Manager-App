package com.example.recipemanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etRecipeName, etIngredients, etInstructions;
    private Button btnAddRecipe;
    private ListView lvRecipes;
    private TextView tvIngredients, tvInstructions;
    private RecipeDBHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        etRecipeName = findViewById(R.id.etRecipeName);
        etIngredients = findViewById(R.id.etIngredients);
        etInstructions = findViewById(R.id.etInstructions);
        btnAddRecipe = findViewById(R.id.btnAddRecipe);
        lvRecipes = findViewById(R.id.lvRecipes);
        tvIngredients = findViewById(R.id.tvIngredients);
        tvInstructions = findViewById(R.id.tvInstructions);

        // Initialize database helper and adapter
        dbHelper = new RecipeDBHelper(this);
        recipeList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipeList);
        lvRecipes.setAdapter(adapter);

        // Button click listener to add new recipe
        btnAddRecipe.setOnClickListener(v -> {
            String name = etRecipeName.getText().toString();
            String ingredients = etIngredients.getText().toString();
            String instructions = etInstructions.getText().toString();
            if (!name.isEmpty() && !ingredients.isEmpty() && !instructions.isEmpty()) {
                Recipe recipe = new Recipe(name, ingredients, instructions);
                dbHelper.addRecipe(recipe);
                loadRecipes();  // Reload recipe list after adding
            }
        });

        // ListView item click listener to display recipe details
        lvRecipes.setOnItemClickListener((parent, view, position, id) -> {
            String recipeName = recipeList.get(position);
            displayRecipeDetails(recipeName);
        });

        // Load saved recipes into the ListView
        loadRecipes();
    }

    // Load all recipe names from database into the ListView
    private void loadRecipes() {
        Cursor cursor = dbHelper.getAllRecipes();
        recipeList.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(RecipeDBHelper.COL_NAME));
                recipeList.add(name);
            } while (cursor.moveToNext());
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    // Display ingredients and instructions when a recipe is selected
    private void displayRecipeDetails(String recipeName) {
        Cursor cursor = dbHelper.getRecipeByName(recipeName);
        if (cursor != null && cursor.moveToFirst()) {
            String ingredients = cursor.getString(cursor.getColumnIndex(RecipeDBHelper.COL_INGREDIENTS));
            String instructions = cursor.getString(cursor.getColumnIndex(RecipeDBHelper.COL_INSTRUCTIONS));
            tvIngredients.setText("Ingredients: \n" + ingredients);
            tvInstructions.setText("Instructions: \n" + instructions);
            cursor.close();
        }
    }
}
