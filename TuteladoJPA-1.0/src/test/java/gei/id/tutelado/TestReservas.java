package gei.id.tutelado;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.MethodSorters;

import gei.id.tutelado.configuracion.Configuracion;
import gei.id.tutelado.configuracion.ConfiguracionJPA;
import gei.id.tutelado.dao.ClienteDao;
import gei.id.tutelado.dao.ClienteDaoJPA;
import gei.id.tutelado.dao.ReservaDao;
import gei.id.tutelado.dao.ReservaDaoJPA;
import gei.id.tutelado.model.Cliente;
import gei.id.tutelado.model.EntradaLog;
import gei.id.tutelado.model.Reserva;
import gei.id.tutelado.model.Usuario;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestReservas {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static DataBDtutelado data = new DataBDtutelado();
    
    private static Configuracion cfg;
    private static ClienteDao cliDao;
    private static ReservaDao resDao;
    
    @Rule
    public TestRule watcher = new TestWatcher() {
       protected void starting(Description description) {
    	   log.info("");
    	   log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    	   log.info("Iniciando test: " + description.getMethodName());
    	   log.info("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
       }
       protected void finished(Description description) {
    	   log.info("");
    	   log.info("-----------------------------------------------------------------------------------------------------------------------------------------");
    	   log.info("Finalizado test: " + description.getMethodName());
    	   log.info("-----------------------------------------------------------------------------------------------------------------------------------------");
       }
    };
    
    
    @BeforeClass
    public static void init() throws Exception {
    	cfg = new ConfiguracionJPA();
    	cfg.start();

    	cliDao = new ClienteDaoJPA();
    	resDao = new ReservaDaoJPA();
    	cliDao.setup(cfg);
    	resDao.setup(cfg);
    	
    	data = new DataBDtutelado();
    	data.Setup(cfg);
    }
    
    @AfterClass
    public static void endclose() throws Exception {
    	cfg.endUp();    	
    }
    
    
    
	@Before
	public void setUp() throws Exception {		
		log.info("");	
		log.info("Limpando BD -----------------------------------------------------------------------------------------------------");
		data.limpaBD();
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test 
    public void t1a_CRUD_TestAlmacena() {


    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

    	data.crearClientesSueltos();
    	data.gravaClientes();
    	data.crearReservasSueltas();
    	
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da gravación de reservas soltas\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Primeira reserva vinculada a un cliente\n"
    			+ "\t\t\t\t b) Nova reserva para un cliente con reservas previas\n");     	

    	// Situación de partida:
    	// u1 desligado    	
    	// e1A, e1B transitorios

    	data.c1.addReserva(data.r1);
		
    	log.info("");	
		log.info("Gravando primeira reserva dun cliente --------------------------------------------------------------------");
    	Assert.assertNull(data.r1.getId());
    	resDao.almacena(data.r1);
    	Assert.assertNotNull(data.r1.getId());

    	data.c1.addReserva(data.r2);

    	log.info("");	
		log.info("Gravando segunda reserva dun cliente ---------------------------------------------------------------------");
		Assert.assertNull(data.r2.getId());
    	resDao.almacena(data.r2);
    	Assert.assertNotNull(data.r2.getId());

    }
	
    @Test 
    public void t1b_CRUD_TestAlmacena() {

   	    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

    	data.crearClientesSueltos();
    	data.crearReservasSueltas();
    	data.c1.addReserva(data.r1);
    	data.c1.addReserva(data.r2);

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da gravación de novo cliente con reservas (novas) asociadas\n");   

    	// Situación de partida:
    	// u1, e1A, e1B transitorios

    	Assert.assertNull(data.c1.getId());
    	Assert.assertNull(data.r1.getId());
    	Assert.assertNull(data.r2.getId());
    	
		log.info("Gravando na BD cliente con reservas ----------------------------------------------------------------------");

    	// Aqui o persist sobre u1 debe propagarse a e1A e e1B
		cliDao.almacena(data.c1);
		resDao.almacena(data.r1);
		resDao.almacena(data.r2);

		Assert.assertNotNull(data.c1.getId());
    	Assert.assertNotNull(data.r1.getId());
    	Assert.assertNotNull(data.r2.getId());    	
    }
    
    @Test 
    public void t2a_CRUD_TestRecupera() {
   	
    	Reserva r;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");
		
		data.crearClientesSueltos();
    	data.crearReservasSueltas();
    	data.c1.addReserva(data.r1);
    	data.c1.addReserva(data.r2);
    	data.c5.addReserva(data.r3);

		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da recuperación (por codigo) de reservas soltas\n"   
		+ "\t\t\t\t Casos contemplados:\n"
		+ "\t\t\t\t a) Recuperación por codigo existente\n"
		+ "\t\t\t\t b) Recuperacion por codigo inexistente\n");     	

    	// Situación de partida:
    	// u1, e1A, e1B desligados
    	
		log.info("Probando recuperacion por codigo EXISTENTE --------------------------------------------------");

		cliDao.almacena(data.c1);
		cliDao.almacena(data.c2);
		cliDao.almacena(data.c3);
		cliDao.almacena(data.c4);
		cliDao.almacena(data.c5);
		resDao.almacena(data.r1);
		resDao.almacena(data.r2);
		resDao.almacena(data.r3);
		
		
    	r = resDao.recuperaPorCodigo(data.r1.getCodReserva());
    	
    	Assert.assertEquals (data.r1.getCodReserva(),     r.getCodReserva());
    	Assert.assertEquals (data.r1.getFechaIni(), r.getFechaIni());
    	Assert.assertEquals (data.r1.getFechaFin(), r.getFechaFin());
    	Assert.assertEquals (data.r1.getNomAlbergue(),   r.getNomAlbergue());

    	log.info("");	
		log.info("Probando recuperacion por codigo INEXISTENTE --------------------------------------------------");
    	
    	r = resDao.recuperaPorCodigo("iwbvyhuebvuwebvi");
    	Assert.assertNull (r);
    }

    @Test 
    public void t2b_CRUD_TestRecupera() {
    	
    	Cliente cli1 ;
    	Reserva res1;
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		data.crearClientesSueltos();
		data.gravaClientes();
    	data.crearReservasSueltas();
    	data.c1.addReserva(data.r1);
    	data.c1.addReserva(data.r2);
    	data.c5.addReserva(data.r3);

		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba da recuperación de propiedades LAZY\n"   
		+ "\t\t\t\t Casos contemplados:\n"
		+ "\t\t\t\t a) Recuperación de cliente con colección (LAZY) reservas \n"
		+ "\t\t\t\t b) Carga forzada de colección LAZY da dita coleccion\n"     	
		+ "\t\t\t\t c) Recuperacion de reserva solta con referencia (EAGER) a cliente\n");     	

    	// Situación de partida:
    	// u1, e1A, e1B desligados
    	
		resDao.almacena(data.r1);
		resDao.almacena(data.r2);
		resDao.almacena(data.r3);
    	
		log.info("Probando (excepcion tras) recuperacion LAZY ---------------------------------------------------------------------");
    	
    	cli1 = cliDao.recuperaCodigoCli(data.c1.getCodCliente());
		log.info("Acceso a entradas de log de usuario");
    	try	{
        	Assert.assertEquals(2, cli1.getReservas().size());
        	Assert.assertEquals(true, cli1.getReservas().contains(data.r1));
        	Assert.assertEquals(true, cli1.getReservas().contains(data.r2));
        	excepcion=false;
    	} catch (LazyInitializationException ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	};    	
    	Assert.assertTrue(excepcion);
    
    	log.info("");
    	log.info("Probando carga forzada de coleccion LAZY ------------------------------------------------------------------------");
    	
    	   // Usuario u con proxy sen inicializar
					// Usuario u con proxy xa inicializado
    	
    	cli1 = cliDao.recuperaCodigoCli(data.c1.getCodCliente());
    	cli1 = cliDao.restauraReservas(cli1);
    	System.out.println(cli1.getReservas().size());
    	Assert.assertEquals(2, cli1.getReservas().size());
    	Assert.assertEquals(true, cli1.getReservas().contains(data.r1));
    	Assert.assertEquals(true, cli1.getReservas().contains(data.r2));

    	log.info("");
    	log.info("Probando acceso a referencia EAGER ------------------------------------------------------------------------------");
    
    	res1 = resDao.recuperaPorCodigo(data.r1.getCodReserva());
    	Assert.assertEquals(data.c1, res1.getCliente());
    } 	
    
    @Test 
    public void t3a_CRUD_TestElimina() {
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");
    	
    	data.crearClientesSueltos();
    	data.gravaClientes();

    	data.crearReservasSueltas();
    	data.c1.addReserva(data.r1);
    	data.c1.addReserva(data.r2);
    	data.c5.addReserva(data.r3);
 
    	resDao.almacena(data.r1);
		resDao.almacena(data.r2);
		resDao.almacena(data.r3);
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de eliminación de reserva solta (asignada a cliente)\n");
    	
    	// Situación de partida:
    	// e1A desligado

		Assert.assertNotNull(resDao.recuperaPorCodigo(data.r1.getCodReserva()));
    	resDao.elimina(data.r1);    	
		Assert.assertNull(resDao.recuperaPorCodigo(data.r1.getCodReserva()));

    }
    
    @Test 
    public void t3b_CRUD_TestElimina() {
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");
   	
		data.crearClientesSueltos();
    	data.gravaClientes();
		
		data.crearReservasSueltas();
    	data.c1.addReserva(data.r1);
    	data.c1.addReserva(data.r2);
    	data.c5.addReserva(data.r3);
 
    	resDao.almacena(data.r1);
		resDao.almacena(data.r2);
		resDao.almacena(data.r3);

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de eliminación de de cliente con reservas asociadas\n");

    	// Situación de partida:
    	// u1, e1A, e1B desligados
		
		Assert.assertNotNull(cliDao.recuperaCodigoCli(data.c1.getCodCliente()));
		Assert.assertNotNull(resDao.recuperaPorCodigo(data.r1.getCodReserva()));
		Assert.assertNotNull(resDao.recuperaPorCodigo(data.r2.getCodReserva()));
		// Aqui o remove sobre u1 debe propagarse a e1A e e1B
		  	
		cliDao.elimina(data.c1);
		
		Assert.assertNull(cliDao.recuperaCodigoCli(data.c1.getCodCliente()));
		Assert.assertNull(resDao.recuperaPorCodigo(data.r1.getCodReserva()));
		Assert.assertNull(resDao.recuperaPorCodigo(data.r2.getCodReserva()));

    } 
    
    @Test 
    public void t4_CRUD_TestModifica() {

    	Reserva res1, res2;
    	String nameAlb;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");
  
		data.crearClientesSueltos();
    	data.gravaClientes();
		
		data.crearReservasSueltas();
    	data.c1.addReserva(data.r1);
    	data.c1.addReserva(data.r2);
    	data.c5.addReserva(data.r3);
 
    	resDao.almacena(data.r1);
		resDao.almacena(data.r2);
		resDao.almacena(data.r3);

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de modificación da información dunha reserva solta\n");
 
    	
    	// Situación de partida:
    	// e1A desligado
    	
		nameAlb = new String ("Albergue20");

		res1 = resDao.recuperaPorCodigo(data.r1.getCodReserva());

		Assert.assertNotEquals(nameAlb, res1.getNomAlbergue());
    	res1.setNomAlbergue(nameAlb);

    	resDao.modifica(res1);
    	
		res2 = resDao.recuperaPorCodigo(data.r1.getCodReserva());
		Assert.assertEquals (nameAlb, res2.getNomAlbergue());

    	// NOTA: Non probamos modificación de usuario da entrada porque non ten sentido no dominio considerado

    }
    
    @Test
    public void t5_CRUD_TestExcepcions() {
    	
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		data.crearClientesSueltos();
    	data.gravaClientes();
		
		data.crearReservasSueltas();
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de violacion de restricions not null e unique\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Gravación de entrada con cliente nulo\n"
    			+ "\t\t\t\t b) Gravación de entrada con codigo nulo\n"
    			+ "\t\t\t\t c) Gravación de entrada con codigo duplicado\n");

    	// Situación de partida:
    	// u0, u1 desligados
    	// e1A desligado, e1B transitorio (e sen usuario asociado)
    	
		log.info("Probando gravacion de entrada con cliente nulo ------------------------------------------------------------------");
    	try {
    		resDao.almacena(data.r1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);

    	// Ligar entrada a usuario para poder probar outros erros
		data.c1.addReserva(data.r1);
    	    	
    	log.info("");	
		log.info("Probando gravacion de entrada con codigo nulo -------------------------------------------------------------------");
		data.r2.setCodReserva(null);
    	try {
        	resDao.almacena(data.r2);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);

    	log.info("");	
		log.info("Probando gravacion de entrada con codigo duplicado --------------------------------------------------------------");
		
		data.r2.setCodReserva(data.r1.getCodReserva());
    	try {
        	resDao.almacena(data.r2);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);

    } 	
    
}
