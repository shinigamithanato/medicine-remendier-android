package com.example.carlos.recordatorio01;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Carlos on 23/11/2015.
 */
public class FechasHelper {
    private agregarMedicamentoDataBaseHelper dbHelper;

    public FechasHelper(Context context){
        dbHelper=new agregarMedicamentoDataBaseHelper(context);

        /*CLase que ayuda con todos eventos que tenga que ver con fecha, cada
        * funcion tiene sercio en para cada caso de app a usar
           recibe el Context de la MainActivity poruqe la clase dbHelper, que noa ayudará actlzar la fecha
           ocupa recibir un COntext*/
       }

    public boolean borraarAyudaFecha(String fechaFinal) {
        /*Funcion que nos regresa un boleanoq ue nos determina si un medicamento ya cumplio su ciclo
        Y si es necesario ya borrarlo de labase de datos*/

        Calendar fechaYHoraActual=Calendar.getInstance();//calendar es una clase del sistema. os ayuda con la fecha
        String []separarFecha={"null","null","null"};
        String []separarHora={"null","null"};
        boolean control;

        separarFecha=fechaFinal.split("-"); //separamos llafecha, ya que esta en formato string a un medio de manipulacion
        //separarHora=horaFinal.split(":");
        int diaBaseDeDatos= Integer.parseInt(separarFecha[2]);
        int mesBaseDeDatos=Integer.parseInt(separarFecha[1]);
        int anioBaseDeDatos=Integer.parseInt(separarFecha[0]);

        int diaActual=fechaYHoraActual.get(Calendar.DAY_OF_MONTH);
        int mesActual=fechaYHoraActual.get(Calendar.MONTH) + 1;
        int aniooActual=fechaYHoraActual.get(Calendar.YEAR); //con la cllase calendar obtenemos los valores


        if(diaActual>diaBaseDeDatos && (mesActual>=mesBaseDeDatos && aniooActual>=anioBaseDeDatos))
            return false; // Mientras el dia sea actual retornafalso que significa que sera boorrado
        else if(mesActual>mesBaseDeDatos && aniooActual>=anioBaseDeDatos)
                return false; /*si el dia actual no fue nayor, tenemosqu verificar que no salto de mes, si sallto de vez regresa
                        //falso que significa que ya no puede seguir en la base de datos false=exitorBaseDeDatos*/
            else if (aniooActual>anioBaseDeDatos)
                    return false; //si ni el mes ni el dia son mayores, ahora verificamos que no salto de año
                else return true; //si lo anterior fallo significa que la fecha actual sigue siendo mayor a la ultima de la
                   //que puede mandar notificaciones de la base de dagtos y no puede boorarlo mando un true=existirenlabasededatos
    }


    public void updataHoraYFecha(String idMedicamento,String horaMedicamento,String lapsosMedicamento,String fechaMedicamento){
        //Actualiza la hora y fehca siguientes, usando la informacion al momento que se manda  la notificacion
        String[] horaCadena=divideHora(horaMedicamento);
        String[] fechaCaden=divideFecha(fechaMedicamento);
        String horaNueva;
        String fechaNueva;

        int id=Integer.parseInt(idMedicamento);

        int hora=Integer.valueOf(horaCadena[0]);
        int minutos=Integer.valueOf(horaCadena[1]);

        int anio=Integer.valueOf(fechaCaden[0]);
        int mes=Integer.valueOf(fechaCaden[1]);
        int dia=Integer.valueOf(fechaCaden[2]);

        int peridos=Integer.valueOf(lapsosMedicamento);
        //las declariones anteriores e inicializaciones nos dan el dato recuperdadopor el cursor en string en un formaro para
        //su facil manipualacion, para poder relzar correctament el updade

        int[] horaDiaMesAnio ={0,0,0,0};
        horaDiaMesAnio=actualizaHoraConLapsosControlador(hora,dia,mes,anio,peridos);
        //realiza los calculos para obtener la nueva fecha

        hora=horaDiaMesAnio[0];
        dia=horaDiaMesAnio[1];
        mes=horaDiaMesAnio[2];
        anio=horaDiaMesAnio[3];

        horaNueva=recomponerHora(hora,minutos);//con los calculos antriores obtien la nueva hora
        fechaNueva=recomponerFecha(dia,mes,anio);//con los calclos ateriores ontiene la nueva fehca

        Log.d("Fecha Actualizada",String.valueOf(id) + " " + horaNueva+" " +fechaNueva );
        dbHelper.updateMedicamento(id,horaNueva,fechaNueva); //reliza el update en la base de datps
    }



