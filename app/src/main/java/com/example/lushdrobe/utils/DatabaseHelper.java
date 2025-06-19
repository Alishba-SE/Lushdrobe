package com.example.lushdrobe.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.lushdrobe.models.Product;
import com.example.lushdrobe.models.User;
import com.example.lushdrobe.models.CartItem;
import com.example.lushdrobe.models.Order;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version - INCREMENTED to force recreation
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "LushdrobeDB";

    // Table Names
    private static final String TABLE_USERS = "users";
    private static final String TABLE_PRODUCTS = "products";
    private static final String TABLE_CART = "cart";
    private static final String TABLE_ORDERS = "orders";

    // Common column names
    private static final String KEY_ID = "id";

    // Users Table Columns
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_PASSWORD = "password";

    // Products Table Columns
    private static final String KEY_PRODUCT_NAME = "name";
    private static final String KEY_PRODUCT_DESC = "description";
    private static final String KEY_PRODUCT_PRICE = "price";
    private static final String KEY_PRODUCT_IMAGE = "image_url";
    private static final String KEY_PRODUCT_CATEGORY = "category";
    private static final String KEY_PRODUCT_STOCK = "stock";

    // Cart Table Columns
    private static final String KEY_CART_PRODUCT_ID = "product_id";
    private static final String KEY_CART_USER_ID = "user_id";
    private static final String KEY_CART_QUANTITY = "quantity";
    private static final String KEY_CART_TOTAL_PRICE = "total_price";

    // Orders Table Columns
    private static final String KEY_ORDER_USER_ID = "user_id";
    private static final String KEY_ORDER_DATE = "date";
    private static final String KEY_ORDER_TOTAL = "total";
    private static final String KEY_ORDER_STATUS = "status";
    private static final String KEY_ORDER_ADDRESS = "address";
    private static final String KEY_ORDER_CITY = "city";
    private static final String KEY_ORDER_ZIP = "zip";
    private static final String KEY_ORDER_PHONE = "phone";
    private static final String KEY_ORDER_PAYMENT = "payment_method";

    // Table Create Statements
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_USER_NAME + " TEXT,"
            + KEY_USER_EMAIL + " TEXT UNIQUE,"
            + KEY_USER_PASSWORD + " TEXT" + ")";

    private static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + TABLE_PRODUCTS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PRODUCT_NAME + " TEXT,"
            + KEY_PRODUCT_DESC + " TEXT,"
            + KEY_PRODUCT_PRICE + " REAL,"
            + KEY_PRODUCT_IMAGE + " TEXT,"
            + KEY_PRODUCT_CATEGORY + " TEXT,"
            + KEY_PRODUCT_STOCK + " INTEGER" + ")";

    private static final String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_CART_USER_ID + " INTEGER,"
            + KEY_CART_PRODUCT_ID + " INTEGER,"
            + KEY_CART_QUANTITY + " INTEGER,"
            + KEY_CART_TOTAL_PRICE + " REAL,"
            + "FOREIGN KEY(" + KEY_CART_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + "),"
            + "FOREIGN KEY(" + KEY_CART_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + KEY_ID + ")" + ")";

    private static final String CREATE_TABLE_ORDERS = "CREATE TABLE " + TABLE_ORDERS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ORDER_USER_ID + " INTEGER,"
            + KEY_ORDER_DATE + " TEXT,"
            + KEY_ORDER_TOTAL + " REAL,"
            + KEY_ORDER_STATUS + " TEXT,"
            + KEY_ORDER_ADDRESS + " TEXT,"
            + KEY_ORDER_CITY + " TEXT,"
            + KEY_ORDER_ZIP + " TEXT,"
            + KEY_ORDER_PHONE + " TEXT,"
            + KEY_ORDER_PAYMENT + " TEXT,"
            + "FOREIGN KEY(" + KEY_ORDER_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + KEY_ID + ")" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // For debugging - log database path
        Log.d("DB_DEBUG", "Database path: " + context.getDatabasePath(DATABASE_NAME).getPath());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB_DEBUG", "Creating new database tables");

        // Create tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_PRODUCTS);
        db.execSQL(CREATE_TABLE_CART);
        db.execSQL(CREATE_TABLE_ORDERS);

        // Insert sample products
        insertSampleProducts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DB_DEBUG", "Upgrading database from version " + oldVersion + " to " + newVersion);

        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }

    private void insertSampleProducts(SQLiteDatabase db) {
        Log.d("DB_DEBUG", "Inserting sample products");

        List<Product> sampleProducts = new ArrayList<>();

        // Sample products with image names WITHOUT .jpg extension


        sampleProducts.add(new Product(1, "Matte Lipstick", "Long-lasting matte finish", 12.99, "lipstick", "Lipstick", 50));
        sampleProducts.add(new Product(2, "Eyeshadow Palette", "12 vibrant colors", 24.99, "eyeshadow", "Eyeshadow", 30));
        sampleProducts.add(new Product(3, "Mascara", "Volume and length", 9.99, "mascara", "Mascara", 40));
        sampleProducts.add(new Product(4, "Foundation", "Natural finish", 15.99, "foundation", "Foundation", 25));
        sampleProducts.add(new Product(5, "Radiant Glow Foundation", "Gives a radiant, dewy finish", 29.99, "foundation", "Foundation", 35));
        sampleProducts.add(new Product(6, "Matte Lipstick - Ruby Red", "Intense red matte lipstick", 18.50, "lipstick", "Lipstick", 45));
        sampleProducts.add(new Product(7, "Volume Boost Mascara", "Boosts lash volume dramatically", 22.99, "mascara", "Mascara", 38));
        sampleProducts.add(new Product(8, "Eyeshadow Palette - Sunset", "Sunset-inspired warm shades", 34.99, "eyeshadow", "Eyeshadow", 28));
        sampleProducts.add(new Product(9, "Hydrating Concealer", "Covers imperfections and hydrates", 16.99, "concealer", "Concealer", 32));
        sampleProducts.add(new Product(10, "Cream Blush - Peach", "Peach-tinted creamy blush", 19.99, "blush_pan", "Blush", 27));
        sampleProducts.add(new Product(11, "Liquid Eyeliner - Black", "Precision liquid eyeliner", 15.50, "eyeliner", "Eyeliner", 33));
        sampleProducts.add(new Product(12, "Shimmer Highlighter", "Adds a luminous glow", 24.99, "highlighter", "Highlighter", 26));
        sampleProducts.add(new Product(13, "Lip Gloss - Clear", "Non-sticky, glossy finish", 12.99, "lip_gloss", "Lip Gloss", 42));
        sampleProducts.add(new Product(14, "Makeup Setting Spray", "Keeps makeup in place all day", 21.50, "setting_spray", "Setting Spray", 30));
        sampleProducts.add(new Product(15, "Pore Minimizing Primer", "Blurs pores and preps skin", 26.99, "face_primer", "Primer", 22));
        sampleProducts.add(new Product(16, "Sun-Kissed Bronzer", "Warm-toned bronzer for a tanned look", 23.50, "bronzer", "Bronzer", 29));
        sampleProducts.add(new Product(17, "Eyebrow Pencil - Dark Brown", "Defines brows precisely", 14.99, "eyebrow_pencil", "Eyebrow", 36));
        sampleProducts.add(new Product(18, "Nail Polish - Rose Gold", "Shiny rose gold finish", 9.99, "nail_polish", "Nail Polish", 44));
        sampleProducts.add(new Product(19, "Foundation Brush", "Soft bristles for smooth blending", 17.99, "makeup_brush", "Brush", 37));
        sampleProducts.add(new Product(20, "Beauty Blender Sponge", "Flawless blending sponge", 12.50, "beauty_sponge", "Sponge", 50));
        sampleProducts.add(new Product(21, "Hydrating Skincare Set", "Complete set for hydrated skin", 49.99, "skincare_set", "Skincare", 18));
        for (Product product : sampleProducts) {
            ContentValues values = new ContentValues();
            values.put(KEY_ID, product.getId());
            values.put(KEY_PRODUCT_NAME, product.getName());
            values.put(KEY_PRODUCT_DESC, product.getDescription());
            values.put(KEY_PRODUCT_PRICE, product.getPrice());
            values.put(KEY_PRODUCT_IMAGE, product.getImageUrl());
            values.put(KEY_PRODUCT_CATEGORY, product.getCategory());
            values.put(KEY_PRODUCT_STOCK, product.getStock());

            long id = db.insert(TABLE_PRODUCTS, null, values);
            Log.d("DB_DEBUG", "Inserted product: " + product.getName() + " with ID: " + id);
        }
    }

    // Helper method for safe column access
    public List<Product> getFeaturedProducts() {
        List<Product> featuredProducts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to get featured products (for example, first 8 products)
        String query = "SELECT * FROM " + TABLE_PRODUCTS + " LIMIT 8";

        try (Cursor cursor = db.rawQuery(query, null)) {
            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.setId(cursor.getInt(getColumnIndexSafe(cursor, KEY_ID)));
                    product.setName(cursor.getString(getColumnIndexSafe(cursor, KEY_PRODUCT_NAME)));
                    product.setDescription(cursor.getString(getColumnIndexSafe(cursor, KEY_PRODUCT_DESC)));
                    product.setPrice(cursor.getDouble(getColumnIndexSafe(cursor, KEY_PRODUCT_PRICE)));
                    product.setImageUrl(cursor.getString(getColumnIndexSafe(cursor, KEY_PRODUCT_IMAGE)));
                    product.setCategory(cursor.getString(getColumnIndexSafe(cursor, KEY_PRODUCT_CATEGORY)));
                    product.setStock(cursor.getInt(getColumnIndexSafe(cursor, KEY_PRODUCT_STOCK)));

                    featuredProducts.add(product);
                } while (cursor.moveToNext());
            }
        }
        return featuredProducts;
    }
    private int getColumnIndexSafe(Cursor cursor, String columnName) {
        int index = cursor.getColumnIndex(columnName);
        if (index == -1) {
            throw new IllegalArgumentException("Column " + columnName + " doesn't exist");
        }
        return index;
    }

    // User CRUD Operations
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.getName());
        values.put(KEY_USER_EMAIL, user.getEmail());
        values.put(KEY_USER_PASSWORD, user.getPassword());

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        try (Cursor cursor = db.query(TABLE_USERS,
                new String[]{KEY_ID, KEY_USER_NAME, KEY_USER_EMAIL, KEY_USER_PASSWORD},
                KEY_USER_EMAIL + "=?",
                new String[]{email}, null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                user = new User(
                        cursor.getInt(getColumnIndexSafe(cursor, KEY_ID)),
                        cursor.getString(getColumnIndexSafe(cursor, KEY_USER_NAME)),
                        cursor.getString(getColumnIndexSafe(cursor, KEY_USER_EMAIL)),
                        cursor.getString(getColumnIndexSafe(cursor, KEY_USER_PASSWORD))
                );
            }
        }
        return user;
    }

    // Password Change Methods
    public boolean checkUserPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.query(TABLE_USERS,
                new String[]{KEY_USER_PASSWORD},
                KEY_USER_EMAIL + "=?",
                new String[]{email}, null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                String storedPassword = cursor.getString(0);
                return storedPassword.equals(password); // In production, use password hashing!
            }
        }
        return false;
    }

    public boolean updateUserPassword(String email, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_PASSWORD, newPassword); // Hash this in production!

        int rowsAffected = db.update(TABLE_USERS, values,
                KEY_USER_EMAIL + "=?",
                new String[]{email});
        db.close();
        return rowsAffected > 0;
    }

    // Product Operations
    public List<Product> getAllProducts() {
        Log.d("DB_DEBUG", "Fetching all products from database");
        List<Product> products = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        try (Cursor cursor = db.rawQuery(selectQuery, null)) {
            Log.d("DB_DEBUG", "Found " + cursor.getCount() + " products in database");
            if (cursor.moveToFirst()) {
                do {
                    Product product = new Product();
                    product.setId(cursor.getInt(getColumnIndexSafe(cursor, KEY_ID)));
                    product.setName(cursor.getString(getColumnIndexSafe(cursor, KEY_PRODUCT_NAME)));
                    product.setDescription(cursor.getString(getColumnIndexSafe(cursor, KEY_PRODUCT_DESC)));
                    product.setPrice(cursor.getDouble(getColumnIndexSafe(cursor, KEY_PRODUCT_PRICE)));
                    product.setImageUrl(cursor.getString(getColumnIndexSafe(cursor, KEY_PRODUCT_IMAGE)));
                    product.setCategory(cursor.getString(getColumnIndexSafe(cursor, KEY_PRODUCT_CATEGORY)));
                    product.setStock(cursor.getInt(getColumnIndexSafe(cursor, KEY_PRODUCT_STOCK)));

                    products.add(product);
                    Log.d("DB_DEBUG", "Loaded product: " + product.getName());
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DB_DEBUG", "Error getting products", e);
        }
        return products;
    }

    public Product getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Product product = null;

        try (Cursor cursor = db.query(TABLE_PRODUCTS,
                new String[]{KEY_ID, KEY_PRODUCT_NAME, KEY_PRODUCT_DESC, KEY_PRODUCT_PRICE,
                        KEY_PRODUCT_IMAGE, KEY_PRODUCT_CATEGORY, KEY_PRODUCT_STOCK},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                product = new Product(
                        cursor.getInt(getColumnIndexSafe(cursor, KEY_ID)),
                        cursor.getString(getColumnIndexSafe(cursor, KEY_PRODUCT_NAME)),
                        cursor.getString(getColumnIndexSafe(cursor, KEY_PRODUCT_DESC)),
                        cursor.getDouble(getColumnIndexSafe(cursor, KEY_PRODUCT_PRICE)),
                        cursor.getString(getColumnIndexSafe(cursor, KEY_PRODUCT_IMAGE)),
                        cursor.getString(getColumnIndexSafe(cursor, KEY_PRODUCT_CATEGORY)),
                        cursor.getInt(getColumnIndexSafe(cursor, KEY_PRODUCT_STOCK))
                );
            }
        }
        return product;
    }

    // Cart Operations
    public long addToCart(CartItem cartItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CART_USER_ID, cartItem.getUserId());
        values.put(KEY_CART_PRODUCT_ID, cartItem.getProductId());
        values.put(KEY_CART_QUANTITY, cartItem.getQuantity());
        values.put(KEY_CART_TOTAL_PRICE, cartItem.getTotalPrice());

        long id = db.insert(TABLE_CART, null, values);
        db.close();
        return id;
    }

    public List<CartItem> getCartItems(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CART + " WHERE " + KEY_CART_USER_ID + "=" + userId;

        SQLiteDatabase db = this.getWritableDatabase();
        try (Cursor cursor = db.rawQuery(selectQuery, null)) {
            if (cursor.moveToFirst()) {
                do {
                    CartItem cartItem = new CartItem();
                    cartItem.setId(cursor.getInt(getColumnIndexSafe(cursor, KEY_ID)));
                    cartItem.setUserId(cursor.getInt(getColumnIndexSafe(cursor, KEY_CART_USER_ID)));
                    cartItem.setProductId(cursor.getInt(getColumnIndexSafe(cursor, KEY_CART_PRODUCT_ID)));
                    cartItem.setQuantity(cursor.getInt(getColumnIndexSafe(cursor, KEY_CART_QUANTITY)));
                    cartItem.setTotalPrice(cursor.getDouble(getColumnIndexSafe(cursor, KEY_CART_TOTAL_PRICE)));

                    cartItems.add(cartItem);
                } while (cursor.moveToNext());
            }
        }
        return cartItems;
    }

    public int updateCartItemQuantity(int cartItemId, int quantity, double price) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CART_QUANTITY, quantity);
        values.put(KEY_CART_TOTAL_PRICE, quantity * price);

        int rowsAffected = db.update(TABLE_CART, values, KEY_ID + " = ?",
                new String[]{String.valueOf(cartItemId)});
        db.close();
        return rowsAffected;
    }

    public void removeFromCart(int cartItemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, KEY_ID + " = ?",
                new String[]{String.valueOf(cartItemId)});
        db.close();
    }

    public void clearCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, KEY_CART_USER_ID + " = ?",
                new String[]{String.valueOf(userId)});
        db.close();
    }

    // Order Operations
    public long createOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_USER_ID, order.getUserId());
        values.put(KEY_ORDER_DATE, order.getDate());
        values.put(KEY_ORDER_TOTAL, order.getTotalPrice());
        values.put(KEY_ORDER_STATUS, order.getStatus());
        values.put(KEY_ORDER_ADDRESS, order.getAddress());
        values.put(KEY_ORDER_CITY, order.getCity());
        values.put(KEY_ORDER_ZIP, order.getZip());
        values.put(KEY_ORDER_PHONE, order.getPhone());
        values.put(KEY_ORDER_PAYMENT, order.getPaymentMethod());

        long id = db.insert(TABLE_ORDERS, null, values);
        db.close();
        return id;
    }

    public List<Order> getOrders(int userId) {
        List<Order> orders = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ORDERS + " WHERE " + KEY_ORDER_USER_ID + "=" + userId;

        SQLiteDatabase db = this.getWritableDatabase();
        try (Cursor cursor = db.rawQuery(selectQuery, null)) {
            if (cursor.moveToFirst()) {
                do {
                    Order order = new Order();
                    order.setId(cursor.getInt(getColumnIndexSafe(cursor, KEY_ID)));
                    order.setUserId(cursor.getInt(getColumnIndexSafe(cursor, KEY_ORDER_USER_ID)));
                    order.setDate(cursor.getString(getColumnIndexSafe(cursor, KEY_ORDER_DATE)));
                    order.setTotalPrice(cursor.getDouble(getColumnIndexSafe(cursor, KEY_ORDER_TOTAL)));
                    order.setStatus(cursor.getString(getColumnIndexSafe(cursor, KEY_ORDER_STATUS)));
                    order.setAddress(cursor.getString(getColumnIndexSafe(cursor, KEY_ORDER_ADDRESS)));
                    order.setCity(cursor.getString(getColumnIndexSafe(cursor, KEY_ORDER_CITY)));
                    order.setZip(cursor.getString(getColumnIndexSafe(cursor, KEY_ORDER_ZIP)));
                    order.setPhone(cursor.getString(getColumnIndexSafe(cursor, KEY_ORDER_PHONE)));
                    order.setPaymentMethod(cursor.getString(getColumnIndexSafe(cursor, KEY_ORDER_PAYMENT)));

                    orders.add(order);
                } while (cursor.moveToNext());
            }
        }
        return orders;
    }

    public Order getOrder(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Order order = null;

        try (Cursor cursor = db.query(TABLE_ORDERS,
                null,
                KEY_ID + "=?",
                new String[]{String.valueOf(orderId)},
                null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                order = new Order();
                order.setId(cursor.getInt(getColumnIndexSafe(cursor, KEY_ID)));
                order.setUserId(cursor.getInt(getColumnIndexSafe(cursor, KEY_ORDER_USER_ID)));
                order.setAddress(cursor.getString(getColumnIndexSafe(cursor, KEY_ORDER_ADDRESS)));
                order.setCity(cursor.getString(getColumnIndexSafe(cursor, KEY_ORDER_CITY)));
                order.setZip(cursor.getString(getColumnIndexSafe(cursor, KEY_ORDER_ZIP)));
                order.setPhone(cursor.getString(getColumnIndexSafe(cursor, KEY_ORDER_PHONE)));
                order.setPaymentMethod(cursor.getString(getColumnIndexSafe(cursor, KEY_ORDER_PAYMENT)));
                order.setTotalPrice(cursor.getDouble(getColumnIndexSafe(cursor, KEY_ORDER_TOTAL)));
                order.setStatus(cursor.getString(getColumnIndexSafe(cursor, KEY_ORDER_STATUS)));
                order.setDate(cursor.getString(getColumnIndexSafe(cursor, KEY_ORDER_DATE)));
            }
        }
        return order;
    }

    public boolean doesEmailExist(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.query(TABLE_USERS,
                new String[]{KEY_USER_EMAIL},
                KEY_USER_EMAIL + "=?",
                new String[]{email}, null, null, null)) {
            return cursor != null && cursor.getCount() > 0;
        }
    }

    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        try (Cursor cursor = db.query(TABLE_USERS,
                new String[]{KEY_ID, KEY_USER_NAME, KEY_USER_EMAIL, KEY_USER_PASSWORD},
                KEY_ID + "=?",
                new String[]{String.valueOf(userId)}, null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                user = new User(
                        cursor.getInt(getColumnIndexSafe(cursor, KEY_ID)),
                        cursor.getString(getColumnIndexSafe(cursor, KEY_USER_NAME)),
                        cursor.getString(getColumnIndexSafe(cursor, KEY_USER_EMAIL)),
                        cursor.getString(getColumnIndexSafe(cursor, KEY_USER_PASSWORD))
                );
            }
        }
        return user;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.getName());
        values.put(KEY_USER_EMAIL, user.getEmail());
        // Add other fields as needed

        int rowsAffected = db.update(TABLE_USERS, values,
                KEY_ID + "=?",
                new String[]{String.valueOf(user.getId())});
        db.close();
        return rowsAffected > 0;
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}