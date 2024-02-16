package vn.edu.stu.doanandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import vn.edu.stu.doanandroid.dao.DBHelper;
import vn.edu.stu.doanandroid.model.Category;
import vn.edu.stu.doanandroid.model.Product;

public class ProductDetails extends AppCompatActivity {
    private ImageView imageView;
    private EditText txtName;
    private EditText txtPrice;
    private EditText txtDescription;
    private EditText txtCategory;
    private Product product;
    private ArrayAdapter<String> spinnerAdapter;
    private DBHelper dbHelper;
    private BottomNavigationView menu;
    private ShowProduct showProduct = new ShowProduct();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        addControls();

        showProduct.setActionMenu(ProductDetails.this, menu);

        setIntentData();
    }

    private void setIntentData() {
        Intent intent = getIntent();

        if(intent.hasExtra("product")) {
            product = (Product) intent.getSerializableExtra("product");

            txtName.setText(product.getName());
            txtDescription.setText(product.getDescription());
            txtPrice.setText(product.getPrice() +"");
            txtCategory.setText(dbHelper.getCategoryName(product.getCategoryId()));

            if(product.getImage() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private void addControls() {
        imageView = findViewById(R.id.img_product_details);
        txtName = findViewById(R.id.txtProductName_details);
        txtPrice = findViewById(R.id.txtProductPrice_details);
        txtDescription = findViewById(R.id.txtProductDescription_details);
        txtCategory = findViewById(R.id.txtProductCategory_details);
        product = new Product();
        dbHelper = new DBHelper(ProductDetails.this);
        menu = findViewById(R.id.menu);
    }
}