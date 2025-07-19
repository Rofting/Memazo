package com.svalero.memazo.activity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.svalero.memazo.R;
import com.svalero.memazo.contract.MapContract;
import com.svalero.memazo.domain.Publication;
import com.svalero.memazo.presenter.MapPresenter;

import java.util.List;

public class MapActivity extends AppCompatActivity implements Style.OnStyleLoaded, MapContract.View {

    private MapView mapView;
    private MapboxMap mapboxMap;
    private PointAnnotationManager pointAnnotationManager;
    private MapContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = findViewById(R.id.mapView);
        presenter = new MapPresenter(this);

        mapboxMap = mapView.getMapboxMap();
        mapboxMap.loadStyleUri(Style.MAPBOX_STREETS, this);
    }

    @Override
    public void onStyleLoaded(@NonNull Style style) {
        initializePointAnnotationManager();
        presenter.loadPublications();
    }

    private void initializePointAnnotationManager() {
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        AnnotationConfig annotationConfig = new AnnotationConfig();
        pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, annotationConfig);
    }

    @Override
    public void showPublicationsOnMap(List<Publication> publications) {
        if (pointAnnotationManager == null) return;
        pointAnnotationManager.deleteAll();

        Drawable drawable = AppCompatResources.getDrawable(this, R.drawable.ic_map_marker);
        Bitmap markerIcon = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(markerIcon);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        // BUCLE CORREGIDO
        for (Publication publication : publications) {
            if (publication.getLatitude() == 0.0 && publication.getLongitude() == 0.0) {
                continue;
            }

            Point point = Point.fromLngLat(publication.getLongitude(), publication.getLatitude());
            PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                    .withPoint(point)
                    .withIconImage(markerIcon);
            pointAnnotationManager.create(pointAnnotationOptions);
        }

        CameraOptions cameraPosition = new CameraOptions.Builder()
                .center(Point.fromLngLat(-0.8891, 41.6488)) // Zaragoza
                .zoom(11.0)
                .build();
        mapboxMap.setCamera(cameraPosition);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}