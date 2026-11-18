package am.sh.ameen_final_new_project.viewkmodel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.io.File;

import am.sh.ameen_final_new_project.model.code_for_project.PlantIdResponse;
import am.sh.ameen_final_new_project.repositories.PlantRepository;

public class PlantViewModel extends ViewModel {

    private final PlantRepository repository = new PlantRepository();

    private final MutableLiveData<PlantIdResponse.Result> plantResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public LiveData<PlantIdResponse.Result> getPlantResult() {
        return plantResult;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void identifyPlant(File imageFile) {
        isLoading.setValue(true);
        repository.identifyPlantImage(imageFile, new PlantRepository.OnResultCallback() {
            @Override
            public void onSuccess(PlantIdResponse.Result result) {
                isLoading.postValue(false);
                plantResult.postValue(result);
            }

            @Override
            public void onError(String message) {
                isLoading.postValue(false);
                errorMessage.postValue(message);
            }
        });
    }
}