package com.example.recipemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDBHelper extends SQLiteOpenHelper {

    // Database and table names
    public static final String DB_NAME = "recipe_manager";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "recipes";

    // Columns
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_INGREDIENTS = "ingredients";
    public static final String COL_INSTRUCTIONS = "instructions";

    // Constructor
    public RecipeDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the recipes table
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT, " +
                COL_INGREDIENTS + " TEXT, " +
                COL_INSTRUCTIONS + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table and create a new one on upgrade
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to add a new recipe to the database
    public void addRecipe(Recipe recipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, recipe.getName());
        values.put(COL_INGREDIENTS, recipe.getIngredients());
        values.put(COL_INSTRUCTIONS, recipe.getInstructions());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Method to get all recipes (names only)
    public Cursor getAllRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, new String[]{COL_NAME}, null, null, null, null, null);
    }

    // Method to get a recipe by its name
    public Cursor getRecipeByName(String recipeName) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database for the recipe with the specified name
        String[] selectionArgs = {recipeName};
        return db.query(TABLE_NAME,
                new String[]{COL_NAME, COL_INGREDIENTS, COL_INSTRUCTIONS},
                COL_NAME + " = ?",
                selectionArgs,
                null,
                null,
                null);
    }
}
