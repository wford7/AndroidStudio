package com.gymrattrax.gymrattrax;

import android.graphics.Canvas;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Spinner;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.GridLabelRenderer;

import java.util.Date;


public class ProgressActivity extends ActionBarActivity {

    private Spinner GraphSpin;
    private GraphView graph;
    private GridLabelRenderer o;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_progress);

        graph = (GraphView) findViewById(R.id.graph);
        o = new GridLabelRenderer(graph);
        graph.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
        GraphSpin = (Spinner)findViewById(R.id.graph_spinner);


        GraphSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String input = GraphSpin.getItemAtPosition(position).toString();
                // display appropriate graph

                switch(input){
                    case "Weight":
                        graph.removeAllSeries();
                        graph.setTitle("Weight");
                        graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
                        graph.getGridLabelRenderer().setVerticalAxisTitle("Weight");

                        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(1, 250),
                                new DataPoint(2, 248),
                                new DataPoint(3, 240),
                                new DataPoint(4, 270),
                                new DataPoint(5, 250)
                        });
                        graph.addSeries(series);
                        break;

                    case "Calories":
                        graph.removeAllSeries();
                        graph.setTitle("Calories Burned");
                        graph.getGridLabelRenderer().setHorizontalAxisTitle("Date");
                        graph.getGridLabelRenderer().setVerticalAxisTitle("Calories");

                        LineGraphSeries<DataPoint> series1 = new LineGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, 1),
                                new DataPoint(1, 5),
                                new DataPoint(2, 3),
                                new DataPoint(3, 2),
                                new DataPoint(4, 6)
                        });
                        graph.addSeries(series1);
                        break;
                }

//                DBHelper dbh = new DBHelper(ProgressActivity.this);
//                String[][] dbValues = dbh.secretDebugDeleteFromFinalReleaseRawQuery(input);
//                dbh.close();

//                updateLayoutScroll.removeAllViews();
//                updateLayoutScroll.addView(tableItself);
//                linearLayout.removeAllViews();
//                linearLayout.addView(table);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_progress, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
