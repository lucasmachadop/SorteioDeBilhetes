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

	public static boolean EmExecucao;
	public static String en_Extracao, en_DataExtracao, en_Premio1, en_Premio2,
			en_Premio3, en_Premio4, en_Premio5, en_SorteioNumero,
			en_DataSorteio, en_DataDiarioOficial, en_TotalBilhetes,
			en_TotalPremios, en_NomeArquivoTXT;

	public static String sai_ValorNumeroFaixas;
	public static String sai_ValorEsperado;
	public static String sai_ValorDesvioPadrao;
	public static String sai_ValorMedio;
	public static String sai_ValorMaximo;
	public static String sai_ValorMinimo;
	public static String sai_ValorDiferencaMaxMin;
	public static String sai_HashResultado;
	public static int Vezes;
	public static int Premios;
	public static String Nome;
	public static String MsgErro;
	public static boolean PararSorteio;

	static String MD5_Aferido = "2dca7c8d565cf3a15d1fce4c8997c636";

	
	public EfetuarSorteio(){
	
	}
	
	public EfetuarSorteio(String str, String Extracao, String DataExtracao,
			String Premio1, String Premio2, String Premio3, String Premio4,
			String Premio5, String SorteioNumero, String DataSorteio,
			String DataDiarioOficial, String TotalBilhetes,
			String TotalPremios, String NomeArquivoTXT) {
		super(str);
		Nome = str;
		en_Extracao = Extracao;
		en_DataExtracao = DataExtracao;
		en_Premio1 = Premio1;
		en_Premio2 = Premio2;
		en_Premio3 = Premio3;
		en_Premio4 = Premio4;
		en_Premio5 = Premio5;
		en_SorteioNumero = SorteioNumero;
		en_DataSorteio = DataSorteio;
		en_DataDiarioOficial = DataDiarioOficial;
		en_TotalBilhetes = TotalBilhetes;
		en_TotalPremios = TotalPremios;
		en_NomeArquivoTXT = NomeArquivoTXT;
	}

	public void run() {
		EmExecucao = true;
		PararSorteio = false;
		String Resultado = "";

		MsgErro = "";

		sai_ValorEsperado = "";
		sai_ValorDesvioPadrao = "";
		sai_ValorMedio = "";
		sai_ValorMaximo = "";
		sai_ValorMinimo = "";
		sai_ValorDiferencaMaxMin = "";
		sai_HashResultado = "";

		CharSequence target = ".";
		CharSequence replacement = "";
		String StrBilhetes = en_TotalBilhetes.replace(target, replacement);
		int NumBilhetes = Integer.parseInt(StrBilhetes);

		String StrPremios = en_TotalPremios.replace(target, replacement);
		int NumPremios = Integer.parseInt(StrPremios);

		sai_ValorNumeroFaixas = FormatacaoDocumento.mf
				.format(Sorteio.NumeroFaixas);
		double ValorMedioEsperado = NumPremios / Sorteio.NumeroFaixas;
		sai_ValorEsperado = FormatacaoDocumento.mf.format(ValorMedioEsperado);

		SortedMap<Integer, Integer> BilhetesSorteados = new TreeMap<Integer, Integer>();

		int[] valores;

		boolean MetodoHASH = usarMetodoHash(NumPremios);

		Vezes = 1;

		Resultado = FormatacaoDocumento.montaCabeçalhoArquivo();

		OutputStreamWriter outS = null;
		PrintWriter out = null;

		outS = EscreveESalvaSorteio.geraArquivo(en_NomeArquivoTXT);
		if (outS==null) {
			encerrarSorteio();
			return;
		}
		out = new PrintWriter(new BufferedWriter(outS));
		out.print(Resultado);

		// Sorteio dos bilhetes.
		Random rg = determinaRNGdoSorteio(geraSemente(en_Extracao,
				en_DataExtracao, en_Premio1, en_Premio2, en_Premio3,
				en_Premio4, en_Premio5, en_SorteioNumero, en_DataSorteio));

		if (MetodoHASH) {
			valores = new int[1];
			BilhetesSorteados = sorteioDosBilhetes(out, rg, NumPremios,
					NumBilhetes, BilhetesSorteados);

		} else {

			valores = sorteioDosBilhetes(out, rg, NumPremios, NumBilhetes);

		}
		if (BilhetesSorteados == null) {
			MsgErro = "A operação foi cancelada pelo operador!";
			encerrarSorteio();
			return;
		}

		// Obter os valores por faixas de valores dos bilhetes.

		double delta = calculaQuantidadeFaixas(NumBilhetes,
				Sorteio.NumeroFaixas);
		int[] faixas;

		if (BilhetesSorteados.isEmpty()) {

			faixas = calculaFaixasDeValores(delta, NumBilhetes, valores);

		} else {

			faixas = calculaFaixasDeValores(delta, NumBilhetes,
					BilhetesSorteados);

		}
		if (faixas == null) {
			encerrarSorteio();
			return;
		}

		Resultado = FormatacaoDocumento.montaCabecalhoFaixas();

		Resultado += calculaDesvioPadrao(delta, faixas);

		Resultado += FormatacaoDocumento.montaEstatisticas();

		out.print(Resultado);
		// Fechar o arquivo.
		out.close();

		sai_HashResultado = GeradorDeHash.retornaHash(EscreveESalvaSorteio
				.lerArquivo(en_NomeArquivoTXT));

		if (!Sorteio.AferindoPrograma) {
			// Criar o arquivo para gravar o HASH.
			if(!EscreveESalvaSorteio.geraArquivoHash()){
				encerrarSorteio();
				return;
			}

			abrir(en_NomeArquivoTXT);
		}
		if (Nome.equals("Aferir") && !MD5_Aferido.equals(sai_HashResultado)) {
			MsgErro = "Não confere com o resultado esperado! o resultado é = "
					+ sai_HashResultado;
		}
		if (Sorteio.AferindoPrograma) {
			// Remover o arquivo temporÃ¡rio criado.
			(new File(en_NomeArquivoTXT)).delete();
		}
		EmExecucao = false;
	}

	public void abrir(String arquivo) {
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
		return EmExecucao;
	}

	public static void Parar() {
		PararSorteio = true;
	}

	public void encerrarSorteio() {

		EmExecucao = false;

	}

	public SortedMap<Integer, Integer> sorteioDosBilhetes(PrintWriter out,
			Random rg, int numPremios, int nBilhetes,
			SortedMap<Integer, Integer> bilhetesSorteados) {
		String resultado = "";
		for (EfetuarSorteio.Premios = 1; EfetuarSorteio.Premios <= numPremios; EfetuarSorteio.Vezes++) {
			if (!PararSorteio) {
				int ri = rg.nextInt(nBilhetes) + 1;
				if (!bilhetesSorteados.containsKey(ri)) {
					bilhetesSorteados.put(ri, EfetuarSorteio.Vezes);
				} else {
					resultado = String
							.format("%10s  %10s %11s  %s\r\n",
									FormatacaoDocumento.mf
											.format(EfetuarSorteio.Vezes),
									"-",
									FormatacaoDocumento.mf.format(ri),
									"Bilhete já foi sorteado na iteração "
											+ FormatacaoDocumento.mf
													.format(bilhetesSorteados
															.get(ri)));
					out.print(resultado);
					continue;
				}
				resultado = montaResultado(Vezes, Premios, ri);
				EfetuarSorteio.Premios++;
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
		for (EfetuarSorteio.Premios = 1; EfetuarSorteio.Premios <= numPremios; EfetuarSorteio.Vezes++) {
			if (!PararSorteio) {

				int ri = rg.nextInt(nBilhetes) + 1;
				if (valores[ri] > 0) {
					resultado = String
							.format("%10s  %10s %11s  %s\r\n",
									FormatacaoDocumento.mf
											.format(EfetuarSorteio.Vezes),
									"-",
									FormatacaoDocumento.mf.format(ri),
									"Bilhete já foi sorteado na iteração "
											+ FormatacaoDocumento.mf
													.format(valores[ri]));
					out.print(resultado);
					continue;
				}
				valores[ri] = EfetuarSorteio.Vezes;
				resultado = montaResultado(Vezes, Premios, ri);
				EfetuarSorteio.Premios++;
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
		int[] faixas = new int[Sorteio.NumeroFaixas];
		int i, j;
		// MÃ©todo DIRETO
		
		for (i = 1; i <= nBilhetes; i++) {
			if (PararSorteio) {
				MsgErro = "A operação foi cancelada pelo operador!";

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

		int[] faixas = new int[Sorteio.NumeroFaixas];

		int i, j;

		for (Entry<Integer, Integer> nextEntry : bilhetesSorteados.entrySet()) {
			if (PararSorteio) {
				MsgErro = "A operação foi cancelada pelo operador!";

				return null;
			}
			for (j = 0; j < Sorteio.NumeroFaixas; j++) {
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
		int Max = 0, Min = 2 * (int) delta;
		double SomaDosQuadrados = 0;
		double DesvioPadrao = 0;
		double Media = 0;
		String Resultado = "";
		int Soma = 0;
		for (int j = 0; j < Sorteio.NumeroFaixas; j++) {
			Resultado += String.format(
					"%10s      %11s    %11s        %10s\r\n",
					FormatacaoDocumento.mf.format(j + 1),
					FormatacaoDocumento.mf.format((int) (delta * j) + 1),
					FormatacaoDocumento.mf.format((int) (delta * (j + 1))),
					FormatacaoDocumento.mf.format(faixas[j]));
			Soma += faixas[j];
			if (Min > faixas[j])
				Min = faixas[j];
			if (Max < faixas[j])
				Max = faixas[j];
		}

		Media = Soma / Sorteio.NumeroFaixas;

		for (int j = 0; j < Sorteio.NumeroFaixas; j++) {
			SomaDosQuadrados += Math.pow((faixas[j] - Media), 2);
		}
		DesvioPadrao = Math.sqrt(SomaDosQuadrados / (Sorteio.NumeroFaixas - 1));

		int DifMaxMin = Max - Min;

		// FormataÃ§Ã£o dos resultados.
		sai_ValorDesvioPadrao = FormatacaoDocumento.mf
				.format((int) DesvioPadrao);
		sai_ValorMedio = FormatacaoDocumento.mf.format(Media);
		sai_ValorMaximo = FormatacaoDocumento.mf.format(Max);
		sai_ValorMinimo = FormatacaoDocumento.mf.format(Min);
		sai_ValorDiferencaMaxMin = FormatacaoDocumento.mf.format(DifMaxMin);

		return Resultado;
	}

}
