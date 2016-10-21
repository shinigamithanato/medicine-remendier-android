package com.example.carlos.recordatorio01;

/**
 * Created by Carlos on 17/11/2015.
 */
public class RecordatorioMedicamento {
    /*
    Es una clase de soporte, en cual se usara junto con el apadter, sirve para crear objetos de
    un tipo y guardar sus datos de forma temporal, y daar uso de ellos más tarde.

     */
    private String nombreMedicamentos;
    private String HoraATomarMedicamentos;

    public RecordatorioMedicamento(String nombreMedicamento, String HoraATomarMedicamento){
        this.nombreMedicamentos=nombreMedicamento;
        this.HoraATomarMedicamentos=HoraATomarMedicamento;
    }

    public String getNombreMedicamnetos(){
        return nombreMedicamentos;
    }
    public String getHoraATomarMedicamentos(){
        return HoraATomarMedicamentos;
    }

    public void setHoraATomarMedicamentos(String horaATomarMedicamentos) {
        HoraATomarMedicamentos = horaATomarMedicamentos;
    }

    public void setNombreMedicamentos(String nombreMedicamento){
        nombreMedicamentos=nombreMedicamento;
    }
}
