package am.sh.ameen_final_new_project.viewPkg;
// Import Android UI widgets, Fragment tools, ViewModel, and file streams
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

    // ViewModel holds the API data and survives screen rotations
    private PlantViewModel plantViewModel;

    // UI elements from fragment_camera.xml
    private ImageView ivPlantPhoto;
    private TextView tvPlantInfo;
    private ProgressBar progressBar;
    private Button btnCapture;
    private Button btnGallery;

    // 1. CAMERA LAUNCHER: Opens the camera app and receives a Bitmap image when a photo is taken
    private final ActivityResultLauncher<Void> takePhotoLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicturePreview(), bitmap -> {
                if (bitmap != null) {
                    // Display the captured photo in the ImageView
                    ivPlantPhoto.setImageBitmap(bitmap);

                    // Convert the Bitmap into a temporary File so the API can upload it
                    File photoFile = saveBitmapToFile(bitmap);
                    if (photoFile != null) {
                        tvPlantInfo.setText("Analyzing plant...");
                        // Send the file to the ViewModel to call the Plant.id API
                        plantViewModel.identifyPlant(photoFile);
                    }
                }
            });

    // 2. GALLERY LAUNCHER: Opens the phone's gallery and receives a Uri link to the chosen image
    private final ActivityResultLauncher<String> pickGalleryLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    // Display the selected gallery photo in the ImageView
                    ivPlantPhoto.setImageURI(uri);

                    // Copy the gallery Uri into a temporary File so the API can upload it
                    File photoFile = saveUriToFile(uri);
                    if (photoFile != null) {
                        tvPlantInfo.setText("Analyzing plant...");
                        // Send the file to the ViewModel to call the Plant.id API
                        plantViewModel.identifyPlant(photoFile);
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate (load) the XML layout for this fragment
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        // Connect Java variables to the XML view IDs
        ivPlantPhoto = view.findViewById(R.id.ivPlantPhoto);
        tvPlantInfo = view.findViewById(R.id.tvPlantInfo);
        progressBar = view.findViewById(R.id.progressBar);
        btnCapture = view.findViewById(R.id.btnCapture);
        btnGallery = view.findViewById(R.id.btnGallery);

        // Initialize the ViewModel
        plantViewModel = new ViewModelProvider(this).get(PlantViewModel.class);

        // Set button click listeners to launch the camera or open the gallery
        btnCapture.setOnClickListener(v -> takePhotoLauncher.launch(null));
        btnGallery.setOnClickListener(v -> pickGalleryLauncher.launch("image/*"));

        // Start listening for API updates from the ViewModel
        observeViewModel();

        return view;
    }

    // 3. LIVE DATA OBSERVERS: Automatically updates the UI when the API finishes
    private void observeViewModel() {
        // Show the spinning ProgressBar while loading, and hide it when done
        plantViewModel.getIsLoading().observe(getViewLifecycleOwner(), loading ->
                progressBar.setVisibility(loading ? View.VISIBLE : View.GONE));

        // Runs when the Plant.id API successfully returns plant and health results
        plantViewModel.getPlantResult().observe(getViewLifecycleOwner(), result -> {
            if (result != null && result.classification != null && !result.classification.suggestions.isEmpty()) {
                // Get the #1 top plant match and convert probability (e.g., 0.96) to a percentage (96%)
                PlantIdResponse.PlantSuggestion topPlant = result.classification.suggestions.get(0);
                int confidence = (int) (topPlant.probability * 100);

                // Get the common name if available, otherwise fall back to the scientific name
                String commonName = (topPlant.details != null && topPlant.details.commonNames != null && !topPlant.details.commonNames.isEmpty())
                        ? topPlant.details.commonNames.get(0)
                        : topPlant.name;

                // Get the general care/plant description
                String description = (topPlant.details != null && topPlant.details.description != null)
                        ? topPlant.details.description.value
                        : "No description available.";

                // Check the API's health assessment for diseases or dried leaves
                String healthSummary;
                if (result.isHealthy != null && result.isHealthy.binary) {
                    // The plant is healthy
                    healthSummary = "Status: Healthy Plant (" + (int) (result.isHealthy.probability * 100) + "%)";
                } else if (result.disease != null && result.disease.suggestions != null && !result.disease.suggestions.isEmpty()) {
                    // A problem was detected: grab the top issue, its description, and a prevention tip
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

                // Display all combined plant info and health diagnostics in the TextView
                tvPlantInfo.setText(
                        "Plant: " + commonName + "\n" +
                                "Scientific: " + topPlant.name + "\n" +
                                "Match: " + confidence + "%\n\n" +
                                "Health Assessment:\n" + healthSummary + "\n\n" +
                                "About Plant:\n" + description
                );
            }
        });

        // Runs if the network request fails (e.g., no internet or invalid API key)
        plantViewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                tvPlantInfo.setText("Could not identify plant. Try a clearer photo.");
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 4. HELPER METHOD: Compresses a camera Bitmap into a JPEG file inside the app's cache folder
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

    // 5. HELPER METHOD: Copies bytes from a gallery image Uri into a JPEG file inside the cache folder
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