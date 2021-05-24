package gei.id.tutelado.dao;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.Cliente;

public interface ClienteDao {
    void setup (Configuracion config);

    //OPERACIONS CRUD BASICAS
    
    Cliente almacena (Cliente cliente);
    
    Cliente modifica (Cliente cliente);
    void elimina (Cliente cliente);
    //Cliente recuperarReservas (Cliente cliente);
    Cliente recuperaCodigoCli(String cod);

	Cliente restauraReservas(Cliente cliente);
}
