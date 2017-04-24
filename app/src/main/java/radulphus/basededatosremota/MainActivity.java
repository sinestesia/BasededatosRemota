package radulphus.basededatosremota;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {
    private EditText dniET;
    private TextView letraTV;

    static final String SOAPACTION = "http://DNI/getLetraNif";
    private static final String METHOD = "getLetraNif";
    private static final String NAMESPACE = "http://DNI/";
    private static final String URL = "http://10.1.2.108:8084/LetraDNI/ServicioLetraDNI?wsdl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button letraBoton = (Button) findViewById(R.id.letraBoton);
        letraBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calcularLetra();
            }
        });

    }

    public void calcularLetra(){
        dniET = (EditText) findViewById(R.id.dniET);
        letraTV = (TextView) findViewById(R.id.letraTV);

        new PeticionAsincrona().execute(Integer.parseInt(dniET.getText().toString()));




    }

    private class PeticionAsincrona extends AsyncTask<Integer, Void, String >{

        @Override
        protected String doInBackground(Integer... params) {
            String resultadoFinal = null;


            try {
            /*Lo recomendado es crear esa tarea en un subproceso o hilo secundario,
        no obstante, si necesitáis hacerlo a la fuerza, se puede establecer un cambio en las políticas de restricciones
        de Android para nuestra clase (repito, no es recomendable). Lo único que habría que hacer es insertar estas dos líneas
         de código en el onCreate() de nuestra clase principal, y Android se tragará cualquier acceso a red que hagamos en el Main Thread, sin rechistar */

               // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
                //StrictMode.setThreadPolicy(policy);


                String resultadoFINAL;

                //Creacion de la Solicitud
                SoapObject request = new SoapObject(NAMESPACE, METHOD);
                // Creacion del Envelope
                SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                sobre.setOutputSoapObject(request);
                //Creacion del transporte
                HttpTransportSE transporte = new HttpTransportSE(URL);

                // Paso de parámetro
                PropertyInfo dni = new PropertyInfo();
                dni.setName("numeroDNI");
                dni.setValue(params[0]);
                dni.setType(Integer.class);
                request.addProperty(dni);

                //Llamada
                transporte.call(SOAPACTION, sobre);

                //Resultado
                SoapPrimitive resultado = (SoapPrimitive) sobre.getResponse();
                resultadoFinal = "LETRA: "+resultado.toString();
                      }
            catch (Exception e) {}

            return resultadoFinal;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            MainActivity.this.letraTV.setText(s);

        }
    }

}
