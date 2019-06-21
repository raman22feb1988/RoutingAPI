/*
 * Copyright (c) 2011-2019 HERE Global B.V. and its affiliate(s).
 * All rights reserved.
 * The use of this software is conditional upon having a separate agreement
 * with a HERE company for the use or utilization of this software. In the
 * absence of such agreement, the use of the software is not allowed.
 */

package com.example.hereapi.locationservices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.routing.Route;
import com.here.android.mpa.routing.RouteManager;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.search.Address;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.ReverseGeocodeRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ETA extends Activity implements AsyncResponse6 {
    Button b1;
    ListView g1;

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

    List<String> g;

    int distance;
    int time;
    long eta;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eta);

        b1 = findViewById(R.id.previous);
        g1 = findViewById(R.id.grid);

        Intent intent22 = getIntent();
        a = intent22.getDoubleExtra("a", 12.9618);
        b = intent22.getDoubleExtra("b", 80.2382);
        c = intent22.getDoubleExtra("c", 13.121);
        d = intent22.getDoubleExtra("d", 80.225);
        latitude = intent22.getDoubleExtra("latitude", 12.9618);
        longitude = intent22.getDoubleExtra("longitude", 80.2382);
        updated = intent22.getBooleanExtra("updated", true);

        latwp = intent22.getDoubleArrayExtra("latwp");
        longwp = intent22.getDoubleArrayExtra("longwp");

        default1 = intent22.getStringExtra("default1");
        default2 = intent22.getStringExtra("default2");

        g = new ArrayList<>();

        g.add("Destination");
        g.add("Distance");
        g.add("Time");
        g.add("ETA");

        distance = 0;
        time = 0;
        eta = System.currentTimeMillis();

        triggerRevGeocodeRequest(a, b);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent23 = new Intent(ETA.this, RoutingActivity.class);
                intent23.putExtra("a", a);
                intent23.putExtra("b", b);
                intent23.putExtra("c", c);
                intent23.putExtra("d", d);
                intent23.putExtra("latitude", latitude);
                intent23.putExtra("longitude", longitude);
                intent23.putExtra("updated", updated);
                intent23.putExtra("latwp", latwp);
                intent23.putExtra("longwp", longwp);
                intent23.putExtra("foremost", 4);
                intent23.putExtra("default1", default1);
                intent23.putExtra("default2", default2);
                startActivity(intent23);
                finish();
            }
        });
    }

    public void onBackPressed()
    {
        Intent intent24 = new Intent(ETA.this, RoutingActivity.class);
        intent24.putExtra("a", a);
        intent24.putExtra("b", b);
        intent24.putExtra("c", c);
        intent24.putExtra("d", d);
        intent24.putExtra("latitude", latitude);
        intent24.putExtra("longitude", longitude);
        intent24.putExtra("updated", updated);
        intent24.putExtra("latwp", latwp);
        intent24.putExtra("longwp", longwp);
        intent24.putExtra("foremost", 4);
        intent24.putExtra("default1", default1);
        intent24.putExtra("default2", default2);
        startActivity(intent24);
        finish();
    }

    private void triggerRevGeocodeRequest(double x, double y) {
        /* Create a ReverseGeocodeRequest object with a GeoCoordinate. */
        GeoCoordinate coordinate = new GeoCoordinate(x, y);
        String address = new String();
        ReverseGeocodeListener3 listener = new ReverseGeocodeListener3();
        listener.delegate = this;
        ReverseGeocodeRequest request = new ReverseGeocodeRequest(coordinate);
        request.execute(listener);

//        if (request.execute(listener) != ErrorCode.NONE) {
//            // Handle request error
//            address = "ERROR: RevGeocode Request returned error code:" + errorCode2;
//        }
//        else
//        {
//            // Process result data
//            /*
//             * From the start object, we retrieve the address and display to the screen.
//             * Please refer to HERE Android SDK doc for other supported APIs.
//             */
//            address = street2;
//        }
    }

    @Override
    public void processFinish(String output1, String output2) {
        g.add(output1);

        if(g.size() == 5)
        {
            distance = 0;
            time = 0;
            eta = System.currentTimeMillis();

            g.add((distance / 1000) + " km");
            g.add((time / 60) + " min");
            SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
            Date date = new Date(eta);
            g.add(formatter.format(date));

            if(latwp == null) {
                triggerRevGeocodeRequest(c, d);
            }
            else {
                triggerRevGeocodeRequest(latwp[0], longwp[0]);
            }
        }
        else if(latwp == null && g.size() == 9)
        {
            getDirections(a, b, c, d);
        }
        else if(latwp != null && g.size() == 9)
        {
            getDirections(a, b, latwp[((g.size() - 1) / 4) - 2], longwp[((g.size() - 1) / 4) - 2]);
        }
        else if(latwp != null && ((g.size() - 1) / 4) <= latwp.length + 1) {
            getDirections(latwp[((g.size() - 1) / 4) - 3], longwp[((g.size() - 1) / 4) - 3], latwp[((g.size() - 1) / 4) - 2], longwp[((g.size() - 1) / 4) - 2]);
        }
        else if(latwp != null && ((g.size() - 1) / 4) == latwp.length + 2) {
            getDirections(latwp[((g.size() - 1) / 4) - 3], longwp[((g.size() - 1) / 4) - 3], c, d);
        }
        else {
            List<String> l = new ArrayList<>();

            for(int i = 0; i < (g.size() - 1) / 4; i++)
            {
                if(i <= 1) {
                    l.add(g.get(i * 4) + "#" + g.get((i * 4) + 1) + "#" + g.get((i * 4) + 2) + "#" + g.get((i * 4) + 3));
                }
                else {
                    l.add(g.get((i * 4) + 4) + "#" + g.get((i * 4) + 1) + "#" + g.get((i * 4) + 2) + "#" + g.get((i * 4) + 3));
                }
            }

            customadapter cusadapter = new customadapter(ETA.this, R.layout.gridelement, l);
            g1.setAdapter(cusadapter);
        }
    }

    public class customadapter extends ArrayAdapter<String> {
        Context con;
        int _resource;
        List<String> lival;

        public customadapter(Context context, int resource, List<String> li) {
            super(context, resource, li);
            // TODO Auto-generated constructor stub
            con = context;
            _resource = resource;
            lival = li;
        }

        @Override
        public View getView(final int position, View v, ViewGroup vg) {
            View vi = null;
            LayoutInflater linflate = (LayoutInflater) (ETA.this).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = linflate.inflate(_resource, null);

            TextView t1 = vi.findViewById(R.id.destination);
            TextView t2 = vi.findViewById(R.id.distance);
            TextView t3 = vi.findViewById(R.id.time);
            TextView t4 = vi.findViewById(R.id.clock);

            String parts[] = (lival.get(position)).split("#");

            String s1 = parts[0];
            String s2 = parts[1];
            String s3 = parts[2];
            String s4 = parts[3];

            t1.setText(s1);
            t2.setText(s2);
            t3.setText(s3);
            t4.setText(s4);

            return vi;
        }
    }

    public void getDirections(double d1, double d2, double d3, double d4)
    {
        // 2. Initialize RouteManager
        RouteManager routeManager = new RouteManager();

        // 3. Select routing options
        RoutePlan routePlan = new RoutePlan();

        RouteOptions routeOptions = new RouteOptions();
        if (default1.equals("Pedestrian")) {
            routeOptions.setTransportMode(RouteOptions.TransportMode.PEDESTRIAN);
        } else {
            routeOptions.setTransportMode(RouteOptions.TransportMode.CAR);
        }
        if (default2.equals("Shortest")) {
            routeOptions.setRouteType(RouteOptions.Type.SHORTEST);
        }
        else {
            routeOptions.setRouteType(RouteOptions.Type.FASTEST);
        }
        routePlan.setRouteOptions(routeOptions);

        // 4. Select Waypoints for your routes
        // START: Source
        routePlan.addWaypoint(new GeoCoordinate(d1, d2));

        // END: Destination
        routePlan.addWaypoint(new GeoCoordinate(d3, d4));

        // 5. Retrieve Routing information via RouteManagerEventListener
        RouteManager.Error error = routeManager.calculateRoute(routePlan, routeManagerListener);
        if (error != RouteManager.Error.NONE) {
            Toast.makeText(getApplicationContext(),
                    "Route calculation failed with: " + error.toString(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private RouteManager.Listener routeManagerListener = new RouteManager.Listener() {
        public void onCalculateRouteFinished(RouteManager.Error errorCode,
                                             List<RouteResult> result) {

            if (errorCode == RouteManager.Error.NONE && result.get(0).getRoute() != null) {
                // create a map route object and place it on the map
                // Get the bounding box containing the route and zoom in (no animation)
                distance += result.get(0).getRoute().getLength();
                time += result.get(0).getRoute().getTta(Route.TrafficPenaltyMode.DISABLED, Route.WHOLE_ROUTE).getDuration();
                eta += result.get(0).getRoute().getTta(Route.TrafficPenaltyMode.DISABLED, Route.WHOLE_ROUTE).getDuration() * 1000;

                g.add((distance / 1000) + " km");
                g.add((time / 60) + " min");
                SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
                Date date = new Date(eta);
                g.add(formatter.format(date));

                triggerRevGeocodeRequest(result.get(0).getRoute().getDestination().getLatitude(), result.get(0).getRoute().getDestination().getLongitude());
            } else {
            }
        }

        public void onProgress(int percentage) {
        }
    };
}

// Implementation of ResultListener
class ReverseGeocodeListener3 implements ResultListener<Address> {
    public AsyncResponse6 delegate = null;

    String street1 = new String();
    String errorCode1 = new String();

    @Override
    public void onCompleted(Address data, ErrorCode error) {
        errorCode1 = error.toString();
        if (error != ErrorCode.NONE) {
            // Handle error
            street1 = "ERROR: RevGeocode Request returned error code:" + error.toString();
            delegate.processFinish(street1, errorCode1);
        } else {
            // Process result data
            /*
             * From the start object, we retrieve the address and display to the screen.
             * Please refer to HERE Android SDK doc for other supported APIs.
             */
            street1 = data.getText();
            delegate.processFinish(street1, errorCode1);
        }
    }
}