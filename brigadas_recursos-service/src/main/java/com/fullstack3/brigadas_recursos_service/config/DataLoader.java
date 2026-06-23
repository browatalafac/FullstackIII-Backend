package com.fullstack3.brigadas_recursos_service.config;

import com.fullstack3.brigadas_recursos_service.model.Brigada;
import com.fullstack3.brigadas_recursos_service.repository.BrigadaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner initDatabase(BrigadaRepository repository) {
        return args -> {
            if (repository.count() == 0) {

                // 1. Generar 4 Brigadas Forestales
                for (int i = 1; i <= 4; i++) {
                    Brigada b = new Brigada();
                    b.setNombre("Conaf - Brigada Forestal " + i);
                    b.setTipoEquipo("BOMBEROS_FORESTALES"); // OJO: Debe coincidir con tu Enum
                    b.setEstado("DISPONIBLE");
                    repository.save(b);
                }

                // 2. Generar 4 Brigadas Urbanas
                for (int i = 1; i <= 4; i++) {
                    Brigada b = new Brigada();
                    b.setNombre("Bomberos - Compañía Urbana " + i);
                    b.setTipoEquipo("BOMBEROS_URBANOS"); // OJO: Debe coincidir con tu Enum
                    b.setEstado("DISPONIBLE");
                    repository.save(b);
                }

                // 3. Generar 4 Brigadas Mixtas (Comodín)
                for (int i = 1; i <= 4; i++) {
                    Brigada b = new Brigada();
                    b.setNombre("Rescate Mixto - Unidad " + i);
                    b.setTipoEquipo("MIXTO"); // OJO: Debe coincidir con tu Enum
                    b.setEstado("DISPONIBLE");
                    repository.save(b);
                }

                System.out.println("Datos de Brigadas inicializados: 12 unidades operativas (4 de cada tipo).");
            }
        };
    }
}
