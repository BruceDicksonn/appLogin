package view.form_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.apploginfirebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

import factory.FactoryFirebaseInstances;
import view.form_cadastro.FormCadastro;
import view.telaPrincipal.TelaPrincipal;

public class FormLogin extends AppCompatActivity {

    TextView cadastro;
    EditText email,senha;
    Button btnEntrar;
    FactoryFirebaseInstances factoryInstances = new FactoryFirebaseInstances();
    FirebaseAuth auth = factoryInstances.createFireBaseAuthInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        getSupportActionBar().hide();
        initComponents();

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FormCadastro.class);
                startActivity(intent);
                finish();
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ProgressBar progress = findViewById(R.id.progress);
                progress.setVisibility(View.VISIBLE);

                if(email.getText().toString().isEmpty() || senha.getText().toString().isEmpty()){

                    messageError(view,"Preencha os campos vazios");
                    progress.setVisibility(View.GONE);

                } else {

                    // metodo para logar com uma conta
                    auth.signInWithEmailAndPassword(email.getText().toString(),senha.getText().toString())
                            //metodo para fazer validacoes ao logar
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                        String msgError = "";

                                        if(task.isSuccessful()){

                                            navegarTelaPrincipal();
                                            finish();

                                        } else {

                                            try{
                                                throw task.getException();
                                            }catch (FirebaseNetworkException e){
                                                   msgError = "Sem conexão com a internet!";
                                            }catch(FirebaseAuthInvalidUserException e){ // caso o user n exista no db
                                                   msgError = "Usuário inexistente!";
                                            } catch (FirebaseAuthInvalidCredentialsException e){
                                                   msgError = "O email ou senha podem estar incorretos.";
                                            }catch (Exception e){
                                                   msgError = "Erro ao logar no servidor.";
                                            }
                                                   messageError(view,msgError);
                                        }

                                                   progress.setVisibility(View.GONE);
                                }
                            });
                }
            }
        });

    }


    // verificar se o usuario já está ou nao logado no sistema
    @Override
    protected void onStart() {
        // pega o usuario logado no sistema
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            navegarTelaPrincipal();
        }

        super.onStart();

    }

    private void navegarTelaPrincipal(){
        Intent intent = new Intent(this, TelaPrincipal.class);
        startActivity(intent);
    }

    public void messageError(View view,String message){
        Snackbar snackbar = Snackbar.make(view ,message, Snackbar.LENGTH_LONG);
        snackbar.setText(message);
        snackbar.setBackgroundTint(Color.RED); // this color is a dark green
        snackbar.show();
    }

    public void limparDados(){
        email.setText("");
        senha.setText("");
        senha.clearFocus();
    }

    public void initComponents(){
        email = findViewById(R.id.txtUserEmail);
        senha = findViewById(R.id.txtUserSenha);
        btnEntrar = findViewById(R.id.btnEntrar);
        cadastro = findViewById(R.id.txtCadastre_se);
    }
}