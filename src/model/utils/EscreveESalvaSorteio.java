package model.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import model.EfetuarSorteio;

public class EscreveESalvaSorteio {

	public static OutputStreamWriter geraArquivo(String nomeArquivo, String encoding)
			throws UnsupportedEncodingException, FileNotFoundException {
		if (nomeArquivo.length() == 0) {
			try {
				File tempFile = File.createTempFile("meu", ".tmp");
				nomeArquivo = tempFile.getAbsolutePath();
				return new OutputStreamWriter(new FileOutputStream(tempFile),
						encoding);
			} catch (IOException e) {
				
				return null;
			}
		} else {
			return new OutputStreamWriter(new FileOutputStream(nomeArquivo),
					encoding);
		}

	}
	
	public static FileInputStream lerArquivo (String nomeDoArquivo){
		try {
			return new FileInputStream(nomeDoArquivo);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	
	
	}
}
