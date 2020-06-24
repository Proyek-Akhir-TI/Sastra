package sastra.panji.dhimas.proggeres.menu;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.charts.LinearGauge;
import com.anychart.enums.Anchor;
import com.anychart.enums.Position;
import com.anychart.scales.Base;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import sastra.panji.dhimas.proggeres.R;

public class Suhu extends AppCompatActivity {


    static ListView listView;
    SwipeRefreshLayout refreshLayout;
    // private AnyChartView  ;
    private LinearGauge linearGauge;
    LineChart chart;
    ArrayList<Entry> entries = new ArrayList<Entry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suhu);
        refreshLayout = findViewById(R.id.swipe);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        APIlib.getInstance().setActiveAnyChartView(anyChartView);


        Suhunya();
        termo(anyChartView);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Suhunya();
                    }
                }, 1000);
            }
        });

    }

    public void Suhunya() {
        refreshLayout.setRefreshing(false);
        String url = "https://api.thingspeak.com/channels/1085076/fields/1.json?api_key=R28BW9NXV8RGGCQ6&results=50";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("feeds");
                    JSONObject suh = array.getJSONObject(array.length() - 1);
                    String nilai = suh.getString("field1");
                    linearGauge.data(new SingleValueDataSet(new Integer[]{Integer.parseInt(nilai)}));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

    public void termo(AnyChartView anyChartView) {

        linearGauge = AnyChart.linear();

        // TODO data

        linearGauge.label(0).useHtml(true);
        linearGauge.label(0)
                .text("C&deg;")
                .position(Position.LEFT_BOTTOM)
                .anchor(Anchor.LEFT_BOTTOM)
                .offsetY("20px")
                .offsetX("38%")
                .fontColor("black")
                .fontSize(17);


        Base scale = linearGauge.scale()
                .minimum(0)
                .maximum(40);


        linearGauge.axis(0).scale(scale);
        linearGauge.axis(0)
                .offset("-1%")
                .width("0.5%");

        linearGauge.axis(0).labels()
                .format("{%Value}C&deg;")
                .useHtml(true);

        linearGauge.thermometer(0)
                .name("Suhu Kandang")
                .id(1);

        linearGauge.axis(0).minorTicks(true);
        linearGauge.axis(0).labels()
                .format(
                        "function () {\n" +
                                "    return '<span style=\"color:black;\">' + this.value + '&deg;</span>'\n" +
                                "  }")
                .useHtml(true);

        anyChartView.setChart(linearGauge);
    }


}
