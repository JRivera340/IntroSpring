package com.MiProyecto.proyecto.service;

import com.MiProyecto.proyecto.model.Vehiculo;
import com.MiProyecto.proyecto.model.Conductor;
import com.MiProyecto.proyecto.repository.ConductorRepository; // Mockeamos la interfaz
import com.MiProyecto.proyecto.repository.VehiculoRepository; // Mockeamos la interfaz
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita Mockito para JUnit 5
public class VehiculoServiceTest {

    // @Mock crea una instancia "falsa" del repositorio que podemos controlar
    @Mock
    private VehiculoRepository vehiculoRepository;

    @Mock
    private ConductorRepository conductorRepository;

    // Aunque VehiculoService dependa de ConductorService, para las pruebas unitarias
    // de VehiculoService, lo ideal es mockear ConductorService también si sus métodos
    // tienen lógica compleja que no queremos ejecutar.
    // Sin embargo, si ConductorService solo es un intermediario que delega al ConductorRepository,
    // y la lógica de asignación/desasignación reside puramente en VehiculoService,
    // entonces podemos mockear directamente ConductorRepository como ya lo hicimos.
    // Para simplificar y seguir el patrón, nos quedamos con el mock de ConductorRepository.
    // Si tus métodos como `obtenerTodosLosConductores()` en ConductorService tienen lógica adicional,
    // entonces deberías mockear `ConductorService` en `VehiculoServiceTest` y mockear sus métodos.
    // Por ahora, asumimos que `VehiculoService` usa directamente `ConductorRepository` para buscar conductores.
    // **NOTA**: Revisando tu último VehiculoService, veo que aún inyectas ConductorService.
    // Para ser estricto en el aislamiento, deberíamos mockearlo. Actualizaré el setUp para eso.
    @Mock
    private ConductorService conductorService;


    // @InjectMocks crea una instancia real de VehiculoService e inyecta los mocks anteriores en ella
    @InjectMocks
    private VehiculoService vehiculoService;

    // Objetos de prueba para usar en los tests
    private Vehiculo vehiculo1;
    private Vehiculo vehiculo2;
    private Conductor conductor1;
    private Conductor conductor2;

    @BeforeEach
    void setUp() {
        // Inicializa objetos de prueba antes de cada test
        vehiculo1 = new Vehiculo("v-001", "ABC123", 1600, "Gasolina", "MOTOR001", "Toyota", 2020);
        vehiculo2 = new Vehiculo("v-002", "DEF456", 1800, "Diesel", "MOTOR002", "Nissan", 2018);
        conductor1 = new Conductor("Juan Perez", "Gerente", "CC", "100000001");
        conductor2 = new Conductor("Maria Lopez", "Analista", "CC", "100000002");

        // IMPORTANTE: Los @Mocks se resetean automáticamente con @BeforeEach y @ExtendWith(MockitoExtension.class).
        // No necesitas 'MockitoAnnotations.initMocks(this);' ni resetear mocks manualmente a menos que tengas un caso muy particular.

        // En tu `inicializarDatos()` de `VehiculoService`, se llama a `conductorService.agregarConductor(c1)`.
        // Para que `inicializarDatos()` no falle en un test unitario si se ejecuta,
        // necesitamos stubbear ese método (aunque en un test unitario, PostConstruct
        // normalmente no se ejecuta a menos que lo llamemos explícitamente o usemos
        // @SpringBootTest con contextos).
        // Para pruebas unitarias puras del servicio, generalmente no inicializamos datos vía @PostConstruct.
        // Si lo haces, asegúrate de que los mocks manejen esas llamadas.
        // Por simplicidad, los tests individuales stubbearán solo lo que necesiten.
    }

