package com.example.carlos.recordatorio01;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Carlos on 16/11/2015.
 */
public class AgregarMedicamentoAdapter extends BaseAdapter{
    private ArrayList<RecordatorioMedicamento> medicamentos=new ArrayList<RecordatorioMedicamento>();
    /*se creará un array list de objetos del tipo, en cual guarda un objeto en X posicion del array
    list, de esta forma se accede a ellos despues, al llamar a su posicion y se extrae el datoo
    necesario*/

    public AgregarMedicamentoAdapter(){
       //medicamentos.add(new RecordatorioMedicamento("Depakene","8:00"));
       //crea el objeto y lo guarda en el array list en su X, este soloo se crea y existe en la
       //ejecucion de la app, "desapareceran" allcerrrarla, pero para eso usamos la base de datos
       //para que exista de forma permanente
    }

    public void usarCursorconlista(String getMedicamentos,String getHora){
        medicamentos.add(new RecordatorioMedicamento(getMedicamentos,getHora));
    }

    @Override
    public int getCount() {
        return medicamentos.size();
        //return 0;
        //nos regresa la cantidad de objetos que se han almacenado en el array list
    }

    @Override
    public Object getItem(int position) {
        return getItem(position);
        //nos regresa el objeto en la posicion indicada
        //return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
        //nos da el objeto en la posicioon indicada, si id
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater=LayoutInflater.from(parent.getContext());
            convertView=inflater.inflate(R.layout.medicamentossuport,parent,false);
            //checa su nuestra view es nula, en caso de cerla, la recibe y la "infla"
        }

        RecordatorioMedicamento recordatorios=medicamentos.get(position);
        //Nos sirve para agregar a la lista y llenar el View

        TextView nombreTextView=(TextView)convertView.findViewById(R.id.medicamento_view);
        TextView horaTextView=(TextView)convertView.findViewById(R.id.hora_view);

        nombreTextView.setText(recordatorios.getNombreMedicamnetos());
        horaTextView.setText(recordatorios.getHoraATomarMedicamentos());
        //Estas últimas lineas, nos sirven como apoyo, con unos textView, para poder agregar,
        //multiples elementos a un listView en este caso, o a un tipo de View que usaremos
        return convertView;
        //nos regresa el View, que usamos como soporte, que se añadira al listView, que para este uso
        //se pcupa el adapter que hemos usado. normalmente un listView usa arrayList o list, pero
        //como trabajamos con multiples elementos para uno solo, para eso usamos el adaptador,
        //que nos sirve de inrmediario, para ajsutar los elementos   al tipo listView.
    }

}
