package am.sh.ameen_final_new_project.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import am.sh.ameen_final_new_project.LiveData.AppDataBase;
import am.sh.ameen_final_new_project.LiveData.MyUserTable.MyUser;
import am.sh.ameen_final_new_project.LiveData.MyUserTable.MyUserQuery;

public class UserRepository {
    private MyUserQuery userQuery;

    private LiveData<List<MyUser>> allUser;

    public UserRepository(Application application)
    {
        AppDataBase db = AppDataBase.getDB(application);
        userQuery=db.getMyUserQuery();
        allUser=userQuery.getAll();
    }

    public LiveData<List<MyUser>> getAllUser()
    {
        return allUser;
    }

    public LiveData<List<MyUser>> getAllByIds(int[] keyid)
    {
        return userQuery.loadAllByIds(keyid);
    }

    public List<MyUser> checkEmailPassw(String myEmail, String myPassw)
    {
        return userQuery.checkEmailPassw(myEmail ,myPassw);
    }

    public void insert(MyUser...myUser)
    {
        userQuery.insertAll(myUser);
    }

    public void update(MyUser...myUser){
        userQuery.update(myUser);
    }

    public void deleteById(int id){
        userQuery.delete(id);
    }

    public UserRepository(MyUserQuery userQuery) {
        this.userQuery = userQuery;
    }

}