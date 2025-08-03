package com.example.mycanteen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.mycanteen.model.Cart;
import com.example.mycanteen.model.CartProduct;
import com.example.mycanteen.model.Order;
import com.example.mycanteen.model.OrderProduct;
import com.example.mycanteen.model.Product;
import com.example.mycanteen.model.User;
import com.example.mycanteen.service.CurrentUser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class DBHelper<T extends DBHelper<T>> extends SQLiteOpenHelper {
    public String tableName;

    public HashMap<String, String> wheres;
    public List<String> withs;

    protected Context context;
    public DBHelper(Context context, String tableName) {
        super(context, "my_canteen.db", null, 6);
        wheres = new HashMap<>();
        withs = new LinkedList<>();
        this.context = context;
        this.tableName = tableName;
    }
// Create and update should return an object
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
        db.execSQL(createTableQuery("users", User.schema()));
        db.execSQL(createTableQuery("products", Product.schema()));
        db.execSQL(createTableQuery("carts", Cart.schema()));
        db.execSQL(createTableQuery("cart_products", CartProduct.schema()));
        db.execSQL(createTableQuery("orders", Order.schema()));
        db.execSQL(createTableQuery("order_products", OrderProduct.schema()));

        db.execSQL("INSERT INTO users(username, email, password, role) VALUES ('admin', 'admin@gmail.com', 'admin1234', 'admin'), ('user', 'user@gmail.com', 'password', 'user')");
    }

    private String createTableQuery(String tableName, LinkedHashMap<String, String> columns) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");

        String foreignKeys = null;
        for (Map.Entry<String, String> entry : columns.entrySet()) {
            if (entry.getKey().equals("FOREIGN_KEYS")) {
                foreignKeys = entry.getValue();
                continue;
            }
            builder.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        }

        if (foreignKeys != null) {
            builder.append(foreignKeys).append(", ");
        }

        builder.setLength(builder.length() - 2);
        builder.append(");");
        return builder.toString();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "users");
        db.execSQL("DROP TABLE IF EXISTS " + "products");
        db.execSQL("DROP TABLE IF EXISTS " + "carts");
        db.execSQL("DROP TABLE IF EXISTS " + "cart_products");
        db.execSQL("DROP TABLE IF EXISTS " + "orders");
        db.execSQL("DROP TABLE IF EXISTS " + "order_products");
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

    public Cursor first() {
        SQLiteDatabase db = this.getWritableDatabase();

        String mainAlias = "main";
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");

        // SELECT main_table.*
        for (String field : fillable()) {
            query.append(mainAlias).append(".").append(field).append(" AS ").append(field).append(", ");
        }

        // JOIN related tables and add their select columns
        HashMap<String, Class<? extends DBHelper<?>>> relations = relations();
        for (String relationKey : withs) {
            Class<? extends DBHelper<?>> relClass = relations.get(relationKey.toLowerCase()); // you can map withs to keys
            if (relClass == null) continue;

            try {
                DBHelper<?> relInstance = relClass.getDeclaredConstructor(Context.class).newInstance(context);
                String alias = relationKey;

                for (String field : relInstance.fillable()) {
                    query.append(alias).append(".").append(field)
                            .append(" AS ").append(alias.toLowerCase()).append("_").append(field).append(", ");
                }

            } catch (Exception e) {
                Log.e("DBHelper", "Failed to load relation: " + e.getMessage(), e);
            }
        }

        // Remove last comma
        if (query.charAt(query.length() - 2) == ',') {
            query.setLength(query.length() - 2);
        }

        // FROM clause
        query.append(" FROM ").append(this.tableName).append(" ").append(mainAlias).append(" ");

        // JOIN clause
        for (String relationKey : withs) {
            Class<? extends DBHelper<?>> relClass = relations.get(relationKey.toLowerCase());
            if (relClass == null) continue;

            try {
                DBHelper<?> relInstance = relClass.getDeclaredConstructor(Context.class).newInstance(context);
                String relTable = relInstance.tableName;
                String alias = relationKey;

                // Assumes foreign key is {relationKey}_id
                String fk = alias.toLowerCase() + "_id";

                query.append("LEFT JOIN ").append(relTable).append(" ").append(alias)
                        .append(" ON ").append(mainAlias).append(".").append(fk)
                        .append(" = ").append(alias).append(".id ");
            } catch (Exception e) {
                Log.e("DBHelper", "Join failed: " + e.getMessage(), e);
            }
        }

        // WHERE clause
        if (!wheres.isEmpty()) {
            query.append("WHERE ");
            for (Map.Entry<String, String> column : wheres.entrySet()) {
                String value = "'" + column.getValue() + "'";
                query.append(mainAlias).append(".").append(column.getKey()).append(" = ").append(value).append(" AND ");
            }
            query.setLength(query.length() - 5); // remove last ' AND '
        }

        query.append(" LIMIT 1;");

        Log.d("DynamicQuery", query.toString());

        wheres.clear();
        withs.clear();

        return db.rawQuery(query.toString(), null);
    }


    public Cursor get() {
        SQLiteDatabase db = this.getWritableDatabase();

        String mainAlias = "main";
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");

        // Main table columns
        for (String field : fillable()) {
            query.append(mainAlias).append(".").append(field)
                    .append(" AS ").append(field).append(", ");
        }

        // Relations handling
        HashMap<String, Class<? extends DBHelper<?>>> relations = relations();
        for (String relationName : withs) {
            Class<? extends DBHelper<?>> relClass = relations.get(relationName.toLowerCase());
            if (relClass == null) continue;

            try {
                DBHelper<?> relInstance = relClass.getDeclaredConstructor(Context.class).newInstance(context);
                String relAlias = relationName;

                for (String relField : relInstance.fillable()) {
                    query.append(relAlias).append(".").append(relField)
                            .append(" AS ").append(relationName.toLowerCase()).append("_").append(relField)
                            .append(", ");
                }
            } catch (Exception e) {
                Log.e("DBHelper", "Relation load failed: " + e.getMessage(), e);
            }
        }

        // Remove last comma
        if (query.charAt(query.length() - 2) == ',') {
            query.setLength(query.length() - 2);
        }

        // FROM clause
        query.append(" FROM ").append(this.tableName).append(" ").append(mainAlias).append(" ");

        // JOIN clauses
        for (String relationName : withs) {
            Class<? extends DBHelper<?>> relClass = relations.get(relationName.toLowerCase());
            if (relClass == null) continue;

            try {
                DBHelper<?> relInstance = relClass.getDeclaredConstructor(Context.class).newInstance(context);
                String relTable = relInstance.tableName;
                String relAlias = relationName;
                String foreignKey = relationName.toLowerCase() + "_id"; // e.g. product_id

                query.append("LEFT JOIN ").append(relTable).append(" ").append(relAlias)
                        .append(" ON ").append(mainAlias).append(".").append(foreignKey)
                        .append(" = ").append(relAlias).append(".id ");
            } catch (Exception e) {
                Log.e("DBHelper", "Join failed: " + e.getMessage(), e);
            }
        }

        // WHERE clause
        if (!wheres.isEmpty()) {
            query.append("WHERE ");
            for (Map.Entry<String, String> column : wheres.entrySet()) {
                String value = "'" + column.getValue() + "'";
                query.append(mainAlias).append(".").append(column.getKey()).append(" = ").append(value).append(" AND ");
            }
            query.setLength(query.length() - 5); // Remove trailing ' AND '
        }

        query.append(";");

        Log.d("DynamicQuery", query.toString());

        // Reset query state
        wheres.clear();
        withs.clear();

        return db.rawQuery(query.toString(), null);
    }

    public Cursor all(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + this.tableName, null);
    }

    public abstract String[] fillable();

    public abstract LinkedHashMap<String, String> columns();

    public abstract HashMap<String, Class<? extends DBHelper<?>>> relations();

