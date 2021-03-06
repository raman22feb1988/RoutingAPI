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

public class BasicMapActivity1 extends FragmentActivity implements AsyncResponse2 {
    private static final String LOG_TAG = BasicMapActivity1.class.getSimpleName();

    PopupWindow popupWindow;
    MapMarker defaultMarker1;

    // permissions request code
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    // map embedded in the map fragment
    private Map map1 = null;

    private List<AutoSuggest> suggestions = new ArrayList<AutoSuggest>();

    // map fragment embedded in this activity
    private SupportMapFragment mapFragment1 = null;

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

    Button b1;
    Button g1;
    EditText e1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();

        b1 = findViewById(R.id.ok1);
        g1 = findViewById(R.id.reset1);
        e1 = findViewById(R.id.search1);

        Intent intent3 = getIntent();
        a = intent3.getDoubleExtra("a", 12.9618);
        b = intent3.getDoubleExtra("b", 80.2382);
        c = intent3.getDoubleExtra("c", 13.121);
        d = intent3.getDoubleExtra("d", 80.225);
        latitude = intent3.getDoubleExtra("latitude", 12.9618);
        longitude = intent3.getDoubleExtra("longitude", 80.2382);
        updated = intent3.getBooleanExtra("updated", true);

        latwp = intent3.getDoubleArrayExtra("latwp");
        longwp = intent3.getDoubleArrayExtra("longwp");

        default1 = intent3.getStringExtra("default1");
        default2 = intent3.getStringExtra("default2");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(BasicMapActivity1.this, RoutingActivity.class);
                intent5.putExtra("a", a);
                intent5.putExtra("b", b);
                intent5.putExtra("c", c);
                intent5.putExtra("d", d);
                intent5.putExtra("latitude", latitude);
                intent5.putExtra("longitude", longitude);
                intent5.putExtra("updated", updated);
                intent5.putExtra("latwp", latwp);
                intent5.putExtra("longwp", longwp);
                intent5.putExtra("foremost", 1);
                intent5.putExtra("default1", default1);
                intent5.putExtra("default2", default2);
                startActivity(intent5);
                finish();
            }
        });

        g1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a = latitude;
                b = longitude;

                map1.setCenter(new GeoCoordinate(a, b, 0.0),
                        Map.Animation.NONE);
                defaultMarker1.setCoordinate(new GeoCoordinate(a, b, 0.0));
            }
        });

        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow != null) {
                    popupWindow.dismiss();
                }

                if((e1.getText()).length() > 0) {
                    AutoSuggestionQueryListener1 listener = new AutoSuggestionQueryListener1();
                    listener.delegate = BasicMapActivity1.this;
                    TextAutoSuggestionRequest request = new TextAutoSuggestionRequest((e1.getText()).toString()).setSearchCenter(map1.getCenter());
                    request.execute(listener);
                }
            }
        });

        e1.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // you can call or do what you want with your EditText here
                // yourEditText...
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(popupWindow != null) {
                    popupWindow.dismiss();
                }

                if((e1.getText()).length() > 0) {
                    AutoSuggestionQueryListener1 listener = new AutoSuggestionQueryListener1();
                    listener.delegate = BasicMapActivity1.this;
                    TextAutoSuggestionRequest request = new TextAutoSuggestionRequest((e1.getText()).toString()).setSearchCenter(map1.getCenter());
                    request.execute(listener);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent8 = new Intent(BasicMapActivity1.this, RoutingActivity.class);
        intent8.putExtra("a", a);
        intent8.putExtra("b", b);
        intent8.putExtra("c", c);
        intent8.putExtra("d", d);
        intent8.putExtra("latitude", latitude);
        intent8.putExtra("longitude", longitude);
        intent8.putExtra("updated", updated);
        intent8.putExtra("latwp", latwp);
        intent8.putExtra("longwp", longwp);
        intent8.putExtra("foremost", 1);
        intent8.putExtra("default1", default1);
        intent8.putExtra("default2", default2);
        startActivity(intent8);
        finish();
    }

    private SupportMapFragment getSupportMapFragment2() {
        return (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment2);
    }

    private void initialize() {
        setContentView(R.layout.start);

        // Search for the map fragment to finish setup by calling init().
        mapFragment1 = getSupportMapFragment2();
        mapFragment1.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // retrieve a reference of the map from the map fragment
                    map1 = mapFragment1.getMap();
                    // Set the map center to the GPS region (no animation)
                    map1.setCenter(new GeoCoordinate(a, b, 0.0),
                            Map.Animation.NONE);
                    // Set the zoom level to the average between min and max
                    map1.setZoomLevel((map1.getMaxZoomLevel() + map1.getMinZoomLevel()) / 2);

                    defaultMarker1 = new MapMarker();
                    defaultMarker1.setCoordinate(new GeoCoordinate(a, b, 0.0));
                    defaultMarker1.setDraggable(true);
                    map1.addMapObject(defaultMarker1);

                    mapFragment1.setMapMarkerDragListener(new MapMarker.OnDragListener() {
                        @Override
                        public void onMarkerDrag(MapMarker mapMarker) {
                        }

                        @Override
                        public void onMarkerDragEnd(MapMarker mapMarker) {
                            GeoCoordinate c1 = mapMarker.getCoordinate();
                            a = c1.getLatitude();
                            b = c1.getLongitude();
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
                a = latitudelist.get(arg2);
                b = longitudelist.get(arg2);
                defaultMarker1.setCoordinate(new GeoCoordinate(a, b, 0.0));
                map1.setCenter(new GeoCoordinate(a, b, 0.0),
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
        popupWindow.showAsDropDown(e1, -5, 0);
    }
}

class AutoSuggestionQueryListener1 implements ResultListener<List<AutoSuggest>> {
    public AsyncResponse2 delegate = null;

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