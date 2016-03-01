package test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import model.utils.GeradorDeHash;

import org.junit.Test;

public class testeGeradorDeHash {

	@Test
	public void test() {
FileInputStream is = null;
try {
	is = new FileInputStream("testeHash.txt");
} catch (FileNotFoundException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
String hash = "d41d8cd98f00b204e9800998ecf8427e";
	assertEquals(hash, GeradorDeHash.retornaHash(is));
		
	}

}
