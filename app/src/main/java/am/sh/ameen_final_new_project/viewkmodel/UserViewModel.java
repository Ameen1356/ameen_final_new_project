package am.sh.ameen_final_new_project.viewkmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import am.sh.ameen_final_new_project.LiveData.MyUserTable.MyUser;
import am.sh.ameen_final_new_project.repositories.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private UserRepository repository;
    private LiveData<List<MyUser>> allUser;

    /**
     * منشئ الكلاس (Constructor).
     * يقوم بتهيئة المستودع (TaskRepository) وجلب قائمة جميع المهام الأولية.
     *
     * @param application سياق التطبيق (Application Context) الممرر للـ AndroidViewModel.
     */
    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        allUser = repository.getAllUser();
    }
    /**
     * جلب جميع users في LiveData لمراقبتها من واجهة المستخدم.
     *
     * @return قائمة بجميع المستخدمين محدثة تلقائياً.
     */
    public LiveData<List<MyUser>> getAllUser()
    {
        return allUser;
    }

    /**
     * return every user by their id
     */
    public LiveData<List<MyUser>> getAllByIds(int[] keyid)
    {
        return repository.getAllByIds(keyid);
    }

    /**
     * check if the email password is correct
     */
    public List<MyUser> checkEmailPassw(String myEmail, String myPassw)
    {
        return repository.checkEmailPassw(myEmail ,myPassw);
    }

    /**
     * add a new user
     */
    public void insert(MyUser...user)
    {
        repository.insert(user);
    }

    /**
     * updates a pre-existing user
     */
    public void update(MyUser...user)
    {
        repository.update(user);
    }

    /**
     * deletes a user by their id
     */
    public void deleteById(int id)
    {
        repository.deleteById(id);
    }

}

