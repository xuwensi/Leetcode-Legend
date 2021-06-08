package edu.uw.wensix.leetcodelegend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Anchor;
import com.anychart.graphics.vector.Stroke;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.uw.wensix.leetcodelegend.R;

public class VisualActivity extends AppCompatActivity implements View.OnClickListener {

    List<DataEntry> bar = new ArrayList<>();
    private static int[] records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual);
        records = get_json();
        Button lineGraph = findViewById(R.id.lineGraph);
        lineGraph.setOnClickListener(this);
        Button pieGraph = findViewById(R.id.pieGraph);
        pieGraph.setOnClickListener(this);
        findViewById(R.id.btnTimer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VisualActivity.this, TimerActivity.class));
            }
        });
        findViewById(R.id.btnPastProblem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VisualActivity.this, PastProblemActivity.class));
            }
        });
    }


    public int[] get_json() {
        String json;
        int[] dataArray = new int[] {0, 0, 0};
        try {
            InputStream is = getAssets().open("past_problem.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                bar.add(new ValueDataEntry(obj.getString("date"), obj.getInt("durationSecond")));
                if (obj.getString("difficulty").equals("easy")){
                    dataArray[0] = dataArray[0] + 1;
                } else if (obj.getString("difficulty").equals("medium")) {
                    dataArray[1] = dataArray[1] + 1;
                } else {
                    dataArray[2] = dataArray[2] + 1;
                }
            }

        } catch (IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataArray;
    }

    @Override
    public void onClick(View v) {
        AnyChartView anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        switch (v.getId()) {
            case R.id.lineGraph:
                Toast.makeText(this, "Line Graph clicked", Toast.LENGTH_SHORT).show();
                Cartesian cartesian = AnyChart.line();

                cartesian.animation(true);

                cartesian.padding(50d, 10d, 5d, 10d);

                cartesian.crosshair().enabled(true);
                cartesian.crosshair()
                        .yLabel(true)
                        .yStroke((Stroke) null, null, null, (String) null, (String) null);

                cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

                cartesian.title("Trend of time consumption");
                Set set = Set.instantiate();
                set.data(bar);
                Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");
                Line series1 = cartesian.line(series1Mapping);
                series1.name("Time Cost");
                series1.hovered().markers().enabled(true);
                series1.hovered().markers()
                        .type(MarkerType.CIRCLE)
                        .size(4d);
                series1.tooltip()
                        .position("right")
                        .anchor(String.valueOf(Anchor.LEFT_CENTER))
                        .offsetX(5d)
                        .offsetY(5d);
                cartesian.legend().enabled(true);
                cartesian.legend().fontSize(10d);
                cartesian.legend().padding(0d, 0d, 3d, 0d);


                anyChartView.setChart(cartesian);
                break;

            case R.id.pieGraph:
                Toast.makeText(this, "Pie Graph clicked", Toast.LENGTH_SHORT).show();
                Pie pie = AnyChart.pie();

                List<DataEntry> data = new ArrayList<>();
                data.add(new ValueDataEntry("Easy", records[0]));
                data.add(new ValueDataEntry("Medium", records[1]));
                data.add(new ValueDataEntry("Hard", records[2]));
                pie.data(data);
                anyChartView.setChart(pie);
                break;
        }
    }
}
