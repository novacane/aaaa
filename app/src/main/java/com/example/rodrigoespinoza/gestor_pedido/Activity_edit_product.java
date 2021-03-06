package com.example.rodrigoespinoza.gestor_pedido;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rodrigoespinoza.gestor_pedido.entitties.Product;
import com.example.rodrigoespinoza.gestor_pedido.entitties.SqlConecttion;

import static com.example.rodrigoespinoza.gestor_pedido.R.layout.activity_edit_product;

public class Activity_edit_product extends Activity implements View.OnClickListener {

    Product product;
    Button btnUpdateProduct, btnDeleteProduct;
    EditText txUpdateProductName, txUpdateProductStock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_edit_product);
        this.btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        this.btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
        this.txUpdateProductName = findViewById(R.id.txUpdatProductName);
        this.txUpdateProductStock = findViewById(R.id.txUpdaProductStock);
        this.btnUpdateProduct.setOnClickListener(this);
        this.btnDeleteProduct.setOnClickListener(this);
        Intent productIntent =  getIntent();
        Bundle productBundle = productIntent.getExtras();


        if (productBundle != null) {
            this.product = new Product(
                    Integer.parseInt(productBundle.get("product_id").toString()),
                    productBundle.get("product_name").toString(),
                    Integer.parseInt(productBundle.get("product_stock").toString())
            );
            this.txUpdateProductName.setText(this.product.getName());
            this.txUpdateProductStock.setText(this.product.getStock().toString());
        }

    }

    @Override
    public void onClick(View v) {
        SqlConecttion conn = new SqlConecttion(
                this, "bd_gestor_pedidos", null,1);
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parametros = {this.product.getId().toString()};
        Intent intent = new Intent(this, Activity_products.class);
        switch (v.getId()){
            case R.id.btnDeleteProduct:
                // TODO TRY CATCH
                try {
                    db.delete("product", "id=?", parametros);
                    Toast.makeText(this, "Producto eliminado.", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                catch (Exception exc){
                    Toast.makeText(this,"Wrong update.",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnUpdateProduct:

                try{
                this.product.setStock(Integer.parseInt(this.txUpdateProductStock.getText().toString()));
                this.product.setName(this.txUpdateProductName.getText().toString());
                ContentValues values = new ContentValues();
                values.put("stock", this.product.getStock());
                values.put("name", this.product.getName());
                db.update("product", values, "id=?",parametros);
                startActivity(intent);//TODO  No lo mejor.
                }catch (Exception exp){
                    Toast.makeText(this,"Wrong delete.",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
