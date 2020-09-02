package sastra.panji.dhimas.proggeres.sarang.Sarang_tiga;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

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
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import es.dmoral.toasty.Toasty;
import sastra.panji.dhimas.proggeres.R;
import sastra.panji.dhimas.proggeres.helper.Preferences;

public class SarangTigaFragment extends Fragment {

    private final Handler handler = new Handler();
    private CircularProgressBar circularProgressBar;
    private Button panen;
    private String url;
    private AnyChartView chart;
    private Set set;
    private final Runnable r = new Runnable() {
        public void run() {
            //loading.setVisibility(View.VISIBLE);
            // suhunya(url,"");
            getBerat3();
        }
    };
    private Cartesian cartesian;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel = ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sarang_tiga, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        circularProgressBar = view.findViewById(R.id.circularBerat3);
        panen = view.findViewById(R.id.btn_panen3);
        panen.setVisibility(View.INVISIBLE);
        chart = view.findViewById(R.id.fragment_sarang_tiga_chart);
        url = Preferences.getUrlActive(getContext());
        cartesian = AnyChart.line();
        line();

    }

    @Override
    public void onResume() {
        handler.post(r);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(r);
    }

    private void getBerat3() {
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        StringRequest request = new StringRequest(Request.Method.GET, url+"&timezone=Asia/Jakarta", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                int max = 2000;
                double persen = max * 0.8;
                try {
                    object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("feeds");
                    List<DataEntry> seriesData = new ArrayList<>();
                    String nilai = "";
                    if (array.length() != 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object1 = array.getJSONObject(i);
                            nilai = object1.getString("field5");

                            String date = object1.getString("created_at");
                            if (object1.isNull("field3") || nilai.isEmpty()) {
                                nilai = "0";
                            }

                            seriesData.add(new CustomDataEntry(getDate(date), Double.valueOf(nilai)));
                            if (Double.valueOf(nilai) >= persen) {
                                panen.setVisibility(View.VISIBLE);
                            } else {
                                panen.setVisibility(View.INVISIBLE);
                            }

                        }
                        circularProgressBar.setProgress(Float.valueOf(nilai));
                        set.data(seriesData);
                    } else {
                        Toasty.info(
                                Objects.requireNonNull(getContext()), "Data Kosong", Toasty.LENGTH_SHORT, true).show();
                    }
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
        queue.add(request);
    }

    private void line() {


        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Berat Sarang 3");

        cartesian.yAxis(0).title("Nilai Berat Sarang 3");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("No Data", 0));
        set = Set.instantiate();
        set.data(seriesData);

        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");


        Line series1 = cartesian.line(series1Mapping);
        //for color https://docs.anychart.com/Appearance_Settings/Lines_Settings
        series1.name("Berat Sarang 3");
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


        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        chart.setChart(cartesian);
        chart.setZoomEnabled(true);
        chart.invalidate();

    }

    private String getDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
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

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, double value) {
            super(x, value);


        }

    }
}