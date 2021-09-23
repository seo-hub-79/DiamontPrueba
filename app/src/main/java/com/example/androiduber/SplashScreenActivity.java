package com.example.androiduber;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.androiduber.Model.DriverInfoModel;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Driver;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;


public class SplashScreenActivity extends AppCompatActivity {

    private final static int LOGIN_REQUEST_CODE = 7171;
    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener listener;

    @BindView(R.id.progress_bar)
    ProgressBar progress_Bar;

    FirebaseDatabase database;
    DatabaseReference driverInfoRef;


    @Override
    protected void onStart() {
        super.onStart();
       delaySplashScreen();
    }

    @Override
    protected void onStop() {
        if(firebaseAuth != null && listener != null){
            firebaseAuth.removeAuthStateListener(listener);
        }
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        init();
    }

    private void init() {

        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance();
        driverInfoRef = database.getReference(Common.DRIVER_INFO_REFERENCE);

        providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        firebaseAuth = FirebaseAuth.getInstance();
        listener = myFirebaseAuth -> {
            FirebaseUser user = myFirebaseAuth.getCurrentUser();
            if(user != null ){
                //delaySplashScreen();
                //Toast.makeText(this, "bienbenido" + user.getUid(), Toast.LENGTH_SHORT).show();
                checkUserFromFireBase();
            }
            else
            {
                showLoginLayout();
            }
        };
    }

    private void checkUserFromFireBase() {
        driverInfoRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            //Toast.makeText(SplashScreenActivity.this, "User already register", Toast.LENGTH_SHORT).show();
                            DriverInfoModel driverInfoModel = snapshot.getValue(DriverInfoModel.class);
                            gotoHomeActivity(driverInfoModel);
                        }
                        else
                        {
                            showRegisterLayout();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SplashScreenActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void gotoHomeActivity(DriverInfoModel driverInfoModel) {
        Common.currentUser =  driverInfoModel; //INit value
        startActivity(new Intent(SplashScreenActivity.this, DriverHomeActivity.class));
        finish();
    }

    private void showRegisterLayout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        View itemView = LayoutInflater.from(this).inflate(R.layout.layout_register,null);

        TextInputEditText edt_Nombre = (TextInputEditText)itemView.findViewById(R.id.edt_first_name);
        TextInputEditText edt_Apellido = (TextInputEditText)itemView.findViewById(R.id.edt_apellido);
        TextInputEditText edt_Telefono = (TextInputEditText)itemView.findViewById(R.id.edt_numeroTelefono);

        Button btn_Continuar = (Button)itemView.findViewById(R.id.btn_registrar);

        //setData
        if(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber() != null &&
        TextUtils.isEmpty(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()))
            edt_Telefono.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

        //setView
        builder.setView(itemView);
        AlertDialog dialog =  builder.create();
        dialog.show();

        btn_Continuar.setOnClickListener(v -> {
                if (TextUtils.isEmpty(edt_Nombre.getText().toString()))
                {
                    Toast.makeText(this, "Por favor ingrese su nombre", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(edt_Apellido.getText().toString()))
                {
                    Toast.makeText(this, "Por favor ingrese su apellido", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (TextUtils.isEmpty(edt_Telefono.getText().toString()))
                {
                    Toast.makeText(this, "Por favor ingrese su Telefono", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    DriverInfoModel model =  new DriverInfoModel();
                    model.setNombre(edt_Nombre.getText().toString());
                    model.setApellido(edt_Apellido.getText().toString());
                    model.setNumero(edt_Telefono.getText().toString());
                    model.setRating(0.0);

                    driverInfoRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(model).addOnFailureListener(e ->
                            {
                                dialog.dismiss();
                                Toast.makeText(SplashScreenActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            )
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        gotoHomeActivity(model);
                    });
                }
        });

    }

    private void showLoginLayout() {
        AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout
                .Builder(R.layout.layout_sing_in)
                .setPhoneButtonId(R.id.btn_phone_sing_in)
                .setGoogleButtonId(R.id.btn_correo_sing_in)
                .build();

        startActivityForResult(AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAuthMethodPickerLayout(authMethodPickerLayout)
        .setIsSmartLockEnabled(false)
                .setTheme(R.style.LoginScreen)
        .setAvailableProviders(providers)
        .build(),LOGIN_REQUEST_CODE);
    }

    private void delaySplashScreen() {

        progress_Bar.setVisibility(View.VISIBLE);

        Completable.timer(3, TimeUnit.SECONDS,
                AndroidSchedulers.mainThread())
                .subscribe(() ->
                        firebaseAuth.addAuthStateListener(listener)
                );
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            }
            else
            {
                Toast.makeText(this, "[ERROR]."+response.getError().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}