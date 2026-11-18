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
        Button btnCapture = view.findViewById(R.id.btnCapture);
        Button btnGallery = view.findViewById(R.id.btnGallery);

        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);

        btnCapture.setOnClickListener(v -> takePhotoLauncher.launch(null));
        btnGallery.setOnClickListener(v -> pickGalleryLauncher.launch("image/*"));

        observeViewModel();

        return view;
    }

    private void observeViewModel() {
        plantViewModel.getIsLoading().observe(getViewLifecycleOwner(), loading ->
                progressBar.setVisibility(loading ? View.VISIBLE : View.GONE));

        plantViewModel.getPlantResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null && result.classification != null && !result.classification.suggestions.isEmpty()) {
                PlantIdResponse.PlantSuggestion topPlant = result.classification.suggestions.get(0);
                int confidence = (int) (topPlant.probability * 100);

                String commonName = (topPlant.details != null && topPlant.details.commonNames != null && !topPlant.details.commonNames.isEmpty())
                        ? topPlant.details.commonNames.get(0)
                        : topPlant.name;

                String description = (topPlant.details != null && topPlant.details.description != null)
                        ? topPlant.details.description.value
                        : "No description available.";

                String healthSummary;
                if (result.isHealthy != null && result.isHealthy.binary) {
                    healthSummary = "Status: Healthy Plant (" + (int) (result.isHealthy.probability * 100) + "%)";
                } else if (result.disease != null && result.disease.suggestions != null && !result.disease.suggestions.isEmpty()) {
                    PlantIdResponse.DiseaseSuggestion topIssue = result.disease.suggestions.get(0);
                    String issueDesc = (topIssue.details != null && topIssue.details.description != null)
                            ? topIssue.details.description
                            : "";
                    String treatmentTip = (topIssue.details != null && topIssue.details.treatment != null
                            && topIssue.details.treatment.prevention != null && !topIssue.details.treatment.prevention.isEmpty())
                            ? "\nTip: " + topIssue.details.treatment.prevention.get(0)
                            : "";
                    healthSummary = "Problem Detected: " + topIssue.name + " (" + (int) (topIssue.probability * 100) + "%)\n"
                            + issueDesc + treatmentTip;
                } else {
                    healthSummary = "Status: Check leaves for dryness or pests.";
                }

                tvPlantInfo.setText(
                        "Plant: " + commonName + "\n" +
                                "Scientific: " + topPlant.name + "\n" +
                                "Match: " + confidence + "%\n\n" +
                                "Health Assessment:\n" + healthSummary + "\n\n" +
                                "About Plant:\n" + description
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