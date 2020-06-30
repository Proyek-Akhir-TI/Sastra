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
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import sastra.panji.dhimas.proggeres.R;

public class Suhu extends AppCompatActivity {


    static ListView listView;
    SwipeRefreshLayout refreshLayout;
    private AnyChartView chart;
    private Cartesian cartesian;
    DonutProgress donutProgress;
    private Set set;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suhu);
        refreshLayout = findViewById(R.id.swipe);
        donutProgress = findViewById(R.id.donut_progress);
        donutProgress.setMax(50);

        chart = findViewById(R.id.any_chart_view);
        cartesian = AnyChart.line();
        line();
        suhunya();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        suhunya();
                    }
                }, 1000);
                refreshLayout.setRefreshing(false);
            }
        });

    }


    public void suhunya() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.thingspeak.com/channels/1085076/feeds.json?api_key=R28BW9NXV8RGGCQ6&results=1000";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    String nil = "";
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("feeds");
                    List<DataEntry> seriesData = new ArrayList<>();

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject suh = array.getJSONObject(i);
                        String nilai = suh.getString("field1");
                        String nilai2 = suh.getString("field2");
                        String date = suh.getString("created_at");
                        seriesData.add(new Suhu.CustomDataEntry(getDate(date), Double.valueOf(nilai2), Double.valueOf(nilai)));

                    }
                    set.data(seriesData);

                    JSONObject aw = array.getJSONObject(array.length() - 1);
                    nil = aw.getString("field1");
                    donutProgress.setProgress(Float.valueOf(nil));
                    donutProgress.setTextSize(66);
                    donutProgress.setText("Suhunya " + nil + "\u2103");
                    handler.postDelayed(r, 20000);

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

        queue.add(stringRequest);
    }


    public void line() {


        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Suhu dan Qelembapan Beeset");

        cartesian.yAxis(0).title("Nilai Suhu dan Qelembapan");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new Suhu.CustomDataEntry("Espresso", 1, 3));
        set = Set.instantiate();
        set.data(seriesData);

        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");

        Line series1 = cartesian.line(series1Mapping);
        //for color https://docs.anychart.com/Appearance_Settings/Lines_Settings
        series1.name("Celembapan Candang");
        series1.stroke("red");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.DIAMOND)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        Line series2 = cartesian.line(series2Mapping);
        series2.name("Suhu Candang");
        series2.hovered().markers().enabled(true);
        series2.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series2.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);


        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);
        chart.setChart(cartesian);
        chart.invalidate();

    }

    private String getDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        try {
            value = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormatter.setTimeZone(TimeZone.getDefault());
        String dt = dateFormatter.format(value);

        return dt;
    }

    final Handler handler = new Handler();
    final Runnable r = new Runnable() {
        public void run() {
            suhunya();
        }
    };

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, double value, double value2) {
            super(x, value);
            setValue("value2", value2);

        }

    }
}
