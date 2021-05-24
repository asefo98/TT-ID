package gei.id.tutelado.model;

import javax.persistence.*;


@Entity
@Table(name="persona")
public class Persona extends Cliente {

	@Column(unique = false, nullable = false)
    private String nombre;
	
	@Column(unique = false, nullable = false)
    private String apellidos;
	
	@Column(unique = true, nullable = false)
    private String dni;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}
	
}
