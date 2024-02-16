package vn.edu.stu.doanandroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import vn.edu.stu.doanandroid.dao.DBHelper;
import vn.edu.stu.doanandroid.model.Category;

public class ShowCategory extends AppCompatActivity {
    private ShowProduct showProduct = new ShowProduct();
    private BottomNavigationView menu;
    private ArrayAdapter<Category> adapter;
    private DBHelper dbHelper;
    private ListView listView;
    private TextView txtCategoryId;
    private EditText txtCategoryName;
    private Category category;
    private ImageButton btnAdd;
    private List<Category> categoryList;
    private Button button;
    private int pos = -1;
    private Button btnDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_category);

        addControls();
        getAllCategories();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                category = categoryList.get(position);
                txtCategoryId.setText(category.getId() + "");
                txtCategoryName.setText(category.getName());
                pos = position;
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCategoryId.setText("Mã phân loại");
                txtCategoryName.setText("");

                category = new Category();
                pos = -1;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category.setName(txtCategoryName.getText().toString());

                if (txtCategoryId.getText().equals("Mã phân loại")) {

                    long result = dbHelper.addCategory(category);
                    if (result > 0) {
                        adapter.clear();
                        getAllCategories();
                    } else {
                        Toast.makeText(ShowCategory.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        category.setId(Integer.parseInt(txtCategoryId.getText().toString()));
                        long result = dbHelper.updateCategory(category);
                        if (result > 0) {
                            adapter.clear();
                            getAllCategories();
                        } else {
                            Toast.makeText(ShowCategory.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception exception) {
                        Toast.makeText(ShowCategory.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pos < 0) {
                    Toast.makeText(ShowCategory.this, "Chọn phần tử muốn xoá", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowCategory.this);
                    builder.setTitle("Xác nhận xoá");
                    builder.setMessage("Bạn có chắc chắn muốn xoá không?");

                    builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Integer id = Integer.parseInt(txtCategoryId.getText().toString());
                            long result = dbHelper.deleteCategory(id);
                            if (result > 0) {
                                categoryList.remove(pos);
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(ShowCategory.this, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }


    private void addControls() {
        listView = findViewById(R.id.lvCategory);
        menu = findViewById(R.id.menu);
        showProduct.setActionMenu(ShowCategory.this, menu);
        dbHelper = new DBHelper(ShowCategory.this);
        category = new Category();
        txtCategoryId = findViewById(R.id.txtIdCategory);
        txtCategoryName = findViewById(R.id.txtCategory);
        btnAdd = findViewById(R.id.btnAdd);
        button = findViewById(R.id.btnConfirmCategory);
        btnDelete = findViewById(R.id.btnDeleteCategory);
    }

    private void getAllCategories() {
        categoryList = dbHelper.getAllCategories();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, categoryList);
        listView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

}