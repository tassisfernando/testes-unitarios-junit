import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {

	@Test
	public void test() {
		//verificando booleans 
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		 
		//verificando longs e doubles
		Assert.assertEquals("Erro de comparação", 1, 1);
		Assert.assertEquals(0.52, 0.65, 0.2); //terceiro parâmetro é o delta de comparação
		Assert.assertNotEquals("Erro de comparação", 1, 0);
		
		//verificando valores primitivos x classes
		int i = 5;
		Integer i2 = 5;
		Assert.assertEquals(Integer.valueOf(i), i2);
		Assert.assertEquals(i, i2.intValue());
		
		//verificando strings
		Assert.assertEquals("bola", "bola");
		Assert.assertTrue("bola".equalsIgnoreCase("Bola"));
		Assert.assertTrue("bola".startsWith("bo"));
		
		//verificando objetos
		Usuario u1 = new Usuario("Usuario 1");
		Usuario u2 = new Usuario("Usuario 1");
		Usuario u3 = null;
		Assert.assertEquals(u1, u2);
		Assert.assertSame(u2, u2);
		
		//verificando se é nulo
		Assert.assertTrue(u3 == null);
		Assert.assertNull(u3);
		
		//AssertThat
		
	}
}
