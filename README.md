# DroidLiteORM 🧩

A **lightweight ORM library for Android** built on top of SQLiteOpenHelper. Inspired by Laravel’s Eloquent, **DroidLiteORM** makes database access in Android clean, fluent, and intuitive — with support for relationships, eager loading, and convention-based models.

---

## 🚀 Features

- ✅ Fluent Laravel-style API (`create()`, `update()`, `find()`, etc.)
- ✅ Eager loading via `with()`
- ✅ Relationship handling (`hasMany`, `belongsTo`, etc.)
- ✅ Minimal setup — no annotations or code generation
- ✅ Convention-based model structure
- ✅ 100% built on SQLite — no extra dependencies
- ✅ Easy to extend and customize

---

## 📦 Installation

Coming soon via Maven/Gradle.

For now, clone or download the library and include it in your Android project manually.

```bash
https://github.com/leynnnnnn0/droid-lite-orm.git
````

---

## 🛠️ Getting Started

### 1. Define Your Model

```java
public class User extends DBHelper<User> {
    Context context;

    public User(Context context)  {
        super(context, "users");
        this.context = context;
    }


    @Override
    public String[] fillable() {
        return new String[]{
                "id",
                "username",
                "email",
                "role"
        };
    }

    public static LinkedHashMap<String, String> schema() {
        LinkedHashMap<String, String> columns = new LinkedHashMap<>();
        columns.put("id", "INTEGER PRIMARY KEY AUTOINCREMENT");
        columns.put("username", "TEXT UNIQUE");
        columns.put("email", "TEXT UNIQUE");
        columns.put("password", "TEXT");
        columns.put("role", "TEXT DEFAULT 'user'");
        return columns;
    }

    @Override
    public LinkedHashMap<String, String> columns() {
        return schema();
    }

    @Override
    public HashMap<String, Class<? extends DBHelper<?>>> relations() {
        return null;
    }

}
```

### 2. Basic Usage

```java
product.create(new HashMap<>() {{
    put("image", imageViewToByte(image));
    put("name", productName);
    put("description", productDescription);
    put("price", productPrice);
    put("stock", productStock);
}});
```

### 3. Eager Loading

```java
List<User> users = new User().with("posts").get();
```

### 4. Update and Delete

```java
product.update(new HashMap<>(){{
    put("image", imageViewToByte(image));
    put("name", productName);
    put("description", productDescription);
    put("price", productPrice);
    put("stock", productStock);
}}, product.getId());

product.delete(product.getId());
```

## 📐 Naming Conventions

To keep things lightweight, **DroidLiteORM uses naming conventions**:

| Convention               | Description                                                |
| ------------------------ | ---------------------------------------------------------- |
| Table name               | Plural snake\_case of model name (`User` → `users`)        |
| Primary key              | `id` by default                                            |
| Foreign key              | `{model}_id` (e.g. `user_id`)                              |
| Relationship method name | Matches the related model name (`posts`, `comments`, etc.) |

---

Query with eager loading:

```java
List<Post> posts = new Post().with("user").get();
```

---

## 📚 Documentation

More detailed documentation is coming soon.

---

## 🧪 Roadmap

* [x] Basic CRUD
* [x] Eager loading
* [x] Relationship support
* [ ] Query builder
* [ ] Migrations
* [ ] Publish to Maven Central

---

## 🤝 Contributing

Contributions are welcome! Please submit a pull request or open an issue if you have ideas or bugs to report.

---

## 📄 License

MIT License © 2025 \Nathaniel

```

