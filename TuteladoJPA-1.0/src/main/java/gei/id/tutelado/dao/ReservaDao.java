package gei.id.tutelado.dao;

import java.time.LocalDate;
import java.util.List;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Reserva;

public interface ReservaDao {
    void setup (Configuracion config);

    //OPERACIONS CRUD BASICAS
    
    Reserva almacena (Reserva reserva);
    Reserva modifica (Reserva reserva);
    void elimina (Reserva reserva);
    Reserva recuperaPorCodigo (String codReserva);
    List<Reserva> filtroporFecha(LocalDate fechaIni);
    List<Reserva> filtroporNomAlbergue(String nomAlbergue);
}
