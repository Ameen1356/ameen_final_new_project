package am.sh.ameen_final_new_project.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import am.sh.ameen_final_new_project.model.AppDataBase;
import java.io.File;

import am.sh.ameen_final_new_project.viewPkg.PlantNetApiService_java.PlantNetApiService;
import am.sh.ameen_final_new_project.viewPkg.PlantNetApiService_java.PlantNetResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import am.sh.ameen_final_new_project.model.code_for_project.MyClass_PlantCare;
import am.sh.ameen_final_new_project.model.code_for_project.MyClass_PlantCare_Query;

public class PlantRepository {
    private  MyClass_PlantCare_Query plantQuery;//واجهة الاستعلامات

    public LiveData<List<MyClass_PlantCare>> allplants;
    public PlantRepository(Application application)
    {
        AppDataBase db = AppDataBase.getDB(application);
        plantQuery = db.getMyClass_PlantCareQuery();
        allplants=plantQuery.getAllplants();
    }
    public void insert(MyClass_PlantCare... plantCares)
    {
        plantQuery.insert(plantCares);
    }
    public void update(MyClass_PlantCare... plantCares)
    {
        plantQuery.update(plantCares);
    }

    public LiveData<List<MyClass_PlantCare>> getPlantssByPlantId(long plantid)
    {
        return plantQuery.getPlantssByPlantId(plantid);
    }
    public LiveData<List<MyClass_PlantCare>> checksunRequirement(String sunRequirment)
    {
        return plantQuery.checksunRequirement(sunRequirment);
    }
    public LiveData<List<MyClass_PlantCare>> checkwaterFrequencyDays(int waterRequirment)
    {
        return plantQuery.checkwaterFrequencyDays(waterRequirment);
    }

    public PlantRepository(MyClass_PlantCare_Query plantQuery)
    {
        this.plantQuery = plantQuery;
    }

    public LiveData<List<MyClass_PlantCare>> getAllplants() {
        return allplants;
    }

    public void deleteplant(long plantid)
    {
        plantQuery.deleteplant(plantid);
    }

    private static final String BASE_URL = "https://my-api.plantnet.org/";
    private static final String API_KEY = "YOUR_PLANTNET_API_KEY";
    private  PlantNetApiService apiService;

    public PlantRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(PlantNetApiService.class);
    }

    public void identifyPlantImage(File imageFile, OnResultCallback callback) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("images", imageFile.getName(), requestFile);
        RequestBody organPart = RequestBody.create(MediaType.parse("text/plain"), "auto");

        apiService.identifyPlant(API_KEY, imagePart, organPart).enqueue(new Callback<PlantNetResponse>() {
            @Override
            public void onResponse(Call<PlantNetResponse> call, Response<PlantNetResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().results.isEmpty()) {
                    PlantNetResponse.Result topMatch = response.body().results.get(0);
                    callback.onSuccess(topMatch);
                } else {
                    callback.onError("Could not identify plant.");
                }
            }

            @Override
            public void onFailure(Call<PlantNetResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface OnResultCallback {
        void onSuccess(PlantNetResponse.Result result);
        void onError(String message);
    }
}
