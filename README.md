## AppClima
Aplicación "AppClima" - Segundo Parcial de Aplicaciones Móviles 2025.

## Equipo 1 - Contribuidores:
Este proyecto es el resultado del trabajo en equipo:
- Nicolás Savoia - [@SavNico](https://github.com/SavNico)
- Lorenzo Segada López - [@lorenzosegada](https://github.com/lorenzosegada)
- Francisco Acuña - [@frantureco]
- Mariano Matias Valle - [@nanovall](https://github.com/nanovall)
- Ornella Soledad Leiva Fioravanti - [@ornellaleivaistea](https://github.com/ornellaleivaistea) / [@OrnellaLF](https://github.com/OrnellaLF)

## Descripción
AppClima es una aplicación móvil desarrollada en Kotlin para la plataforma Android que proporciona información meteorológica detallada, pronósticos a varios días y funcionalidades avanzadas de gestión de ciudades y geolocalización.

## Funcionalidades
* **Inicio Inteligente:** Al abrir la aplicación, si no hay una ciudad guardada, se navega a la pantalla de Ciudades. Si ya hay una ciudad guardada, se navega directamente a la pantalla de Clima.
* **Pantalla de Ciudades:**
    * Muestra una lista de ciudades.
    * Permite buscar ciudades por nombre.
    * Incluye un botón para buscar la ciudad actual vía geolocalización.
* **Pantalla de Clima:**
    * Muestra el pronóstico detallado** para el día actual (temperatura, humedad, estado del tiempo, etc.).
    * Muestra un gráfico o lista con el pronóstico extendido de 5 o más días (incluyendo máximas y mínimas).
    * Botón para cambiar la ciudad.
    * Botón para compartir el pronóstico actual.
* **Persistencia:** Guarda la última ciudad seleccionada por el usuario.

## Configuración Local
Para compilar y ejecutar el proyecto localmente, sigue estos pasos:
### 1. Clonar el Repositorio
```bash
git clone [https://github.com/nanovall/AppClima.git](https://github.com/nanovall/AppClima.git)
cd AppClima
```
### 2. Obtener Clave de API
Esta aplicación requiere una clave de API de OpenWeatherMap.
* Regístrate en OpenWeatherMap para obtener tu clave.
* Crea un archivo llamado local.properties en el directorio raíz de tu proyecto (junto a build.gradle.kts del proyecto).
* Añade tu clave en ese archivo con el siguiente formato exacto:
```bash
OPEN_WEATHER_MAP_API_KEY="TU_CLAVE_AQUI"
```
### 3. Ejecutar el Proyecto
* Abre el proyecto en Android Studio.
* Espera a que Gradle sincronice las dependencias.
* Ejecuta la aplicación en un emulador o dispositivo físico.

