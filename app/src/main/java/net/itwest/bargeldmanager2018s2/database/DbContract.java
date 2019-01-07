package net.itwest.bargeldmanager2018s2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.itwest.bargeldmanager2018s2.database.dto.Entry;
import net.itwest.bargeldmanager2018s2.database.dto.Category;

public final class DbContract {

    private DbContract() {
    }

    public static class DbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "data.db";

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void clear() {
            clear(null);
        }

        private void clear(SQLiteDatabase db) {
            if (db == null)
                db = getWritableDatabase();

            db.execSQL(SQL_DELETE_ENTRIES);
            db.execSQL(SQL_DELETE_CATEGORIES);

            onCreate(db);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_CATEGORIES);
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            clear(db);
        }

        // Categories

        public long addCategory(Category category) {
            SQLiteDatabase db = this.getWritableDatabase();

            if (getCategory(category.getTitle()) != null)
                return -1;

            ContentValues values = new ContentValues();
            values.put(CategoryRow.COLUMN_TITLE, category.getTitle());

            long id = db.insert(CategoryRow.TABLE_NAME, null, values);
            db.close();

            category.setId(id);
            return id;
        }

        public boolean deleteCategory(Category category) {
            SQLiteDatabase db = this.getWritableDatabase();

            if (getCategory(category.getId()) == null)
                return false;

            int affected = db.delete(CategoryRow.TABLE_NAME, CategoryRow._ID + " = " + category.getId(), null);

            return affected > 0;
        }

        public Category getCategory(String title) {
            return _getCategory(CategoryRow.COLUMN_TITLE + " = '" + title + "'");
        }

        public Category getCategory(long id) {
            return _getCategory(CategoryRow._ID + " = " + id);
        }

        private Category _getCategory(String where) {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(
                    "SELECT " +
                        CategoryRow._ID + ", " +
                        CategoryRow.COLUMN_TITLE + " " +
                    "FROM " +
                        CategoryRow.TABLE_NAME + " " +
                    "WHERE " +
                        where,

                    null
            );

            if (cursor.moveToFirst()) {
                Category category = categoryFromCursor(cursor);
                cursor.close();
                return category;
            }

            return null;
        }

        public List<Category> getCategories() {
            List<Category> categories = new ArrayList<>();

            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery(
                    "SELECT " +
                        CategoryRow._ID + ", " +
                        CategoryRow.COLUMN_TITLE + " " +
                    "FROM " +
                        CategoryRow.TABLE_NAME, null
            );

            if (cursor.moveToFirst()) {
                do {
                    categories.add(categoryFromCursor(cursor));
                } while (cursor.moveToNext());
                cursor.close();
            }

            return categories;
        }

        private Category categoryFromCursor(Cursor cursor) {
            Category category = new Category();
            category.setId(cursor.getLong(0));
            category.setTitle(cursor.getString(1));
            return category;
        }

        // Entries

        public long addEntry(Entry entry) {
            SQLiteDatabase db = this.getWritableDatabase();

            if (entry.getDate() == null)
                entry.setDate(new Date());

            if (entry.getText() == null)
                entry.setText("");

            ContentValues values = new ContentValues();
            values.put(EntryRow.COLUMN_AMOUNT, entry.getAmount());
            values.put(EntryRow.COLUMN_DATE, entry.getDate().getTime() / 1000);
            values.put(EntryRow.COLUMN_TEXT, entry.getText());
            values.put(EntryRow.COLUMN_CATEGORY_ID, entry.getCategoryId());

            long id = db.insert(EntryRow.TABLE_NAME, null, values);
            db.close();

            entry.setId(id);
            return id;
        }

        public List<Entry> getEntries() {
            return getEntries(null);
        }

        public List<Entry> getEntries(Category categoryFilter) {
            List<Entry> entriesen = new ArrayList<>();

            SQLiteDatabase db = this.getReadableDatabase();

            String sql =
                    "SELECT " +
                        EntryRow._ID + ", " +
                        EntryRow.COLUMN_AMOUNT + ", " +
                        EntryRow.COLUMN_DATE + ", " +
                        EntryRow.COLUMN_TEXT + ", " +
                        EntryRow.COLUMN_CATEGORY_ID + " " +
                    "FROM " +
                        EntryRow.TABLE_NAME;

            if (categoryFilter != null)
                sql += " WHERE " + EntryRow.COLUMN_CATEGORY_ID + " = " + categoryFilter.getId();

            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    entriesen.add(entryFromCursor(cursor));
                } while (cursor.moveToNext());
                cursor.close();
            }

            return entriesen;
        }

        private Entry entryFromCursor(Cursor cursor) {
            Entry entry = new Entry();
            entry.setId(cursor.getLong(0));
            entry.setAmount(cursor.getLong(1));
            entry.setDate(new Date(cursor.getLong(2) * 1000));
            entry.setText(cursor.getString(3));
            entry.setCategoryId(cursor.getLong(4));
            return entry;
        }
    }

    // Table Categories

    public static class CategoryRow implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_TITLE = "title";
    }

    private static final String SQL_CREATE_CATEGORIES =
            "CREATE TABLE " + CategoryRow.TABLE_NAME + " (" +
                    CategoryRow._ID + " INTEGER PRIMARY KEY NOT NULL," +
                    CategoryRow.COLUMN_TITLE + " TEXT NOT NULL)";

    private static final String SQL_DELETE_CATEGORIES =
            "DROP TABLE IF EXISTS " + CategoryRow.TABLE_NAME;

    // Table Entries

    public static class EntryRow implements BaseColumns {
        public static final String TABLE_NAME = "entries";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_CATEGORY_ID = "categoryId";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + EntryRow.TABLE_NAME + " (" +
                    EntryRow._ID + " INTEGER PRIMARY KEY NOT NULL," +
                    EntryRow.COLUMN_AMOUNT + " INTEGER NOT NULL," +
                    EntryRow.COLUMN_DATE + " INTEGER NOT NULL," +
                    EntryRow.COLUMN_TEXT + " TEXT NOT NULL," +
                    EntryRow.COLUMN_CATEGORY_ID + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + EntryRow.TABLE_NAME;
}
