package emirodriguez.loginapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import emirodriguez.loginapp.modelos.Evento;

public class EventosActivity extends AppCompatActivity {

    private ListView lvEventos;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);

        setTitle("Eventos");
        lvEventos = (ListView) findViewById(R.id.lvEventos);

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Cargando eventos...");

        EventosApi eventosApi = new EventosApi();
        eventosApi.execute("http://190.246.157.102/congreso/Api/eventos/todos");
    }

    public class EventosApi extends AsyncTask<String, String, String> {

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
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(final String jsonString) {
            super.onPostExecute(jsonString);

            dialog.dismiss();

            try
            {
                final JSONObject json = new JSONObject(jsonString);
                final JSONArray jsonArray = json.getJSONArray("eventos");

                final ArrayList<Evento> eventos = Evento.fromJson(jsonArray);

                EventosAdapter adapter = new EventosAdapter(EventosActivity.this, eventos);

                lvEventos.setAdapter(adapter);

                lvEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Evento evento = eventos.get(position);
                        Intent intent = new Intent(EventosActivity.this, CursosActivity.class);

                        try
                        {
                            intent.putExtra("evento", jsonArray.getJSONObject(position).toString());

                            startActivity(intent);
                        }catch (JSONException e)
                        {
                            Toast.makeText(EventosActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            catch (Exception e)
            {
                Log.e("ERROR", "Json no convertido");
            }
        }
    }

    public class EventosAdapter extends ArrayAdapter<Evento> {
        public EventosAdapter(Context context, ArrayList<Evento> eventos) {
            super(context, 0, eventos);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Evento evento = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_evento, parent, false);
            }

            TextView tvNombre = (TextView) convertView.findViewById(R.id.tvNombre);

            tvNombre.setText(evento.getNombre());

            return convertView;
        }
    }
}
