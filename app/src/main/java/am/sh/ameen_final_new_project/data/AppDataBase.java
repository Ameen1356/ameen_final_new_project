package am.sh.ameen_final_new_project.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import am.sh.ameen_final_new_project.data.MySubjectTable.MySubject;
import am.sh.ameen_final_new_project.data.MySubjectTable.MySubjectQuery;
import am.sh.ameen_final_new_project.data.MyTaskTable.MyTask;
import am.sh.ameen_final_new_project.data.MyTaskTable.MyTaskQuery;
import am.sh.ameen_final_new_project.data.MyUserTable.MyUser;
import am.sh.ameen_final_new_project.data.MyUserTable.MyUserQuery;

/*
 * تعريف الجداول ورقم الاصدار
 * version
 * عند تغيير اي شيء يخص جدول او داول علينا تغيير رقم الاصدار يتم بناء قاعدة البيانات من جديد
 */
@Database(entities = {MyUser.class, MySubject.class, MyTask.class}, version = 1)
/**
 * الفئة المسؤولة عن بناء قاعدة البيانات بكل جداولها
 * وتوفر لنا كائن للتعامل مع قاعدة البيانات
 */
public abstract class AppDataBase extends RoomDatabase {

    /**
     * كائن للتعامل مع قاعدة البيانات
     */
    private static AppDataBase db;

    /**
     * يعيد كائن لعمليات جدول المستعملين
     * @return
     */
    public abstract MyUserQuery getMyUserQuery();

    /**
     * يعيد كائن لعمليات جدول المواضيع
     * @return
     */
    public abstract MySubjectQuery getMySubjectQuery();

    /**
     * يعيد كائن لعمليات جدول المهمات
     * @return
     */
    public abstract MyTaskQuery getMyTaskQuery();

    /**
     * بناء قاعدة البيانات واعادة كائن يؤشر عليها
     * @param context
     * @return
     */
    public static AppDataBase getDB(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context,
                            AppDataBase.class,
                            "samihDataBase") // اسم قاعدة البيانات
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return db;
    }
}