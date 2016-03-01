package model.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import model.EfetuarSorteio;

public class EscreveESalvaSorteio {

	public static OutputStreamWriter geraArquivo(String nomeArquivo)
			{
		String encoding = "ISO-8859-1";
		if (nomeArquivo.length() == 0) {
			try {
				File tempFile = File.createTempFile("meu", ".tmp");
				nomeArquivo = tempFile.getAbsolutePath();
				EfetuarSorteio.en_NomeArquivoTXT = nomeArquivo;
				return new OutputStreamWriter(new FileOutputStream(tempFile),
						encoding);
			} catch (IOException e) {
				EfetuarSorteio.MsgErro = "DEU ERRO: "+e.getMessage();
				return null;
			}
		} else {
			try {
				return new OutputStreamWriter(new FileOutputStream(nomeArquivo),
						encoding);
			} catch (UnsupportedEncodingException | FileNotFoundException e) {
				// TODO Auto-generated catch block
				EfetuarSorteio.MsgErro = "DEU ERRO: "+e.getMessage();
				e.printStackTrace();
				return null;
			}
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
	
	public static boolean geraArquivoHash(){
		String Resultado = "";
		String NomeArquivoMD5 = EfetuarSorteio.en_NomeArquivoTXT + ".md5";
		BufferedWriter Output;
		try {
			Output = new BufferedWriter(new FileWriter(NomeArquivoMD5));
		} catch (IOException e1) {
			EfetuarSorteio.MsgErro = "N�o conseguiu criar " + NomeArquivoMD5
					+ ". Deu erro: " + e1.getMessage();
			EfetuarSorteio.EmExecucao = false;
			return false;
		}
		Resultado = String.format("%s  %s%s", EfetuarSorteio.sai_HashResultado,
				EfetuarSorteio.en_NomeArquivoTXT, System.getProperty("line.separator"));
		try {
			Output.append(Resultado.toLowerCase());
		} catch (IOException e1) {
			EfetuarSorteio.MsgErro = "N�o conseguiu gravar em " + NomeArquivoMD5
					+ ". Deu erro: " + e1.getMessage();
			try {
				Output.close();
			} catch (IOException e) {
				EfetuarSorteio.MsgErro = "N�o conseguiu fechar " + NomeArquivoMD5
						+ ". Deu erro: " + e1.getMessage();
				EfetuarSorteio.EmExecucao = false;
				e.printStackTrace();
			}
			return false;
		}
		// Fechar o arquivo.
		try {
			Output.close();
		} catch (IOException e1) {
			EfetuarSorteio.MsgErro = "N�o conseguiu fechar " + NomeArquivoMD5
					+ ". Deu erro: " + e1.getMessage();
			EfetuarSorteio.EmExecucao = false;
			return false ;
		}
		return true;
	}
	
	
	
}
