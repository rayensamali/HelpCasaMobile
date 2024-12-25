package com.example.helpcasamobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private GeoPoint selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().setUserAgentValue(getPackageName());
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.mapView);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);

        // Set the initial view to a default location
        mapView.getController().setZoom(12.0);
        mapView.getController().setCenter(new GeoPoint(36.8065, 10.1815)); // Tunis

        // Add a touch listener to detect map clicks
        mapView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                // Get the GeoPoint of the click location
                selectedLocation = (GeoPoint) mapView.getProjection().fromPixels((int) event.getX(), (int) event.getY());

                // Clear previous markers
                mapView.getOverlays().clear();

                // Create and add a new marker at the clicked location
                Marker marker = new Marker(mapView);
                marker.setPosition(selectedLocation);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                mapView.getOverlays().add(marker);
                mapView.invalidate();

                // Show a Toast with the coordinates
                Toast.makeText(MapActivity.this, "Location Selected: " + selectedLocation.getLatitude() + ", " + selectedLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            }
            return true;
        });

        // Handle confirmation of location selection
        findViewById(R.id.btnConfirmLocation).setOnClickListener(v -> {
            if (selectedLocation != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude", selectedLocation.getLatitude());
                resultIntent.putExtra("longitude", selectedLocation.getLongitude());
                setResult(RESULT_OK, resultIntent);
                finish(); // Close the map activity and return result to previous activity
            } else {
                Toast.makeText(MapActivity.this, "Please select a location on the map", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
