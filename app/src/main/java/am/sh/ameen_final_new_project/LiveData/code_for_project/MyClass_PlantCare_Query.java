package am.sh.ameen_final_new_project.LiveData.code_for_project;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import am.sh.ameen_final_new_project.LiveData.code_for_project.MyClass_PlantCare;


public interface MyClass_PlantCare_Query {
    @Query("SELECT * FROM MyClass_PlantCare ")
    List<MyClass_PlantCare> getAllSubjects();

    /**
     * ادخال مهمات
     * @param s مجموعة مهمات
     */
    @Insert
    void insert(MyClass_PlantCare... s); // ثلاثة نقاط تعني مجموعة

    /**
     * تعديل المهمات
     * @param s
     */
    @Update
    void update(MyClass_PlantCare... s);

    /**
     * حذف مهمة او مهمات
     * @param s حذف المهمات (حسب المفتاح الرئيسي)
     */
    @Delete
    void deleteTask(MyClass_PlantCare... s);

}
