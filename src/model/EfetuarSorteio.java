package model;

import gui.Sorteio;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import model.utils.EscreveESalvaSorteio;
import model.utils.FormatacaoDocumento;
import model.utils.GeradorDeHash;

public class EfetuarSorteio extends Thread {

	public static boolean emExecucao;
	public static String extracao, dataExtracao, premio1, premio2,
			premio3, premio4, premio5, numDoSorteio,
			dataSorteio, dataDiarioOficial, totalDeBilhetes,
			totalDePremios, nomeArquivoTxt;

	public static String numDeFaixas;
	public static String valorEsperado;
	public static String valorDesvioPadrao;
	public static String valorMedio;
	public static String valorMaximo;
	public static String valorMinimo;
	public static String diferencaValoresMaxMin;
	public static String hashResultado;
	public static int vezes;
	public static int premios;
	public static String nomeSorteio;
	public static String msgDeErro;
	public static boolean sorteioParado;

	static String MD5_Aferido = "2dca7c8d565cf3a15d1fce4c8997c636";

	
	public EfetuarSorteio(){
	
	}
	
	public EfetuarSorteio(String str, String Extracao, String DataExtracao,
			String Premio1, String Premio2, String Premio3, String Premio4,
			String Premio5, String SorteioNumero, String DataSorteio,
			String DataDiarioOficial, String TotalBilhetes,
			String TotalPremios, String NomeArquivoTXT) {
		super(str);
		nomeSorteio = str;
		extracao = Extracao;
		dataExtracao = DataExtracao;
		premio1 = Premio1;
		premio2 = Premio2;
		premio3 = Premio3;
		premio4 = Premio4;
		premio5 = Premio5;
		numDoSorteio = SorteioNumero;
		dataSorteio = DataSorteio;
		dataDiarioOficial = DataDiarioOficial;
		totalDeBilhetes = TotalBilhetes;
		totalDePremios = TotalPremios;
		nomeArquivoTxt = NomeArquivoTXT;
	}

	public void run() {
		emExecucao = true;
		sorteioParado = false;
		String resultado = "";

		msgDeErro = "";

		valorEsperado = "";
		valorDesvioPadrao = "";
		valorMedio = "";
		valorMaximo = "";
		valorMinimo = "";
		diferencaValoresMaxMin = "";
		hashResultado = "";

		CharSequence target = ".";
		CharSequence replacement = "";
		String strBilhetes = totalDeBilhetes.replace(target, replacement);
		int numDeBilhetes = Integer.parseInt(strBilhetes);

		String strPremios = totalDePremios.replace(target, replacement);
		int numDePremios = Integer.parseInt(strPremios);

		numDeFaixas = FormatacaoDocumento.mf
				.format(Sorteio.numDeFaixas);
		double valorMedioEsperado = numDePremios / Sorteio.numDeFaixas;
		valorEsperado = FormatacaoDocumento.mf.format(valorMedioEsperado);

		SortedMap<Integer, Integer> BilhetesSorteados = new TreeMap<Integer, Integer>();

		int[] valorDosBilhetes;

		boolean metodoHash = usarMetodoHash(numDePremios);

		vezes = 1;

		resultado = FormatacaoDocumento.montaCabecalhoArquivo();

		OutputStreamWriter outSWriter = null;
		PrintWriter printWriter = null;

		outSWriter = EscreveESalvaSorteio.geraArquivo(nomeArquivoTxt);
		if (outSWriter==null) {
			encerrarSorteio();
			return;
		}
		printWriter = new PrintWriter(new BufferedWriter(outSWriter));
		printWriter.print(resultado);

		// Sorteio dos bilhetes.
		Random rngDoSorteio = determinaRNGdoSorteio(geraSemente(extracao,
				dataExtracao, premio1, premio2, premio3,
				premio4, premio5, numDoSorteio, dataSorteio));

		if (metodoHash) {
			valorDosBilhetes = new int[1];
			BilhetesSorteados = sorteioDosBilhetes(printWriter, rngDoSorteio, numDePremios,
					numDeBilhetes, BilhetesSorteados);

		} else {

			valorDosBilhetes = sorteioDosBilhetes(printWriter, rngDoSorteio, numDePremios, numDeBilhetes);

		}
		if (BilhetesSorteados == null) {
			msgDeErro = "A opera��o foi cancelada pelo operador!";
			encerrarSorteio();
			return;
		}

		// Obter os valores por faixas de valores dos bilhetes.

		double delta = calculaQuantidadeFaixas(numDeBilhetes,
				Sorteio.numDeFaixas);
		int[] numFaixas;

		if (BilhetesSorteados.isEmpty()) {

			numFaixas = calculaFaixasDeValores(delta, numDeBilhetes, valorDosBilhetes);

		} else {

			numFaixas = calculaFaixasDeValores(delta, numDeBilhetes,
					BilhetesSorteados);

		}
		if (numFaixas == null) {
			encerrarSorteio();
			return;
		}

		resultado = FormatacaoDocumento.montaCabecalhoFaixas();

		resultado += calculaDesvioPadrao(delta, numFaixas);

		resultado += FormatacaoDocumento.montaEstatisticas();

		printWriter.print(resultado);
		// Fechar o arquivo.
		printWriter.close();

		hashResultado = GeradorDeHash.retornaHash(EscreveESalvaSorteio
				.lerArquivo(nomeArquivoTxt));

		if (!Sorteio.programaAferido) {
			// Criar o arquivo para gravar o HASH.
			if(!EscreveESalvaSorteio.geraArquivoHash()){
				encerrarSorteio();
				return;
			}

			abrirArquivoTxt(nomeArquivoTxt);
		}
		if (nomeSorteio.equals("Aferir") && !MD5_Aferido.equals(hashResultado)) {
			msgDeErro = "N�o confere com o resultado esperado! o resultado � = "
					+ hashResultado;
		}
		if (Sorteio.programaAferido) {
			// Remover o arquivo temporário criado.
			(new File(nomeArquivoTxt)).delete();
		}
		emExecucao = false;
	}

