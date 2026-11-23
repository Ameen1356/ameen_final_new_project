package am.sh.ameen_final_new_project.model.code_for_project;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PlantIdResponse {

    @SerializedName("result")
    public Result result;

    public static class Result {
        @SerializedName("classification")
        public Classification classification;

        @SerializedName("is_healthy")
        public IsHealthy isHealthy;

        @SerializedName("disease")
        public Disease disease;
    }

    public static class Classification {
        @SerializedName("suggestions")
        public List<PlantSuggestion> suggestions;
    }

    public static class PlantSuggestion {
        @SerializedName("name")
        public String name;

        @SerializedName("probability")
        public double probability;

        @SerializedName("details")
        public PlantDetails details;
    }

    public static class PlantDetails {
        @SerializedName("common_names")
        public List<String> commonNames;

        @SerializedName("description")
        public Description description;

        @SerializedName("watering")
        public Watering watering;

        @SerializedName("best_watering")
        public String bestWatering;

        @SerializedName("best_light_condition")
        public String bestLightCondition;
    }

    public static class Description {
        @SerializedName("value")
        public String value;
    }

    public static class Watering {
        @SerializedName("max")
        public int max;

        @SerializedName("min")
        public int min;
    }

    public static class IsHealthy {
        @SerializedName("binary")
        public boolean binary;

        @SerializedName("probability")
        public double probability;
    }

    public static class Disease {
        @SerializedName("suggestions")
        public List<DiseaseSuggestion> suggestions;
    }

    public static class DiseaseSuggestion {
        @SerializedName("name")
        public String name;

        @SerializedName("probability")
        public double probability;

        @SerializedName("details")
        public DiseaseDetails details;
    }

    public static class DiseaseDetails {
        @SerializedName("description")
        public String description;

        @SerializedName("treatment")
        public Treatment treatment;
    }

    public static class Treatment {
        @SerializedName("biological")
        public List<String> biological;

        @SerializedName("chemical")
        public List<String> chemical;

        @SerializedName("prevention")
        public List<String> prevention;
    }
}