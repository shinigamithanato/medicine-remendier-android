package com.example.carlos.recordatorio01;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Carlos on 20/11/2015.
 */
public class AgregarMedicamentoCursorAdapter extends CursorAdapter{

    public AgregarMedicamentoCursorAdapter(Context context, Cursor cursor){
        super(context,cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.medicamentossuport,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        try {
            TextView nombreTextView = (TextView) view.findViewById(R.id.medicamento_view);
            TextView horaTextView = (TextView) view.findViewById(R.id.hora_view);

            nombreTextView.setText(cursor.getString(0));
            horaTextView.setText(cursor.getString(1));
        }catch (Exception ex){
            System.out.println(ex);
        }
    }
}