//

    public DBHelper<T> with(Class<? extends DBHelper<?>> relationModel) {
        if (!withs.contains(relationModel.getSimpleName())) {
            withs.add(relationModel.getSimpleName());
        }
        return this;
    }


    public T mapCursor(Cursor cursor) {
        try {
            if (cursor == null || cursor.getCount() == 0) return null;

            cursor.moveToFirst();

            @SuppressWarnings("unchecked")
            T instance = (T) this.getClass()
                    .getDeclaredConstructor(Context.class)
                    .newInstance(context);




            for (String field : fillable()) {
                int index = cursor.getColumnIndex(field);
                if (index == -1) continue;
                Field classField = instance.getClass().getDeclaredField(field);
                Log.d("test", classField.getName());
                classField.setAccessible(true);

                switch (cursor.getType(index)) {
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
            HashMap<String, Class<? extends DBHelper<?>>> relations = instance.relations();
            if (relations != null && !relations.isEmpty()) {

               for (Map.Entry<String, Class<? extends DBHelper<?>>> entry : relations.entrySet()) {
                   String relationName = entry.getKey();

                   Class<? extends DBHelper<?>> relationClass = entry.getValue();

                   DBHelper<?> relatedInstance = relationClass
                           .getDeclaredConstructor(Context.class)
                           .newInstance(context);

                   String prefix = relationName + "_"; // e.g., "product_"

                   // Fill fields of related model based on prefix + fillable()
                   for (String relField : relatedInstance.fillable()) {
                       String columnName = prefix + relField;
                       // product_id
                       int index = cursor.getColumnIndex(columnName);
                       if (index == -1) continue;

                       Field f = relatedInstance.getClass().getDeclaredField(relField);
                       f.setAccessible(true);

                       switch (cursor.getType(index)) {
                           case Cursor.FIELD_TYPE_INTEGER:
                               f.set(relatedInstance, cursor.getInt(index));
                               break;
                           case Cursor.FIELD_TYPE_STRING:
                               f.set(relatedInstance, cursor.getString(index));
                               break;
                           case Cursor.FIELD_TYPE_FLOAT:
                               f.set(relatedInstance, cursor.getFloat(index));
                               break;
                           case Cursor.FIELD_TYPE_BLOB:
                               f.set(relatedInstance, cursor.getBlob(index));
                               break;
                           case Cursor.FIELD_TYPE_NULL:
                               f.set(relatedInstance, null);
                               break;
                       }
                   }

                   // Attach the related instance to the main instance's public field
                   Field relField = instance.getClass().getDeclaredField(relationName);
                   relField.setAccessible(true);
                   relField.set(instance, relatedInstance);
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

        try {
            while (cursor.moveToNext()) {
                @SuppressWarnings("unchecked")
                T item = (T) this.getClass()
                        .getDeclaredConstructor(Context.class)
                        .newInstance(context);

                // 1. Map scalar fields
                for (String field : fillable()) {
                    int index = cursor.getColumnIndex(field);
                    if (index == -1) continue;
                    Field classField = item.getClass().getDeclaredField(field);
                    classField.setAccessible(true);
                    setFieldFromCursor(cursor, item, classField, index);
                }

                // 2. Map related models (if any)
                HashMap<String, Class<? extends DBHelper<?>>> relations = item.relations();
                if (relations != null && !relations.isEmpty()) {
                    for (Map.Entry<String, Class<? extends DBHelper<?>>> entry : relations.entrySet()) {
                        String relationName = entry.getKey();
                        Class<? extends DBHelper<?>> relationClass = entry.getValue();

                        DBHelper<?> relatedInstance = relationClass
                                .getDeclaredConstructor(Context.class)
                                .newInstance(context);

                        String prefix = relationName + "_";

                        for (String relField : relatedInstance.fillable()) {
                            String columnName = prefix + relField;
                            int index = cursor.getColumnIndex(columnName);
                            if (index == -1) continue;

                            Field f = relatedInstance.getClass().getDeclaredField(relField);
                            f.setAccessible(true);
                            setFieldFromCursor(cursor, relatedInstance, f, index);
                        }

                        Field relField = item.getClass().getDeclaredField(relationName);
                        relField.setAccessible(true);
                        relField.set(item, relatedInstance);
                    }
                }

                list.add(item);
            }
        } catch (Exception e) {
            Log.e("DBHelper", "List mapping failed: " + e.getMessage(), e);
        } finally {
            cursor.close();
        }

        return list;
    }

    private void setFieldFromCursor(Cursor cursor, Object target, Field field, int index) throws IllegalAccessException {
        switch (cursor.getType(index)) {
            case Cursor.FIELD_TYPE_INTEGER:
                field.set(target, cursor.getInt(index));
                break;
            case Cursor.FIELD_TYPE_STRING:
                field.set(target, cursor.getString(index));
                break;
            case Cursor.FIELD_TYPE_FLOAT:
                field.set(target, cursor.getFloat(index));
                break;
            case Cursor.FIELD_TYPE_BLOB:
                field.set(target, cursor.getBlob(index));
                break;
            case Cursor.FIELD_TYPE_NULL:
                field.set(target, null);
                break;
        }
    }


}
