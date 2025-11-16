package com.example.apppronsticoclima.Repository

import android.content.Context
import com.example.apppronsticoclima.Repository.modelos.Ciudad

/**
 * Responsable de guardar y leer la configuración del usuario.
 * En este caso: la ciudad seleccionada.
 *
 * Implementado con SharedPreferences.
 */
interface UserPreferences {

    /** Guarda la última ciudad seleccionada por el usuario. */
    fun guardarCiudadSeleccionada(ciudad: Ciudad)

    /** Devuelve la ciudad seleccionada, o null si no hay ninguna guardada. */
    fun obtenerCiudadSeleccionada(): Ciudad?

    /** Limpia la ciudad guardada  */
    fun limpiarCiudadSeleccionada()
}

class UserPreferencesImpl(context: Context) : UserPreferences {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    override fun guardarCiudadSeleccionada(ciudad: Ciudad) {
        prefs.edit()
            .putString(KEY_NOMBRE, ciudad.name)
            .putFloat(KEY_LAT, ciudad.lat)
            .putFloat(KEY_LON, ciudad.lon)
            .putString(KEY_COUNTRY, ciudad.country)
            .putString(KEY_STATE, ciudad.state)
            .apply()
    }

    override fun obtenerCiudadSeleccionada(): Ciudad? {
        val nombre = prefs.getString(KEY_NOMBRE, null) ?: return null

        // Si no tenemos lat/lon, consideramos que no hay ciudad válida guardada
        if (!prefs.contains(KEY_LAT) || !prefs.contains(KEY_LON)) return null

        val lat = prefs.getFloat(KEY_LAT, 0f)
        val lon = prefs.getFloat(KEY_LON, 0f)
        val country = prefs.getString(KEY_COUNTRY, "") ?: ""
        val state = prefs.getString(KEY_STATE, null)

        return Ciudad(
            name = nombre,
            lat = lat,
            lon = lon,
            country = country,
            state = state
        )
    }

    override fun limpiarCiudadSeleccionada() {
        prefs.edit()
            .remove(KEY_NOMBRE)
            .remove(KEY_LAT)
            .remove(KEY_LON)
            .remove(KEY_COUNTRY)
            .remove(KEY_STATE)
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val KEY_NOMBRE = "ciudad_nombre"
        private const val KEY_LAT = "ciudad_lat"
        private const val KEY_LON = "ciudad_lon"
        private const val KEY_COUNTRY = "ciudad_country"
        private const val KEY_STATE = "ciudad_state"
    }
}
