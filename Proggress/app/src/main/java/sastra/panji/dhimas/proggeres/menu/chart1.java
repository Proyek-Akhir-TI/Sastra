package sastra.panji.dhimas.proggeres.menu;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Random;

import sastra.panji.dhimas.proggeres.R;

public class chart1 extends AppCompatActivity {

    private  LineGraphSeries<DataPoint> series;
    public static  final Random RANDOM = new Random();
    private  int lastx = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart1);

        GraphView graphView = findViewById(R.id.graph);
        series = new LineGraphSeries<DataPoint>();
        graphView.addSeries(series);
        Viewport viewport = graphView.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(10);
        viewport.setScrollable(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setMinX(0);
        viewport.setMaxX(10);
    }
    @Override
    protected  void  onResume(){
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i=0; i<100; i++){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            addEntry();
                        }
                    });
                    try {
                        Thread.sleep(600);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void addEntry(){
    series.appendData(new DataPoint( lastx++,RANDOM.nextDouble()*10d ),true,10);
    }
}
