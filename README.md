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

To use the DataController class as it, you have to add this `db_conf` file in your `res/raw` app directory :
```
db.version=1
db.name=jsonData

db.table=data

db.table.field.request.name=request
db.table.field.request.type=TEXT
db.table.field.request.column=0
db.table.field.request.constraint=PRIMARY

db.table.field.params.name=params
db.table.field.params.type=TEXT
db.table.field.params.column=1
db.table.field.params.constraint=PRIMARY

db.table.field.result.name=result
db.table.field.result.type=TEXT
db.table.field.result.column=2
db.table.field.result.constraint=NONE

db.table.field.lastDate.name=lastDate
db.table.field.lastDate.type=INTEGER
db.table.field.lastDate.column=3
db.table.field.lastDate.constraint=NONE
```
