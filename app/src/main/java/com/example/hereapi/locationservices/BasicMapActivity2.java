/*
 * Copyright (c) 2011-2019 HERE Global B.V. and its affiliate(s).
 * All rights reserved.
 * The use of this software is conditional upon having a separate agreement
 * with a HERE company for the use or utilization of this software. In the
 * absence of such agreement, the use of the software is not allowed.
 */

package com.example.hereapi.locationservices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.Menu;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.SupportMapFragment;

public class BasicMapActivity2 extends FragmentActivity {
    private static final String LOG_TAG = BasicMapActivity1.class.getSimpleName();

    // permissions request code
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    // map embedded in the map fragment
    private Map map2 = null;

    // map fragment embedded in this activity
    private SupportMapFragment mapFragment2 = null;

    double a;
    double b;
    double c;
    double d;
    double latitude;
    double longitude;
    boolean updated;

    Button b2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();

        b2 = findViewById(R.id.ok2);

        Intent intent4 = getIntent();
        a = intent4.getDoubleExtra("a", 12.9618);
        b = intent4.getDoubleExtra("b", 80.2382);
        c = intent4.getDoubleExtra("c", 13.121);
        d = intent4.getDoubleExtra("d", 80.225);
        latitude = intent4.getDoubleExtra("latitude", 12.9618);
        longitude = intent4.getDoubleExtra("longitude", 80.2382);
        updated = intent4.getBooleanExtra("updated", true);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6 = new Intent(BasicMapActivity2.this, RoutingActivity.class);
                intent6.putExtra("a", a);
                intent6.putExtra("b", b);
                intent6.putExtra("c", c);
                intent6.putExtra("d", d);
                intent6.putExtra("latitude", latitude);
                intent6.putExtra("longitude", longitude);
                intent6.putExtra("updated", updated);
                intent6.putExtra("foremost", 2);
                startActivity(intent6);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent9 = new Intent(BasicMapActivity2.this, RoutingActivity.class);
        intent9.putExtra("a", a);
        intent9.putExtra("b", b);
        intent9.putExtra("c", c);
        intent9.putExtra("d", d);
        intent9.putExtra("latitude", latitude);
        intent9.putExtra("longitude", longitude);
        intent9.putExtra("updated", updated);
        intent9.putExtra("foremost", 2);
        startActivity(intent9);
        finish();
    }

    private SupportMapFragment getSupportMapFragment3() {
        return (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment3);
    }

    private void initialize() {
        setContentView(R.layout.end);

        // Search for the map fragment to finish setup by calling init().
        mapFragment2 = getSupportMapFragment3();
        mapFragment2.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // retrieve a reference of the map from the map fragment
                    map2 = mapFragment2.getMap();
                    // Set the map center to the GPS region (no animation)
                    map2.setCenter(new GeoCoordinate(c, d, 0.0),
                            Map.Animation.NONE);
                    // Set the zoom level to the average between min and max
                    map2.setZoomLevel((map2.getMaxZoomLevel() + map2.getMinZoomLevel()) / 2);

                    MapMarker defaultMarker2 = new MapMarker();
                    defaultMarker2.setCoordinate(new GeoCoordinate(c, d, 0.0));
                    defaultMarker2.setDraggable(true);
                    map2.addMapObject(defaultMarker2);

                    mapFragment2.setMapMarkerDragListener(new MapMarker.OnDragListener() {
                        @Override
                        public void onMarkerDrag(MapMarker mapMarker) {
                        }

                        @Override
                        public void onMarkerDragEnd(MapMarker mapMarker) {
                            GeoCoordinate c2 = mapMarker.getCoordinate();
                            c = c2.getLatitude();
                            d = c2.getLongitude();
                        }

                        @Override
                        public void onMarkerDragStart(MapMarker mapMarker) {
                        }
                    });
                } else {
                    Log.e(LOG_TAG, "Cannot initialize SupportMapFragment (" + error + ")");
                }
            }
        });
    }

    /**
     * Checks the dynamically controlled permissions and requests missing permissions from end user.
     */
    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<String>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // all permissions were granted
                initialize();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_routing, menu);
        return true;
    }
}