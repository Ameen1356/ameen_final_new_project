package am.sh.ameen_final_new_project.data.MySubjectTable;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MySubjectQuery {
    /**
     * اعادة جميع معطيات جدول المواضيع
     * @return قائمة من المواضيع
     */
    @Query("SELECT * FROM MySubject")
    List<MySubject> getAllSubjects();

    /**
     * ادخال مهمات
     * @param s مجموعة مهمات
     */
    @Insert
    void insert(MySubject... s); // ثلاثة نقاط تعني مجموعة

    /**
     * تعديل المهمات
     * @param s
     */
    @Update
    void update(MySubject... s);

    /**
     * حذف مهمة او مهمات
     * @param s حذف المهمات (حسب المفتاح الرئيسي)
     */
    @Delete
    void deleteTask(MySubject... s);

    @Query("DELETE FROM MySubject WHERE key_id=:keyid")
    void delete(long keyid);

    @Query("SELECT * From MySubject WHERE title=:sub")
    MySubject checkSubject(String sub);
}