    // --- Test: obtenerTodosLosVehiculos ---
    @Test
    @DisplayName("Debe retornar una lista vacía cuando no hay vehículos en el repositorio")
    void testObtenerTodosLosVehiculos_ListaVacia() {
        // Given (Dado): El repositorio simula que no tiene vehículos
        when(vehiculoRepository.findAll()).thenReturn(Collections.emptyList());

        // When (Cuando): Llamamos al método del servicio
        List<Vehiculo> result = vehiculoService.obtenerTodosLosVehiculos();

        // Then (Entonces): Verificamos el resultado
        assertNotNull(result);
        assertTrue(result.isEmpty());
        // Verificamos que el método findAll del repositorio fue llamado exactamente una vez
        verify(vehiculoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe retornar todos los vehículos del repositorio")
    void testObtenerTodosLosVehiculos_ConVehiculos() {
        // Given
        List<Vehiculo> vehiculosEsperados = Arrays.asList(vehiculo1, vehiculo2);
        when(vehiculoRepository.findAll()).thenReturn(vehiculosEsperados);

        // When
        List<Vehiculo> result = vehiculoService.obtenerTodosLosVehiculos();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(vehiculosEsperados, result);
        verify(vehiculoRepository, times(1)).findAll();
    }

    // --- Test: obtenerVehiculoPorPlaca ---
    @Test
    @DisplayName("Debe retornar un vehículo por placa si existe")
    void testObtenerVehiculoPorPlaca_Existente() {
        // Given
        when(vehiculoRepository.findByPlaca("ABC123")).thenReturn(Optional.of(vehiculo1));

        // When
        Optional<Vehiculo> result = vehiculoService.obtenerVehiculoPorPlaca("ABC123");

        // Then
        assertTrue(result.isPresent());
        assertEquals(vehiculo1, result.get());
        verify(vehiculoRepository, times(1)).findByPlaca("ABC123");
    }

    @Test
    @DisplayName("Debe retornar Optional.empty si el vehículo no existe")
    void testObtenerVehiculoPorPlaca_NoExistente() {
        // Given
        when(vehiculoRepository.findByPlaca("NONEXIST")).thenReturn(Optional.empty());

        // When
        Optional<Vehiculo> result = vehiculoService.obtenerVehiculoPorPlaca("NONEXIST");

        // Then
        assertFalse(result.isPresent());
        verify(vehiculoRepository, times(1)).findByPlaca("NONEXIST");
    }

    @Test
    @DisplayName("Debe retornar Optional.empty si la placa es nula o vacía")
    void testObtenerVehiculoPorPlaca_NulaOVacia() {
        // When & Then
        assertTrue(vehiculoService.obtenerVehiculoPorPlaca(null).isEmpty());
        assertTrue(vehiculoService.obtenerVehiculoPorPlaca("").isEmpty());
        assertTrue(vehiculoService.obtenerVehiculoPorPlaca("   ").isEmpty());
        // Verificamos que el repositorio no fue llamado en estos casos
        verify(vehiculoRepository, never()).findByPlaca(anyString());
    }

    // --- Test: registrarVehiculo ---
    @Test
    @DisplayName("Debe registrar un vehículo nuevo con éxito")
    void testRegistrarVehiculo_Exito() {
        // Given
        Vehiculo nuevoVehiculo = new Vehiculo("v-010", "NEW123", 1500, "Gasolina", "MOTOR010", "Tesla", 2024);
        // Configuramos el mock para que `existsByPlaca` devuelva `false` (no existe)
        when(vehiculoRepository.existsByPlaca("NEW123")).thenReturn(false);
        // Para métodos void (como save), usamos doNothing().when()
        doNothing().when(vehiculoRepository).save(nuevoVehiculo);

        // When
        boolean result = vehiculoService.registrarVehiculo(nuevoVehiculo);

        // Then
        assertTrue(result);
        // Verificamos que `existsByPlaca` fue llamado una vez
        verify(vehiculoRepository, times(1)).existsByPlaca("NEW123");
        // Verificamos que `save` fue llamado una vez con el nuevo vehículo
        verify(vehiculoRepository, times(1)).save(nuevoVehiculo);
    }

    @Test
    @DisplayName("No debe registrar un vehículo si la placa ya existe")
    void testRegistrarVehiculo_PlacaYaExiste() {
        // Given
        Vehiculo vehiculoExistente = new Vehiculo("v-001", "ABC123", 1600, "Gasolina", "MOTOR001", "Toyota", 2020);
        // Configuramos el mock para que `existsByPlaca` devuelva `true` (ya existe)
        when(vehiculoRepository.existsByPlaca("ABC123")).thenReturn(true);

        // When
        boolean result = vehiculoService.registrarVehiculo(vehiculoExistente);

        // Then
        assertFalse(result);
        verify(vehiculoRepository, times(1)).existsByPlaca("ABC123");
        // Verificamos que `save` NUNCA fue llamado
        verify(vehiculoRepository, never()).save(any(Vehiculo.class));
    }

    // --- Test: asignarVehiculoAConductor ---
    @Test
    @DisplayName("Debe asignar un vehículo a un conductor si existen y el vehículo no está asignado")
    void testAsignarVehiculoAConductor_Exito() {
        // Given
        // Configuramos los mocks para que devuelvan el vehículo y el conductor
        when(vehiculoRepository.findByPlaca(vehiculo1.getPlaca())).thenReturn(Optional.of(vehiculo1));
        when(conductorRepository.findByIdentificacion(conductor1.getNumeroIdentificacion())).thenReturn(Optional.of(conductor1));
        // Simular que NO hay otros conductores que tengan este vehículo ya asignado
        when(conductorRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(conductor1))); // conductor1 aún no tiene v1

        // When
        boolean result = vehiculoService.asignarVehiculoAConductor(vehiculo1.getPlaca(), conductor1.getNumeroIdentificacion());

        // Then
        assertTrue(result);
        // Verificamos que el vehículo se añadió al conductor (esto es una interacción con el objeto real conductor1)
        assertTrue(conductor1.getVehiculos().contains(vehiculo1));
        // Verificamos las llamadas a los repositorios
        verify(vehiculoRepository, times(1)).findByPlaca(vehiculo1.getPlaca());
        verify(conductorRepository, times(1)).findByIdentificacion(conductor1.getNumeroIdentificacion());
        verify(conductorRepository, times(1)).findAll(); // Se llama para la lógica de desasignación
        verify(conductorRepository, times(1)).save(conductor1); // Se guarda el conductor actualizado
    }

    @Test
    @DisplayName("Debe reasignar un vehículo a un nuevo conductor desvinculándolo del anterior")
    void testAsignarVehiculoAConductor_Reasignar() {
        // Given
        Conductor conductorAntiguo = new Conductor("Pedro", "Antiguo", "CC", "101");
        conductorAntiguo.agregarVehiculo(vehiculo1); // V1 ya está asignado a Pedro

        // Configuramos los mocks para que devuelvan el vehículo y los conductores
        when(vehiculoRepository.findByPlaca(vehiculo1.getPlaca())).thenReturn(Optional.of(vehiculo1));
        when(conductorRepository.findByIdentificacion(conductor2.getNumeroIdentificacion())).thenReturn(Optional.of(conductor2));
        // Simular que ambos conductores existen y el antiguo tiene el vehículo
        when(conductorRepository.findAll()).thenReturn(Arrays.asList(conductorAntiguo, conductor2));

        // When
        boolean result = vehiculoService.asignarVehiculoAConductor(vehiculo1.getPlaca(), conductor2.getNumeroIdentificacion());

        // Then
        assertTrue(result);
        assertFalse(conductorAntiguo.getVehiculos().contains(vehiculo1), "El vehículo debe ser desasignado del conductor antiguo");
        assertTrue(conductor2.getVehiculos().contains(vehiculo1), "El vehículo debe ser asignado al nuevo conductor");

        verify(vehiculoRepository, times(1)).findByPlaca(vehiculo1.getPlaca());
        verify(conductorRepository, times(1)).findByIdentificacion(conductor2.getNumeroIdentificacion());
        verify(conductorRepository, times(1)).findAll();
        verify(conductorRepository, times(1)).save(conductorAntiguo); // Se guarda el antiguo actualizado
        verify(conductorRepository, times(1)).save(conductor2); // Se guarda el nuevo actualizado
    }

    @Test
    @DisplayName("No debe asignar vehículo si no existe el vehículo")
    void testAsignarVehiculoAConductor_VehiculoNoExiste() {
        // Given
        when(vehiculoRepository.findByPlaca("NONEXIST")).thenReturn(Optional.empty());
        when(conductorRepository.findByIdentificacion(conductor1.getNumeroIdentificacion())).thenReturn(Optional.of(conductor1));

        // When
        boolean result = vehiculoService.asignarVehiculoAConductor("NONEXIST", conductor1.getNumeroIdentificacion());

        // Then
        assertFalse(result);
        verify(vehiculoRepository, times(1)).findByPlaca("NONEXIST");
        verify(conductorRepository, times(1)).findByIdentificacion(conductor1.getNumeroIdentificacion());
        verify(conductorRepository, never()).findAll(); // No debería buscar conductores si el vehículo no existe
        verify(conductorRepository, never()).save(any(Conductor.class));
    }

    @Test
    @DisplayName("No debe asignar vehículo si no existe el conductor")
    void testAsignarVehiculoAConductor_ConductorNoExiste() {
        // Given
        when(vehiculoRepository.findByPlaca(vehiculo1.getPlaca())).thenReturn(Optional.of(vehiculo1));
        when(conductorRepository.findByIdentificacion("NONEXIST_ID")).thenReturn(Optional.empty());

        // When
        boolean result = vehiculoService.asignarVehiculoAConductor(vehiculo1.getPlaca(), "NONEXIST_ID");

        // Then
        assertFalse(result);
        verify(vehiculoRepository, times(1)).findByPlaca(vehiculo1.getPlaca());
        verify(conductorRepository, times(1)).findByIdentificacion("NONEXIST_ID");
        verify(conductorRepository, never()).findAll();
        verify(conductorRepository, never()).save(any(Conductor.class));
    }

    @Test
    @DisplayName("No debe asignar vehículo si la placa o identificación es nula/vacía")
    void testAsignarVehiculoAConductor_DatosInvalidos() {
        // When & Then
        assertFalse(vehiculoService.asignarVehiculoAConductor(null, "123"));
        assertFalse(vehiculoService.asignarVehiculoAConductor("ABC", null));
        assertFalse(vehiculoService.asignarVehiculoAConductor("", "123"));
        assertFalse(vehiculoService.asignarVehiculoAConductor("ABC", ""));

        verify(vehiculoRepository, never()).findByPlaca(anyString());
        verify(conductorRepository, never()).findByIdentificacion(anyString());
        verify(conductorRepository, never()).findAll();
        verify(conductorRepository, never()).save(any(Conductor.class));
    }

    // --- Test: desasignarVehiculoDeConductor ---
    @Test
    @DisplayName("Debe desasignar un vehículo de un conductor si está asignado")
    void testDesasignarVehiculoDeConductor_Exito() {
        // Given
        conductor1.agregarVehiculo(vehiculo1); // Asignamos el vehículo al conductor para la prueba
        List<Conductor> conductores = new ArrayList<>(Arrays.asList(conductor1));
        when(conductorRepository.findAll()).thenReturn(conductores);
        doNothing().when(conductorRepository).save(any(Conductor.class)); // Stubbing para el save

        // When
        boolean result = vehiculoService.desasignarVehiculoDeConductor(vehiculo1.getPlaca());

        // Then
        assertTrue(result);
        assertFalse(conductor1.getVehiculos().contains(vehiculo1), "El vehículo debe ser removido del conductor");
        verify(conductorRepository, times(1)).findAll();
        verify(conductorRepository, times(1)).save(conductor1); // Se verifica que el conductor se guardó después de la modificación
    }

    @Test
    @DisplayName("No debe desasignar un vehículo si no está asignado a ningún conductor")
    void testDesasignarVehiculoDeConductor_NoAsignado() {
        // Given
        when(conductorRepository.findAll()).thenReturn(Collections.emptyList()); // Ningún conductor tiene vehículos

        // When
        boolean result = vehiculoService.desasignarVehiculoDeConductor("NONEXISTENT");

        // Then
        assertFalse(result);
        verify(conductorRepository, times(1)).findAll();
        verify(conductorRepository, never()).save(any(Conductor.class));
    }

    @Test
    @DisplayName("No debe desasignar un vehículo si la placa es nula o vacía")
    void testDesasignarVehiculoDeConductor_DatosInvalidos() {
        // When & Then
        assertFalse(vehiculoService.desasignarVehiculoDeConductor(null));
        assertFalse(vehiculoService.desasignarVehiculoDeConductor(""));
        assertFalse(vehiculoService.desasignarVehiculoDeConductor("   "));

        verify(conductorRepository, never()).findAll();
        verify(conductorRepository, never()).save(any(Conductor.class));
    }

    // --- Test: eliminarVehiculo ---
    @Test
    @DisplayName("Debe eliminar un vehículo del repositorio global y desvincularlo de cualquier conductor")
    void testEliminarVehiculo_Exito() {
        // Given
        conductor1.agregarVehiculo(vehiculo1); // Asignamos el vehículo para simular la desvinculación
        List<Conductor> conductores = new ArrayList<>(Arrays.asList(conductor1));

        // Simular que el vehículo existe en el repositorio y se puede eliminar
        when(vehiculoRepository.deleteByPlaca(vehiculo1.getPlaca())).thenReturn(true);
        // Simular que el conductor existe en el repositorio de conductores
        when(conductorRepository.findAll()).thenReturn(conductores);
        doNothing().when(conductorRepository).save(any(Conductor.class)); // Stubbing para el save del conductor

        // When
        boolean result = vehiculoService.eliminarVehiculo(vehiculo1.getPlaca());

        // Then
        assertTrue(result);
        assertFalse(conductor1.getVehiculos().contains(vehiculo1), "El vehículo debe ser desvinculado del conductor");
        verify(vehiculoRepository, times(1)).deleteByPlaca(vehiculo1.getPlaca());
        verify(conductorRepository, times(1)).findAll();
        verify(conductorRepository, times(1)).save(conductor1); // Verifica que el conductor fue guardado después de la modificación
    }

    @Test
    @DisplayName("No debe eliminar un vehículo si no existe en el repositorio global")
    void testEliminarVehiculo_NoExiste() {
        // Given
        when(vehiculoRepository.deleteByPlaca("NONEXIST")).thenReturn(false);

        // When
        boolean result = vehiculoService.eliminarVehiculo("NONEXIST");

        // Then
        assertFalse(result);
        verify(vehiculoRepository, times(1)).deleteByPlaca("NONEXIST");
        verify(conductorRepository, never()).findAll(); // No debería intentar buscar conductores si no eliminó el vehículo
    }

    @Test
    @DisplayName("No debe eliminar un vehículo si la placa es nula o vacía")
    void testEliminarVehiculo_DatosInvalidos() {
        // When & Then
        assertFalse(vehiculoService.eliminarVehiculo(null));
        assertFalse(vehiculoService.eliminarVehiculo(""));
        assertFalse(vehiculoService.eliminarVehiculo("   "));

        verify(vehiculoRepository, never()).deleteByPlaca(anyString());
        verify(conductorRepository, never()).findAll();
    }
}