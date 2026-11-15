package am.sh.ameen_final_new_project.viewPkg.PlantNetApiService_java;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PlantNetApiService {

    @Multipart
    @POST("v2/identify/all")
    Call<PlantNetResponse> identifyPlant(
            @Query("api-key") String apiKey,
            @Part MultipartBody.Part image,
            @Part("organs") RequestBody organ
    );
}
