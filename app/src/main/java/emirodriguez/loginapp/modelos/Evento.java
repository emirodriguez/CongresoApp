package emirodriguez.loginapp.modelos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Evento {
    private int idEvento;
    private String nombre;
    private String fechaInicio;
    private String fechaFin;
    private String descripcion;
    private String lugar;

    private List<Curso> cursos;

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public void setCursos(List<Curso> cursos) {
        this.cursos = cursos;
    }

    public static Evento fromJson(JSONObject jsonObject) {
        Evento evento = new Evento();

        try {
            evento.idEvento = jsonObject.getInt("idEvento");
            evento.nombre = jsonObject.getString("nombre");
            evento.fechaInicio = jsonObject.getString("fecha_inicio");
            evento.fechaFin = jsonObject.getString("fecha_fin");
            evento.descripcion = jsonObject.getString("descripcion");
            evento.lugar = jsonObject.getString("lugar");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return evento;
    }

    public static ArrayList<Evento> fromJson(JSONArray jsonArray) {
        JSONObject eventosJson;
        ArrayList<Evento> eventos = new ArrayList<Evento>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            try {
                eventosJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Evento evento = Evento.fromJson(eventosJson);
            if (evento != null) {
                eventos.add(evento);
            }
        }

        return eventos;
    }
}
