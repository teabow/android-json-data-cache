package com.teabow.app.jsoncache.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.teabow.app.jsoncache.R;
import com.teabow.app.jsoncache.db.model.DBField;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;


public class DatabaseHelper extends SQLiteOpenHelper {
	
	// Database Version
    private static final int DB_VERSION = 1;
 
    // Database Name
    private static final String DB_NAME = "jsonData";
 
    // Table Names
    private static final String TABLE = "db.table";
    
    private static final String DB_FIELD_PREFIX =  "db.table.field.";
    
    private static final String DB_FIELD_NAME_SUFFIX =  ".name";
    
    private static final String DB_FIELD_TYPE_SUFFIX =  ".type";
    
    private static final String DB_FIELD_COLUMN_SUFFIX =  ".column";
    
    private static final String DB_FIELD_CONSTRAINT_SUFFIX =  ".constraint";
    
    private final HashMap<String, DBField> fieldsMap;
    private final Properties properties;
    
    public DatabaseHelper(Context context) {
    	super(context, DB_NAME, null, DB_VERSION);
    	properties = new Properties();
		InputStream rawResource = context.getResources().openRawResource(R.raw.db_conf);
		try {
			properties.load(rawResource);
		} catch (IOException e) {
			Log.e(getClass().getCanonicalName(), "Cannot load properties.", e);
		}
		
		fieldsMap = new HashMap<>();
		setFields();
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		// creating required tables
        db.execSQL(this.createTableRequest());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + properties.getProperty(TABLE));
        // create new tables
        onCreate(db);
	}
	
	public String getTableName() {
		return this.properties.getProperty(TABLE);
	}
	
	public String createTableRequest() {
		
		StringBuilder request = new StringBuilder();
		request.append("CREATE TABLE IF NOT EXISTS ").append(this.properties.getProperty(TABLE)).append("(");
		ArrayList<String> primaryKeys = new ArrayList<>();
		
		ArrayList<DBField> dbFields = new ArrayList<>();
		for (String key : this.fieldsMap.keySet()) {
			DBField dbField = this.fieldsMap.get(key);
			dbFields.add(dbField);
		}
		Collections.sort(dbFields);
		
		for (DBField dbField : dbFields) {
			request.append(dbField.getName()).append(" ").append(dbField.getType()).append(",");
			if (DBField.Constraint.PRIMARY.equals(dbField.getConstraint())) {
				primaryKeys.add(dbField.getName());
			}
		}
		if (primaryKeys.size() > 0) {
			request.append("PRIMARY KEY(");
			for (String primaryKey : primaryKeys) {
				String sep = primaryKeys.indexOf(primaryKey) == primaryKeys.size() - 1 ? "" : ",";
				request.append(primaryKey).append(sep);
			}
			request.append(")");
		}
		request.append(");");
		
		Log.d("DEBUG", request.toString());
		return request.toString();
	}
	
	private void setFields() {
		for (Object entry : this.properties.keySet()) {
			if (entry instanceof String) {
				
				String sEntry = (String) entry;
				
				if (sEntry.startsWith(DB_FIELD_PREFIX)) {
					if (sEntry.endsWith(DB_FIELD_NAME_SUFFIX)) {
						String fieldName = this.properties.getProperty(sEntry);
						DBField dbField;
						if (this.fieldsMap.get(fieldName) != null) {
							dbField = this.fieldsMap.get(fieldName);
							dbField.setName(fieldName);
						} else {
							dbField = new DBField();
							dbField.setName(fieldName);
							this.fieldsMap.put(this.properties.getProperty(sEntry), dbField);
						}
					} else if (sEntry.endsWith(DB_FIELD_TYPE_SUFFIX)) {
						String fieldName = sEntry.substring(DB_FIELD_PREFIX.length(), sEntry.indexOf(DB_FIELD_TYPE_SUFFIX));
						DBField dbField;
						if (this.fieldsMap.get(fieldName) != null) {
							dbField = this.fieldsMap.get(fieldName);
							dbField.setType(this.properties.getProperty(sEntry));
						} else {
							dbField = new DBField();
							dbField.setType(this.properties.getProperty(sEntry));
							this.fieldsMap.put(fieldName, dbField);
						}
					}
					else if (sEntry.endsWith(DB_FIELD_COLUMN_SUFFIX)) {
						String fieldName = sEntry.substring(DB_FIELD_PREFIX.length(), sEntry.indexOf(DB_FIELD_COLUMN_SUFFIX));
						DBField dbField;
						if (this.fieldsMap.get(fieldName) != null) {
							dbField = this.fieldsMap.get(fieldName);
							dbField.setColumnIndex(Integer.parseInt(this.properties.getProperty(sEntry)));
						} else {
							dbField = new DBField();
							dbField.setColumnIndex(Integer.parseInt(this.properties.getProperty(sEntry)));
							this.fieldsMap.put(fieldName, dbField);
						}
					}
					else if (sEntry.endsWith(DB_FIELD_CONSTRAINT_SUFFIX)) {
						String fieldName = sEntry.substring(DB_FIELD_PREFIX.length(), sEntry.indexOf(DB_FIELD_CONSTRAINT_SUFFIX));
						DBField dbField;
						if (this.fieldsMap.get(fieldName) != null) {
							dbField = this.fieldsMap.get(fieldName);
							dbField.setConstraint(DBField.Constraint.valueOf(this.properties.getProperty(sEntry)));
						} else {
							dbField = new DBField();
							dbField.setConstraint(DBField.Constraint.valueOf(this.properties.getProperty(sEntry)));
							this.fieldsMap.put(fieldName, dbField);
						}
					}
				}
			}
		}
	}
	
}
