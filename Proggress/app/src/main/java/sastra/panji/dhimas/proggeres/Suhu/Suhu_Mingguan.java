package sastra.panji.dhimas.proggeres.Suhu;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import es.dmoral.toasty.Toasty;
import sastra.panji.dhimas.proggeres.R;
import sastra.panji.dhimas.proggeres.helper.Preferences;

public class Suhu_Mingguan extends AppCompatActivity {

    final Handler handler = new Handler();
    private DonutProgress donutProgress;
    private AnyChartView chart;
    private Cartesian cartesian;
    private Set set;
    private ProgressBar loading;
    private Calendar calendar;
    private SimpleDateFormat dateFormat, nowFormat;
    private String date, url, now;
    final Runnable r = new Runnable() {
        public void run() {
            loading.setVisibility(View.VISIBLE);
            dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            date = dateFormat.format(calendar.getTime());
            mingguIni(url, "&timezone=Asia/Jakarta&start=" + getWeekDateString() + "&end=" + date);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suhu__mingguan);
        chart = findViewById(R.id.any_chart_view_mingguan);
        loading = findViewById(R.id.suhu_loading_mingguan);
        donutProgress = findViewById(R.id.donut_progress_mingguan);
        donutProgress.setMax(50);
        cartesian = AnyChart.line();
        url = Preferences.getUrlActive(getBaseContext());
        line();
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        date = dateFormat.format(calendar.getTime());
        mingguIni(url, "&timezone=Asia/Jakarta&start=" + getWeekDateString() + "&end=" + date);
        Log.d("Seminggu", getWeekDateString());
    }

    private Date semingguLalu() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        return cal.getTime();
    }

    private String getWeekDateString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(semingguLalu());
    }

    public void mingguIni(String url, String filter) {


        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d("filter", url + filter);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + filter, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    String nil = "";
                    loading.setVisibility(View.INVISIBLE);
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("feeds");
                    List<DataEntry> seriesData = new ArrayList<>();
                    String nilai = "";
                    String nilai2;
                    if (array.length() != 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject suh = array.getJSONObject(i);

                            nilai = suh.getString("field1");
                            Log.d("Nilai", nilai);
                            nilai2 = suh.getString("field2");
                            String date = suh.getString("created_at");
                            if (suh.isNull("field1") || nilai.isEmpty()) {
                                nilai = "0";
                            }
                            if (suh.isNull("field2") || nilai2.isEmpty()) {
                                nilai2 = "0";
                            }

                            seriesData.add(new CustomDataEntry(getDate(date), Float.valueOf(nilai2), Float.valueOf(nilai)));

                        }

                        set.data(seriesData);
                        donutProgress.invalidate();
                        if (Float.valueOf(nilai) >= 36) {
                            donutProgress.setFinishedStrokeColor(Color.RED);
                            donutProgress.setTextColor(Color.RED);
                        }
                        donutProgress.setProgress(Float.valueOf(nilai));
                        donutProgress.setTextSize(66);
                        donutProgress.setText("Suhunya " + nilai + "\u2103");
                        handler.postDelayed(r, 20000);
                    } else {
                        Toasty.info(Suhu_Mingguan.this, "Data Kosong", Toasty.LENGTH_SHORT, true).show();

                    }


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

        cartesian.title("Suhu dan Kelembapan Beeset");

        cartesian.yAxis(0).title("Nilai Suhu dan Kelembapan Mingguan");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("Espresso", 1, 3));
        set = Set.instantiate();
        set.data(seriesData);

        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }");

        Line series1 = cartesian.line(series1Mapping);
        //for color https://docs.anychart.com/Appearance_Settings/Lines_Settings
        series1.name("Kelembapan Kandang");
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
        chart.setZoomEnabled(true);
        chart.invalidate();

    }

    private String getDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC+7"));
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

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, float value, float value2) {
            super(x, value);
            setValue("value2", value2);

        }

    }
}
