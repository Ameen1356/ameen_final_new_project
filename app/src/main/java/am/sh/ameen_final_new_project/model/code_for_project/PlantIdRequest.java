package am.sh.ameen_final_new_project.model.code_for_project;
import java.util.Collections;
import java.util.List;

public class PlantIdRequest {
    public List<String> images;
    public String health = "all";

    public PlantIdRequest(String base64Image) {
        this.images = Collections.singletonList(base64Image);
    }
}