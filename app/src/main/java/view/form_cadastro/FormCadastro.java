package view.form_cadastro;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.NetworkErrorException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.apploginfirebase.R;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import factory.FactoryFirebaseInstances;
import view.form_login.FormLogin;

public class FormCadastro extends AppCompatActivity {

    private EditText email,senha;
    private Button btnCadastrar;

    //private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FactoryFirebaseInstances factoryInstances = new FactoryFirebaseInstances();
    private FirebaseAuth auth = factoryInstances.createFireBaseAuthInstance(); // usando factory

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);
        getSupportActionBar().hide();
        initComponents();

        btnCadastrar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Snackbar snackbar =  Snackbar.make(view, "",Snackbar.LENGTH_SHORT);

                   if(email.getText().toString().isEmpty() || senha.getText().toString().isEmpty()){

                       snackbar.setText("Preencha todos os campos");
                       snackbar.setBackgroundTint(Color.RED);
                       snackbar.show();

                   } else {


                        ProgressBar p = findViewById(R.id.progress);
                        p.setVisibility(View.VISIBLE);

                        auth.createUserWithEmailAndPassword(email.getText().toString(),senha.getText().toString()).addOnCompleteListener(cadastro -> { // -> lambda

                              if(cadastro.isSuccessful()){

                              auth.signOut();//Desloga, se n??o o user ?? dado como logado no sistema
                              p.setVisibility(View.INVISIBLE);
                              messageSuccess(view,"Cadastrado com sucesso!");
                              limparDados();


                              /* Delay antes de mandar para a outra tela */
                              new Handler().postDelayed(new Runnable() {
                                  @Override
                                  public void run() {

                                      navegarTelaLogin(); // parte p/ tela de login ap??s o cadastro
                                      finish();

                                  }
                              },3025);

                              } else {
                                  String messageError = "";

                                  try {
                                      throw cadastro.getException();

                                  } catch (FirebaseAuthWeakPasswordException e){ // senha precisa ter no m??nimo 6 digitos

                                      messageError = "A senha precisa ter no m??nimo 6 d??gitos.";

                                  } catch (FirebaseAuthInvalidCredentialsException e) { // email inv??lido

                                      messageError = "Digite um email v??lido!";

                                  }catch (FirebaseAuthUserCollisionException e) { // quando o usuario j?? est?? cadastrado

                                      messageError = "Este usu??rio j?? est?? cadastrado!";

                                  }catch (FirebaseNetworkException e) { // quando o usuario est?? sem conexao com a internet

                                      messageError = "Sem conex??o com a internet!";

                                      /*
                                      *     Como estamos validando uma Exception de Network, precisamos
                                      *     adicionar uma permissao de internet em nosso Android.Manifest
                                      *
                                      *     <uses-permission android:name="android.permission.INTERNET"/>
                                      *
                                      * */

                                  }catch (Exception e) {
                                      messageError = "Erro ao cadastrar o usu??rio";
                                  }

                                  messageError(view,messageError);

                                  p.setVisibility(View.INVISIBLE);

                              }
                        });
                   }
            }
        });

    }

    public void navegarTelaLogin(){
        Intent intent = new Intent(getApplicationContext(), FormLogin.class);
        startActivity(intent);
    }

    public void limparDados(){
        email.setText("");
        senha.setText("");
        senha.clearFocus();
    }

    public void initComponents(){
        email = findViewById(R.id.txtUserEmail);
        senha = findViewById(R.id.txtUserSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);
    }

    public void messageSuccess(View view,String message){
        Snackbar snackbar = Snackbar.make(view ,message, Snackbar.LENGTH_LONG);
        snackbar.setText(message);
        snackbar.setBackgroundTint(Color.parseColor("#386D3A")); // this color is a dark green
        snackbar.show();
    }

    public void messageError(View view,String message){
        Snackbar snackbar = Snackbar.make(view ,message, Snackbar.LENGTH_LONG);
        snackbar.setText(message);
        snackbar.setBackgroundTint(Color.RED); // this color is a dark green
        snackbar.show();
    }
}