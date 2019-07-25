package com.androidavanzado.pruebatecnicainterfell.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidavanzado.pruebatecnicainterfell.DB.AdminSQLiteOpenHelper;
import com.androidavanzado.pruebatecnicainterfell.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddContravencionFragment extends Fragment {

    private Context context;
    AdminSQLiteOpenHelper myDB;

    private static final String CERO = "0";
    private static final String BARRA = "/";
    private static final String DOS_PUNTOS = ":";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);

    private String dayOfWeek = "";

    private String hourInitA = "7:00";
    private String hourInitB = "9:30";
    private String hourFinalA = "16:00";
    private String hourFinalB = "19:30";

    // String selected
    private String plateSelected = "";
    private String dateSelected = "";
    private String hourSelected = "";

    //Views
    private EditText edtPlate, edtTime, edtHour;
    private Button btnSearch;

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_contravencion, container, false);

        context = getActivity();
        myDB = new AdminSQLiteOpenHelper(context);

        int incidencias = myDB.countTotalData();

        //Toast.makeText(context, "Total de incidencias: " + incidencias, Toast.LENGTH_LONG).show();

        initViews();
        initActions();

        return view;
    }


    private void initViews(){
        edtPlate = view.findViewById(R.id.editTextPlate);
        edtTime = view.findViewById(R.id.editTextTime);
        edtHour = view.findViewById(R.id.editTextHour);
        btnSearch = view.findViewById(R.id.btnSearch);
    }

    private void initActions(){
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()){
                    //Validamos el ultimo digito
                    plateSelected = edtPlate.getText().toString();
                    char[] cs = plateSelected.toCharArray();
                    char digitChar = cs[plateSelected.length()-1];
                    String digitStr = Character.toString(digitChar);
                    try {
                        int digit = Integer.parseInt(digitStr);
                        // Validamos si coincide el día
                        String day = getDayXPlate(digit);
                        if(day.equals(dayOfWeek)){
                            // Validamos si coincide la hora
                            if(validateHour()){
                                // Obtenemos el numero de Contravencias e insertamos una nueva
                                int incidencias = myDB.countData(plateSelected);
                                if(incidencias > 0){
                                    alertDialog(incidencias);
                                } else {
                                    insertContraversia();
                                }
                            } else {
                                Toast.makeText(context, "Puede circular sin problema", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "Puede circular sin problema", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e){
                        Toast.makeText(context, "Su placa necesita terminar con número", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        edtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerFecha();
            }
        });

        edtHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerHora();
            }
        });
    }

    private void alertDialog(int contravenciones){
        try{
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setCancelable(false);
            alertBuilder.setIcon(android.R.drawable.ic_dialog_alert);
            alertBuilder.setMessage("Esta placa ya cuenta con " + contravenciones + " reincidencias de contravenciones");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    insertContraversia();
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } catch (Exception e){
            e.getMessage();
        }
    }

    private void insertContraversia(){
        if(myDB.insertData(plateSelected, dateSelected, hourSelected))
            Toast.makeText(context, "se ha insertado la controversia, placa: " + edtPlate.getText().toString(), Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "no se ha insertado la controversia de la placa: " + edtPlate.getText().toString(), Toast.LENGTH_LONG).show();
    }

    private String getDayXPlate(int digit){
        String day = "";
        switch (digit){
            case 1:
            case 2:
                day = "lunes";
                break;
            case 3:
            case 4:
                day = "martes";
                break;
            case 5:
            case 6:
                day = "miércoles";
                break;
            case 7:
            case 8:
                day = "jueves";
                break;
            case 9:
            case 0:
                day = "viernes";
                break;
        }
        return day;
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                edtTime.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                dateSelected = diaFormateado + BARRA + mesFormateado + BARRA + year;
                SimpleDateFormat simpledateformat = new SimpleDateFormat("EEEE");
                Date date = new Date(year, month, dayOfMonth-1);
                dayOfWeek = simpledateformat.format(date);
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }

    private boolean validateHour(){
        boolean validate = false;
        try {
            SimpleDateFormat format = new SimpleDateFormat("hh:mm"); //if 24 hour format
            Date time1 = format.parse(hourInitA);
            Date time2 = format.parse(hourInitB);
            Date time3 = format.parse(hourFinalA);
            Date time4 = format.parse(hourFinalB);
            Date timeInput = format.parse(edtHour.getText().toString());

            if((timeInput.after(time1) && timeInput.before(time2) || timeInput.after(time3) && timeInput.before(time4))){
                validate = true;
            }
        } catch(Exception e) {
            Log.e("Exception is ", e.toString());
        }

        return validate;
    }

    private void obtenerHora(){
        TimePickerDialog recogerHora = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Muestro la hora con el formato deseado
                edtHour.setText(horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM);
                hourSelected = horaFormateada + DOS_PUNTOS + minutoFormateado + " " + AM_PM;
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hora, minuto, false);

        recogerHora.show();
    }

    private boolean validateForm(){
        boolean validate;
        List<Boolean> results = new ArrayList<>();
        results.clear();

        if(TextUtils.isEmpty(edtPlate.getText())){
            edtPlate.setError("El numero de placa es requerido");
            results.add(false);
        } else {
            edtPlate.setError(null);
            results.add(true);
        }

        if(TextUtils.isEmpty(edtTime.getText())){
            edtTime.setError("El día es requerido");
            results.add(false);
        } else {
            edtTime.setError(null);
            results.add(true);
        }

        if(TextUtils.isEmpty(edtHour.getText())){
            edtHour.setError("La hora es requerida");
            results.add(false);
        } else {
            edtHour.setError(null);
            results.add(true);
        }
        validate = !results.contains(false);
        return validate;
    }
}
