package am.sh.ameen_final_new_project.LiveData.code_for_project;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class MyClass_PlantCare {
    @PrimaryKey(autoGenerate = true)
    public long plant_id;
    /*
    says what the problem is and gives care Instructions to it
     */
    public String careInstructions;
    /*
    gives reminder to when the person should water the plant
    (for some plants its hours and for some its days depends on the plant)
     */
    public int waterFrequencyDays;
    /*
    gives reminders or instruction to put the plant in a place with sunlight or to remove the plant from the place
    (depends on the plant)
     */
    public String sunRequirement;
    public long getPlant_id() {
        return plant_id;
    }

    public void setPlant_id(long plant_id) {
        this.plant_id = plant_id;
    }

    public String getCareInstructions() {
        return careInstructions;
    }

    public void setCareInstructions(String careInstructions) {
        this.careInstructions = careInstructions;
    }

    public int getWaterFrequencyDays() {
        return waterFrequencyDays;
    }

    public void setWaterFrequencyDays(int waterFrequencyDays) {
        this.waterFrequencyDays = waterFrequencyDays;
    }

    public String getSunRequirement() {
        return sunRequirement;
    }

    public void setSunRequirement(String sunRequirement) {
        this.sunRequirement = sunRequirement;
    }

    @Override
    public String toString() {
        return "MyClass_PlantCare{" +
                "plant_id=" + plant_id +
                ", careInstructions='" + careInstructions + '\'' +
                ", waterFrequencyDays=" + waterFrequencyDays +
                ", sunRequirement='" + sunRequirement + '\'' +
                '}';
    }
}
