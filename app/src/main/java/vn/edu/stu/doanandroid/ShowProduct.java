package vn.edu.stu.doanandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.stu.doanandroid.adapter.ProductAdapter;
import vn.edu.stu.doanandroid.dao.DBHelper;
import vn.edu.stu.doanandroid.model.Product;
import vn.edu.stu.doanandroid.ultil.DBConfigUtil;

public class ShowProduct extends AppCompatActivity {
    private ImageButton btnAdd;
    private ImageView imageProduct;
    private BottomNavigationView menu;
    private ProductAdapter productAdapter;
    private DBHelper dbHelper;
    private List<Product> productList = new ArrayList<>();
    private ListView listView;
    public final static int add = 123;
    public final static int update = 456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        DBConfigUtil.copyDatabaseFromAssets(ShowProduct.this);
        addControls();
        setActionMenu(ShowProduct.this, menu);

        getAll();


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowProduct.this, AddUpdateProduct.class);
                startActivityForResult(intent, ShowProduct.add);
            }
        });


    }

    public void setActionMenu(AppCompatActivity activity, BottomNavigationView menu) {
        menu.setSelectedItemId(R.id.homePage);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.about) {

                    Intent intent = new Intent(activity, About.class);
                    activity.startActivity(intent);

                    return true;

                } else if (item.getItemId() == R.id.exit) {
                    activity.finish();

                    return true;
                }

                return false;
            }
        });
    }

    private void addControls() {
        btnAdd = findViewById(R.id.btnAdd);
        dbHelper = new DBHelper(ShowProduct.this);
        listView = findViewById(R.id.lv);
        menu = findViewById(R.id.menu);
        imageProduct = findViewById(R.id.img_product_layout);
    }

    private void getAll() {
        productList = dbHelper.getAllProducts();
        productAdapter = new ProductAdapter(ShowProduct.this, R.layout.layout_show_product, productList, dbHelper);
        listView.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Product product = (Product) data.getSerializableExtra("product");

            if (requestCode == ShowProduct.add) {

                if (dbHelper.addProduct(product) > 0) {

                    dbHelper.addProduct(product);
                    productAdapter.clear();
                    getAll();
                    Toast.makeText(this, "Thêm thành công ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Thêm thất bại ", Toast.LENGTH_SHORT).show();
                }
            }

            if (requestCode == ShowProduct.update) {
                if (dbHelper.updateProduct(product) > 0) {
                    productAdapter.clear();
                    getAll();
                    Toast.makeText(this, "Cập nhật thành công ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Cập nhật thất bại ", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}