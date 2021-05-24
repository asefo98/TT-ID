package gei.id.tutelado;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.model.*;

public class DataBDtutelado {
	private EntityManagerFactory emf=null;
	
	public Cliente c1, c2, c3, c4, c5;
	public List<Cliente> listaCliente;
	
	public Reserva r1, r2, r3;
	public List<Reserva> listaReserva;
	
	public void Setup (Configuracion config) {
		this.emf=(EntityManagerFactory) config.get("EMF");
	}
	
	public void crearClientesSueltos() {
		
		this.c1 = new Persona();
		this.c1.setCodCliente("codCli1");
		this.c1.setEmail("email@c1.es");
		this.c1.setNumTarjeta("123456789");
		this.c1.setTlf("632514263");
		((Persona)this.c1).setNombre("Juan");
		((Persona)this.c1).setApellidos("De Arco");
		((Persona)this.c1).setDni("62351524R");
	
		this.c2 = new Persona();
		this.c2.setCodCliente("codCli2");
		this.c2.setEmail("email@c2.es");
		this.c2.setNumTarjeta("123456000");
		this.c2.setTlf("632514374");
		((Persona)this.c2).setNombre("Andrea");
		((Persona)this.c2).setApellidos("Diz Fontecoba");
		((Persona)this.c2).setDni("62562389P");
		
		this.c3 = new Persona();
		this.c3.setCodCliente("codCli3");
		this.c3.setEmail("email@c3.es");
		this.c3.setNumTarjeta("123456001");
		this.c3.setTlf("632514355");
		((Persona)this.c3).setNombre("Ana");
		((Persona)this.c3).setApellidos("De Armas");
		((Persona)this.c3).setDni("62354545T");
		
		this.c4 = new Persona();
		this.c4.setCodCliente("codCli4");
		this.c4.setEmail("email@c4.es");
		this.c4.setNumTarjeta("123456011");
		this.c4.setTlf("632514377");
		((Persona)this.c4).setNombre("Roberto");
		((Persona)this.c4).setApellidos("Rey Castillo");
		((Persona)this.c4).setDni("62467812W");
		
		this.c5 = new Grupo();
		this.c5.setCodCliente("codCli5");
		this.c5.setEmail("email@c5.es");
		this.c5.setNumTarjeta("123456011");
		this.c5.setTlf("632514377");
		((Grupo)this.c5).setNum_integrantes(3);
		
		this.listaCliente = new ArrayList<Cliente> ();
		this.listaCliente.add(0, c1);
		this.listaCliente.add(1, c2);
		this.listaCliente.add(2, c3);
		this.listaCliente.add(3, c4);
		this.listaCliente.add(4, c5);
	}
	
	public void crearReservasSueltas () {
		this.r1 = new Reserva();
		this.r1.setCodReserva("codRes1");
		this.r1.setFechaIni(LocalDate.of(2021, 06, 20));
		this.r1.setFechaFin(LocalDate.of(2021, 06, 23));
		this.r1.setNomAlbergue("Albergue1");
		
		this.r2 = new Reserva();
		this.r2.setCodReserva("codRes2");
		this.r2.setFechaIni(LocalDate.of(2021, 07, 20));
		this.r2.setFechaFin(LocalDate.of(2021, 07, 23));
		this.r2.setNomAlbergue("Albergue2");
		
		this.r3 = new Reserva();
		this.r3.setCodReserva("codRes3");
		this.r3.setFechaIni(LocalDate.of(2021, 07, 20));
		this.r3.setFechaFin(LocalDate.of(2021, 07, 23));
		this.r3.setNomAlbergue("Albergue2");
		
		this.listaReserva = new ArrayList<Reserva> ();
		this.listaReserva.add(0, r1);
		this.listaReserva.add(1, r2);
	}
	
	public void crearGrupoConIntegrantes () {
		this.crearClientesSueltos();
		
		Set<Persona> integrantes = new HashSet<Persona>();
		integrantes.add((Persona)c2);
		integrantes.add((Persona)c3);
		integrantes.add((Persona)c4);
		
		((Grupo)this.c5).setIntegrantes(integrantes);
	}
	
	public void crearClientesConReservas () {
		this.crearClientesSueltos();
		this.crearReservasSueltas();
		
		this.c1.addReserva(this.r1);
		this.c1.addReserva(this.r2);
		this.c5.addReserva(this.r3);
		
	}
	
	public void gravaClientes() {
		EntityManager em=null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			Iterator<Cliente> itC = this.listaCliente.iterator();
			while (itC.hasNext()) {
				Cliente c = itC.next();
				em.persist(c);
				// DESCOMENTAR SE A PROPAGACION DO PERSIST NON ESTA ACTIVADA
				Iterator<Reserva> itR = c.getReservas().iterator();
				while (itR.hasNext()) {
					em.persist(itR.next());
				}
			}
			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw (e);
			}
		}	
	}
	
	public void limpaBD () {
		EntityManager em=null;
		try {
			em = emf.createEntityManager();
			em.getTransaction().begin();

			em.createQuery("DELETE FROM Reserva").executeUpdate();
			em.createQuery("DELETE FROM Persona").executeUpdate();
			em.createQuery("DELETE FROM Grupo").executeUpdate();
			em.createNativeQuery("UPDATE tabla_ids SET ultimo_valor_id=0 WHERE nombre_id='idReserva'" ).executeUpdate();
			em.createNativeQuery("UPDATE tabla_ids SET ultimo_valor_id=0 WHERE nombre_id='idCliente'" ).executeUpdate();

			em.getTransaction().commit();
			em.close();
		} catch (Exception e) {
			if (em!=null && em.isOpen()) {
				if (em.getTransaction().isActive()) em.getTransaction().rollback();
				em.close();
				throw (e);
			}
		}
	}
}
