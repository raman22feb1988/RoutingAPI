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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
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

public class BasicMapActivity3 extends FragmentActivity implements AsyncResponse4 {
    private static final String LOG_TAG = BasicMapActivity3.class.getSimpleName();

    PopupWindow popupWindow;
    MapMarker defaultMarker3;

    // permissions request code
    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;

    /**
     * Permissions that need to be explicitly requested from end user.
     */
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE };

    // map embedded in the map fragment
    private Map map3 = null;

    private List<AutoSuggest> suggestions = new ArrayList<AutoSuggest>();

    // map fragment embedded in this activity
    private SupportMapFragment mapFragment3 = null;

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

    double e;
    double f;

    int mode;
    int index;

    Button b3;
    Button g3;
    EditText e3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent13 = getIntent();
        a = intent13.getDoubleExtra("a", 12.9618);
        b = intent13.getDoubleExtra("b", 80.2382);
        c = intent13.getDoubleExtra("c", 13.121);
        d = intent13.getDoubleExtra("d", 80.225);
        latitude = intent13.getDoubleExtra("latitude", 12.9618);
        longitude = intent13.getDoubleExtra("longitude", 80.2382);
        updated = intent13.getBooleanExtra("updated", true);

        latwp = intent13.getDoubleArrayExtra("latwp");
        longwp = intent13.getDoubleArrayExtra("longwp");

        default1 = intent13.getStringExtra("default1");
        default2 = intent13.getStringExtra("default2");

        mode = intent13.getIntExtra("mode", 0);
        index = intent13.getIntExtra("index", latwp == null? 0 : latwp.length);

        checkPermissions();

        b3 = findViewById(R.id.ok3);
        g3 = findViewById(R.id.reset3);
        e3 = findViewById(R.id.search3);

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Double> l1 = new ArrayList<Double>();
                List<Double> l2 = new ArrayList<Double>();

                if(latwp != null)
                {
                    for(int y = 0; y < latwp.length; y++)
                    {
                        l1.add(latwp[y]);
                        l2.add(longwp[y]);
                    }
                }

                switch(mode)
                {
                    case 1:
                        l1.add(e);
                        l2.add(f);
                        break;
                    case 2:
                        l1.set(index, e);
                        l2.set(index, f);
                        break;
                    case 3:
                        l1.add(index, e);
                        l2.add(index, f);
                        break;
                }

                if(l1.size() > 0) {
                    latwp = new double[l1.size()];
                    longwp = new double[l2.size()];
                    for(int i = 0; i < l1.size(); i++)
                    {
                        latwp[i] = l1.get(i);
                        longwp[i] = l2.get(i);
                    }
                }
                else {
                    latwp = null;
                    longwp = null;
                }

                Intent intent14 = new Intent(BasicMapActivity3.this, Waypoints.class);
                intent14.putExtra("a", a);
                intent14.putExtra("b", b);
                intent14.putExtra("c", c);
                intent14.putExtra("d", d);
                intent14.putExtra("latitude", latitude);
                intent14.putExtra("longitude", longitude);
                intent14.putExtra("updated", updated);
                intent14.putExtra("latwp", latwp);
                intent14.putExtra("longwp", longwp);
                intent14.putExtra("default1", default1);
                intent14.putExtra("default2", default2);
                startActivity(intent14);
                finish();
            }
        });

        g3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e = latitude;
                f = longitude;

                map3.setCenter(new GeoCoordinate(e, f, 0.0),
                        Map.Animation.NONE);
                defaultMarker3.setCoordinate(new GeoCoordinate(e, f, 0.0));
            }
        });

        e3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(popupWindow != null) {
                    popupWindow.dismiss();
                }

                if((e3.getText()).length() > 0) {
                    AutoSuggestionQueryListener3 listener = new AutoSuggestionQueryListener3();
                    listener.delegate = BasicMapActivity3.this;
                    TextAutoSuggestionRequest request = new TextAutoSuggestionRequest((e3.getText()).toString()).setSearchCenter(map3.getCenter());
                    request.execute(listener);
                }
            }
        });

        e3.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // you can call or do what you want with your EditText here
                // yourEditText...
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(popupWindow != null) {
                    popupWindow.dismiss();
                }

                if((e3.getText()).length() > 0) {
                    AutoSuggestionQueryListener3 listener = new AutoSuggestionQueryListener3();
                    listener.delegate = BasicMapActivity3.this;
                    TextAutoSuggestionRequest request = new TextAutoSuggestionRequest((e3.getText()).toString()).setSearchCenter(map3.getCenter());
                    request.execute(listener);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        List<Double> l3 = new ArrayList<Double>();
        List<Double> l4 = new ArrayList<Double>();

        if(latwp != null)
        {
            for(int z = 0; z < latwp.length; z++)
            {
                l3.add(latwp[z]);
                l4.add(longwp[z]);
            }
        }

        switch(mode)
        {
            case 1:
                l3.add(e);
                l4.add(f);
                break;
            case 2:
                l3.set(index, e);
                l4.set(index, f);
                break;
            case 3:
                l3.add(index, e);
                l4.add(index, f);
                break;
        }

        if(l3.size() > 0) {
            latwp = new double[l3.size()];
            longwp = new double[l4.size()];
            for(int j = 0; j < l3.size(); j++)
            {
                latwp[j] = l3.get(j);
                longwp[j] = l4.get(j);
            }
        }
        else {
            latwp = null;
            longwp = null;
        }

        Intent intent16 = new Intent(BasicMapActivity3.this, Waypoints.class);
        intent16.putExtra("a", a);
        intent16.putExtra("b", b);
        intent16.putExtra("c", c);
        intent16.putExtra("d", d);
        intent16.putExtra("latitude", latitude);
        intent16.putExtra("longitude", longitude);
        intent16.putExtra("updated", updated);
        intent16.putExtra("latwp", latwp);
        intent16.putExtra("longwp", longwp);
        intent16.putExtra("default1", default1);
        intent16.putExtra("default2", default2);
        startActivity(intent16);
        finish();
    }

    private SupportMapFragment getSupportMapFragment4() {
        return (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapfragment4);
    }

    private void initialize() {
        setContentView(R.layout.middle);

        switch(mode)
        {
            case 1:
                e = latitude;
                f = longitude;
                break;
            case 2:
                e = latwp[index];
                f = longwp[index];
                break;
            case 3:
                e = latitude;
                f = longitude;
                break;
        }

        // Search for the map fragment to finish setup by calling init().
        mapFragment3 = getSupportMapFragment4();
        mapFragment3.init(new OnEngineInitListener() {
            @Override
            public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                if (error == OnEngineInitListener.Error.NONE) {
                    // retrieve a reference of the map from the map fragment
                    map3 = mapFragment3.getMap();
                    // Set the map center to the GPS region (no animation)
                    map3.setCenter(new GeoCoordinate(e, f, 0.0),
                            Map.Animation.NONE);
                    // Set the zoom level to the average between min and max
                    map3.setZoomLevel((map3.getMaxZoomLevel() + map3.getMinZoomLevel()) / 2);

                    defaultMarker3 = new MapMarker();
                    defaultMarker3.setCoordinate(new GeoCoordinate(e, f, 0.0));
                    defaultMarker3.setDraggable(true);
                    map3.addMapObject(defaultMarker3);

                    mapFragment3.setMapMarkerDragListener(new MapMarker.OnDragListener() {
                        @Override
                        public void onMarkerDrag(MapMarker mapMarker) {
                        }

                        @Override
                        public void onMarkerDragEnd(MapMarker mapMarker) {
                            GeoCoordinate c3 = mapMarker.getCoordinate();
                            e = c3.getLatitude();
                            f = c3.getLongitude();
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
                defaultMarker3.setCoordinate(new GeoCoordinate(a, b, 0.0));
                map3.setCenter(new GeoCoordinate(a, b, 0.0),
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
        popupWindow.showAsDropDown(e3, -5, 0);
    }
}

class AutoSuggestionQueryListener3 implements ResultListener<List<AutoSuggest>> {
    public AsyncResponse4 delegate = null;

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