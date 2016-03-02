package test;

import static org.junit.Assert.*;

import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import model.EfetuarSorteio;
import model.utils.FormatacaoDocumento;

import org.junit.Test;

public class testesEfetuarSorteio {
	EfetuarSorteio tmp = new EfetuarSorteio();
	@Test
	public void testaUsarMetodoHashVerdadeiro() {

		
		
		int tmpInt = 3000000;
		
		assertEquals(true, tmp.usarMetodoHash( tmpInt ));
		
		
		
	}
	
	
	@Test
	public void testaUsarMetodoHashFalso() {

		
		
		int tmpInt =1000000;
		
		assertEquals(false, tmp.usarMetodoHash( tmpInt ));
		
		
		
	}
	
	

	@Test
	public void testaCalculaQuantidadeDeFaixas() {

	
		
		int numBilhetes =10;
		int numFaixas = 10;
		double res = 1.0;
	
		assertEquals(res, tmp.calculaQuantidadeFaixas(numBilhetes, numFaixas),0);
		
		
		
	}
	
	@Test
	public void testaDeterminaRNGDoSorteio(){
		String semente = "bozo";
		long sd = semente.hashCode();
		Random rg = new Random();
		rg.setSeed(sd);
		assertEquals(rg.nextInt(), tmp.determinaRNGdoSorteio("bozo").nextInt());
	}
	
	
	@Test
	public void testaGeraSemente(){
		String str = "bozoopalha�odosatan�s,voc�vemsempreaqui?";
		
		assertEquals(str,tmp.geraSemente("bozo", "o", "palha�o", "do", "satan�s", ",voc�", "vem", "sempre", "aqui?"));
	
	}
	
	@Test
	public void testaMontaResultado(){
		String resultado = "";
		resultado = String.format("%10s  %10s %11s  %s\r\n",
				FormatacaoDocumento.mf.format(1),
				FormatacaoDocumento.mf.format(2),
				FormatacaoDocumento.mf.format(3), "");
		assertEquals(resultado,tmp.montaResultado(1, 2, 3));
		
	}
	
	@Test
	public void testaCalculaFaixaDevalores(){
	tmp.sorteioParado = false;
	
	int []valores={0,0,2,1,0,0,0,1,0,0,1 };
	
	int[]faixas ={2,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
			,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
			,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
			
	
	
	
		assertArrayEquals(faixas, tmp.calculaFaixasDeValores(5, 10, valores));
		
	}
	@Test
	public void testaCalculaFaixaDevaloresQueUsouHash(){
	tmp.sorteioParado = false;
	
	SortedMap<Integer, Integer> bilhetesSorteados = new TreeMap<Integer, Integer>();
	bilhetesSorteados.put(0, 0);
	bilhetesSorteados.put(1, 0);
	bilhetesSorteados.put(2, 2);
	bilhetesSorteados.put(3, 1);
	bilhetesSorteados.put(4, 0);
	bilhetesSorteados.put(5, 0);
	bilhetesSorteados.put(6, 0);
	bilhetesSorteados.put(7, 1);
	bilhetesSorteados.put(8, 0);
	bilhetesSorteados.put(9, 0);
	bilhetesSorteados.put(10, 1);

	
	int[]faixas ={5,5,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
			,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
			,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
			
	
	
		assertArrayEquals(faixas, tmp.calculaFaixasDeValores(5, 10,bilhetesSorteados));
		
	}
	
}
