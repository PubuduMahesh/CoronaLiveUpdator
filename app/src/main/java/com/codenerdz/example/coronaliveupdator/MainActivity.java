package com.codenerdz.example.coronaliveupdator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codenerdz.example.coronaliveupdator.graph.GraphDrawer;
import com.codenerdz.example.coronaliveupdator.toolkit.ConstantToolkit;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView totalCases;
    private TextView deaths;
    private TextView activeCases;
    private TextView recovers;
    private TextView lastUpdate;
    private TextView newCases;
    private TextView newDeaths;
    private TextView suspectedCases;
    private CardView cardViewTotalCases;
    private CardView cardViewTotalDeaths;
    private CardView cardViewTotalRecovers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        totalCases = (TextView)findViewById(R.id.total_cases);
        deaths = (TextView)findViewById(R.id.total_deaths);
        activeCases = (TextView)findViewById(R.id.active_cases);
        recovers = (TextView)findViewById(R.id.total_recovers);
        lastUpdate = (TextView)findViewById(R.id.last_update);
        newCases = (TextView)findViewById(R.id.new_cases);
        newDeaths = (TextView)findViewById(R.id.new_deaths);
        suspectedCases = (TextView)findViewById(R.id.suspected_cases);

        cardViewTotalCases = (CardView) findViewById(R.id.card_total_cases);
        cardViewTotalDeaths = (CardView) findViewById(R.id.card_total_deaths);
        cardViewTotalRecovers = (CardView) findViewById(R.id.card_total_recovers);

        getSupportActionBar().setTitle("   COVID-19 SRI LANKA UPDATER");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20d2f4")));
        requestCoronaUpdate();

        addTouchEvent();
    }

    private void requestCoronaUpdate() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://hpb.health.gov.lk/api/get-current-statistical";
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (null != response) {
                            try {
                                totalCases.setText(response.getJSONObject("data").get("local_total_cases").toString());
                                deaths.setText(response.getJSONObject("data").get("local_deaths").toString());
                                activeCases.setText(response.getJSONObject("data").get("local_active_cases").toString());
                                recovers.setText(response.getJSONObject("data").get("local_recovered").toString());
                                newCases.setText(response.getJSONObject("data").get("local_new_cases").toString());
                                newDeaths.setText(response.getJSONObject("data").get("local_new_deaths").toString());
                                suspectedCases.setText(response.getJSONObject("data").get("local_total_number_of_individuals_in_hospitals").toString());
                                lastUpdate.setText(response.getJSONObject("data").get("update_date_time").toString());


                            } catch (Exception e) {
                                //e.printStackTrace();
                            }
                        }
                    }
                },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

    private void addTouchEvent()
    {
        cardViewTotalCases.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openGraphDrawingFragment(ConstantToolkit.DAILY_CONFIRMED_CASES_FRAGMENT);
                return true;
            }
        });

        cardViewTotalDeaths.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openGraphDrawingFragment(ConstantToolkit.DAILY_DEATH_CASES_FRAGMENT);
                return true;
            }
        });

        cardViewTotalRecovers.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openGraphDrawingFragment(ConstantToolkit.DAILY_RECOVERS_CASES_FRAGMENT);
                return true;
            }
        });
    }

    private void openGraphDrawingFragment(String tag)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        GraphDrawer graphFragment = new GraphDrawer();
        graphFragment.show(fragmentManager, tag);


    }

}
