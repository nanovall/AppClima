package com.example.apppronsticoclima.Presentation.clima

import com.example.apppronsticoclima.Presentation.Clima.ClimaEfecto
import com.example.apppronsticoclima.Presentation.Clima.ClimaEstado
import com.example.apppronsticoclima.Presentation.Clima.ClimaIntencion
import com.example.apppronsticoclima.Presentation.Clima.ClimaViewModel
import com.example.apppronsticoclima.Presentation.ciudades.MainDispatcherRule
import com.example.apppronsticoclima.Repository.Repositorio
import com.example.apppronsticoclima.Repository.modelos.Clima
import com.example.apppronsticoclima.Repository.modelos.ListForecast
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ClimaViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var repositorio: Repositorio

    private lateinit var viewModel: ClimaViewModel

    // Datos de prueba que se usarán en varios tests.
    private val lat = 40.7128f
    private val lon = -74.0060f
    private val nombre = "New York"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = ClimaViewModel(repositorio, lat, lon, nombre)
    }

    // Prueba para verificar el estado inicial del ViewModel.
    @Test
    fun `dado que se ejecuta CargarClima, cuando el repositorio funciona, entonces el estado debe ser Exitoso`() = runTest {
        // 1. Given
        // Usamos un mock relajado para no tener que definir todos los campos. Antes me daba error porque había que definir todos los campos.
        val mockClima = mockk<Clima>(relaxed = true)
        val mockPronostico = emptyList<ListForecast>()

        // Definimos solo el comportamiento de los campos que nos importan para la aserción.
        every { mockClima.name } returns "New York"
        every { mockClima.sys.country } returns "US"
        every { mockClima.main.temp } returns 25.0f

        coEvery { repositorio.traerClima(lat, lon) } returns mockClima
        coEvery { repositorio.traerPronostico(nombre) } returns mockPronostico

        // 2. When
        viewModel.ejecutar(ClimaIntencion.CargarClima)

        // 3. Then
        // Verifica que el estado emitido es Exitoso con los datos correctos.
        assert(viewModel.uiState is ClimaEstado.Exitoso)
        val estado = viewModel.uiState as ClimaEstado.Exitoso
        assertEquals("New York, US", estado.ciudad)
        assertEquals("25°", estado.temperatura)
    }

    // Prueba para verificar el manejo de errores en el repositorio.
    @Test
    fun `dado que se ejecuta CargarClima, cuando el repositorio falla, entonces el estado debe ser Error`() = runTest {
        // 1. Given
        val errorMessage = "Fallo de red"
        coEvery { repositorio.traerClima(lat, lon) } throws Exception(errorMessage)

        // 2. When
        viewModel.ejecutar(ClimaIntencion.CargarClima)

        // 3. Then
        // Verifica que el estado emitido es Error con el mensaje de error correcto.
        val expectedState = ClimaEstado.Error(errorMessage)
        assertEquals(expectedState, viewModel.uiState)
    }

    // Prueba para verificar el efecto emitido al ejecutar VolverAtras.
    @Test
    fun `dado que se ejecuta VolverAtras, entonces debe emitir el efecto NavegarAtras`() = runTest {
        // 1. Given
        val expectedEffect = ClimaEfecto.NavegarAtras

        // 2. When
        viewModel.ejecutar(ClimaIntencion.VolverAtras)

        // 3. Then
        // Verifica que el efecto emitido es NavegarAtras.
        val actualEffect = viewModel.efecto.first()
        assertEquals(expectedEffect, actualEffect)
    }

    // Prueba para verificar el efecto emitido al ejecutar CompartirPronostico.
    @Test
    fun `dado que se ejecuta CompartirPronostico, cuando el estado es Exitoso, debe emitir el efecto Compartir`() = runTest {
        // 1. Given
        val mockClima = mockk<Clima>(relaxed = true)
        coEvery { repositorio.traerClima(lat, lon) } returns mockClima
        coEvery { repositorio.traerPronostico(nombre) } returns emptyList()
        viewModel.ejecutar(ClimaIntencion.CargarClima)

        // 2. When
        viewModel.ejecutar(ClimaIntencion.CompartirPronostico)

        // 3. Then
        // Verifica que el efecto emitido es Compartir.
        val effect = viewModel.efecto.first()
        assert(effect is ClimaEfecto.Compartir)
    }

    // Prueba para verificar el efecto emitido al ejecutar CompartirPronostico.
    @Test
    fun `dado que se ejecuta CargarClima con geolocalizacion, cuando el repositorio funciona, entonces el estado debe ser Exitoso`() = runTest {
        // 1. Given
        val latGeo = 34.0522f
        val lonGeo = -118.2437f
        val nombreGeo = "Mi Ubicación"
        val viewModelGeo = ClimaViewModel(repositorio, latGeo, lonGeo, nombreGeo)
        val mockClima = mockk<Clima>(relaxed = true)

        every { mockClima.name } returns "Los Angeles"
        every { mockClima.sys.country } returns "US"

        coEvery { repositorio.traerClima(latGeo, lonGeo) } returns mockClima
        coEvery { repositorio.traerPronostico(latGeo, lonGeo) } returns emptyList()

        // 2. When
        viewModelGeo.ejecutar(ClimaIntencion.CargarClima)

        // 3. Then
        // Verifica que el estado emitido es Exitoso con los datos correctos.
        assert(viewModelGeo.uiState is ClimaEstado.Exitoso)
        val estado = viewModelGeo.uiState as ClimaEstado.Exitoso
        assertEquals("Los Angeles, US", estado.ciudad)
    }
}