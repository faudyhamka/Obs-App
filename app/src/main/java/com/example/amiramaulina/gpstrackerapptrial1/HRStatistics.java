package com.example.amiramaulina.gpstrackerapptrial1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HRStatistics extends AppCompatActivity {
    String name, ip, hrTime, hrValue;
    ArrayList<String> array10 = new ArrayList<>(); //array untuk hrTimestamp
    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
    String[] xLabels ;
    int x = 0;
    int i = 0;
    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr_statistics);
        final GraphView graph = (GraphView)findViewById(R.id.graphHRValue);
        final StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        graph.addSeries(series);
        Viewport vp = graph.getViewport();
        vp.setXAxisBoundsManual(true);
        vp.setMinX(0);
        vp.setMaxX(2); //yg ditunjukin max berapa
        vp.setYAxisBoundsManual(true);
        vp.setMinY(0);
        vp.setMaxY(200); //yg ditunjukin max berapa
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        graph.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        graph.setTitleTextSize(40);

        Intent intent = getIntent();
        if (intent != null) { name = intent.getStringExtra("name");
        graph.setTitle(name+"'s Daily Heartrate Stats" );
        ip = intent.getStringExtra("ip");}

        queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://"+ip+ ":3000/hrate1", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            for (i = 0; i < 15; i++) {
                                if (response.has("hr" + i)) {
                                    hrValue = response.getString("hr" + i);
                                    hrTime = response.getString("time" + i);
                                    array10.add(hrTime);
                                    DataPoint point = new DataPoint(x, Integer.parseInt(hrValue));
                                    series.appendData(point, false, 1000);
                                    xLabels = new String[array10.size()];
                                    array10.toArray(xLabels);
                                    x++;

                                    graph.getGridLabelRenderer().setHumanRounding(true);
                                    graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
                                    graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                                        @Override
                                        public String formatLabel(double value, boolean isValueX) {
                                            if (isValueX) {
                                                return xLabels[(int) value];
                                            }
                                            return super.formatLabel(value, isValueX);
                                        }
                                    });
                                    series.setOnDataPointTapListener(new OnDataPointTapListener() {
                                        @Override
                                        public void onTap(Series series, DataPointInterface dataPoint) {
                                            Toast.makeText(getApplicationContext(), "Heartrate: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        } catch (Exception e) {e.printStackTrace();}
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { error.printStackTrace(); }
        });
        request.setTag("Graph");
        queue.add(request);
    }
}