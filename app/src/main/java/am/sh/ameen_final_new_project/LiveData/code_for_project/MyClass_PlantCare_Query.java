package am.sh.ameen_final_new_project.LiveData.code_for_project;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import am.sh.ameen_final_new_project.LiveData.MyTaskTable.MyTask;
import am.sh.ameen_final_new_project.LiveData.MyUserTable.MyUser;
import am.sh.ameen_final_new_project.LiveData.code_for_project.MyClass_PlantCare;


public interface MyClass_PlantCare_Query {
    @Query("SELECT * FROM MyClass_PlantCare ")
    LiveData<List<MyClass_PlantCare>> getAllplants();

    /**
     * ادخال مهمات
     * @param p مجموعة مهمات
     */
    @Insert
    void insert(MyClass_PlantCare... p); // ثلاثة نقاط تعني مجموعة

    /**
     * تعديل المهمات
     * @param p
     */
    @Update
    void update(MyClass_PlantCare... p);

    /**
     * حذف مهمة او مهمات
     */
    @Query("DELETE FROM MyClass_PlantCare WHERE plant_id=:plantid")
    void deleteplant(long plantid);

    @Query("SELECT * FROM MyClass_PlantCare WHERE plant_id=:plantid ")
    LiveData<List<MyClass_PlantCare>> getPlantssByPlantId(long plantid);

    @Query("SELECT * FROM MyClass_PlantCare WHERE sunRequirement = :sunRequirement LIMIT 1")
    LiveData<List<MyClass_PlantCare>> checksunRequirement(String sunRequirement);

    @Query("SELECT * FROM MyClass_PlantCare WHERE waterFrequencyDays = :waterFrequencyDays LIMIT 1")
    LiveData<List<MyClass_PlantCare>> checkwaterFrequencyDays(int waterFrequencyDays);

}
