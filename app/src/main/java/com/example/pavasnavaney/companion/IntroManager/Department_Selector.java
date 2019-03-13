package com.example.pavasnavaney.companion.IntroManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pavasnavaney.companion.Main.MainActivity;
import com.example.pavasnavaney.companion.R;

public class Department_Selector extends AppCompatActivity{

    private EditText inputName, inputEmail, inputPhone;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPhone;
    private Button depart,conti;
    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        setContentView(R.layout.activity_department__selector);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        inputName = (EditText) findViewById(R.id.input_name);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPhone = (EditText) findViewById(R.id.input_phone);
        depart = (Button)findViewById(R.id.department);
        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPhone.addTextChangedListener(new MyTextWatcher(inputPhone));

        depart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               submitForm();
                //launchHomeScreen();
            }
        });
    }
    private void submitForm() {
        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
       launchHomeScreen();
    }
    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validatePassword() {
        String phone = inputPhone.getText().toString().trim();
        if (phone.isEmpty()||!isValidPhone(phone)) {
            inputLayoutPhone.setError(getString(R.string.err_msg_phone));
            requestFocus(inputPhone);
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }
        return true;
    }

    public void savepreference()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("login",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name",inputName.getText().toString().trim());
        editor.putString("phone",inputPhone.getText().toString().trim());
        editor.putString("email",inputEmail.getText().toString().trim());
        editor.commit();
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private static boolean isValidPhone(String phone) {
        int len = phone.length();
        if(len==10)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

   private void launchHomeScreen() {
       //prefManager.setFirstTimeLaunch(false);
       SharedPreferences pref = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
       String name = pref.getString("name",null);
       if(name==null)
       {
           savepreference();
           Intent i = new Intent(Department_Selector.this, Department_List.class);
           startActivity(i);
       }
       else
       {
           Intent i = new Intent(Department_Selector.this, Department_List.class);
           startActivity(i);
       }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_phone:
                    validatePassword();
                    break;
            }
        }
    }
}

