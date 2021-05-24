package gei.id.tutelado;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import gei.id.tutelado.model.Cliente;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestClientes {

    private Logger log = LogManager.getLogger("gei.id.tutelado");

    private static DataBDtutelado data = new DataBDtutelado();
    
    private static Configuracion cfg;
    private static ClienteDao cliDao;
    
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
    	cliDao.setup(cfg);
    	
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
		log.info("Limpando BD --------------------------------------------------------------------------------------------");
		data.limpaBD();
	}

	@After
	public void tearDown() throws Exception {
	}
	
    @Test 
    public void t1_CRUD_TestAlmacena() {

    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");
  
		data.crearClientesSueltos();
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de gravación na BD de novo cliente \n");
    	
    	// Situación de partida:
    	// u0 transitorio    	
    	
    	Assert.assertNull(data.c1.getId());
    	cliDao.almacena(data.c1);    	
    	Assert.assertNotNull(data.c1.getId());
    }
    
    @Test 
    public void t2_CRUD_TestRecupera() {
    	
    	Cliente c;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		data.crearClientesSueltos();
    	data.gravaClientes();
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de recuperación desde a BD de usuario (sen reservas asociadas) por codCliente\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Recuperación por codCliente existente\n"
    			+ "\t\t\t\t b) Recuperacion por codCliente inexistente\n");

    	// Situación de partida:
    	// u0 desligado    	

    	log.info("Probando recuperacion por codCliente EXISTENTE --------------------------------------------------");

    	c = cliDao.recuperaCodigoCli(data.c1.getCodCliente());

    	Assert.assertEquals(data.c1.getCodCliente(), c.getCodCliente());
    	Assert.assertEquals(data.c1.getEmail(), c.getEmail());
    	Assert.assertEquals(data.c1.getTlf(), c.getTlf());
    	Assert.assertEquals(data.c1.getNumTarjeta(), c.getNumTarjeta());
    	
    	
    	log.info("");	
		log.info("Probando recuperacion por codCliente INEXISTENTE -----------------------------------------------");
    	
    	c = cliDao.recuperaCodigoCli("iwbvyhuebvuwebvi");
    	Assert.assertNull (c);
    } 
    
    @Test 
    public void t3_CRUD_TestElimina() {
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		data.crearClientesSueltos();
    	data.gravaClientes();

    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de eliminación da BD de Cliente sen reservas\n");   
 
    	// Situación de partida:
    	// u0 desligado  

    	Assert.assertNotNull(cliDao.recuperaCodigoCli(data.c1.getCodCliente()));
    	cliDao.elimina(data.c1);    	
    	Assert.assertNull(cliDao.recuperaCodigoCli(data.c1.getCodCliente()));
    } 
    
    @Test 
    public void t4_CRUD_TestModifica() {
    	
    	Cliente cli1, cli2;
    	String newMail;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		data.crearClientesSueltos();
    	data.gravaClientes();

    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de modificación da información básica dun cliente sen reservas\n");

    	// Situación de partida:
    	// u0 desligado  

    	newMail = new String ("mail@new.es");

		cli1 = cliDao.recuperaCodigoCli(data.c1.getCodCliente());
		Assert.assertNotEquals(newMail, cli1.getEmail());
    	cli1.setEmail(newMail);

    	cliDao.modifica(cli1);    	
    	
		cli2 = cliDao.recuperaCodigoCli(data.c1.getCodCliente());
		Assert.assertEquals (newMail, cli2.getEmail());

    } 
    
    @Test
    public void t5_CRUD_TestExcepcions() {
    	
    	Boolean excepcion;
    	
    	log.info("");	
		log.info("Configurando situación de partida do test -----------------------------------------------------------------------");

		data.crearClientesSueltos();
    	cliDao.almacena(data.c1);
    	
    	log.info("");	
		log.info("Inicio do test --------------------------------------------------------------------------------------------------");
    	log.info("Obxectivo: Proba de violación de restricións not null e unique\n"   
    			+ "\t\t\t\t Casos contemplados:\n"
    			+ "\t\t\t\t a) Gravación de cliente con codCliente duplicado\n"
    			+ "\t\t\t\t b) Gravación de cliente con codCliente nulo\n");

    	// Situación de partida:
    	// u0 desligado, u1 transitorio
    	
		log.info("Probando gravacion de cliente con cliente duplicado -----------------------------------------------");
    	data.c1.setCodCliente(data.c1.getCodCliente());
    	try {
        	cliDao.almacena(data.c1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    	
    	// Nif nulo
    	log.info("");	
		log.info("Probando gravacion de cliente con cliente nulo ----------------------------------------------------");
    	data.c1.setCodCliente(null);
    	try {
        	cliDao.almacena(data.c1);
        	excepcion=false;
    	} catch (Exception ex) {
    		excepcion=true;
    		log.info(ex.getClass().getName());
    	}
    	Assert.assertTrue(excepcion);
    } 	
}
