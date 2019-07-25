package com.androidavanzado.pruebatecnicainterfell.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidavanzado.pruebatecnicainterfell.Adapters.contraversia_adapter;
import com.androidavanzado.pruebatecnicainterfell.DB.AdminSQLiteOpenHelper;
import com.androidavanzado.pruebatecnicainterfell.Models.Contraversia;
import com.androidavanzado.pruebatecnicainterfell.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ListContravencionFragment extends Fragment {

    private Context context;
    private AdminSQLiteOpenHelper myDB;
    private contraversia_adapter adapter;

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

    // String selected
    private String dateSelected = "";
    private String hourSelected = "";

    //Views
    private EditText edtTime, edtHour;
    private Button btnSearch;
    private RecyclerView recyclerViewList;

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_contravencion, container, false);

        context = getActivity();
        myDB = new AdminSQLiteOpenHelper(context);

        initViews();
        initActions();

        return view;
    }

    private void initViews(){
        edtTime = view.findViewById(R.id.editTextTime);
        edtHour = view.findViewById(R.id.editTextHour);
        btnSearch = view.findViewById(R.id.buttonSearch);
        recyclerViewList = view.findViewById(R.id.recycler_list);
    }

    private void initActions(){
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateForm()){
                    //Validamos el ultimo digito
                    List<Contraversia> contraversias = myDB.selectData(edtTime.getText().toString(), edtHour.getText().toString());
                    if(contraversias.size() > 0){
                        if(adapter == null){
                            adapter = new contraversia_adapter(context, contraversias);
                        }
                        recyclerViewList.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewList.setAdapter(adapter);
                        recyclerViewList.setLayoutManager(new LinearLayoutManager(context));
                        //Toast.makeText(context, "Se tienen: " + contraversias.size() + " contraversias", Toast.LENGTH_LONG).show();
                    } else {
                        adapter = new contraversia_adapter(context, contraversias);
                        recyclerViewList.setItemAnimator(new DefaultItemAnimator());
                        recyclerViewList.setAdapter(adapter);
                        recyclerViewList.setLayoutManager(new LinearLayoutManager(context));
                        Toast.makeText(context, "No se tienen contraversias", Toast.LENGTH_LONG).show();
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

    private boolean validateForm(){
        boolean validate;
        List<Boolean> results = new ArrayList<>();
        results.clear();

        if(TextUtils.isEmpty(edtTime.getText())){
            edtTime.setError("La fecha es requerida");
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
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }
}
