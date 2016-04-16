package emirodriguez.loginapp.modelos;

import android.content.ContentUris;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import emirodriguez.loginapp.CursosActivity;

public class Curso {
    private int idCurso;
    private String nombre;
    private String descripcion;
    private boolean activo;
    private String diaHora;
    private int duracion;
    private int idEvento;

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getDiaHora() {
        return diaHora;
    }

    public void setDiaHora(String diaHora) {
        this.diaHora = diaHora;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public static Curso fromJson(JSONObject jsonObject) {
        Curso curso = new Curso();

        try {
            curso.idCurso = jsonObject.getInt("id");
            curso.nombre = jsonObject.getString("nombre");
            curso.descripcion = jsonObject.getString("descripcion");
            curso.diaHora = jsonObject.getString("dia_hora");
            curso.duracion = jsonObject.getInt("Duracion");
            curso.idEvento = jsonObject.getInt("Eventos_idEvento");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return curso;
    }

    public static ArrayList<Curso> fromJson(JSONArray jsonArray, int idEvento) {
        JSONObject cursosJson;
        ArrayList<Curso> cursos = new ArrayList<Curso>(jsonArray.length());

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                cursosJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Curso curso = Curso.fromJson(cursosJson);
            if (curso != null && curso.getIdEvento() == idEvento) {
                cursos.add(curso);
            }
        }

        return cursos;
    }
}
