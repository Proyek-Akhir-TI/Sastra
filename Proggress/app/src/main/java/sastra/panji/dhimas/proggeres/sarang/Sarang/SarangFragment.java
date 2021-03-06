package sastra.panji.dhimas.proggeres.sarang.Sarang;

import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import es.dmoral.toasty.Toasty;
import sastra.panji.dhimas.proggeres.R;
import sastra.panji.dhimas.proggeres.helper.Preferences;

public class SarangFragment extends Fragment {

    private final Handler handler = new Handler();
    private String url;
    private AnyChartView chart;
    private Set set;

    private Cartesian cartesian;
    private CircularProgressBar circularFillableLoaders;
    private Button panen, panen2;
    private final Runnable r = new Runnable() {
        public void run() {
            //loading.setVisibility(View.VISIBLE);
            // suhunya(url,"");
            getBerat1(url);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_sarang, container, false);

        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        circularFillableLoaders = view.findViewById(R.id.circularBerat1);
        chart = view.findViewById(R.id.fragment_sarang_chart);
        panen = view.findViewById(R.id.btn_panen1);
//        panen.setVisibility(View.INVISIBLE);
        url = Preferences.getUrlActive(getContext());
        cartesian = AnyChart.line();
        Log.d("TElpon", Preferences.getNoTelpon(getContext()));
        line();
//        getBerat1(url);
        panen = view.findViewById(R.id.btn_panen1);
        panen2 = view.findViewById(R.id.btn_selesai1);

        panen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(Preferences.getNoTelpon(getContext()), null, "3", null, null);
                Toast.makeText(getContext(), "Panen Dimulai.",
                        Toast.LENGTH_LONG).show();
                panen.setVisibility(View.INVISIBLE);
                setPanen(Preferences.getBearerUser(getContext()));
                panen.setBackgroundColor(R.drawable.btn_selesai);
                panen2.setVisibility(View.VISIBLE);
            }
        });
        panen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(Preferences.getNoTelpon(getContext()), null, "2", null, null);
                Toast.makeText(getContext(), "Panen Selesai Dimulai.",
                        Toast.LENGTH_LONG).show();
                panen2.setVisibility(View.INVISIBLE);
                setSelesai(Preferences.getBearerUser(getContext()));
                panen2.setBackgroundColor(R.drawable.btn_selesai);
                panen.setVisibility(View.VISIBLE);
            }
        });

    }

//    @Override
//    public void onResume() {
//        handler.post(r);
//        super.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        handler.removeCallbacks(r);
//    }

    private void getBerat1(String urlnya) {
        RequestQueue queue = Volley.newRequestQueue(Objects.requireNonNull(getContext()));
        StringRequest request = new StringRequest(Request.Method.GET, urlnya + "&timezone=Asia/Jakarta", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                int max = 1500;
                double persen = max * 0.8;
                try {
                    object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("feeds");
                    List<DataEntry> seriesData = new ArrayList<>();
                    String nilai = "";
                    if (array.length() != 0) {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object1 = array.getJSONObject(i);
                            nilai = object1.getString("field3");

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
                        circularFillableLoaders.setProgress(Float.valueOf(nilai));

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

    private void setPanen(final String bearer) {
        String urlnya = "http://ta.poliwangi.ac.id/~ti17183/laravel/public/api/peternak/kandang/setpanen";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, urlnya, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", String.valueOf(1));
                params.put("kandang_id", Preferences.getIdKandang(getContext()));

                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + bearer);
                return params;
            }
        };
        queue.add(request);

    }

    private void setSelesai(final String bearer) {
        String urlnya = "http://ta.poliwangi.ac.id/~ti17183/laravel/public/api/peternak/kandang/setpanen";
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, urlnya, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Panennya", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", String.valueOf(0));
                params.put("kandang_id", Preferences.getIdKandang(getContext()));

                return params;

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + bearer);
                return params;
            }
        };
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

        cartesian.title("Berat Sarang 1");

        cartesian.yAxis(0).title("Nilai Berat Sarang 1");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("Data berat", 1200));
        set = Set.instantiate();
        set.data(seriesData);

        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");


        Line series1 = cartesian.line(series1Mapping);
        //for color https://docs.anychart.com/Appearance_Settings/Lines_Settings
        series1.name("Berat Sarang 1");
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