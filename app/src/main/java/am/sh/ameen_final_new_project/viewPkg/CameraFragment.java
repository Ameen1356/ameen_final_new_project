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
import am.sh.ameen_final_new_project.model.code_for_project.LanguageHelper;
import am.sh.ameen_final_new_project.model.code_for_project.PlantIdResponse;
import am.sh.ameen_final_new_project.viewkmodel.PlantViewModel;

public class CameraFragment extends Fragment {

    private PlantViewModel plantViewModel;
    private ImageView ivPlantPhoto;
    private TextView tvPlantInfo;
    private ProgressBar progressBar;
    private Button btnCapture;
    private Button btnGallery;

    // Launches the device camera to capture a plant photo and sends it for identification
    private final ActivityResultLauncher<Void> takePhotoLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
                if (bitmap != null) {
                    ivPlantPhoto.setImageBitmap(bitmap);
                    File photoFile = saveBitmapToFile(bitmap);
                    if (photoFile != null) {
                        tvPlantInfo.setText(getString(R.string.analyzing_plant));
                        plantViewModel.identifyPlant(photoFile);
                    }
                }
            });

    // Opens the phone gallery so the user can pick an existing plant image for identification
    private final ActivityResultLauncher<String> pickGalleryLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    ivPlantPhoto.setImageURI(uri);
                    File photoFile = saveUriToFile(uri);
                    if (photoFile != null) {
                        tvPlantInfo.setText(getString(R.string.analyzing_plant));
                        plantViewModel.identifyPlant(photoFile);
                    }
                }
            });

    // Initializes the fragment layout, UI views, ViewModel, language button, and click listeners
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        ivPlantPhoto = view.findViewById(R.id.ivPlantPhoto);
        tvPlantInfo = view.findViewById(R.id.tvPlantInfo);
        progressBar = view.findViewById(R.id.progressBar);
        btnCapture = view.findViewById(R.id.btnCapture);
        btnGallery = view.findViewById(R.id.btnGallery);
        Button btnLanguage = view.findViewById(R.id.btnLanguage);

        String currentLang = LanguageHelper.getLanguage(requireContext());
        btnLanguage.setText(currentLang.equals("ar") ? "English" : "العربية");

        btnLanguage.setOnClickListener(v -> {
            String nextLang = LanguageHelper.getLanguage(requireContext()).equals("en") ? "ar" : "en";
            LanguageHelper.setLanguage(requireContext(), nextLang);
        });

        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);

        btnCapture.setOnClickListener(v -> takePhotoLauncher.launch(null));
        btnGallery.setOnClickListener(v -> pickGalleryLauncher.launch("image/*"));

        observeViewModel();

        return view;
    }

    // Observes LiveData from PlantViewModel to show loading state, danger alerts, or localized plant info and care instructions
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

                String plantHeader = getString(R.string.label_plant, plantName, confidence);

                // 2. CHECK IF PLANT IS DANGEROUS / TOXIC (English & Arabic)
                String rawToxicity = null;
                if (topPlant.details != null) {
                    if (topPlant.details.toxicity != null && !topPlant.details.toxicity.trim().isEmpty()) {
                        rawToxicity = topPlant.details.toxicity.trim();
                    } else if (topPlant.details.description != null && topPlant.details.description.value != null) {
                        String desc = topPlant.details.description.value;
                        String lowerDesc = desc.toLowerCase();
                        if (lowerDesc.contains("toxic") || lowerDesc.contains("poison") || desc.contains("سام")) {
                            rawToxicity = desc;
                        }
                    }
                }

                // IF DANGEROUS: Show ONLY the warning, short info, and first-aid steps (skip growing/care info)
                if (rawToxicity != null) {
                    String shortDangerInfo = shortenText(rawToxicity);

                    tvPlantInfo.setText(
                            plantHeader + "\n\n" +
                                    getString(R.string.danger_header) + "\n" +
                                    shortDangerInfo + "\n\n" +
                                    getString(R.string.danger_what_to_do)
                    );
                    return;
                }

                // 3. SAFE PLANT: Short summary info about the plant
                String aboutPlant = "";
                if (topPlant.details != null && topPlant.details.description != null && topPlant.details.description.value != null) {
                    aboutPlant = getString(R.string.label_about) + "\n" + shortenText(topPlant.details.description.value.trim()) + "\n\n";
                }

                // 4. HOW MANY TIMES TO WATER
                String wateringInfo;
                if (topPlant.details != null && topPlant.details.bestWatering != null) {
                    wateringInfo = topPlant.details.bestWatering;
                } else if (topPlant.details != null && topPlant.details.watering != null) {
                    int level = topPlant.details.watering.max;
                    if (level == 1) {
                        wateringInfo = getString(R.string.water_low);
                    } else if (level == 3) {
                        wateringInfo = getString(R.string.water_high);
                    } else {
                        wateringInfo = getString(R.string.water_medium);
                    }
                } else {
                    wateringInfo = getString(R.string.water_medium);
                }

                // 5. WHERE TO PLACE IT (SUNLIGHT)
                String sunlightInfo = (topPlant.details != null && topPlant.details.bestLightCondition != null)
                        ? topPlant.details.bestLightCondition
                        : getString(R.string.sunlight_default);

                // 6. HEALTH STATUS, PROBLEM & HOW TO FIX IT
                String healthSection;
                if (result.isHealthy != null && result.isHealthy.binary) {
                    healthSection = getString(R.string.health_healthy, (int) (result.isHealthy.probability * 100));
                } else if (result.disease != null && result.disease.suggestions != null && !result.disease.suggestions.isEmpty()) {
                    PlantIdResponse.DiseaseSuggestion topIssue = result.disease.suggestions.get(0);
                    int issueConfidence = (int) (topIssue.probability * 100);

                    String fixStep = getString(R.string.health_fix_default);
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

                    healthSection = getString(R.string.health_problem, topIssue.name, issueConfidence, fixStep);
                } else {
                    healthSection = getString(R.string.health_unclear);
                }

                tvPlantInfo.setText(
                        plantHeader + "\n\n" +
                                aboutPlant +
                                getString(R.string.label_watering) + "\n" + wateringInfo + "\n\n" +
                                getString(R.string.label_place) + "\n" + sunlightInfo + "\n\n" +
                                healthSection
                );
            }
        });

        plantViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                tvPlantInfo.setText(getString(R.string.identify_error));
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Trims descriptions to a consistent 2-sentence summary (max ~240 characters) for both toxic and safe plants
    private String shortenText(String text) {
        if (text == null || text.isEmpty()) return "";

        int firstPeriod = text.indexOf('.');
        if (firstPeriod > 0 && firstPeriod < 220) {
            int secondPeriod = text.indexOf('.', firstPeriod + 1);
            if (secondPeriod > 0 && secondPeriod < 260) {
                return text.substring(0, secondPeriod + 1).trim();
            }
            return text.substring(0, firstPeriod + 1).trim();
        }

        if (text.length() > 240) {
            return text.substring(0, 237).trim() + "...";
        }
        return text.trim();
    }

    // Compresses the captured camera Bitmap into a temporary JPEG file in cache so Retrofit can upload it
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

    // Copies the selected gallery image from its content Uri into a temporary cache file for uploading
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