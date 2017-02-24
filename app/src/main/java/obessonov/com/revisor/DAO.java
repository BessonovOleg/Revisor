package obessonov.com.revisor;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import obessonov.com.revisor.Documents.DocumentTitle;
import obessonov.com.revisor.Documents.WareHouse;

public class DAO {

    private Context ctx;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    public DAO(Context ctx) {
        this.ctx = ctx;
        dbHelper = new DbHelper(ctx);
        db = dbHelper.getWritableDatabase();
    }
//AGENTS
    public ArrayList<Agent> getAgents() {
        ArrayList<Agent> result = new ArrayList<>();
        Agent tmpAgent;

        Cursor c = db.query("Agents", null, null, null, null, null, "ag_name");

        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idColIndex   = c.getColumnIndex("_id");
            int guidColIndex = c.getColumnIndex("ag_guid");
            int nameColIndex = c.getColumnIndex("ag_name");

            do {
                tmpAgent = new Agent();
                tmpAgent.setAg_id(c.getInt(idColIndex));
                tmpAgent.setAg_guid(c.getString(guidColIndex));
                tmpAgent.setAg_name(c.getString(nameColIndex));
                result.add(tmpAgent);
            } while (c.moveToNext());
        } else {
            c.close();
        }

        return result;
    }

    public void insertAgent(String strCsv) {
        String [] strArray = strCsv.split(";");
        insertAgent(strArray[0],strArray[1]);
    }

    public void insertAgent(Agent agent){
        insertAgent(agent.getAg_guid(),agent.getAg_name());
    }

    public void insertAgent(String ag_guid,String ag_name){
        ContentValues cv = new ContentValues();
        cv.put("ag_name",ag_name);

        if(isAgentExists(ag_guid)) {
            int updCount = db.update("Agents",cv,"ag_guid = ?",new String[] {ag_guid});
        }else {
            cv.put("ag_guid",ag_guid);
            db.insert("Agents", null, cv);
        }
    }

    private boolean isAgentExists(String ag_guid) {
        boolean res = false;
        String sqlQury = "select ag_name from Agents where ag_guid = ?";
        Cursor c = db.rawQuery(sqlQury,new String[] {ag_guid});

        if (c.getCount() > 0) {
            res = true;
        }

        c.close();
        return res;
    }

//ENTITY
    public ArrayList<Entity> getEntities() {
        ArrayList<Entity> result = new ArrayList<>();
        Entity tmpEntity;

        Cursor c = db.query("Entities", null, null, null, null, null, "ent_name");

        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int idColIndex      = c.getColumnIndex("_id");
            int guidColIndex    = c.getColumnIndex("ent_guid");
            int nameColIndex    = c.getColumnIndex("ent_name");
            int barcodeColIndex = c.getColumnIndex("barcode");

            do {
                tmpEntity = new Entity();
                tmpEntity.setEnt_id(c.getInt(idColIndex));
                tmpEntity.setEnt_guid(c.getString(guidColIndex));
                tmpEntity.setEnt_name(c.getString(nameColIndex));
                tmpEntity.setBarcode(c.getString(barcodeColIndex));
                result.add(tmpEntity);
            } while (c.moveToNext());
        } else {
            c.close();
        }

        return result;
    }

    public void insertEntity(Entity entity) {
        insertEntity(entity.getEnt_guid(),entity.getEnt_name(),entity.getEnt_guid());
    }

    public void insertEntity(String strCsv) {
        String [] strArray = strCsv.split(";");
        insertEntity(strArray[0],strArray[1],strArray[2]);
    }

    public void insertEntity(String ent_guid,String ent_name,String barcode){
        ContentValues cv = new ContentValues();
        cv.put("ent_name",ent_name);
        cv.put("barcode", barcode);

        if(isEntityExists(ent_guid)) {
           int updCount = db.update("Entities",cv,"ent_guid = ?",new String[] {ent_guid});
        }else {
            cv.put("ent_guid",ent_guid);
            db.insert("Entities", null, cv);
        }
    }

    private boolean isEntityExists(String ent_guid) {
        boolean res = false;
        String sqlQury = "select ent_name from Entities where ent_guid = ?";
        Cursor c = db.rawQuery(sqlQury,new String[] {ent_guid});

        if (c.getCount() > 0) {
            res = true;
        }

        c.close();
        return res;
    }


