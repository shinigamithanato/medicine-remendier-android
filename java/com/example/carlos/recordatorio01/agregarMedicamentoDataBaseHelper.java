package com.example.carlos.recordatorio01;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Carlos on 13/11/2015.
 */
public class agregarMedicamentoDataBaseHelper {

    private static final int DATABASE_VERSION=5;

    private static final String DATABSE_NAME="reocordatorimendicamentos.db";
    private static final String TABLE_NAME="medicamentos";

    private static final String MEDICAMENTOS_COMLUMN_ID="_id";//obligatio que sea: '_id' y no id
    private static final String MEDICAMENTOS_COMLUMN_MEDICAMENTO="medicamento";
    private static final String MEDICAMENTOS_COMLUMN_FIRSTDATE="primera_fecha";
    private static final String MEDICAMENTOS_COMLUMN_FINALDATE="fecha_final";
    private static final String MEDICAMENTOS_COMLUMN_HORA="hora";
    private static final String MEDICAMENTOS_COMLUMN_PERIODOS="periodo";
    private static final String MEDICAMENTOS_COMLUMN_HORAFINAL="hora_final";

    private AgregarMedicamentoOpenHelper openhelper;
    private SQLiteDatabase database;

    public  agregarMedicamentoDataBaseHelper(Context context){
        openhelper=new AgregarMedicamentoOpenHelper(context);
        database=openhelper.getWritableDatabase();
    }

    public Cursor getMedicamentos(){
        return database.rawQuery("select "
                + MEDICAMENTOS_COMLUMN_ID +","
                + MEDICAMENTOS_COMLUMN_MEDICAMENTO + ","
                + MEDICAMENTOS_COMLUMN_HORA +","
                + MEDICAMENTOS_COMLUMN_FINALDATE+","
                + MEDICAMENTOS_COMLUMN_FIRSTDATE+","
                + MEDICAMENTOS_COMLUMN_PERIODOS
                +" from " + TABLE_NAME,null);
        //regresa la busqueda solicitada de la base de datos, como un objeto del tipo cursor.
    }

    public Cursor getHoraYfechaParaActulizar(int idMedicamentos){
        return database.rawQuery("select "
            + MEDICAMENTOS_COMLUMN_ID + ","
            + MEDICAMENTOS_COMLUMN_HORA + ","
            + MEDICAMENTOS_COMLUMN_PERIODOS + ","
            + MEDICAMENTOS_COMLUMN_FIRSTDATE+ ","
            + MEDICAMENTOS_COMLUMN_FINALDATE+","
            + MEDICAMENTOS_COMLUMN_HORAFINAL
            + " from " + TABLE_NAME
            + " WHERE "+ MEDICAMENTOS_COMLUMN_ID + "='" + idMedicamentos + "' ",null);
    }


    public void saveMedicamento(String medicamento,String fechaInicial, String fechaFInal, String hora, int lapsos, String horaFinal){
        ContentValues contentValues=new ContentValues();
        contentValues.put(MEDICAMENTOS_COMLUMN_MEDICAMENTO,medicamento);
        contentValues.put(MEDICAMENTOS_COMLUMN_FIRSTDATE,fechaInicial);
        contentValues.put(MEDICAMENTOS_COMLUMN_FINALDATE,fechaFInal);
        contentValues.put(MEDICAMENTOS_COMLUMN_HORA,hora);
        contentValues.put(MEDICAMENTOS_COMLUMN_PERIODOS, lapsos);
        contentValues.put(MEDICAMENTOS_COMLUMN_HORAFINAL,horaFinal);

        database.insert(TABLE_NAME,null,contentValues);
    }

    public void deleteMedicamento(int idMedicamento){
        database.execSQL("DELETE FROM " + TABLE_NAME + " WHERE "
                + MEDICAMENTOS_COMLUMN_ID+ "='"+ idMedicamento+ "' ");
    }

    public void updateMedicamento(int IDItem, String nuevaHora, String nuevaFecha){

        Log.d("Datos A introducir tabla:", String.valueOf(IDItem)+nuevaHora+nuevaFecha);
        database.execSQL("UPDATE " + TABLE_NAME + " SET "
                + MEDICAMENTOS_COMLUMN_HORA + "= '"+ nuevaHora+"',"
                + MEDICAMENTOS_COMLUMN_FIRSTDATE + "= '" + nuevaFecha + "' WHERE "
                + MEDICAMENTOS_COMLUMN_ID + "= '" +IDItem+"'");
        Log.d("Datos introducidos tabla:", String.valueOf(IDItem)+nuevaHora+nuevaFecha);

    }




    private static class AgregarMedicamentoOpenHelper extends SQLiteOpenHelper{


        AgregarMedicamentoOpenHelper(Context context) {
            super(context,DATABSE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sqlSentenceCreateTable="CREATE TABLE " + TABLE_NAME + " ( "
                    + MEDICAMENTOS_COMLUMN_ID +" INTEGER PRIMARY KEY, "
                    + MEDICAMENTOS_COMLUMN_MEDICAMENTO + " TEXT, "
                    + MEDICAMENTOS_COMLUMN_HORA + " TIME, "
                    + MEDICAMENTOS_COMLUMN_FIRSTDATE + " DATE, "
                    + MEDICAMENTOS_COMLUMN_FINALDATE + " DATE, "
                    + MEDICAMENTOS_COMLUMN_PERIODOS+ " INT, "
                    + MEDICAMENTOS_COMLUMN_HORAFINAL + " TIME)"  ;
            Log.d("La base de datos fue creada ", sqlSentenceCreateTable);
            db.execSQL(sqlSentenceCreateTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

}
