package com.teabow.app.jsoncache.db.controller;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.teabow.app.jsoncache.db.DatabaseHelper;
import com.teabow.app.jsoncache.db.listener.DBResultListener;

public class DataController {
	
	private static final String REQUEST =  "request";
	private static final String PARAMS =  "params";
	private static final String RESULT =  "result";
	private static final String LAST_DATE =  "lastDate";
	
	private static final int RESULT_COLUM_INDEX =  2;
	
	private Context context;
	
	public DataController(Context context) {
		this.context = context;
	}
	
	public void getData(String request, String params, DBResultListener listener) {
		new GetDataAsyncTask().execute(request, params, listener);
	}
	
	public void saveData(String request, String params, String result, long lastDate) {
		new SaveDataAsyncTask().execute(request, params, result, lastDate);
	}

	private long pSaveData(String request, String params, String result, long lastDate) {
		
		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(REQUEST, request);
	    values.put(PARAMS, params);
	    values.put(RESULT, result);
	    values.put(LAST_DATE, lastDate);
		
		// insert row
	    StringBuilder delete = new StringBuilder();
	    delete.append(REQUEST).append(" LIKE '").append(request).
		append("' AND ").append(PARAMS).append(" LIKE '").append(params).append("'");
	    
	    db.delete(databaseHelper.getTableName(), delete.toString(), null);
	    long dataId = db.insert(databaseHelper.getTableName(), null, values);
		
	    Log.d("DEBUG", "pSaveData : " + request + "(" + params + ") => " + dataId);
	    
	    db.close();
	    return dataId;
	}
	
	private JSONObject pGetData(String request, String params) throws JSONException {
		
		Log.d("DEBUG", "pGetData : " + request + "(" + params + ")");
		
		DatabaseHelper databaseHelper = new DatabaseHelper(context);
		SQLiteDatabase db = databaseHelper.getReadableDatabase();
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM ").append(databaseHelper.getTableName()).
		append(" WHERE ").append(REQUEST).append(" LIKE '").append(request).
		append("' AND ").append(PARAMS).append(" LIKE '").append(params).append("'");
		
		JSONObject result = null;
		Cursor cursor = db.rawQuery(query.toString(), null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				result = new JSONObject(cursor.getString(RESULT_COLUM_INDEX));
			}
			cursor.close();
		}
		
		Log.d("DEBUG", "pGetData result : " + result);
		
		db.close();
		return result;
	}
	
	class SaveDataAsyncTask extends AsyncTask<Object, String, Long> {

		@Override
		protected Long doInBackground(Object... args) {
			String request = (String) args[0];
			String params = (String) args[1];
			String result = (String) args[2];
			long lastDate = (Long) args[3];
			return pSaveData(request, params, result, lastDate);
		}
	}
	
	class GetDataAsyncTask extends AsyncTask<Object, String, Void> {

		@Override
		protected Void doInBackground(Object... args) {
			String request = (String) args[0];
			String params = (String) args[1];
			DBResultListener listener = (DBResultListener) args[2];
			JSONObject result = null;
			try {
				result = pGetData(request, params);
			} catch (JSONException e) {
				Log.e(getClass().getCanonicalName(), e.getMessage());
			}
			listener.onDBResult(result);
			return null;
		}
	}
	
	

}
