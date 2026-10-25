package am.sh.ameen_final_new_project.viewkmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import am.sh.ameen_final_new_project.LiveData.code_for_project.MyClass_PlantCare;
import am.sh.ameen_final_new_project.repositories.PlantRepository;

public class PlantViewModel extends AndroidViewModel {
    private final PlantRepository repository;//واجهة الاستعلامات
    private final LiveData<List<MyClass_PlantCare>> allplants;
    public PlantViewModel(@NonNull Application application) {
        super(application);
        repository = new PlantRepository(application);
        allplants = repository.getAllplants();
    }
    public void insert(MyClass_PlantCare... plantCares)
    {
        repository.insert(plantCares);
    }
    public void update(MyClass_PlantCare... plantCares)
    {
        repository.update(plantCares);
    }
    public void deleteTaskById(long plantid)
    {
        repository.deleteplant(plantid);
    }
    public LiveData<List<MyClass_PlantCare>> getPlantssByPlantId(long plantid)
    {
        return repository.getPlantssByPlantId(plantid);
    }
    public LiveData<List<MyClass_PlantCare>> checksunRequirement(String sunRequirment)
    {
        return repository.checksunRequirement(sunRequirment);
    }
    public LiveData<List<MyClass_PlantCare>> checkwaterFrequencyDays(int waterRequirment)
    {
        return repository.checkwaterFrequencyDays(waterRequirment);
    }


}
