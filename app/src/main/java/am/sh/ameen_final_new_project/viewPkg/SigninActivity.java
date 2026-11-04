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

public class SigninActivity extends AppCompatActivity {
    private TextView text;
    private EditText etEmail;
    private EditText etPassword;
    private Button btn_sign_in;
    private Button btnGotoSignUp;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);
        text = findViewById(R.id.textView);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        btn_sign_in = findViewById(R.id.button);
        btnGotoSignUp = findViewById(R.id.btnGotoSignUp);

        btn_sign_in.setOnClickListener(view -> signInUser());

        btnGotoSignUp.setOnClickListener(view -> {
            Intent intent = new Intent(SigninActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void signInUser(){
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        MyUser user = userViewModel.login(email, password);
        if (user != null) {
            Toast.makeText(this, "Welcome, " + user.getFullName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }


}
