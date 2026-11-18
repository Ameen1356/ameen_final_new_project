package am.sh.ameen_final_new_project.repositories;
import android.util.Base64;
import java.io.File;
import java.io.FileInputStream;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlantRepository {
    private static final String BASE_URL = "https://plant.id/";
    private static final String API_KEY = "YOUR_PLANT_ID_API_KEY";
    private final PlantApiService apiService;

    public PlantRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(PlantApiService.class);
    }

    public void identifyPlantImage(File imageFile, OnResultCallback callback) {
        String base64Image = encodeFileToBase64(imageFile);
        if (base64Image == null) {
            callback.onError("Failed to process image file.");
            return;
        }

        PlantIdRequest request = new PlantIdRequest(base64Image);
        String details = "common_names,description,treatment";

        apiService.identifyAndCheckHealth(API_KEY, details, request).enqueue(new Callback<PlantIdResponse>() {
            @Override
            public void onResponse(Call<PlantIdResponse> call, Response<PlantIdResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().result != null) {
                    callback.onSuccess(response.body().result);
                } else {
                    callback.onError("Could not analyze plant. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PlantIdResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    private String encodeFileToBase64(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            fis.close();
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface OnResultCallback {
        void onSuccess(PlantIdResponse.Result result);
        void onError(String message);
    }
}