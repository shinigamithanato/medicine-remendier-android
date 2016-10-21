package com.example.carlos.recordatorio01;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class AgregarMedicamento extends ActionBarActivity {

    EditText medicamentoView;
    EditText lapsosDosisView;
    EditText primeraDosisHoraView;
    EditText diasView;
    EditText primeraDosisFechaView;

    private agregarMedicamentoDataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_medicamento);

        medicamentoView=(EditText)findViewById(R.id.medicamentoText);
        lapsosDosisView=(EditText)findViewById(R.id.lapsoText);
        primeraDosisHoraView=(EditText)findViewById(R.id.horaDosis);
        diasView=(EditText)findViewById(R.id.duracionText);
        primeraDosisFechaView=(EditText)findViewById(R.id.fechaText);

        dataBaseHelper=new agregarMedicamentoDataBaseHelper(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_agregar_medicamento, menu);
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

    public void agregarRecordatorio(View view){
        /*AL apretar el boton agregar recordatorio,
           graba el nuevo medicamentos el aabase de datos */
        boolean validarDatos=false; //nos sirve de control para poder hacer tareas en secuencia
                  //si una Y depende de X, nos evita que siga, ademas de agregar a la base de datos
        String []fechaManipulacion = {"null","null","null"};
        String fechaFInal="";
        Intent intent = getIntent();

        try {
            String medicamentoNombre = medicamentoView.getText().toString();
            String lapsoDosisString = lapsosDosisView.getText().toString();
            int lapsoDosis = -1;
            if(!lapsoDosisString.equals("")) lapsoDosis = Integer.parseInt(lapsoDosisString);
            String primeraDosis = primeraDosisHoraView.getText().toString();
            String diasDuracionString = diasView.getText().toString();
            int diasDuracion =-1;
            if(!diasDuracionString.equals("")) diasDuracion = Integer.parseInt(diasDuracionString);
            String fechaInicial = primeraDosisFechaView.getText().toString();
            /*EN estas declaraciones de variables solo se extraen los datos de los text view, y en
            caso de que sea de tipo int el dato a manejar lo extrae y loo convierte
             */

            if(medicamentoNombre.equals("") ) {
                Log.i("Estado: ","Falto El nombre del medicamento");
                validarDatos=true;
            }
            if(lapsoDosis==-1){
                Log.i("Estado: ", "Falto el tiempo ebtre las dosis");
                validarDatos=true;
            }
            if(primeraDosis.equals("")){
                Log.i("Estado: ", "Falto la hora en que se tomo la priemera dosis");
                validarDatos=true;
            }
            if(diasDuracion==-1){
                Log.i("Estado: ", "Falto cuanto tiempo se tomara el medicamento");
                validarDatos=true;
            }
            if(fechaInicial.equals("")){
                Log.i("Estado: ", "Falto el dia en que se tomo la primera dosis");
                validarDatos=true;
            }
            /*Nos sirve de contrl para valdar que los textView no sean nulos y mandar el mensjae,
            * donde falte*/

            Log.i("Estado: ",String.valueOf(!validarDatos));

            if(!validarDatos){
                fechaInicial=fechaInicial.replace("/","-");
                /*Cpmp el tipo de dato DATE en la base de datos, solo perimete '-', nos aseguramos
                que ese sea el formato correcto remplezando el '/' si fue introdocido
                 */
                Log.i("fecha remplazada: ",fechaInicial);
                fechaManipulacion=fechaInicial.split("-",3); //divimos  la fecha en su dia, mes año
                // en un vector, para su manipulacion
            }

            int day = 0,month=0,year=0; //aqui se usara el vector de fecha
            String horaFina="";//Strin que se usara para contener la fecha final

            if(fechaManipulacion[2].equals("null") ||fechaManipulacion[1].equals("null")){
                //compureba que el vector se creo, de lo contrario ya no permite
                Log.i("Estado: ","Fecha mal colocada");
                validarDatos=true;
            } else{
                int monthDay;
                day=Integer.parseInt(fechaManipulacion[0]);
                month=Integer.parseInt(fechaManipulacion[1]);
                year=Integer.parseInt(fechaManipulacion[2]);

                if(year<1000) year=year+2000; //nos valida quue use el formato YYYY y no YY, formato
                    //usado en el tipo de DATE en la base de datps
                if(month>12 && day>12) validarDatos=true; //nos valida que no haya puesto mal el mes
                if(month>12 && validarDatos==false){ //SI puso el formato: MM/DD/YYYY, nos asegura
                        //que se ponga el formato DD-MM-YYYY
                    monthDay=month;
                    month=day;
                    day=monthDay;
                }
                if(validarDatos==false){
                    fechaInicial=String.valueOf(year) + "-" + String.valueOf(month) + "-"
                            +String.valueOf(day);/* si Todolodemas  es validado nosc cambia el
                            formato introducido por el usuario: DD-MM-YYYY a un formato valido para
                            el tipo de Dato DATE que guardaremos en la base de datos para la fecha
                            inicial*/
                } else {
                    Log.i("Estado: ","No existe el mes introducido");
                }
            }

            if(!validarDatos){
                String temporalS=":";
                String []horaManipulacion=primeraDosis.split(temporalS); //Nos divide la hora
                    //introducida en un vector para su correcta manipulacion
                int horaManI=Integer.parseInt(horaManipulacion[0]);

                int horaManF=horaManI+lapsoDosis; //Nos da la siguiente hora de las dosis
                if(horaManF>23){ //Nos asegura que la hora se mantenga menos de 24
                    horaManF=horaManF-23; //Nos da el salto del día en caso de que la neuva hora
                    day=day+1; //se mayor a 24
                }
                horaFina=String.valueOf(horaManF)+":"+horaManipulacion[1]; //No da la ultima hora
                    //en que el medicamento se tomará
                day=day+diasDuracion; //Nos da el día final de la receta medica
                if(day>tipoDeMes(day,year)){ //Nos asegura que no se pase de dia del mes que nos
                    day=day-tipoDeMes(day,year); //corresponde
                    month=month+1; //en caso de que se mayor, le suma un mes
                }
                if(month>12){ //si se pasa de meses 12 meses le suma un año y nos da el neuvo mes
                    month=month-12; // a termianr la dosis
                    year=year+1;
                }
                fechaFInal=String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
                //Nos da la fecha final del la receta
                if(!validarDatos) {
                    Log.i("resultado a inroducir:", medicamentoNombre + " " + fechaInicial + " " + fechaFInal
                            + " " + " " + primeraDosis + " " + lapsoDosis + " " + horaFina);
                    dataBaseHelper.saveMedicamento(medicamentoNombre, fechaInicial,
                            fechaFInal, primeraDosis, lapsoDosis, horaFina);
                    //*Si todoesta bien  los introdice a la base de datos/
                    this.setResult(RESULT_OK,intent);
                    finish();//SI salio bien cierra el intent de agregar medicamentos y manda que t
                    // el resultado que todosalio bien
                }
            }


        }catch (NullPointerException ex){
            System.out.println(ex);
        }
    }


      private int tipoDeMes(int mes,int anio){
        if(mes==2 && (anio%4)!=0)
            return 28;
         else if(mes==2 &&(anio%4)==0) return 29;
        if(mes==1 || mes==3 || mes==5 || mes==7 || mes==8 || mes==10 || mes==12)
            return 31;
        else return 30;
    }

}
