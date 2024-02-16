package vn.edu.stu.doanandroid.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;

import java.util.List;

import vn.edu.stu.doanandroid.AddUpdateProduct;
import vn.edu.stu.doanandroid.ProductDetails;
import vn.edu.stu.doanandroid.R;
import vn.edu.stu.doanandroid.ShowProduct;
import vn.edu.stu.doanandroid.dao.DBHelper;
import vn.edu.stu.doanandroid.model.Category;
import vn.edu.stu.doanandroid.model.Product;

public class ProductAdapter extends ArrayAdapter<Product> {
    private Activity context;
    private int resource;
    private List<Product> objects;
    private DBHelper dbHelper;
    private ImageView image;
    private TextView txtProductId;
    private TextView txtProductName;
    private TextView txtProductCategory;
    private ImageButton btnMenu;

    public ProductAdapter(Activity context, int resource, List<Product> objects, DBHelper dbHelper) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.dbHelper = dbHelper;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = inflater.inflate(this.resource, parent, false);

        image = view.findViewById(R.id.img_product_layout);
        txtProductId = view.findViewById(R.id.tvId);
        txtProductName = view.findViewById(R.id.tvName);
        txtProductCategory = view.findViewById(R.id.tvCategory);

        btnMenu = view.findViewById(R.id.btnMenuProduct);

        final Product product = objects.get(position);

        txtProductId.setText(product.getId() + "");
        txtProductName.setText(product.getName());

        if(product.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
            image.setImageBitmap(bitmap);
        }

        txtProductCategory.setText(dbHelper.getCategoryName(product.getCategoryId()));

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                MenuInflater inflater = popupMenu.getMenuInflater();

                inflater.inflate(R.menu.menu_main, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.detail) {
                            Intent intent = new Intent(getContext(), ProductDetails.class);
                            intent.putExtra("product", product);
                            getContext().startActivity(intent);

                            return true;
                        }

                        if (item.getItemId() == R.id.update) {
                            Intent intent = new Intent(getContext(), AddUpdateProduct.class);
                            intent.putExtra("product", product);
                            ((Activity) getContext()).startActivityForResult(intent, ShowProduct.update);

                            return true;
                        }

                        if (item.getItemId() == R.id.delete) {
                            showDeleteConfirmationDialog(position);

                            return true;
                        }

                        return false;
                    }
                });
            }
        });

        return view;
    }

    private void showDeleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận xoá");
        builder.setMessage("Bạn có chắc chắn muốn xoá không?");

        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long result = dbHelper.deleteProduct(objects.get(position).getId());
                if (result > 0) {
                    objects.remove(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Xoá thất bại", Toast.LENGTH_SHORT).show();
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
