package am.sh.ameen_final_new_project.viewPkg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import am.sh.ameen_final_new_project.R;
import am.sh.ameen_final_new_project.model.MyUserTable.MyUser;
import am.sh.ameen_final_new_project.viewkmodel.UserViewModel;

public class SignUpActivity extends AppCompatActivity {
    private TextView Text_View_Register;
    private EditText et_FullName;
    private EditText et_Email;
    private EditText et_Password;
    private EditText et_Phone;
    private Button btn_Register;
    private UserViewModel userViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        Text_View_Register = findViewById(R.id.TextView_registor);
        et_FullName = findViewById(R.id.etFullName);
        et_Email = findViewById(R.id.etEmail);
        et_Password = findViewById(R.id.etPassword);
        et_Phone = findViewById(R.id.etPhone);
        btn_Register = findViewById(R.id.btnSignUp);

        btn_Register.setOnClickListener(view -> signUpUser());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void signUpUser(){
        String fullName = et_FullName.getText().toString().trim();
        String email = et_Email.getText().toString().trim();
        String password = et_Password.getText().toString().trim();
        String phone = et_Phone.getText().toString().trim();

        if (fullName.isEmpty()) {
            et_FullName.setError("Full name is required");
            et_FullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            et_Email.setError("Email is required");
            et_Email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            et_Password.setError("Password is required");
            et_Password.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            et_Phone.setError("Phone number is required");
            et_Phone.requestFocus();
            return;
        }

        // Create new user object and save to the database via ViewModel
        MyUser newUser = new MyUser();
        newUser.setFullName(fullName);
        newUser.setEmail(email);
        newUser.setPassw(password);
        newUser.setPhone(phone);

        userViewModel.insert(newUser);

        Toast.makeText(this, "Account created successfully! Please sign in.", Toast.LENGTH_LONG).show();

        // Navigate to Sign In screen so the user can log in with their newly created account
        Intent intent = new Intent(SignUpActivity.this, SigninActivity.class);
        startActivity(intent);
        finish();
    }
}
