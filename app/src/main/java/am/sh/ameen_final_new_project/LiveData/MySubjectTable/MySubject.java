package am.sh.ameen_final_new_project.LiveData.MySubjectTable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MySubject
{
    @PrimaryKey(autoGenerate = true)
    public long key_id;

    public String title;

    public String getTitle() {
        return title;
    }

    public long getKey_id() {
        return key_id;
    }

    public void setKey_id(long key_id) {
        this.key_id = key_id;
    }

    public void setTitle(String math) {

    }

    @Override
    public String toString() {
        return "MySubject{" +
                "key_id=" + key_id +
                ", title='" + title + '\'' +
                '}';
    }
}