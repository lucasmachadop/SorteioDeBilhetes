package model.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import model.EfetuarSorteio;

public class GeradorDeHash {

	public static String retornaHash(FileInputStream is){
		byte[] res = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			
			
				DigestInputStream dis = new DigestInputStream(is, md);
				byte[] buf = new byte[10240];
				try {
					
					while (dis.read(buf, 0, 10240) != -1) {
						if (EfetuarSorteio.PararSorteio) {
							dis.close();
							
							return "1";
						}
					}
					dis.close();


				} catch (IOException e) {
					
					
					return "2";
				}
			
			res = md.digest();
			return (new HexBinaryAdapter()).marshal(res).toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			
			return "3";
		}
		
	}
}
