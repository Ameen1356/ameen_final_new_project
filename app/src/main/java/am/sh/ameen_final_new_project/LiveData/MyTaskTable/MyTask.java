package am.sh.ameen_final_new_project.LiveData.MyTaskTable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * فئة تُمثل مهمة
 */
@Entity
public class MyTask
{
    @PrimaryKey(autoGenerate = true)
    /** رقم المهمة **/
    public long keyId;

    /** درجة الأهمية 1-5 **/
    public int importance;

    /** عنوان قصير **/
    public String shortTitle;

    /** نص المهمة **/
    public String text;

    /** زمن بناء المهمة **/
    public long time;

    /** هل تمت المهمة **/
    public boolean isCompleted;

    /** رقم موضوع المهمة **/
    public long subjId;

    /** رقم المستعمل الذي أضاف المهمة **/
    public long userId;
}