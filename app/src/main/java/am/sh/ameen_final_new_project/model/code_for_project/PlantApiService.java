package am.sh.ameen_final_new_project.model.code_for_project;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PlantApiService {

    @POST("api/v3/identification")
    Call<PlantIdResponse> identifyAndCheckHealth(
            @Header("Api-Key") String apiKey,
            @Query("details") String details,
            @Body PlantIdRequest requestBody
    );
}