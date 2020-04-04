package com.codenerdz.example.coronaliveupdator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView totalCases;
    private TextView deaths;
    private TextView activeCases;
    private TextView recovers;
    private TextView lastUpdate;
    private TextView newCases;
    private TextView newDeaths;
    private TextView suspectedCases;
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

        getSupportActionBar().setTitle("   COVID-19 SRI LANKA UPDATER");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20d2f4")));
        requestCoronaUpdate();
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


}
