package com.codenerdz.example.coronaliveupdator.graph;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codenerdz.example.coronaliveupdator.R;
import com.codenerdz.example.coronaliveupdator.toolkit.ConstantToolkit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GraphDrawer extends DialogFragment {

    private View view;
    private WebView webView;
    private String graphType;

    JSONArray jsonArraySL = new JSONArray();;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.confirmed_cases_layout, container, false);
        initChart();
        return view;
    }

    private void initChart()
    {
        View stub =view.findViewById(R.id.line_chart_stub);

        if (stub instanceof ViewStub)
        {
            ((ViewStub)stub).setVisibility(View.VISIBLE);
            webView = (WebView)view.findViewById(R.id.daily_confirmed_line_chart_webview);

            WebSettings webSettings =
                    webView.getSettings();

            webSettings.setJavaScriptEnabled(true);

            webView.setWebChromeClient(new WebChromeClient());

            webView.setWebViewClient(new WebViewClient()
            {
                @Override
                public void onPageFinished(
                        WebView view,
                        String url)
                {
                    if(jsonArraySL.length() == 0)
                    {
                        loadLineChartData();
                    }
                }
            });

            selectLoadURL();
            webSettings.setDomStorageEnabled(true);

        }
    }

    private void selectLoadURL() {

        if(getFragmentManager().findFragmentByTag(ConstantToolkit.DAILY_CONFIRMED_CASES_FRAGMENT) != null)
        {
            webView.loadUrl("file:///android_asset/"+"html/dailyConfirmedLineChart.html");
            graphType = ConstantToolkit.DAILY_CONFIRMED_CASES_FRAGMENT;
        }
        else if(getFragmentManager().findFragmentByTag(ConstantToolkit.DAILY_DEATH_CASES_FRAGMENT) != null)
        {
            webView.loadUrl("file:///android_asset/"+"html/dailyDeathsLineChart.html");
            graphType = ConstantToolkit.DAILY_DEATH_CASES_FRAGMENT;
        }

    }

    private void loadLineChartData()
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="https://pomber.github.io/covid19/timeseries.json";
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (null != response) {
                            try {
                                jsonArraySL = response.getJSONArray("Sri Lanka");
                                StringBuilder textSL = new StringBuilder();
                                textSL.append("[");
                                modifyStringBuilder(textSL);

                                textSL.deleteCharAt(textSL.length()-1);
                                textSL.append("]");

                                webView.loadUrl("javascript:loadLinearChart("+textSL.toString()+")");
                            } catch (Exception e) {
                                //e.printStackTrace();
                            }
                        }
                    }

                    private StringBuilder modifyStringBuilder(StringBuilder textSL) throws JSONException {
                        String apiSelectionAttribute = "";
                        switch (graphType)
                        {
                            case ConstantToolkit.DAILY_CONFIRMED_CASES_FRAGMENT:
                                apiSelectionAttribute = "confirmed";
                                break;
                            case ConstantToolkit.DAILY_DEATH_CASES_FRAGMENT:
                                apiSelectionAttribute = "deaths";
                                break;
                            default:
                                    break;

                        }
                        for(int i=0;i<jsonArraySL.length();i++)
                        {
                            textSL.append("{\"date\":\""+jsonArraySL.getJSONObject(i).
                                    get("date").toString()+"\",\""+apiSelectionAttribute+"\":\""+
                                    jsonArraySL.getJSONObject(i).
                                            get(apiSelectionAttribute).toString()
                                    +"\"},");

                        }
                        return textSL;
                    }
                },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
    }

}
