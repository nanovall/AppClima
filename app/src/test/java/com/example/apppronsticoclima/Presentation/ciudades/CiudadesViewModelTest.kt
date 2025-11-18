package com.example.apppronsticoclima.Presentation.ciudades

import com.example.apppronsticoclima.Repository.Repositorio
import com.example.apppronsticoclima.Repository.UserPreferences
import com.example.apppronsticoclima.Repository.modelos.Ciudad
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CiudadesViewModelTest {

    // Regla para el manejo de corutinas.
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Declaración de las dependencias que vamos a mockear (simular).
    @MockK
    private lateinit var repositorio: Repositorio

    @MockK(relaxed = true) // relaxed = true para no tener que definir el comportamiento de todas sus funciones.
    private lateinit var userPreferences: UserPreferences

    // La instancia del ViewModel que vamos a probar.
    private lateinit var viewModel: CiudadesViewModel

    // Esta función se ejecuta antes de cada prueba.
    @Before
    fun setUp() {
        // Inicializa los mocks declarados con @MockK.
        MockKAnnotations.init(this)
        // Crea una nueva instancia del ViewModel con las dependencias simuladas.
        viewModel = CiudadesViewModel(repositorio, userPreferences)
    }

    // Test para la intención de Buscar cuando el repositorio devuelve una lista de ciudades.
    @Test
    fun `dado que se ejecuta la intencion Buscar, cuando el repositorio devuelve ciudades, entonces el estado debe ser Resultado`() = runTest {
        // 1. Given
        val searchTerm = "London"
        val mockCiudades = listOf(Ciudad("London", 51.5074f, -0.1278f, "GB"))

        // Definición del comportamiento del mock: cuando se llame a buscarCiudad con "London", se debe devolver la lista mockeada.
        coEvery { repositorio.buscarCiudad(searchTerm) } returns mockCiudades

        // 2. When
        // Se ejecuta la intención que queremos probar.
        viewModel.ejecutar(CiudadesIntencion.Buscar(searchTerm))

        // 3. Then
        // Verfica que el estado (uiState) del ViewModel se haya actualizado correctamente.
        val expectedState = CiudadesEstado.Resultado(mockCiudades)
        assertEquals(expectedState, viewModel.uiState)
    }

    // Test para la intención Buscar cuando no se encuentran resultados.
    @Test
    fun `dado que se ejecuta la intencion Buscar, cuando el repositorio no devuelve ciudades, entonces el estado debe ser Vacio`() = runTest {
        // 1. Given
        val searchTerm = "CiudadInexistente"
        // Configuración del mock para que devuelva una lista vacía.
        coEvery { repositorio.buscarCiudad(searchTerm) } returns emptyList()

        // 2. When
        viewModel.ejecutar(CiudadesIntencion.Buscar(searchTerm))

        // 3. Then
        // Verfica que el estado del ViewModel sea Vacio.
        assertEquals(CiudadesEstado.Vacio, viewModel.uiState)
    }

    // Test para la intención Buscar cuando ocurre un error.
    @Test
    fun `dado que se ejecuta la intencion Buscar, cuando el repositorio lanza una excepcion, entonces el estado debe ser Error`() = runTest {
        // 1. Given
        val searchTerm = "CiudadConError"
        val errorMessage = "Error de red"
        // Configuración del mock para que lance una excepción.
        coEvery { repositorio.buscarCiudad(searchTerm) } throws Exception(errorMessage)

        // 2. When
        viewModel.ejecutar(CiudadesIntencion.Buscar(searchTerm))

        // 3. Then
        // Verfica que el estado del ViewModel sea Error con el mensaje correcto.
        val expectedState = CiudadesEstado.Error(errorMessage)
        assertEquals(expectedState, viewModel.uiState)
    }

    // Test para la intención Seleccionar.
    @Test
    fun `dado que se ejecuta la intencion Seleccionar, entonces debe emitir el efecto NavegarAClima`() = runTest {
        // 1. Given
        val ciudadSeleccionada = Ciudad("Paris", 48.8566f, 2.3522f, "FR")
        val expectedEffect = CiudadesEfecto.NavegarAClima(ciudadSeleccionada.lat, ciudadSeleccionada.lon, ciudadSeleccionada.name)

        // 2. When
        viewModel.ejecutar(CiudadesIntencion.Seleccionar(ciudadSeleccionada))

        // 3. Then
        // Verfica que el ViewModel haya emitido el efecto de navegación correcto.
        val actualEffect = viewModel.efecto.first()
        assertEquals(expectedEffect, actualEffect)
    }

    // Test para la intención UsarGeolocalizacion.
    @Test
    fun `dado que se ejecuta la intencion UsarGeolocalizacion, entonces el estado debe ser Cargando y emitir el efecto PedirPermisoUbicacion`() = runTest {
        // 1. Given
        val expectedEffect = CiudadesEfecto.PedirPermisoUbicacion

        // 2. When
        viewModel.ejecutar(CiudadesIntencion.UsarGeolocalizacion)

        // 3. Then
        // Verfica el efecto emitido.
        val actualEffect = viewModel.efecto.first()
        assertEquals(expectedEffect, actualEffect)

        // Verfica también que el estado de la UI se ponga en Cargando.
        assertEquals(CiudadesEstado.Cargando, viewModel.uiState)
    }
}