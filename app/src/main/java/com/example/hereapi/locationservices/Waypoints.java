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

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.search.Address;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.ReverseGeocodeRequest;

import java.util.ArrayList;
import java.util.List;

public class Waypoints extends Activity implements AsyncResponse5 {
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

    List<String> addresses;
    boolean recreate;

    Button b1;
    Button b2;
    ListView l1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waypoints);

        Intent intent11 = getIntent();
        a = intent11.getDoubleExtra("a", 12.9618);
        b = intent11.getDoubleExtra("b", 80.2382);
        c = intent11.getDoubleExtra("c", 13.121);
        d = intent11.getDoubleExtra("d", 80.225);
        latitude = intent11.getDoubleExtra("latitude", 12.9618);
        longitude = intent11.getDoubleExtra("longitude", 80.2382);
        updated = intent11.getBooleanExtra("updated", true);

        latwp = intent11.getDoubleArrayExtra("latwp");
        longwp = intent11.getDoubleArrayExtra("longwp");

        default1 = intent11.getStringExtra("default1");
        default2 = intent11.getStringExtra("default2");

        addresses = new ArrayList<String>();

        if(latwp != null)
        {
            triggerRevGeocodeRequest(latwp[0], longwp[0]);
        }

        b1 = findViewById(R.id.add);
        b2 = findViewById(R.id.back);
        l1 = findViewById(R.id.list);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent12 = new Intent(Waypoints.this, BasicMapActivity3.class);
                intent12.putExtra("a", a);
                intent12.putExtra("b", b);
                intent12.putExtra("c", c);
                intent12.putExtra("d", d);
                intent12.putExtra("latitude", latitude);
                intent12.putExtra("longitude", longitude);
                intent12.putExtra("updated", updated);
                intent12.putExtra("latwp", latwp);
                intent12.putExtra("longwp", longwp);
                intent12.putExtra("mode", 1);
                intent12.putExtra("index", latwp == null? 0 : latwp.length);
                intent12.putExtra("default1", default1);
                intent12.putExtra("default2", default2);
                startActivity(intent12);
                finish();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent15 = new Intent(Waypoints.this, RoutingActivity.class);
                intent15.putExtra("a", a);
                intent15.putExtra("b", b);
                intent15.putExtra("c", c);
                intent15.putExtra("d", d);
                intent15.putExtra("latitude", latitude);
                intent15.putExtra("longitude", longitude);
                intent15.putExtra("updated", updated);
                intent15.putExtra("latwp", latwp);
                intent15.putExtra("longwp", longwp);
                intent15.putExtra("foremost", 3);
                intent15.putExtra("default1", default1);
                intent15.putExtra("default2", default2);
                startActivity(intent15);
                finish();
            }
        });
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
            LayoutInflater linflate = (LayoutInflater) (Waypoints.this).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = linflate.inflate(_resource, null);

            TextView t1 = (TextView) vi.findViewById(R.id.address);
            Button b3 = (Button) vi.findViewById(R.id.edit);
            Button b4 = (Button) vi.findViewById(R.id.insert);
            Button b5 = (Button) vi.findViewById(R.id.delete);

            final String data = lival.get(position);
            t1.setText(data);

            b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent17 = new Intent(Waypoints.this, BasicMapActivity3.class);
                    intent17.putExtra("a", a);
                    intent17.putExtra("b", b);
                    intent17.putExtra("c", c);
                    intent17.putExtra("d", d);
                    intent17.putExtra("latitude", latitude);
                    intent17.putExtra("longitude", longitude);
                    intent17.putExtra("updated", updated);
                    intent17.putExtra("latwp", latwp);
                    intent17.putExtra("longwp", longwp);
                    intent17.putExtra("mode", 2);
                    intent17.putExtra("index", position);
                    intent17.putExtra("default1", default1);
                    intent17.putExtra("default2", default2);
                    startActivity(intent17);
                    finish();
                }
            });

            b4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent18 = new Intent(Waypoints.this, BasicMapActivity3.class);
                    intent18.putExtra("a", a);
                    intent18.putExtra("b", b);
                    intent18.putExtra("c", c);
                    intent18.putExtra("d", d);
                    intent18.putExtra("latitude", latitude);
                    intent18.putExtra("longitude", longitude);
                    intent18.putExtra("updated", updated);
                    intent18.putExtra("latwp", latwp);
                    intent18.putExtra("longwp", longwp);
                    intent18.putExtra("mode", 3);
                    intent18.putExtra("index", position);
                    intent18.putExtra("default1", default1);
                    intent18.putExtra("default2", default2);
                    startActivity(intent18);
                    finish();
                }
            });

            b5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    List<Double> l1 = new ArrayList<Double>();
                    List<Double> l2 = new ArrayList<Double>();

                    if(latwp != null)
                    {
                        for(int x = 0; x < latwp.length; x++)
                        {
                            l1.add(latwp[x]);
                            l2.add(longwp[x]);
                        }
                    }

                    l1.remove(position);
                    l2.remove(position);

                    if(l1.size() > 0) {
                        latwp = new double[l1.size()];
                        longwp = new double[l2.size()];
                        for (int i = 0; i < l1.size(); i++) {
                            latwp[i] = l1.get(i);
                            longwp[i] = l2.get(i);
                        }
                    }
                    else {
                        latwp = null;
                        longwp = null;
                    }

                    Intent intent20 = new Intent(Waypoints.this, Waypoints.class);
                    intent20.putExtra("a", a);
                    intent20.putExtra("b", b);
                    intent20.putExtra("c", c);
                    intent20.putExtra("d", d);
                    intent20.putExtra("latitude", latitude);
                    intent20.putExtra("longitude", longitude);
                    intent20.putExtra("updated", updated);
                    intent20.putExtra("latwp", latwp);
                    intent20.putExtra("longwp", longwp);
                    intent20.putExtra("default1", default1);
                    intent20.putExtra("default2", default2);
                    startActivity(intent20);
                    finish();
                }
            });
            return vi;
        }
    }

    private void triggerRevGeocodeRequest(double x, double y) {
        /* Create a ReverseGeocodeRequest object with a GeoCoordinate. */
        GeoCoordinate coordinate = new GeoCoordinate(x, y);
        String address = new String();
        ReverseGeocodeListener2 listener = new ReverseGeocodeListener2();
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
        addresses.add(output1);

        if(addresses.size() == latwp.length) {
            customadapter cusadapter = new customadapter(Waypoints.this, R.layout.listelement, addresses);
            l1.setAdapter(cusadapter);
        }
        else {
            triggerRevGeocodeRequest(latwp[addresses.size()], longwp[addresses.size()]);
        }
    }

    public void onBackPressed()
    {
        Intent intent19 = new Intent(Waypoints.this, RoutingActivity.class);
        intent19.putExtra("a", a);
        intent19.putExtra("b", b);
        intent19.putExtra("c", c);
        intent19.putExtra("d", d);
        intent19.putExtra("latitude", latitude);
        intent19.putExtra("longitude", longitude);
        intent19.putExtra("updated", updated);
        intent19.putExtra("latwp", latwp);
        intent19.putExtra("longwp", longwp);
        intent19.putExtra("foremost", 3);
        intent19.putExtra("default1", default1);
        intent19.putExtra("default2", default2);
        startActivity(intent19);
        finish();
    }
}

// Implementation of ResultListener
class ReverseGeocodeListener2 implements ResultListener<Address> {
    public AsyncResponse5 delegate = null;

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