	public void abrirArquivoTxt(String arquivo) {
		try {
			// tentando abrir o arquivo no WINDOWS
			Runtime.getRuntime().exec("explorer.exe " + arquivo);
		} catch (Exception e) {
			System.out.printf("Erro ao tentar abrir TXT no WINDOWS");
			try {
				// tentando abrir o arquivo no gedit do LINUX
				Runtime.getRuntime().exec("gedit " + arquivo);
			} catch (Exception e2) {
				System.out.printf("Erro ao tentar abrir TXT no GEDIT DO LINUX");
			}
		}
	}

	public static boolean isRunning() {
		return emExecucao;
	}

	public static void pararSorteio() {
		sorteioParado = true;
	}

	public void encerrarSorteio() {

		emExecucao = false;

	}

	public SortedMap<Integer, Integer> sorteioDosBilhetes(PrintWriter out,
			Random rg, int numPremios, int nBilhetes,
			SortedMap<Integer, Integer> bilhetesSorteados) {
		String resultado = "";
		for (EfetuarSorteio.premios = 1; EfetuarSorteio.premios <= numPremios; EfetuarSorteio.vezes++) {
			if (!sorteioParado) {
				int ri = rg.nextInt(nBilhetes) + 1;
				if (!bilhetesSorteados.containsKey(ri)) {
					bilhetesSorteados.put(ri, EfetuarSorteio.vezes);
				} else {
					resultado = String
							.format("%10s  %10s %11s  %s\r\n",
									FormatacaoDocumento.mf
											.format(EfetuarSorteio.vezes),
									"-",
									FormatacaoDocumento.mf.format(ri),
									"Bilhete j� foi sorteado na itera��o "
											+ FormatacaoDocumento.mf
													.format(bilhetesSorteados
															.get(ri)));
					out.print(resultado);
					continue;
				}
				resultado = montaResultado(vezes, premios, ri);
				EfetuarSorteio.premios++;
				out.println(resultado);
				
			} else {

				return null;
			}
		}
		return bilhetesSorteados;

	}

	public int[] sorteioDosBilhetes(PrintWriter out, Random rg, int numPremios,
			int nBilhetes) {
		int[] valores = new int[nBilhetes + 1];
		String resultado = "";
		for (EfetuarSorteio.premios = 1; EfetuarSorteio.premios <= numPremios; EfetuarSorteio.vezes++) {
			if (!sorteioParado) {

				int chaveBilhetes = rg.nextInt(nBilhetes) + 1;
				if (valores[chaveBilhetes] > 0) {
					resultado = String
							.format("%10s  %10s %11s  %s\r\n",
									FormatacaoDocumento.mf
											.format(EfetuarSorteio.vezes),
									"-",
									FormatacaoDocumento.mf.format(chaveBilhetes),
									"Bilhete j� foi sorteado na itera��o "
											+ FormatacaoDocumento.mf
													.format(valores[chaveBilhetes]));
					out.print(resultado);
					continue;
				}
				valores[chaveBilhetes] = EfetuarSorteio.vezes;
				resultado = montaResultado(vezes, premios, chaveBilhetes);
				EfetuarSorteio.premios++;
				out.println(resultado);

			} else {

				return null;
			}
		}
		return valores;
	}

