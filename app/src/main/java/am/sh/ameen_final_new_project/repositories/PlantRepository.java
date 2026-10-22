package am.sh.ameen_final_new_project.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import am.sh.ameen_final_new_project.LiveData.AppDataBase;

import am.sh.ameen_final_new_project.LiveData.MyTaskTable.MyTask;
import am.sh.ameen_final_new_project.LiveData.code_for_project.MyClass_PlantCare;
import am.sh.ameen_final_new_project.LiveData.code_for_project.MyClass_PlantCare_Query;

public class PlantRepository {
    private  MyClass_PlantCare_Query plantQuery;//واجهة الاستعلامات

    public LiveData<List<MyClass_PlantCare>> allplants;
    public PlantRepository(Application application)
    {
        AppDataBase db = AppDataBase.getDB(application);
        plantQuery = db.getMyClass_PlantCareQuery();
        allplants=plantQuery.getAllplants();
    }
    public void insert(MyClass_PlantCare... plantCares)
    {
        plantQuery.insert(plantCares);
    }
    public void update(MyClass_PlantCare... plantCares)
    {
        plantQuery.update(plantCares);
    }
    public void deleteTaskById(long plantid)
    {
        plantQuery.deleteplant(plantid);
    }
    public LiveData<List<MyClass_PlantCare>> getPlantssByPlantId(long plantid)
    {
        return plantQuery.getPlantssByPlantId(plantid);
    }
    public LiveData<List<MyClass_PlantCare>> checksunRequirement(String sunRequirment)
    {
        return plantQuery.checksunRequirement(sunRequirment);
    }
    public LiveData<List<MyClass_PlantCare>> checkwaterFrequencyDays(int waterRequirment)
    {
        return plantQuery.checkwaterFrequencyDays(waterRequirment);
    }

    public PlantRepository(MyClass_PlantCare_Query plantQuery) {
        this.plantQuery = plantQuery;
    }

}
