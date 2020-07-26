package com.rk.Notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class FingerprintAuth extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fingerprint_auth);

        checkAuthEnabled();
    }

    private void checkAuthEnabled() {
        SharedPreferences preferences = getSharedPreferences("Auth",MODE_PRIVATE);
        boolean isEnabled = preferences.getBoolean("Enabled",false);
        if(isEnabled)
            fingerprintAuthentication();
        else
            gotoMainActivity();
    }

    private void gotoMainActivity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(FingerprintAuth.this,MainActivity.class));
                finish();
            }
        },1000);
    }


    private void fingerprintAuthentication() {

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt prompt = new BiometricPrompt(FingerprintAuth.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

                if(errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON){
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                startActivity(new Intent(FingerprintAuth.this,MainActivity.class));
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(),"Authentication Failed",Toast.LENGTH_SHORT).show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Verification")
                .setDescription("Fingerprint require for Accessing the App")
                .setNegativeButtonText("Cancel")
                .build();

        prompt.authenticate(promptInfo);

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthEnabled();
    }
}