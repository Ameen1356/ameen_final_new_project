package am.sh.ameen_final_new_project.viewPkg;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import am.sh.ameen_final_new_project.R;
import am.sh.ameen_final_new_project.model.code_for_project.PlantIdResponse;
import am.sh.ameen_final_new_project.viewkmodel.PlantViewModel;

public class CameraFragment extends Fragment {

    private PlantViewModel plantViewModel;
    private ImageView ivPlantPhoto;
    private TextView tvPlantInfo;
    private ProgressBar progressBar;
    private Button btnCapture;
    private Button btnGallery;

    private final ActivityResultLauncher<Void> takePhotoLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
                if (bitmap != null) {
                    ivPlantPhoto.setImageBitmap(bitmap);
                    File photoFile = saveBitmapToFile(bitmap);
                    if (photoFile != null) {
                        tvPlantInfo.setText("Analyzing plant...");
                        plantViewModel.identifyPlant(photoFile);
                    }
                }
            });

    private final ActivityResultLauncher<String> pickGalleryLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    ivPlantPhoto.setImageURI(uri);
                    File photoFile = saveUriToFile(uri);
                    if (photoFile != null) {
                        tvPlantInfo.setText("Analyzing plant...");
                        plantViewModel.identifyPlant(photoFile);
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        ivPlantPhoto = view.findViewById(R.id.ivPlantPhoto);
        tvPlantInfo = view.findViewById(R.id.tvPlantInfo);
        progressBar = view.findViewById(R.id.progressBar);
        btnCapture = view.findViewById(R.id.btnCapture);
        btnGallery = view.findViewById(R.id.btnGallery);

        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);

        btnCapture.setOnClickListener(v -> takePhotoLauncher.launch(null));
        btnGallery.setOnClickListener(v -> pickGalleryLauncher.launch("image/*"));

        observeViewModel();

        return view;
    }

    private void observeViewModel() {
        plantViewModel.getIsLoading().observe(getViewLifecycleOwner(), loading ->
                progressBar.setVisibility(loading ? View.VISIBLE : View.GONE));

        plantViewModel.getPlantResult().observe(getViewLifecycleOwner(), response -> {
            if (response != null
                    && response.classification != null
                    && response.classification.suggestions != null
                    && !response.classification.suggestions.isEmpty()) {

                PlantIdResponse.Result result = response;

                // 1. PLANT NAME & MATCH PERCENTAGE
                PlantIdResponse.PlantSuggestion topPlant = result.classification.suggestions.get(0);
                int confidence = (int) (topPlant.probability * 100);
                String plantName = (topPlant.details != null && topPlant.details.commonNames != null && !topPlant.details.commonNames.isEmpty())
                        ? topPlant.details.commonNames.get(0)
                        : topPlant.name;

                // 2. HOW MANY TIMES TO WATER
                String wateringInfo;
                if (topPlant.details != null && topPlant.details.bestWatering != null) {
                    wateringInfo = topPlant.details.bestWatering;
                } else if (topPlant.details != null && topPlant.details.watering != null) {
                    int level = topPlant.details.watering.max;
                    if (level == 1) {
                        wateringInfo = "Low — water every 2 to 3 weeks (let soil dry completely).";
                    } else if (level == 3) {
                        wateringInfo = "High — water 2 to 3 times a week (keep soil moist).";
                    } else {
                        wateringInfo = "Medium — water about once a week (when top inch of soil feels dry).";
                    }
                } else {
                    wateringInfo = "Water about once a week when the top inch of soil feels dry.";
                }

                // 3. WHERE TO PLACE IT (SUNLIGHT)
                String sunlightInfo = (topPlant.details != null && topPlant.details.bestLightCondition != null)
                        ? topPlant.details.bestLightCondition
                        : "Place in bright, indirect sunlight near a window.";

                // 4. HEALTH STATUS, PROBLEM & HOW TO FIX IT
                String healthSection;
                if (result.isHealthy != null && result.isHealthy.binary) {
                    healthSection = "Health: Healthy (" + (int) (result.isHealthy.probability * 100) + "%) — No problems found!";
                } else if (result.disease != null && result.disease.suggestions != null && !result.disease.suggestions.isEmpty()) {
                    PlantIdResponse.DiseaseSuggestion topIssue = result.disease.suggestions.get(0);
                    String problemName = topIssue.name + " (" + (int) (topIssue.probability * 100) + "% match)";

                    String fixStep = "Remove damaged leaves and adjust watering.";
                    if (topIssue.details != null && topIssue.details.treatment != null) {
                        if (topIssue.details.treatment.biological != null && !topIssue.details.treatment.biological.isEmpty()) {
                            fixStep = topIssue.details.treatment.biological.get(0);
                        } else if (topIssue.details.treatment.prevention != null && !topIssue.details.treatment.prevention.isEmpty()) {
                            fixStep = topIssue.details.treatment.prevention.get(0);
                        } else if (topIssue.details.treatment.chemical != null && !topIssue.details.treatment.chemical.isEmpty()) {
                            fixStep = topIssue.details.treatment.chemical.get(0);
                        }
                    } else if (topIssue.details != null && topIssue.details.description != null) {
                        fixStep = topIssue.details.description;
                    }

                    healthSection = "Health: Problem Detected!\n" +
                            "• Problem: " + problemName + "\n" +
                            "• How to fix it: " + fixStep;
                } else {
                    healthSection = "Health: Unclear — check leaves for dryness, yellowing, or pests.";
                }

                tvPlantInfo.setText(
                        "Plant: " + plantName + " (" + confidence + "% match)\n\n" +
                                "Watering:\n" + wateringInfo + "\n\n" +
                                "Where to Place It:\n" + sunlightInfo + "\n\n" +
                                healthSection
                );
            }
        });

        plantViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                tvPlantInfo.setText("Could not identify plant. Try a clearer photo.");
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File saveBitmapToFile(Bitmap bitmap) {
        try {
            File file = new File(requireContext().getCacheDir(), "captured_plant.jpg");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private File saveUriToFile(Uri uri) {
        try {
            File file = new File(requireContext().getCacheDir(), "gallery_plant.jpg");
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}