package com.example.duitang.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DatabaseUtil {
	private MyHelper helper;

	public DatabaseUtil(Context context) {
		super();
		helper = new MyHelper(context);
	}
	
	/**插入数据
	 * @param String
	 * */
	public boolean Insert(Collection collection){
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "insert into "+MyHelper.TABLE_NAME
					+"(_id,name,path) values ("+" ' "+collection.getId()+" ' "+" ," +" ' " + collection.getName() + " ' ," + "  ' "+ collection.getPath() + " ' " +" )";    
		try {            
			db.execSQL(sql); 
			return true;
			} catch (SQLException e){  
				Log.e("err", "insert failed"); 
				return false;
				}finally{
					db.close();
				}
		
	}
	
	
	/**更新数据
	 * @param Person person , int id
	 * */
	
	public void Update(Collection person ,int id){
		
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", person.getName());
		values.put("path", person.getPath());
		int rows = db.update(MyHelper.TABLE_NAME, values, "_id=?", new String[] { id + "" });
		
		db.close();
	}
	
	/**删除数据
	 * @param int id
	 * */
	
	public void Delete(int id){
		
		SQLiteDatabase db = helper.getWritableDatabase();
		int raw = db.delete(MyHelper.TABLE_NAME, "_id=?", new String[]{id+""});
		db.close();
	}
	
	/**查询所有数据
	 * 
	 * */
	public List<Collection> queryAll(){
		SQLiteDatabase db = helper.getReadableDatabase();
		List<Collection> list = new ArrayList<Collection>();
		Cursor cursor = db.query(MyHelper.TABLE_NAME, null, null,null, null, null, null);
		
		while(cursor.moveToNext()){
			Collection collection = new Collection();
			collection.setId(cursor.getInt(cursor.getColumnIndex("_id")));   
			collection.setName(cursor.getString(cursor.getColumnIndex("name"))); 
			collection.setPath(cursor.getString(cursor.getColumnIndex("path")));
			list.add(collection);
		}
		db.close();
		return list;
	}
	
	/**按id查询
	 * 
	 * */
	public boolean queryByid(int id){
		
		SQLiteDatabase db = helper.getReadableDatabase();
		Collection collection = new Collection();
		Cursor cursor = db.query(MyHelper.TABLE_NAME, new String[]{"name","path"}, "_id=?",new String[]{ id + ""}, null, null, null);
//		db.delete(table, whereClause, whereArgs)
		while(cursor.moveToNext()){
			collection.setId(id);   
			collection.setName(cursor.getString(cursor.getColumnIndex("name"))); 
			collection.setPath(cursor.getString(cursor.getColumnIndex("path")));
		}
		db.close();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("_id", collection.getId());
		return map.containsValue(id);
	}

}
