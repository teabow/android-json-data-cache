package com.teabow.app.jsoncache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.teabow.app.jsoncache.db.controller.DataController;
import com.teabow.app.jsoncache.db.listener.DBResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "JSON_CACHE_DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDataController();
    }

    /**
     * Initializes the json data controller
     */
    private void initDataController() {
        try {
            DataController dataController = new DataController(this);
            JSONObject jsonData = new JSONObject();
            jsonData.put("test", "value");
            dataController.saveData("serviceName", "", jsonData.toString(), new Date().getTime());
            dataController.getData("serviceName", "", new DBResultListener() {
                @Override
                public void onDBResult(JSONObject result) {
                    Log.d(DEBUG_TAG, result.toString());
                }
            });
        } catch (JSONException e) {
            Log.e(DEBUG_TAG, e.getMessage(), e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
