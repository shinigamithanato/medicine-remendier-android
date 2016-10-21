package com.example.carlos.recordatorio01;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity {

    ListView listaMedicamentos;
    ///private AgregarMedicamentoCursorAdapter agregarMedicamentoCursorAdapter;
    private AgregarMedicamentoAdapter agregarMedicamentoAdapter;
    private agregarMedicamentoDataBaseHelper dbHelper;
    private Cursor cursor;
    private Cursor cursor2;
    private FechasHelper ayudarFechas;


    public static final int TIME_ENTRY_REQUEST_CODE = 1;
    public static final String MY_ACTION = "com.sample.myaction";

    String[] horaOriginal=new String[1000];
    String[] fechaOriginal=new String[1000];

    ArrayList<PendingIntent> intentArray = new ArrayList<PendingIntent>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper=new agregarMedicamentoDataBaseHelper(this);


        ayudarFechas=new FechasHelper(this);
        listaMedicamentos=(ListView)findViewById(R.id.medicamentosListView);
        agregarMedicamentoAdapter=new AgregarMedicamentoAdapter(); //adapter sin View
        //agregarMedicamentoCursorAdapter=new AgregarMedicamentoCursorAdapter(this,dbHelper.getMedicamentos());

        cursor= dbHelper.getMedicamentos();
        String idMedicamentoCursor,nombreMedicamntocursor,horaMedciamentoCursor,fechaFinalMedicamentoCursor;
        String fechaSiguienteMedicamentoCursor,lapsosMedicmanetoCursor;

        long tiempoBaseDatos;
        boolean comparSiSePuedeActualizar;


        if(cursor.moveToFirst()){//minetras encuentre filas con datoos llenara nuestro listVIew
            do{
                lapsosMedicmanetoCursor=cursor.getString(5);
                idMedicamentoCursor=cursor.getString(0);

                do{ //usaremos un segundo ciclo para actualizars las fechas y despliegue la que toca despues
                    cursor2=dbHelper.getHoraYfechaParaActulizar(Integer.valueOf(idMedicamentoCursor));
                    //usamos un segundo cursor, un temporal, para poder manpular la actualizacion
                    cursor2.moveToFirst();//se ocupa poner para que se mueva al primero y unico elemneto retornado del cursor2

                    horaMedciamentoCursor=cursor2.getString(1);
                    fechaSiguienteMedicamentoCursor=cursor2.getString(3);

                    tiempoBaseDatos=ayudarFechas.fijarHprayFechaNotificacion(horaMedciamentoCursor,
                            fechaSiguienteMedicamentoCursor)+(5*60*1000);//obtenemos sus milegundos
                    //de lafecha y hora que esta en labase de datos (milesegundo 0 = 0:00 1/1/1970)

                    comparSiSePuedeActualizar=System.currentTimeMillis()>tiempoBaseDatos;
                        //el valor boleano si los milesegundos actules del sistema ya superaron los
                        //los de la base de datos mas 5 minutos de tolerancia

                    if(comparSiSePuedeActualizar){ //si el boleano es verdado acctuliza la nueva base de datos
                        ayudarFechas.updataHoraYFecha(idMedicamentoCursor,horaMedciamentoCursor,
                                lapsosMedicmanetoCursor,fechaSiguienteMedicamentoCursor);
                    }

                }while(comparSiSePuedeActualizar);//mientras sea necesarioactulizar, si no es necesario actilizar, solo se hara
                //una vez

                nombreMedicamntocursor=cursor.getString(1);
                horaMedciamentoCursor=cursor.getString(2);
                fechaFinalMedicamentoCursor=cursor.getString(3);
                fechaSiguienteMedicamentoCursor=cursor.getString(4);


                if(ayudarFechas.borraarAyudaFecha(fechaFinalMedicamentoCursor))//IInfla el listView con los datos, mandados como
                    agregarMedicamentoAdapter.usarCursorconlista(nombreMedicamntocursor,cursor2.getString(1));//parametros, usa el
                    // cursor2, porqu es el
                    /// dato ya actualizadoe en cas de requerirlo
                else
                    dbHelper.deleteMedicamento(Integer.valueOf(idMedicamentoCursor)); //si el medicmanrto, ya paso, ya lo borra

            }while(cursor.moveToNext());

        }


        if(!cursor.isClosed()) cursor.close(); //siempre cerraar el cursor
        if(!cursor2.isClosed()) cursor.close();

        listaMedicamentos.setAdapter(agregarMedicamentoAdapter);
        //Añadirmos a el adapter al ListView, sirve para conectar la bbase de datos con el ListView
        //ver pag. 287
        //ayudarFechas.updataHoraYFecha("2","20:00","12","2016-02-29");
        //pruebaaNotificacion(1,"payasada","21:05","2016-01-27");
        //alarmaMedicamento(10, "Yolo","22:00", "2016-02-17");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void agregarMedicamentoBoton(View v){
        Intent intent=new Intent(this,AgregarMedicamento.class);
        startActivityForResult(intent, TIME_ENTRY_REQUEST_CODE);//El request code, constante a int=1
        //Reliza el intent, pero obtiene datos, que seran usados para un uso en este caso llenar el
            //listVIew con el nuevo dato.
        //startActivity(intent); //comienza el segundo Activity
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //metodo para actualizar el list view con un nuevo dato que fue introducido
        if(requestCode==TIME_ENTRY_REQUEST_CODE)//
            if(resultCode==RESULT_OK) {//Si se agregaron los datos, verifica que todoestebien y
                // empieza a cargarlos, si no se agrego nada, simplementoe no hace nada
                cursor = dbHelper.getMedicamentos();
                if (cursor.moveToLast()){//Solo nos inteeresa cargar el ultimo,asiq ue nos vamos al
                //ultimo dato introducido
                    agregarMedicamentoAdapter.usarCursorconlista(cursor.getString(1),
                            cursor.getString(2));
                            //Añadirmos el nuevo dato a nustro adaptador
                    agregarMedicamentoAdapter.notifyDataSetChanged();//este metodo le hace saber a
                    //la lista que hubo un cambio y que debe actualizarla
                    int idMedicamentoNotificacion=Integer.parseInt(cursor.getString(0));
                    String nombreDelMedicamento=cursor.getString(1);
                    String horaAtomarMedicamento=cursor.getString(2);
                    String fechaMedicamento=cursor.getString(4);

                    Log.d("Dato", nombreDelMedicamento);

                    int idOriginal=idMedicamentoNotificacion;
                    horaOriginal[idOriginal]=horaAtomarMedicamento;
                    fechaOriginal[idOriginal]=fechaMedicamento;

                    alarmaMedicamento(idMedicamentoNotificacion, nombreDelMedicamento,
                            horaAtomarMedicamento, fechaMedicamento,idOriginal); /*Esto se hara para activar
                            las notificaciones, en su resppectivas horas y fechas una vez ageregas*/
                }
                if(!cursor.isClosed()) cursor.close();
            }
    }



    public void notificarMedicamento(int idMedicamento , String medicamentoNoti, String horaNoti, int idOriginal){
        //Funcion que nos manda las notificaciones, el id medicamento nos determina el id de la notificiion
        int  mId=idMedicamento; //el ide de la nootificacion nos identifica las diferentes notificaciones corriendo


        NotificationCompat.Builder mBuilder =new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification_icon)
                                    //Mostramos si la notificacion tendra un icno y cual
                        .setContentTitle("Tomar: ") //EL titulo de nuestra notificacion
                        .setContentText("Debe Tomar el medicamento:" + medicamentoNoti +"a las: " + horaNoti )
                                //UNa descricion más detallada a mostrar
                        .setTicker("Tomar Medicamento")
                        .setVibrate(new long[]{100, 250, 200, 450})
                                //si nuestra notificacion "vibrara" y un arreglo de los timepos de vibrado
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            //activamos el sonido
                        .setAutoCancel(true); //se borrará del panel de notifiacines al abrila, si como para metro recibe ""true
                        //.setWhen(System.currentTimeMillis()+ayudarFechas.fijarHprayFechaNotificacion(horaNoti,fechaNoti))
                        //.setShowWhen(true)
                        //Estamos creando la notificacion y como estara compuesta estos soon solo algunas de las opciones dispobles

        Intent resultIntent = new Intent(this, NotificacionActivity.class);
        resultIntent.putExtra("Ïd_Notificacion", mId);
        resultIntent.putExtra("nombre_Medicamento",medicamentoNoti);
        resultIntent.putExtra("hora_medicamento",horaNoti);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack (MainActivity.class); //(ResultActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());
        Log.d("Prueba de notificacion exito"," Funciono");
    }

    public void alarmaMedicamento(int idAlarmIntent,String medicamentoNoti, String horaNoti, String fechaNoti, int idOriginal){
        //POne el temporizador, del sistema para lanzar eventons,
        long milisParaLanzarApp=ayudarFechas.fijarHprayFechaNotificacion(horaNoti,fechaNoti);
        //obtine el valor en milisegundos para el moemntos del sistema en que debe lanzar el evento
        int alarmRequestCode=idAlarmIntent;

        AlarmManager alarms = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        //obtenemos el servicio del sustema a usar

        Receiver receiver = new Receiver(); //creamos un ojeto de clase en que recivirá los eventos
        IntentFilter filter = new IntentFilter("ALARM_ACTION");
        registerReceiver(receiver, filter);

        Intent intent = new Intent("ALARM_ACTION");
        intent.putExtra("idAlarmIntent", idAlarmIntent);
        intent.putExtra("medicamentoAlarmIntent",medicamentoNoti);
        intent.putExtra("horaAlarmiIntent",horaNoti);
        intent.putExtra("fechaAlarmIntent",fechaNoti);
        intent.putExtra("idOriginalIntent",idOriginal);//mando por intent los valores a usar para el evento del temporizador

        PendingIntent operation = PendingIntent.getBroadcast(this, alarmRequestCode, intent, 0);

        Log.d("Datos de milesegundos para alarma",String.valueOf(milisParaLanzarApp)+" "+String.valueOf(alarmRequestCode));
        alarms.set(AlarmManager.RTC_WAKEUP,milisParaLanzarApp, operation);
        intentArray.add(operation);

        Log.d("Esto es una prueba", "El codigod e alarma se ejecuta correctamente");
        //fijamos la alarma con los valores y el servicio necesarios

        cursor=dbHelper.getHoraYfechaParaActulizar(idOriginal);
        if(cursor.moveToFirst()){
            String horaCursor=cursor.getString(1);
            String lapsosCursor=cursor.getString(2);
            String fechaCursor=cursor.getString(3);
            String fechaFinalCursor=cursor.getString(4);
            String horaFinalCursor=cursor.getString(5);

            if(ayudarFechas.fijarHprayFechaNotificacion(horaCursor,fechaCursor)<ayudarFechas.fijarHprayFechaNotificacion(horaFinalCursor,fechaFinalCursor)){
                ayudarFechas.updataHoraYFecha(String.valueOf(idOriginal),horaCursor,lapsosCursor,fechaCursor);

                cursor2=dbHelper.getHoraYfechaParaActulizar(idOriginal);
                if(cursor2.moveToFirst()) {
                    String horaCursor2=cursor2.getString(1);
                    String fechaCursor2=cursor2.getString(3);
                    alarmaMedicamento(idAlarmIntent + 100, medicamentoNoti, horaCursor2, fechaCursor2,idOriginal);
                }
                if(!cursor2.isClosed()) cursor2.close();
                Log.d("La notificacion a futuro fue creada con los datos: Id",String.valueOf(idAlarmIntent+100)
                        +" Hora y fecha: "+horaCursor + fechaCursor);
            }
        }
        if(cursor.isClosed()) cursor.close();
    }

    public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            //Aqui se pone los eventosa usar al lanzar la alarma al termianr el temporizador
            Log.d("Funciono", "hola");
            int idMedicamento=intent.getIntExtra("idAlarmIntent",0);
            String medicamnetoMed=intent.getStringExtra("medicamentoAlarmIntent");
            String horaMed=intent.getStringExtra("horaAlarmiIntent");
            String fechaMed=intent.getStringExtra("fechaAlarmIntent");
            int idOriginall=intent.getIntExtra("idOriginalIntent",-1);

            Log.d("Los intents fueron extraidos correctamente",String.valueOf(idMedicamento));

            notificarMedicamento(idMedicamento,medicamnetoMed,horaMed,idOriginall);
            //Toast.makeText(context, "Probrnando 1,2,3 ", Toast.LENGTH_SHORT).show();
        }
    }
}