//DOCUMENTS
    public ArrayList<DocumentTitle> getDocumentList(){
        ArrayList<DocumentTitle> result = new ArrayList<>();
        DocumentTitle docTitle;

        Cursor c = db.query("Documents", null, null, null, null, null, null);

        if (c.moveToFirst()) {
            docTitle = new DocumentTitle();
            int idColIndex      = c.getColumnIndex("_id");
            int dateColIndex    = c.getColumnIndex("doc_date");
            int stateColIndex   = c.getColumnIndex("doc_state");
            int state;

            do {
                docTitle.setDoc_id(c.getInt(idColIndex));
                docTitle.setDoc_date(c.getString(dateColIndex));
                state = c.getInt(stateColIndex);

                if(state == 0) {
                    docTitle.setDoc_state("Новый");
                }

                if(state == 1) {
                    docTitle.setDoc_state("Выгружен");
                }

                result.add(docTitle);
            } while (c.moveToNext());
        } else {
            c.close();
        }

        return result;
    }

    //Warehouse
    public ArrayList<WareHouse> getWareHouses() {
        ArrayList<WareHouse> result = new ArrayList<>();
        WareHouse wh;

        Cursor c = db.query("Agents",null,null,null,null,null,"ag_name");

        if (c.moveToFirst()) {
            wh = new WareHouse();
            int nameColIndex    = c.getColumnIndex("ag_name");
            int guidColIndex    = c.getColumnIndex("ag_guid");

            do {
                wh = new WareHouse();
                wh.setName(c.getString(nameColIndex));
                wh.setGuid(c.getString(guidColIndex));
                result.add(wh);
            } while (c.moveToNext());
        } else {
            c.close();
        }

        return result;
    }


    public void testInsertDocument(){
        ContentValues cv = new ContentValues();
        cv.put("doc_date","2016-11-20");
        cv.put("doc_no","12345");
        cv.put("doc_ag_guid","fff-fff-ff");
        cv.put("doc_state","1");
        db.insert("Documents", null, cv);
       }


    public int getMaxDocID(){
        int res = 0;
        String[] coulmns = {"max(_id) as id"};

        Cursor c = db.query("Documents",coulmns,null,null,null,null,null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("_id");
            do {
                res = c.getInt(idColIndex);
            } while (c.moveToNext());

        } else {
            c.close();
        }

        return res;
    }



    public Entity getEntityByCode(String code) {
        Entity res = new Entity();

        String table = "Entities";
        String columns[] = {"_id","ent_guid","ent_name","barcode"};
        String selection = "barcode = ?";
        String[] selectionArgs = {code};
        Cursor c = db.query(table, columns, selection, selectionArgs, null, null, null);


        if (c.moveToFirst()) {
            int idColIndex      = c.getColumnIndex("_id");
            int guidColIndex    = c.getColumnIndex("ent_guid");
            int nameColIndex    = c.getColumnIndex("ent_name");
            int barcodeColIndex = c.getColumnIndex("barcode");

            do {
                res.setEnt_id(c.getInt(idColIndex));
                res.setEnt_guid(c.getString(guidColIndex));
                res.setEnt_name(c.getString(nameColIndex));
                res.setBarcode(c.getString(barcodeColIndex));
            } while (c.moveToNext());
        } else {
            c.close();
        }

        return res;
    }


    public Entity getEntitByID(Integer id){
        Entity res = new Entity();

        String table = "Entities";
        String columns[] = {"_id","ent_guid","ent_name","barcode"};
        String selection = "_id = ?";
        String[] selectionArgs = {id.toString()};
        Cursor c = db.query(table, columns, selection, selectionArgs, null, null, null);


        if (c.moveToFirst()) {
            int idColIndex      = c.getColumnIndex("_id");
            int guidColIndex    = c.getColumnIndex("ent_guid");
            int nameColIndex    = c.getColumnIndex("ent_name");
            int barcodeColIndex = c.getColumnIndex("barcode");

            do {
                res.setEnt_id(c.getInt(idColIndex));
                res.setEnt_guid(c.getString(guidColIndex));
                res.setEnt_name(c.getString(nameColIndex));
                res.setBarcode(c.getString(barcodeColIndex));
            } while (c.moveToNext());
        } else {
            c.close();
        }

        return res;
    }





    class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, "revisorDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //Таблица складов
            db.execSQL("create table Agents ("
                   + "_id integer primary key autoincrement,"
                   + "ag_guid text,"
                   + "ag_name text"
                   + ");");

            //Таблица товара
            db.execSQL("create table Entities ("
                   + "_id integer primary key autoincrement,"
                   + "ent_guid text,"
                   + "ent_name text,"
                   + "barcode text"
                   + ");");

            //Таблица документов
            db.execSQL("create table Documents ("
                   + "_id integer primary key autoincrement,"
                   + "doc_date text,"
                   + "doc_no text,"
                   + "doc_ag_guid text,"
                   + "doc_state integer"
                   + ");");

            //Таблица содержимого документов
            db.execSQL("create table Journal ("
                   + "_id integer primary key autoincrement,"
                   + "doc_id integer,"
                   + "row_no integer,"
                   + "ent_guid text,"
                   + "qty real"
                   + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
