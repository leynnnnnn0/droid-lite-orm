package com.example.mycanteen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mycanteen.model.Product;
import com.example.mycanteen.model.User;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class DBHelper<T extends DBHelper<T>> extends SQLiteOpenHelper {
    public String tableName;

    public HashMap<String, String> wheres;

    protected Context context;
    public DBHelper(Context context, String tableName) {
        super(context, "my_canteen.db", null, 2);
        wheres = new HashMap<>();
        this.context = context;
        this.tableName = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableQuery("users", User.schema()));
        db.execSQL(createTableQuery("products", Product.schema()));
    }

    private String createTableQuery(String tableName, LinkedHashMap<String, String> columns) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            builder.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        }
        builder.setLength(builder.length() - 2);
        builder.append(");");
        return builder.toString();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "users");
        db.execSQL("DROP TABLE IF EXISTS " + "products");
        onCreate(db);
    }


    public Boolean create(HashMap<String, Object> data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) {
                contentValues.putNull(key);
            } else if (value instanceof String) {
                contentValues.put(key, (String) value);
            } else if (value instanceof Integer) {
                contentValues.put(key, (Integer) value);
            } else if (value instanceof Float) {
                contentValues.put(key, (Float) value);
            } else if (value instanceof Double) {
                contentValues.put(key, (Double) value);
            } else if (value instanceof Long) {
                contentValues.put(key, (Long) value);
            } else if (value instanceof byte[]) {
                contentValues.put(key, (byte[]) value);
            } else {
                throw new IllegalArgumentException("Unsupported data type for column: " + key);
            }
        }

        try {
            long result = db.insert(this.tableName, null, contentValues);
            return result != -1;
        } catch (Exception e) {
            Log.e("DB_ERROR", "Insert failed: " + e.getMessage(), e);
            return false;
        }
    }


    public Boolean update(HashMap<String, Object> data, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) {
                contentValues.putNull(key);
            } else if (value instanceof String) {
                contentValues.put(key, (String) value);
            } else if (value instanceof Integer) {
                contentValues.put(key, (Integer) value);
            } else if (value instanceof Float) {
                contentValues.put(key, (Float) value);
            } else if (value instanceof Double) {
                contentValues.put(key, (Double) value);
            } else if (value instanceof Long) {
                contentValues.put(key, (Long) value);
            } else if (value instanceof byte[]) {
                contentValues.put(key, (byte[]) value);
            } else {
                throw new IllegalArgumentException("Unsupported data type for column: " + key);
            }
        }
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName +" WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor.getCount() == 0) return false;
        long result = db.update(tableName, contentValues, "id = ?", new String[]{String.valueOf(id)});
        cursor.close();
        return result != -1;
    }

    public Boolean delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor.getCount() == 0) return false;
        long result = db.delete(tableName, "id = ?", new String[]{String.valueOf(id)});
        cursor.close();
        return result != -1;
    }

    public Cursor findById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + this.tableName + " WHERE id = " + id;
        Log.d("query", query);
        return db.rawQuery(query, null);
    }

    public DBHelper<T> where(String column, String value){
        wheres.put(column, value);
        return this;
    }

    public Cursor first(){
        SQLiteDatabase db = this.getWritableDatabase();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(this.tableName).append(" WHERE ");
        for(Map.Entry<String, String> column: this.wheres.entrySet()){
            String value = "'" + column.getValue() + "'";
            query.append(column.getKey()).append(" = ").append(value);
            if(!wheres.isEmpty()) query.append(" AND ");
        }
        if (!query.toString().isEmpty()) {
            query.setLength(query.length() - 5);
        }
        query.append(" LIMIT 1;");
        wheres.clear();
        return db.rawQuery(query.toString(), null);
    }

    public Cursor get(){
        SQLiteDatabase db = this.getWritableDatabase();
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM ").append(this.tableName).append(" WHERE ");
        for(Map.Entry<String, String> column: this.wheres.entrySet()){
            String value = "'" + column.getValue() + "'";
           query.append(column.getKey()).append(" = ").append(value);
           if(!wheres.isEmpty()) query.append(" AND ");
        }
        if (!query.toString().isEmpty()) {
            query.setLength(query.length() - 5);
        }
        query.append(";");
        wheres.clear();
        return db.rawQuery(query.toString(), null);
    }
    public Cursor all(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + this.tableName, null);
    }

    public abstract String[] fillable();

    public abstract LinkedHashMap<String, String> columns();

    public T mapCursor(Cursor cursor) {
        try {
            if (cursor == null || cursor.getCount() == 0) return null;

            cursor.moveToFirst();
            @SuppressWarnings("unchecked")
            T instance = (T) this.getClass()
                    .getDeclaredConstructor(Context.class)
                    .newInstance(context);

            for (String field : fillable()) {
                int index = cursor.getColumnIndexOrThrow(field);
                Field classField = instance.getClass().getDeclaredField(field);
                classField.setAccessible(true);

                int type = cursor.getType(index);
                switch (type) {
                    case Cursor.FIELD_TYPE_INTEGER:
                        classField.set(instance, cursor.getInt(index));
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        classField.set(instance, cursor.getString(index));
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        classField.set(instance, cursor.getFloat(index));
                        break;
                    case Cursor.FIELD_TYPE_BLOB:
                        classField.set(instance, cursor.getBlob(index));
                        break;
                    case Cursor.FIELD_TYPE_NULL:
                        classField.set(instance, null);
                        break;

                }
            }
            cursor.close();
            return instance;
        } catch (Exception e) {
            Log.e("DBHelper", "Mapping failed: " + e.getMessage(), e);
            return null;
        }
    }

    public ArrayList<T> mapCursorList(Cursor cursor) {
        ArrayList<T> list = new ArrayList<>();
        if (cursor == null || cursor.getCount() == 0) return list;

        while (cursor.moveToNext()) {
            try {
                @SuppressWarnings("unchecked")
                T item = (T) this.getClass().getDeclaredConstructor(Context.class).newInstance(context);

                for (String field : fillable()) {
                    int index = cursor.getColumnIndexOrThrow(field);
                    Field classField = item.getClass().getDeclaredField(field);
                    classField.setAccessible(true);

                    int type = cursor.getType(index);
                    switch (type) {
                        case Cursor.FIELD_TYPE_INTEGER:
                            classField.set(item, cursor.getInt(index));
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            classField.set(item, cursor.getString(index));
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            classField.set(item, cursor.getFloat(index));
                            break;
                        case Cursor.FIELD_TYPE_BLOB:
                            classField.set(item, cursor.getBlob(index));
                            break;
                        case Cursor.FIELD_TYPE_NULL:
                            classField.set(item, null);
                            break;
                    }
                }
                list.add(item);
            } catch (Exception e) {
                Log.e("DBHelper", "List mapping failed: " + e.getMessage(), e);
            }
        }
        cursor.close();
        return list;
    }
}
