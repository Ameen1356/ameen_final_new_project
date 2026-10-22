package am.sh.ameen_final_new_project.viewPkg;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import am.sh.ameen_final_new_project.R;

public class RegisterActivity extends AppCompatActivity {
    private TextView Text_View_Register;
    private EditText et_FullName;
    private EditText et_Email;
    private EditText et_Password;
    private Button btn_Register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        Text_View_Register=findViewById(R.id.TextView_registor);
        et_FullName=findViewById(R.id.etFullName);
        et_Email=findViewById(R.id.etEmail);
        et_Password=findViewById(R.id.etPassword);
        btn_Register=findViewById(R.id.btnRegister);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}