    private int tipoDeMes(int mes,int anio){
        //funcion que nos determina si la cantdad de dias en el mes
        if(mes==2 && (anio%4)!=0)
            return 28; //febrero
        else if(mes==2 &&(anio%4)==0) return 29; //febreroe en año bisisto
        if(mes==1 || mes==3 || mes==5 || mes==7 || mes==8 || mes==10 || mes==12)
            return 31; //los meses de 331 d ia
        else return 30;
    }



    public String[] divideHora(String horaMedicamento){
        String[] horaCadena={"null","null"};
        horaCadena=horaMedicamento.split(":");
        return horaCadena;
        //funcion que nos regresa la hora en un arrglo de tipo int para su manipualcion, de
    }

    public String[] divideFecha(String fechMedicmaneto){
        String[] fechaCadena={"null","null","null"};
        fechaCadena=fechMedicmaneto.split("-");
        return  fechaCadena;
    }



    public String recomponerHora(int hora, int minutos){
        String horaString=String.valueOf(hora);
        String minutuosString=String.valueOf(minutos);
        String horaRecompuesta;

        horaRecompuesta=horaString + ":" + minutuosString;
        return  horaRecompuesta;
    }

    public String recomponerFecha(int dia, int mes, int anio){
        String diaString=String.valueOf(dia);
        String mesString=String.valueOf(mes);
        String anioString=String.valueOf(anio);

        String fecha;
        fecha=anioString + "-" +  mesString + "-" + diaString;

        return fecha;
    }



    int[] actualizaHoraConLapsosControlador(int hora, int dia, int mes, int anio, int peridos){
        //funcion que los datos de la fecha y hora, nos dará la nueva  hora tras los calcilos
        int[] horaDiaMesAnio ={0,0,0,0};
        hora=hora+peridos;
        if (hora>23){
            hora=hora-24;
            dia++;
        }
        if (dia>tipoDeMes(mes,anio)){
            dia=dia-tipoDeMes(mes,anio);
            mes++;
        }
        if(mes>12){
            mes=mes-12;
            anio++;
        }
        horaDiaMesAnio[0]=hora;
        horaDiaMesAnio[1]=dia;
        horaDiaMesAnio[2]=mes;
        horaDiaMesAnio[3]=anio;

        return horaDiaMesAnio;
    }

    public long fijarHprayFechaNotificacion(String horaNotificacion, String fechaNotificacion){
        //funcion que recibe los datos en el formato de date, para regresarnos los milesegundos apra ese evetno
        String[] horaCadena=divideHora(horaNotificacion);
        String[] fechaCaden=divideFecha(fechaNotificacion);

        int hora=Integer.valueOf(horaCadena[0]);
        int minutos=Integer.valueOf(horaCadena[1]);

        int anio=Integer.valueOf(fechaCaden[0]);
        int mes=Integer.valueOf(fechaCaden[1]);
        int dia=Integer.valueOf(fechaCaden[2]);

        Calendar calendario = Calendar.getInstance();

        Log.d("Datos:",horaCadena[0]+":"+horaCadena[1]+"   "+fechaCaden[2]+"/"+(fechaCaden[1]+"/"+fechaCaden[0]));

        calendario.setTimeInMillis(System.currentTimeMillis()); //fijamos cuantos segundos vaman como referencia
        calendario.clear();
        calendario.set(anio,mes-1,dia,hora,minutos); //ponemso la fecha para dar la nueva a nuestra instqancioa de calendar


        return calendario.getTimeInMillis(); //nos regresa un long con los milesegundos en la instacia fijada anteriormente
    }

}
