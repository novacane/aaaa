package com.example.rodrigoespinoza.gestor_pedido;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rodrigoespinoza.gestor_pedido.entitties.SqlConecttion;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnOpenOrderView;// Solo para probar

    //Variables relacionadas al login del usuario
    EditText txtUser, txtPass;
    Button btnLogin, btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Instancionamos las variables creadas
        txtUser = (EditText) findViewById(R.id.txtUser);
        txtPass = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(this); // Constantemente escuchando

        //Importamos las variables del diseño hasta aqui
        btnRegistrar = (Button) findViewById(R.id.btnRegister);
        btnRegistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:

                String user = txtUser.getText().toString();
                String pass = txtPass.getText().toString();
                Integer id = autenticaUsuario(user, pass);
                Toast.makeText(this,id.toString(),Toast.LENGTH_SHORT).show();
                if(id != 0){
                    Intent intentMenuUser = new Intent(this, MenuActivity.class);
                    intentMenuUser.putExtra("id",id);
                    startActivity(intentMenuUser);
                } else {
                    Toast.makeText(this,"Usuario o Password incorrectos",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnRegister:

                Intent regisrar = new Intent(this, RegisterUser.class);
                startActivity(regisrar);
                Toast.makeText(this, "New Register", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private Integer autenticaUsuario(String user, String pass) {

        SqlConecttion conn =  new SqlConecttion(this, "bd_gestor_pedidos", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        try {
            String[] parametrosBuscar = {user, pass};
            String[] camposTraer = {"id"};

            Cursor cursor = db.query("user", camposTraer, "email = ? AND pass = ?", parametrosBuscar, null, null, null);

            cursor.moveToFirst();
            Integer id = cursor.getInt(cursor.getColumnIndex("id"));
            //Toast.makeText(this, cursor.getString(0), Toast.LENGTH_SHORT).show();
            cursor.close();
            conn.close();
            return id;
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
            conn.close();
            return 0;
        } finally {
            conn.close();
        }
    }
}
