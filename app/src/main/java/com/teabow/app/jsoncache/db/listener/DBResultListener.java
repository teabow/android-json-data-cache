package com.teabow.app.jsoncache.db.listener;

import org.json.JSONObject;

public interface DBResultListener {

	void onDBResult(JSONObject result);
}
