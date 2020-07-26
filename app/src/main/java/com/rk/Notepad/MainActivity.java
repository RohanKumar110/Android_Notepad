package com.rk.Notepad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<NoteModel> notes;
    Database db;
    public static boolean check = false;
    Button btnToggle;
    TextView txtDialogMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar_ID);
        recyclerView = findViewById(R.id.recycler_ID);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(toolbar);

        db = new Database(this);
        notes = db.getNotes();
        adapter = new NoteAdapter(getApplicationContext(), notes);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.addBtn)
            startActivity(new Intent(MainActivity.this, NoteActivity.class));
        else if (item.getItemId() == R.id.settings) {
            openDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openDialog() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.custom_setting_dialog,null);

        btnToggle = view.findViewById(R.id.btntoggle);
        txtDialogMsg = view.findViewById(R.id.txtDialogMsg);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        if(isEnabled()){
            btnToggle.setBackgroundResource(R.drawable.toggle_on);
        }
        else{
            btnToggle.setBackgroundResource(R.drawable.toggle_off);
        }

        alert.setView(view);
        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle("Settings");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btnToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performAction();
            }
        });
        alertDialog.show();

    }

    private void performAction() {

        if(checkSdkVersion()){

            if(isEnabled()){
                disableFingerprintAuth();
                btnToggle.setBackgroundResource(R.drawable.toggle_off);
                Toast.makeText(getApplicationContext(),"Fingerprint Auth Disabled",Toast.LENGTH_SHORT).show();
            }
            else{
                checksFingerprintRequirements();
                if(check){
                    enableFingerprintAuth();
                    btnToggle.setBackgroundResource(R.drawable.toggle_on);
                    Toast.makeText(getApplicationContext(),"Fingerprint Auth Enabled",Toast.LENGTH_SHORT).show();
                }
            }

        }else{
            txtDialogMsg.setText("Requires 6.0 or Higher Version");
        }
    }

    private void checksFingerprintRequirements() {

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                check = false;
                txtDialogMsg.setVisibility(View.VISIBLE);
                txtDialogMsg.setText("Fingerprint Scanner not detected in Device");
                break;
            case BiometricManager.BIOMETRIC_SUCCESS:
                check = true;
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                check = false;
                txtDialogMsg.setVisibility(View.VISIBLE);
                txtDialogMsg.setText("Biometric Sensor is currently unavailable");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                check = false;
                txtDialogMsg.setVisibility(View.VISIBLE);
                txtDialogMsg.setText("Device don't have any fingerprint enrolled");
                break;
        }
    }

    private void enableFingerprintAuth() {

        SharedPreferences preferences = getSharedPreferences("Auth", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Enabled", true);
        editor.apply();
    }

    private boolean isEnabled() {
        SharedPreferences preferences = getSharedPreferences("Auth", MODE_PRIVATE);
        boolean isEnabledBool = preferences.getBoolean("Enabled", false);
        return isEnabledBool;
    }

    public void disableFingerprintAuth() {

        SharedPreferences preferences = getSharedPreferences("Auth", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public boolean checkSdkVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
