package emirodriguez.loginapp;

import android.app.ProgressDialog;
import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import emirodriguez.loginapp.modelos.Curso;
import emirodriguez.loginapp.modelos.Evento;

public class CursosActivity extends AppCompatActivity {

    TextView tvNombreEvento;
    TextView tvDescripcionEvento;
    TextView tvFechaInicio;
    TextView tvFechaFin;
    TextView tvLugar;
    ListView lvCursos;
    ProgressDialog dialog;

    int idEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursos);

        tvNombreEvento = (TextView) findViewById(R.id.tvNombreEvento);
        tvDescripcionEvento = (TextView) findViewById(R.id.tvDescripcionEvento);
        tvFechaInicio = (TextView) findViewById(R.id.tvFechaInicio);
        tvFechaFin = (TextView) findViewById(R.id.tvFechaFin);
        tvLugar = (TextView) findViewById(R.id.tvLugar);
        lvCursos = (ListView) findViewById(R.id.lvCursos);


        Bundle bundle = getIntent().getExtras();
        String eventoJsonString = bundle.getString("evento");

        try {
            JSONObject eventoJson = new JSONObject(eventoJsonString);

            Evento evento = Evento.fromJson(eventoJson);

            setTitle(evento.getNombre() + " - Cursos");

            tvNombreEvento.setText(evento.getNombre());
            tvDescripcionEvento.setText(evento.getDescripcion());
            tvDescripcionEvento.setMovementMethod(new ScrollingMovementMethod());
            tvLugar.setText(evento.getLugar());
            tvFechaInicio.setText(evento.getFechaInicio());
            tvFechaFin.setText(evento.getFechaFin());

            idEvento = evento.getIdEvento();

            dialog = new ProgressDialog(this);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.setMessage("Cargando cursos...");

            CursosApi eventosApi = new CursosApi();
            eventosApi.execute("http://190.246.157.102/congreso/Api/cursos/todos");

        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
    }

    public class CursosApi extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            dialog.show();
        }

        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(final String jsonString) {
            super.onPostExecute(jsonString);

            dialog.dismiss();

            try {
                final JSONObject json = new JSONObject(jsonString);
                final JSONArray jsonArray = json.getJSONArray("cursos");

                final ArrayList<Curso> cursos = Curso.fromJson(jsonArray, idEvento);

                CursosAdapter adapter = new CursosAdapter(CursosActivity.this, cursos);

                lvCursos.setAdapter(adapter);
            } catch (Exception e) {
                Log.e("ERROR", "Json no convertido");
            }
        }
    }

    public class CursosAdapter extends ArrayAdapter<Curso> {
        public CursosAdapter(Context context, ArrayList<Curso> cursos) {
            super(context, 0, cursos);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Curso curso = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_curso, parent, false);
            }

            TextView tvNombreCurso = (TextView) convertView.findViewById(R.id.tvNombreCurso);
            TextView tvDescripcionCruso = (TextView) convertView.findViewById(R.id.tvDescripcionCurso);
            TextView tvDiaHora = (TextView) convertView.findViewById(R.id.tvDiaHora);
            TextView tvDuracion = (TextView) convertView.findViewById(R.id.tvDuracion);

            tvNombreCurso.setText(curso.getNombre());
            tvDescripcionCruso.setText(curso.getDescripcion());
            tvDiaHora.setText(curso.getDiaHora());
            tvDuracion.setText("Duración: " + curso.getDuracion());

            return convertView;
        }
    }
}
