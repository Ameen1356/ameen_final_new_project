package am.sh.ameen_final_new_project.viewPkg;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import am.sh.ameen_final_new_project.model.AppDataBase;
import am.sh.ameen_final_new_project.model.MySubjectTable.MySubject;
import am.sh.ameen_final_new_project.model.MySubjectTable.MySubjectQuery;
import am.sh.ameen_final_new_project.R;

public class MainActivity extends AppCompatActivity {

    private Button btn_Sign_In_Screen;
    private Button btn_Sign_Up_Screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //بناء قاعدة بيانات وارجاع مؤشر عليها1
        AppDataBase db=AppDataBase.getDB(getApplicationContext());
        //2 مؤشر لكائن عمليات  لجدول
        MySubjectQuery subjectQuery = db.getMySubjectQuery();
        //3  بناء كائن من نوع الجدول وتحديد قيم الصفات
        MySubject s1=new MySubject();
        s1.setTitle("Math");
        MySubject s2=new MySubject();
        s2.title="Computers";
        //4 اضافة كائن للجدول

        setContentView(R.layout.activity_main);




        btn_Sign_In_Screen=findViewById(R.id.btnSignInScreen);
        btn_Sign_In_Screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, SigninActivity.class);
                startActivity(i);
            }
        });
        btn_Sign_Up_Screen=findViewById(R.id.btnSignUpScreen);
        btn_Sign_Up_Screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itmSetting) {
            Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}