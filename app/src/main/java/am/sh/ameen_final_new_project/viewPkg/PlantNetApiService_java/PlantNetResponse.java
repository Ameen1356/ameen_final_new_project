package am.sh.ameen_final_new_project.viewPkg.PlantNetApiService_java;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PlantNetResponse {
    @SerializedName("results")
    public List<Result> results;

    public static class Result {
        @SerializedName("score")
        public double score;

        @SerializedName("species")
        public Species species;
    }

    public static class Species {
        @SerializedName("scientificNameWithoutAuthor")
        public String scientificName;

        @SerializedName("commonNames")
        public List<String> commonNames;
    }
}