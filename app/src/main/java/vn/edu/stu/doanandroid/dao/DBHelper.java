package vn.edu.stu.doanandroid.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import vn.edu.stu.doanandroid.model.Category;
import vn.edu.stu.doanandroid.model.Product;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database.sqlite";
    private static final int DATABASE_VERSION = 10;
    private static final String TABLE_CATEGORY = "category";
    private static final String TABLE_PRODUCT = "product";
    private static final String COLUMN_CATEGORY_ID = "id";
    private static final String COLUMN_CATEGORY_NAME = "name";
    private static final String COLUMN_PRODUCT_ID = "id";
    private static final String COLUMN_PRODUCT_NAME = "name";
    private static final String COLUMN_PRODUCT_IMAGE = "image";
    private static final String COLUMN_PRODUCT_PRICE = "price";
    private static final String COLUMN_PRODUCT_DESCRIPTION = "description";
    private static final String COLUMN_PRODUCT_CATEGORY_ID = "category_id";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        String[] projection = {
                DBHelper.COLUMN_PRODUCT_ID,
                DBHelper.COLUMN_PRODUCT_NAME,
                DBHelper.COLUMN_PRODUCT_IMAGE,
                DBHelper.COLUMN_PRODUCT_PRICE,
                DBHelper.COLUMN_PRODUCT_DESCRIPTION,
                DBHelper.COLUMN_PRODUCT_CATEGORY_ID
        };

        Cursor cursor = getReadableDatabase().query(
                DBHelper.TABLE_PRODUCT, projection, null, null, null, null, null
        );

        while (cursor.moveToNext()) {

            Product product = new Product(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_NAME)),
                    cursor.getBlob(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_IMAGE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_DESCRIPTION)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PRODUCT_CATEGORY_ID))
            );

            productList.add(product);
        }
        cursor.close();

        return productList;
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        String[] projection = {
                DBHelper.COLUMN_CATEGORY_ID,
                DBHelper.COLUMN_CATEGORY_NAME
        };
        Cursor cursor = getReadableDatabase().query(
                DBHelper.TABLE_CATEGORY, projection, null, null, null, null, null
        );

        while (cursor.moveToNext()) {

            Category category = new Category(
                    cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_CATEGORY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_CATEGORY_NAME))
            );
            categoryList.add(category);
        }
        cursor.close();

        return categoryList;
    }

    public long addProduct(Product product) {
        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_PRODUCT_NAME, product.getName());
        values.put(DBHelper.COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(DBHelper.COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(DBHelper.COLUMN_PRODUCT_IMAGE, product.getImage());
        values.put(DBHelper.COLUMN_PRODUCT_CATEGORY_ID, product.getCategoryId());

        return getReadableDatabase().insert(DBHelper.TABLE_PRODUCT, null, values);
    }

    public String getCategoryName(int categoryId) {
        List<Category> categoryList = getAllCategories();
        for (Category category : categoryList) {

            if (categoryId == category.getId()) {

                return category.getName();
            }
        }

        return "";
    }

    public long updateProduct(Product product) {
        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_PRODUCT_NAME, product.getName());
        values.put(DBHelper.COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(DBHelper.COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
        values.put(DBHelper.COLUMN_PRODUCT_IMAGE, product.getImage());
        values.put(DBHelper.COLUMN_PRODUCT_CATEGORY_ID, product.getCategoryId());

        String selection = DBHelper.COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = {product.getId() + ""};

       return getWritableDatabase().update(
                DBHelper.TABLE_PRODUCT, values, selection, selectionArgs
        );
    }

    public long deleteProduct(int id) {
        String selection = DBHelper.COLUMN_PRODUCT_ID +" = ?";
        String[] selectionArgs = {String.valueOf(id)};

        return getReadableDatabase().delete(DBHelper.TABLE_PRODUCT, selection, selectionArgs);
    }

    public long addCategory(Category category) {
        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_CATEGORY_NAME, category.getName());

        return getReadableDatabase().insert(DBHelper.TABLE_CATEGORY, null, values);
    }

    public long updateCategory(Category category) {
        ContentValues values = new ContentValues();

        values.put(DBHelper.COLUMN_CATEGORY_NAME, category.getName());

        String selection = DBHelper.COLUMN_CATEGORY_ID + " = ?";
        String[] selectionArgs = {category.getId() + ""};

        return getWritableDatabase().update(
                DBHelper.TABLE_CATEGORY, values, selection, selectionArgs
        );
    }

    public long deleteCategory(int id) {
        // Check if there are any products associated with the given category ID
        SQLiteDatabase db = getReadableDatabase();

        String[] projection = { DBHelper.COLUMN_PRODUCT_ID };
        String selection = DBHelper.COLUMN_PRODUCT_CATEGORY_ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor cursor = db.query(
                DBHelper.TABLE_PRODUCT,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int count = cursor.getCount();
        cursor.close();

        if (count > 0) {

            return -1;
        } else {
            selection = DBHelper.COLUMN_CATEGORY_ID + " = ?";
            selectionArgs = new String[]{String.valueOf(id)};
            return getWritableDatabase().delete(DBHelper.TABLE_CATEGORY, selection, selectionArgs);
        }
    }

}
