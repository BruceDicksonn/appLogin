package view.telaPrincipal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.apploginfirebase.R;
import com.google.firebase.auth.FirebaseAuth;

import factory.FactoryFirebaseInstances;
import view.form_login.FormLogin;

public class TelaPrincipal extends AppCompatActivity {

    private Button btnDeslogar;
    FactoryFirebaseInstances factory = new FactoryFirebaseInstances();
    FirebaseAuth auth = factory.createFireBaseAuthInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);
        getSupportActionBar().hide();
        initComponents();

        btnDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut(); // metodo responsavel por deslogar o usuario do firebase
                navegarTelaLogin();
            }
        });

    }

    public void navegarTelaLogin(){
        Intent intent = new Intent(this, FormLogin.class);
        startActivity(intent);
        finish();
    }

    public void initComponents(){
        btnDeslogar = findViewById(R.id.btnDeslogar);
    }
}