# android-json-data-cache
Json data cache for Android using SQLite.

You can use this cache to store your web services results and retrieve them in offline mode.

Simple as this sample :
```java
try {
    DataController dataController = new DataController(this); // this = context
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
```
