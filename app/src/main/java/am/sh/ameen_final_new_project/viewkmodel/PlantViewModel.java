package am.sh.ameen_final_new_project.viewkmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

import am.sh.ameen_final_new_project.LiveData.AppDataBase;
import am.sh.ameen_final_new_project.LiveData.MyUserTable.MyUser;
import am.sh.ameen_final_new_project.LiveData.code_for_project.MyClass_PlantCare;
import am.sh.ameen_final_new_project.LiveData.code_for_project.MyClass_PlantCare_Query;
import am.sh.ameen_final_new_project.repositories.PlantRepository;
import am.sh.ameen_final_new_project.repositories.UserRepository;

public class PlantViewModel {
    private final PlantRepository repository;//واجهة الاستعلامات
    private final LiveData<List<MyClass_PlantCare>> allplants;
    public PlantViewModel(@NonNull Application application) {
        super(application);
        repository = new PlantRepository(application);
        allplants = repository.allplants();
    }

    public PlantViewModel(PlantRepository repository, LiveData<List<MyClass_PlantCare>> allplants) {
        this.repository = repository;
        this.allplants = allplants;
    }
}
