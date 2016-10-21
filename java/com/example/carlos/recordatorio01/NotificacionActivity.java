package com.example.carlos.recordatorio01;

import android.app.NotificationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class NotificacionActivity extends ActionBarActivity {

    TextView infoNotificacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion);

        infoNotificacion = (TextView)findViewById(R.id.infoView);

        String medicamentoATomar=getIntent().getStringExtra("nombre_Medicamento");
        String horaATomar=getIntent().getStringExtra("hora_medicamento");
        infoNotificacion.setText("Debe Tomar el medicamento: '" + medicamentoATomar +  "' A la hora: "
                + horaATomar + " \n Es muy importante que tome sus medicamentos, a la hora debida, es "
                + "por su salud, mejore pronto");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notificacion, menu);
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

    public void botonOk (View v){
        finish();
    }
}
