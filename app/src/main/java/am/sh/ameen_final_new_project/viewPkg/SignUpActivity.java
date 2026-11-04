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
        String email=et_Email.getText().toString().trim();
        String password=et_Password.getText().toString().trim();

        if ((email.isEmpty()))
        {
            et_Email.setError("Email is required");
            et_Email.requestFocus();
            return;
        }

        if (password.isEmpty())
        {
            et_Password.setError("Password is required");
            et_Password.requestFocus();
            return;
        }

        MyUser user = userViewModel.login(email, password);
        if ((user != null))
        {
            Toast.makeText(this, "Welcome, "+ user.getFullName(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}
