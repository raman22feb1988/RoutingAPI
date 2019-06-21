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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.util.Log;
import android.view.Menu;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.SupportMapFragment;
import com.here.android.mpa.search.AutoSuggest;
import com.here.android.mpa.search.AutoSuggestPlace;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.TextAutoSuggestionRequest;

public class BasicMapActivity2 extends FragmentActivity implements AsyncResponse3 {
    private static final String LOG_TAG = BasicMapActivity2.class.getSimpleName();

    PopupWindow popupWindow;
    MapMarker defaultMarker2;

    // permissions request code
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    // map embedded in the map fragment
    private Map map2 = null;

    private List<AutoSuggest> suggestions = new ArrayList<AutoSuggest>();

    // map fragment embedded in this activity
    private SupportMapFragment mapFragment2 = null;

    double a;
    double b;
    double c;
    double d;
    double latitude;
    double longitude;
    boolean updated;

    double latwp[];
    double longwp[];

    String default1;
    String default2;

    Button b2;
    Button g2;
    EditText e2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();

        b2 = findViewById(R.id.ok2);
        g2 = findViewById(R.id.reset2);
        e2 = findViewById(R.id.search2);

        Intent intent4 = getIntent();
        a = intent4.getDoubleExtra("a", 12.9618);
        b = intent4.getDoubleExtra("b", 80.2382);
        c = intent4.getDoubleExtra("c", 13.121);
        d = intent4.getDoubleExtra("d", 80.225);
        latitude = intent4.getDoubleExtra("latitude", 12.9618);
        longitude = intent4.getDoubleExtra("longitude", 80.2382);
        updated = intent4.getBooleanExtra("updated", true);

        latwp = intent4.getDoubleArrayExtra("latwp");
        longwp = intent4.getDoubleArrayExtra("longwp");

        default1 = intent4.getStringExtra("default1");
        default2 = intent4.getStringExtra("default2");

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
                intent6.putExtra("latwp", latwp);
                intent6.putExtra("longwp", longwp);
                intent6.putExtra("foremost", 2);
                intent6.putExtra("default1", default1);
                intent6.putExtra("default2", default2);
                startActivity(intent6);
                finish();
            }
        });

        g2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = latitude;
                d = longitude;

                map2.setCenter(new GeoCoordinate(c, d, 0.0),
                        Map.Animation.NONE);
                defaultMarker2.setCoordinate(new GeoCoordinate(c, d, 0.0));
            }
        });

        e2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow != null) {
                    popupWindow.dismiss();
                }

                if((e2.getText()).length() > 0) {
                    AutoSuggestionQueryListener2 listener = new AutoSuggestionQueryListener2();
                    listener.delegate = BasicMapActivity2.this;
                    TextAutoSuggestionRequest request = new TextAutoSuggestionRequest((e2.getText()).toString()).setSearchCenter(map2.getCenter());
                    request.execute(listener);
                }
            }
        });

        e2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // you can call or do what you want with your EditText here
                // yourEditText...
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(popupWindow != null) {
                    popupWindow.dismiss();
                }

                if((e2.getText()).length() > 0) {
                    AutoSuggestionQueryListener2 listener = new AutoSuggestionQueryListener2();
                    listener.delegate = BasicMapActivity2.this;
                    TextAutoSuggestionRequest request = new TextAutoSuggestionRequest((e2.getText()).toString()).setSearchCenter(map2.getCenter());
                    request.execute(listener);
                }
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
        intent9.putExtra("latwp", latwp);
        intent9.putExtra("longwp", longwp);
        intent9.putExtra("foremost", 2);
        intent9.putExtra("default1", default1);
        intent9.putExtra("default2", default2);
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

                    defaultMarker2 = new MapMarker();
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

    public PopupWindow popupWindow() {
        final PopupWindow popupWindow = new PopupWindow(this);
        ListView listView = new ListView(this);
        List<String> suggestlist = new ArrayList<String>();
        final List<Double> latitudelist = new ArrayList<Double>();
        final List<Double> longitudelist = new ArrayList<Double>();
        for(AutoSuggest item : suggestions) {
            if(suggestlist.size() < 5 && item instanceof AutoSuggestPlace) {
                AutoSuggestPlace itemPlace = (AutoSuggestPlace) item;
                suggestlist.add(item.getTitle());
                latitudelist.add((itemPlace.getPosition()).getLatitude());
                longitudelist.add((itemPlace.getPosition()).getLongitude());
            }
        }
        listView.setAdapter(myAdapter(suggestlist));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
                c = latitudelist.get(arg2);
                d = longitudelist.get(arg2);
                defaultMarker2.setCoordinate(new GeoCoordinate(c, d, 0.0));
                map2.setCenter(new GeoCoordinate(c, d, 0.0),
                        Map.Animation.NONE);
                if(popupWindow != null) {
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.setContentView(listView);
        return popupWindow;
    }

    private ArrayAdapter<String> myAdapter(List<String> suggestarray) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, suggestarray);
        return adapter;
    }

    @Override
    public void processFinish(List<AutoSuggest> output1, String output2) {
        if(popupWindow != null) {
            popupWindow.dismiss();
        }

        suggestions = output1;
        popupWindow = popupWindow();
        popupWindow.showAsDropDown(e2, -5, 0);
    }
}

class AutoSuggestionQueryListener2 implements ResultListener<List<AutoSuggest>> {
    public AsyncResponse3 delegate = null;

    private List<AutoSuggest> data;
    private ErrorCode error;

    @Override
    public void onCompleted(List<AutoSuggest> data, ErrorCode error) {
        this.data = data;
        this.error = error;
        if (error != ErrorCode.NONE) {
            // Handle error
            delegate.processFinish(data, error.toString());
        } else {
            // Process result data
            /*
             * From the start object, we retrieve the address and display to the screen.
             * Please refer to HERE Android SDK doc for other supported APIs.
             */
            delegate.processFinish(data, error.toString());
        }
    }
}