package am.sh.ameen_final_new_project.viewkmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import am.sh.ameen_final_new_project.model.MyUserTable.MyUser;
import am.sh.ameen_final_new_project.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository repository;

    /**
     * منشئ الكلاس (Constructor).
     * يقوم بتهيئة المستودع (TaskRepository) وجلب قائمة جميع المهام الأولية.
     *
     * @param application سياق التطبيق (Application Context) الممرر للـ AndroidViewModel.
     */
    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);

    }

    /**
     * add a new user
     */
    public void insert(MyUser...user)
    {
        repository.registerUser(user);
    }

    public LiveData<MyUser> login(String email, String password) {
        return repository.login(email ,password);
    }


}

