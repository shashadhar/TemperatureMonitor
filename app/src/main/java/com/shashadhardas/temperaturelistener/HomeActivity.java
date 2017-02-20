package com.shashadhardas.temperaturelistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.shashadhardas.temperaturelistener.databinding.ActivityHomeBinding;

import net.socket.SocketManager;

import java.util.ArrayList;
import java.util.Map;

import custom.MyMarkerView;
import model.Sensor;
import utils.DataChangedListener;


public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener ,OnChartValueSelectedListener,OnChartGestureListener, DataChangedListener {
    ActivityHomeBinding binding;
    LineChart mChart;
    String selectedScale="recent";
    String selectedSensorName="temperature0";
    Handler handler;

    BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(TemperatureApplication.ACTION_SENSOR_INITIALISED)){
                addListener();
            }
        }
    };

    private void addListener() {
        try {
            AllSensors.getInstance().addLister(selectedSensorName,this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void removeListener() {
        try {
            AllSensors.getInstance().removeListener(selectedSensorName,this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this,R.layout.activity_home);
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            ((Spinner) findViewById(R.id.scaleSpinner)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                   String value= (String)adapterView.getAdapter().getItem(i);
                   selectedScale=value.toLowerCase();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(TemperatureApplication.ACTION_SENSOR_INITIALISED);
            registerReceiver(receiver,intentFilter);
            // addListener();
            handler=new Handler();
            configureGraph();
            // addListener();
            displayView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public  void displayView(){
        try {
            onNavigationItemSelected(binding.navView.getMenu().findItem(R.id.nav_temp9));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            ActionBar bar = getSupportActionBar();
            int id = item.getItemId();

            if (id == R.id.nav_temp0) {
            removeListener();
            selectedSensorName="temperature0";
                bar.setTitle("Temperature0");

            } else if (id == R.id.nav_temp1) {
                removeListener();
                selectedSensorName="temperature1";
                bar.setTitle("Temperature1");
            } else if (id == R.id.nav_temp2) {
                removeListener();
                selectedSensorName="temperature2";
                bar.setTitle("Temperature2");
            } else if (id == R.id.nav_temp3) {
                removeListener();
                selectedSensorName="temperature3";
                bar.setTitle("Temperature3");
            } else if (id == R.id.nav_temp4) {
                removeListener();
                selectedSensorName="temperature4";
                bar.setTitle("Temperature4");

            }else if (id == R.id.nav_temp5) {
                removeListener();
                selectedSensorName="temperature5";
                bar.setTitle("Temperature5");

            }else if (id == R.id.nav_temp6) {
                removeListener();
                selectedSensorName="temperature6";
                bar.setTitle("Temperature6");
            }else if (id == R.id.nav_temp7) {
                removeListener();
                selectedSensorName="temperature7";
                bar.setTitle("Temperature7");

            }else if (id == R.id.nav_temp8) {
                removeListener();
                selectedSensorName="temperature8";
                bar.setTitle("Temperature8");

            }else if (id == R.id.nav_temp9) {
                removeListener();
                selectedSensorName="temperature9";
                bar.setTitle("Temperature9");

            }
            addListener();
            plotLineGraph();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void plotLineGraph(){
        try {
            // x-axis limit line


            XAxis xAxis = mChart.getXAxis();
            xAxis.enableGridDashedLine(10f, 10f, 0f);
            Sensor sensor=AllSensors.getInstance().getSensorByName(selectedSensorName);


            LimitLine ll1 = new LimitLine( sensor.getConfig().getMax(), String.format("Upper deviation limit(%s)",sensor.getConfig().getMax() ));
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);

            LimitLine ll2 = new LimitLine( sensor.getConfig().getMin(), String.format("Lower deviation Limit(%s)",sensor.getConfig().getMin() ));
            ll2.setLineWidth(4f);
            ll2.enableDashedLine(10f, 10f, 0f);
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            ll2.setTextSize(10f);


            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
            leftAxis.addLimitLine(ll1);
            leftAxis.addLimitLine(ll2);
            leftAxis.setAxisMaximum(200f);
            leftAxis.setAxisMinimum(-50f);
            //leftAxis.setYOffset(20f);
            leftAxis.enableGridDashedLine(10f, 10f, 0f);
            leftAxis.setDrawZeroLine(false);
            // limit lines are drawn behind data (and not on top)
            leftAxis.setDrawLimitLinesBehindData(true);

            mChart.getAxisRight().setEnabled(false);
            // add data
            setDataForLineChart();
            // mChart.animateX(2500);
            mChart.invalidate();

            // get the legend (only possible after setting data)
            Legend l = mChart.getLegend();

            // modify the legend ...
            l.setForm(Legend.LegendForm.LINE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configureGraph() {
        try {
            mChart = (LineChart) findViewById(R.id.graph);
            mChart.setOnChartGestureListener(this);
            mChart.setOnChartValueSelectedListener(this);
            mChart.setDrawGridBackground(false);
            // no description text
            mChart.getDescription().setEnabled(false);
            // enable touch gestures
            mChart.setTouchEnabled(true);
            // enable scaling and dragging
            mChart.setDragEnabled(true);
            mChart.setScaleEnabled(true);
            // if disabled, scaling can be done on x- and y-axis separately
            mChart.setPinchZoom(true);
            // create a custom MarkerView (extend MarkerView) and specify the layout
            // to use for it
            MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
            mv.setChartView(mChart); // For bounds control
            mChart.setMarker(mv); // Set the marker to the chart
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setDataForLineChart() {

        try {
            ArrayList<Entry> values = new ArrayList<Entry>();

            Sensor sensor= AllSensors.getInstance().getSensorByName(selectedSensorName);
            if(sensor!=null) {
                if(selectedScale.equals("minute") && sensor.getMinuteScaleData()!=null){
                    int i = 1;
                    for (Map.Entry dataEntry : sensor.getMinuteScaleData().entrySet()) {
                        values.add(new Entry(i, (float) dataEntry.getValue()));
                        i = i + 1;
                    }
                }else if(selectedScale.equals("recent") && sensor.getRecentScaleData()!=null){
                    int i = 1;
                    for (Map.Entry dataEntry : sensor.getRecentScaleData().entrySet()) {
                        values.add(new Entry(i, (float) dataEntry.getValue()));
                        i = i + 1;
                    }
                }

                LineDataSet set1;

                if(values!=null && values.size()>0) {

                    if (mChart.getData() != null &&
                            mChart.getData().getDataSetCount() > 0) {
                        set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                        set1.setValues(values);
                        mChart.getData().notifyDataChanged();
                        mChart.notifyDataSetChanged();
                    } else {
                        // create a dataset and give it a type
                        set1 = new LineDataSet(values, sensor.getName());

                        // set the line to be drawn like this "- - - - - -"
                        set1.enableDashedLine(10f, 5f, 0f);
                        set1.enableDashedHighlightLine(10f, 5f, 0f);
                        set1.setColor(Color.BLACK);
                        set1.setCircleColor(Color.BLACK);
                        set1.setLineWidth(1f);
                        set1.setCircleRadius(3f);
                        set1.setDrawCircleHole(false);
                        set1.setValueTextSize(9f);
                        set1.setDrawFilled(true);
                        set1.setFormLineWidth(1f);
                        set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                        set1.setFormSize(15.f);

                        if (com.github.mikephil.charting.utils.Utils.getSDKInt() >= 18) {
                            // fill drawable only supported on api level 18 and above
                            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                            set1.setFillDrawable(drawable);
                        } else {
                            set1.setFillColor(Color.BLACK);
                        }

                        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                        dataSets.add(set1); // add the datasets
                        // create a data object with the datasets
                        LineData data = new LineData(dataSets);
                        // set data
                        mChart.setData(data);
                    }
               }else{
                    mChart.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        try {
            Log.i("Gesture", "START, x: " + me.getX() + ", y: " + me.getY());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        try {
            Log.i("Gesture", "END, lastGesture: " + lastPerformedGesture);

            // un-highlight values after the gesture is finished and no single-tap
            if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
                mChart.highlightValues(null); // or highlightTouch(null) for callback to onNothingSelected(...)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        try {
            Log.i("LongPress", "Chart longpressed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        try {
            Log.i("DoubleTap", "Chart double-tapped.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        try {
            Log.i("SingleTap", "Chart single-tapped.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        try {
            Log.i("Fling", "Chart flinged. VeloX: " + velocityX + ", VeloY: " + velocityY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        try {
            Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        try {
            Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        try {
            Log.i("Entry selected", e.toString());
            Log.i("LOWHIGH", "low: " + mChart.getLowestVisibleX() + ", high: " + mChart.getHighestVisibleX());
            Log.i("MIN MAX", "xmin: " + mChart.getXChartMin() + ", xmax: " + mChart.getXChartMax() + ", ymin: " + mChart.getYChartMin() + ", ymax: " + mChart.getYChartMax());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected() {
        try {
            Log.i("Nothing selected", "Nothing selected.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            SocketManager.getInstance().clearAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dataUpdated(String scale) {
        try {
            Log.e("Mainactivity","data updated scale:"+scale);
            if(scale.equals("recent")) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        plotLineGraph();
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
