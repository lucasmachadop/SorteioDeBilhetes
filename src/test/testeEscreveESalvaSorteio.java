package test;

import static org.junit.Assert.*;
import model.utils.EscreveESalvaSorteio;

import org.junit.Test;

public class testeEscreveESalvaSorteio {

	@Test
	public void testaLerArquivo() {		
		assertEquals(1134517053,EscreveESalvaSorteio.lerArquivo("testeHash.txt").hashCode());				
	}

}