	public String montaResultado(int vezes, int premios, int ri) {
		String resultado = "";
		resultado = String.format("%10s  %10s %11s  %s\r\n",
				FormatacaoDocumento.mf.format(vezes),
				FormatacaoDocumento.mf.format(premios),
				FormatacaoDocumento.mf.format(ri), "");
		return resultado;
	}

	public Random determinaRNGdoSorteio(String semente) {

		Random rg = new Random();
		long sd = semente.hashCode();
		rg.setSeed(sd);
		return rg;

	}

	public String geraSemente(String extracao, String dataExtracao,
			String premio1, String premio2, String premio3, String premio4,
			String premio5, String nSorteio, String dSorteio) {
		String semente = "";

		semente = extracao + dataExtracao + premio1 + premio2 + premio3
				+ premio4 + premio5 + nSorteio + dSorteio;
		return semente;
	}

	public boolean usarMetodoHash(int nPremios) {

		return (nPremios > 2000000) ? true : false;

	}

	public int[] calculaFaixasDeValores(double delta, int nBilhetes, int[] valores) {
		int[] faixas = new int[Sorteio.numDeFaixas];
		int i, j;
		// Método DIRETO
		
		for (i = 1; i <= nBilhetes; i++) {
			if (sorteioParado) {
				msgDeErro = "A opera��o foi cancelada pelo operador!";

				return null;
			}
			if (valores[i] > 0) {
				if (delta > 0)
					j = (int) ((i - 1) / delta);
				else
					j = 0;
				faixas[j]++;
			}
		}
		return faixas;

	}

	public int[] calculaFaixasDeValores(double delta, int nBilhetes,
			SortedMap<Integer, Integer> bilhetesSorteados) {

		int[] faixas = new int[Sorteio.numDeFaixas];

		int i, j;

		for (Entry<Integer, Integer> nextEntry : bilhetesSorteados.entrySet()) {
			if (sorteioParado) {
				msgDeErro = "A opera��o foi cancelada pelo operador!";

				return null;
			}
			for (j = 0; j < Sorteio.numDeFaixas; j++) {
				i = (int) delta * j;
				int k = (int) delta * (j + 1);
				if ((nextEntry.getKey() > i) && (nextEntry.getKey() <= k))
					faixas[j]++;
			}
		}
		return faixas;
	}

	public double calculaQuantidadeFaixas(int numBilhetes, int numFaixas) {
		return (double) numBilhetes / numFaixas;
	}

	public String calculaDesvioPadrao(double delta, int[] faixas) {
		int maxPremio = 0, minPremio = 2 * (int) delta;
		double somaDosQuadrados = 0;
		double desvioPadrao = 0;
		double Media = 0;
		String Resultado = "";
		int Soma = 0;
		for (int j = 0; j < Sorteio.numDeFaixas; j++) {
			Resultado += String.format(
					"%10s      %11s    %11s        %10s\r\n",
					FormatacaoDocumento.mf.format(j + 1),
					FormatacaoDocumento.mf.format((int) (delta * j) + 1),
					FormatacaoDocumento.mf.format((int) (delta * (j + 1))),
					FormatacaoDocumento.mf.format(faixas[j]));
			Soma += faixas[j];
			if (minPremio > faixas[j])
				minPremio = faixas[j];
			if (maxPremio < faixas[j])
				maxPremio = faixas[j];
		}

		Media = Soma / Sorteio.numDeFaixas;

		for (int j = 0; j < Sorteio.numDeFaixas; j++) {
			somaDosQuadrados += Math.pow((faixas[j] - Media), 2);
		}
		desvioPadrao = Math.sqrt(somaDosQuadrados / (Sorteio.numDeFaixas - 1));

		int difMaxMin = maxPremio - minPremio;

		// Formatação dos resultados.
		valorDesvioPadrao = FormatacaoDocumento.mf
				.format((int) desvioPadrao);
		valorMedio = FormatacaoDocumento.mf.format(Media);
		valorMaximo = FormatacaoDocumento.mf.format(maxPremio);
		valorMinimo = FormatacaoDocumento.mf.format(minPremio);
		diferencaValoresMaxMin = FormatacaoDocumento.mf.format(difMaxMin);

		return Resultado;
	}

}
