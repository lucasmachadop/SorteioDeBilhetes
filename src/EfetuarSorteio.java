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

public class EfetuarSorteio extends Thread {

	static boolean EmExecucao;
	static String en_Extracao, en_DataExtracao, en_Premio1,
			en_Premio2, en_Premio3, en_Premio4,
			en_Premio5, en_SorteioNumero, en_DataSorteio, en_DataDiarioOficial,
			en_TotalBilhetes, en_TotalPremios, en_NomeArquivoTXT;

	static String sai_ValorNumeroFaixas;
	static String sai_ValorEsperado;
	static String sai_ValorDesvioPadrao;
	static String sai_ValorMedio;
	static String sai_ValorMaximo;
	static String sai_ValorMinimo;
	static String sai_ValorDiferencaMaxMin;
	static String sai_HashResultado;
	static int Vezes;
	static int Premios;
	static String Nome;
	static String MsgErro;
	static boolean PararSorteio;

	static String MD5_Aferido = "692f68f5019bd6e1fd4ac4e6b6cb5753";

	public EfetuarSorteio(String str, String Extracao, String DataExtracao, String Premio1,
						  String Premio2, String Premio3, String Premio4,
						  String Premio5, String SorteioNumero, String DataSorteio,
						  String DataDiarioOficial, String TotalBilhetes, String TotalPremios,
						  String NomeArquivoTXT) {
		super(str);
		Nome = str;
		en_Extracao = Extracao;
		en_DataExtracao = DataExtracao;
		en_Premio1 = Premio1; en_Premio2 = Premio2; en_Premio3 = Premio3;
		en_Premio4 = Premio4; en_Premio5 = Premio5;
		en_SorteioNumero = SorteioNumero; en_DataSorteio = DataSorteio;
		en_DataDiarioOficial = DataDiarioOficial;
		en_TotalBilhetes = TotalBilhetes; en_TotalPremios = TotalPremios;
		en_NomeArquivoTXT = NomeArquivoTXT;
	}
	public void run() {
		EmExecucao = true;
		PararSorteio = false;
		String Resultado = "";

		MsgErro = "";

		NumberFormat mf = NumberFormat.getInstance(Locale.GERMAN);

		sai_ValorEsperado = "";
		sai_ValorDesvioPadrao = "";
		sai_ValorMedio = "";
		sai_ValorMaximo = "";
		sai_ValorMinimo = "";
		sai_ValorDiferencaMaxMin = "";
		sai_HashResultado = "";

		Resultado = "Secretaria da Fazenda do Estado de Goi�s\r\n" +
				"====================================================\r\n\r\n" +
				"Vers�o do Programa: " + Sorteio.Versao + "\r\n\r\n" +
				"Sorteio de Bilhetes\r\n" +
				"-------------------\r\n" +
				"Loteria Federal Extração nº " + en_Extracao + " de " + en_DataExtracao + "\r\n" +
				"1º Premio: " + en_Premio1 + "\r\n" +
				"2º Premio: " + en_Premio2 + "\r\n" +
				"3º Premio: " + en_Premio3 + "\r\n" +
				"4º Premio: " + en_Premio4 + "\r\n" +
				"5º Premio: " + en_Premio5 + "\r\n\r\n" +
				"Sorteio nº " + en_SorteioNumero + "\r\n";

		Resultado += "Data do Sorteio:         " + en_DataSorteio + "\r\n" +
				"Data no Di�rio Oficial:  " + en_DataDiarioOficial + "\r\n";
		Resultado += String.format("N�mero de Bilhetes:     %11s\r\n", en_TotalBilhetes);
		Resultado += String.format("N�mero de Premios:      %11s\r\n", en_TotalPremios);

		CharSequence target = ".";
		CharSequence replacement = "";
		String StrBilhetes = en_TotalBilhetes.replace(target , replacement);
		int NumBilhetes = Integer.parseInt(StrBilhetes);

		String StrPremios = en_TotalPremios.replace(target , replacement);
		int NumPremios = Integer.parseInt(StrPremios);

		sai_ValorNumeroFaixas = mf.format(Sorteio.NumeroFaixas);
		double ValorMedioEsperado = NumPremios / Sorteio.NumeroFaixas;
		sai_ValorEsperado = mf.format(ValorMedioEsperado);

		SortedMap<Integer, Integer> BilhetesSorteados = new TreeMap<Integer, Integer>();

		int[] valores;

		boolean MetodoHASH = false;

		if (NumPremios > 2000000) {
			// Se o número de prêmios é superior a 1 milhão então utiliza o método DIRETO
			// de guardar o número de cada bilhete premiado.
			// Este método consome mais memória para o programa armazenar os bilhetes
			// premiados, mas a execução é mais rápida.
			MetodoHASH = false;
		} else {
			// Se o número de prêmios é inferior a 1 milhão então utiliza o método HASH
			// de guardar o número de cada bilhete premiado.
			// Este método consome menos memória para o programa armazenar os bilhetes
			// premiados, mas a execução é mais lenta.
			MetodoHASH = true;
		}

		try {
			if (!MetodoHASH) {
				// Método DIRETO.
				valores = new int[NumBilhetes + 1];
			} else {
				// Método HASH.
				valores = new int[1];
			}
		} catch (OutOfMemoryError e) {
			// Faltou memória!
			int MEGABYTE = (1024*1024);
			String Caminho = System.getProperty("user.dir");
			long MemoriaNecessaria = (8 * (NumBilhetes + 1)) / MEGABYTE;
			long TamMemoria = 256;
			if (MemoriaNecessaria > 2048)
				TamMemoria = 4096;
			else if (MemoriaNecessaria > 1024)
				TamMemoria = 2048;
			else if (MemoriaNecessaria > 512)
				TamMemoria = 1024;
			else if (MemoriaNecessaria > 256)
				TamMemoria = 512;
			MsgErro = "Faltou espaço de memória.\nTente executar o programa assim:\n\njavaw -Xmx" +
					TamMemoria + "m -jar " + Caminho + File.separator + "SorteioNFG_" + Sorteio.Versao + ".jar\n";
			EmExecucao = false;
			return;
		}

		Vezes = 1;

		Resultado += "\r\nRela��o dos Bilhetes Premiados:\r\n" +
				"-------------------------------\r\n" +
				"  Itera��o      Premio     Bilhete  Observa��o\r\n" +
				"==========  ==========  ==========  =====================================\r\n";

		String encoding = "ISO-8859-1";
		OutputStreamWriter outS = null;
		PrintWriter out = null;
		try {
			if (en_NomeArquivoTXT.length() == 0) {
				try {
					File tempFile = File.createTempFile("meu", ".tmp");
					en_NomeArquivoTXT = tempFile.getAbsolutePath();
					outS = new OutputStreamWriter(new FileOutputStream(tempFile), encoding);
				} catch (IOException e) {
					MsgErro = "Não conseguiu criar " + en_NomeArquivoTXT + ". Deu erro: " + e.getMessage();
					EmExecucao = false;
					return;
				}
			} else {
				outS = new OutputStreamWriter(new FileOutputStream(en_NomeArquivoTXT), encoding);
			}
		} catch (UnsupportedEncodingException e2) {
			MsgErro = "Não conseguiu criar " + en_NomeArquivoTXT + ". Deu erro: " + e2.getMessage();
			EmExecucao = false;
			return;
		} catch (FileNotFoundException e2) {
			MsgErro = "Não conseguiu criar " + en_NomeArquivoTXT + ". Deu erro: " + e2.getMessage();
			EmExecucao = false;
			return;
		}
		out = new PrintWriter(new BufferedWriter(outS));
		out.print(Resultado);

		// Sorteio dos bilhetes.
		String semente = "";


		semente = en_Extracao + en_DataExtracao +
				en_Premio1 + en_Premio2 + en_Premio3 + en_Premio4 +
				en_Premio5 + en_SorteioNumero + en_DataSorteio;

		Random rg = new Random();
		long sd = semente.hashCode();
		rg.setSeed(sd);
		
		
		for (Premios = 1; Premios <= NumPremios; Vezes++) {
			if (PararSorteio) {
				MsgErro = "A operação foi cancelada pelo operador!";
				EmExecucao = false;
				return;
			}
			int ri = rg.nextInt(NumBilhetes) + 1;

			if (MetodoHASH) {
				// Método HASH
				if (!BilhetesSorteados.containsKey(ri)) {
					BilhetesSorteados.put(ri, Vezes);
				} else {
					Resultado = String.format("%10s  %10s %11s  %s\r\n",
							mf.format(Vezes), "-", mf.format(ri), "Bilhete já foi sorteado na iteração " + mf.format(BilhetesSorteados.get(ri)));
					out.print(Resultado);
					continue;
				}
			} else {
				// Método DIRETO.
				if (valores[ri] > 0) {
					Resultado = String.format("%10s  %10s %11s  %s\r\n",
							mf.format(Vezes), "-", mf.format(ri), "Bilhete já foi sorteado na iteração " + mf.format(valores[ri]));
					out.print(Resultado);
					continue;
				}
				valores[ri] = Vezes;
			}
			Resultado = String.format("%10s  %10s %11s  %s\r\n",
					mf.format(Vezes), mf.format(Premios), mf.format(ri), "");
			Premios++;
			out.print(Resultado);
		}

		// Obter os valores por faixas de valores dos bilhetes.
		int[] faixas = new int[Sorteio.NumeroFaixas];
		double delta = (double)NumBilhetes / (double)Sorteio.NumeroFaixas;
		int i, j;

		if (MetodoHASH) {
			// Método HASH.
			for (Entry<Integer, Integer> nextEntry : BilhetesSorteados.entrySet()) {
				if (PararSorteio) {
					MsgErro = "A operação foi cancelada pelo operador!";
					EmExecucao = false;
					return;
				}
				for (j = 0; j < Sorteio.NumeroFaixas; j++) {
					i = (int)delta * j;
					int k = (int)delta * (j + 1);
					if ((nextEntry.getKey() > i) && (nextEntry.getKey() <= k))
						faixas[j]++;
				}
			}
		} else {
			// Método DIRETO
			for (i = 1; i <= NumBilhetes; i++) {
				if (PararSorteio) {
					MsgErro = "A operação foi cancelada pelo operador!";
					EmExecucao = false;
					return;
				}
				if (valores[i] > 0) {
					if (delta > 0)
						j = (int)((i - 1) / delta);
					else
						j = 0;
					faixas[j]++;
				}
			}
		}

		int Max = 0, Min = 2 * (int)delta;
		double SomaDosQuadrados = 0;
		double DesvioPadrao = 0;
		double Media = 0;

		Resultado = "\r\nAVALIAÇÃO DO SORTEIO\r\n" +
				"====================\r\n" +
				"Número de Faixas de Bilhetes: " + sai_ValorNumeroFaixas + "\r\n";


		// Obter os valores máximo e mínimo de prêmios por faixa de valores de bilhetes.
		Resultado += "\r\nRelação das Faixas de Bilhetes:\r\n" +
				"-------------------------------\r\n" +
				"     Faixa  Bilhete Inicial  Bilhete Final  Prêmios na Faixa\r\n" +
				"==========  ===============  =============  ================\r\n";

		int Soma = 0;
		for (j = 0; j < Sorteio.NumeroFaixas; j++) {
			Resultado += String.format("%10s      %11s    %11s        %10s\r\n",
					mf.format(j + 1), mf.format((int)(delta * j) + 1), mf.format((int)(delta * (j + 1))), mf.format(faixas[j]));
			Soma += faixas[j];
			if (Min > faixas[j])
				Min = faixas[j];
			if (Max < faixas[j])
				Max = faixas[j];
		}

		// Cálculo do Desvio Padrão.
		Media = Soma / Sorteio.NumeroFaixas;

		for (j = 0; j < Sorteio.NumeroFaixas; j++) {
			SomaDosQuadrados += Math.pow((faixas[j] - Media), 2);
		}
		DesvioPadrao = Math.sqrt(SomaDosQuadrados / (Sorteio.NumeroFaixas - 1));

		int DifMaxMin = Max - Min;

		// Formatação dos resultados.
		sai_ValorDesvioPadrao = mf.format((int)DesvioPadrao);
		sai_ValorMedio = mf.format(Media);
		sai_ValorMaximo = mf.format(Max);
		sai_ValorMinimo = mf.format(Min);
		sai_ValorDiferencaMaxMin = mf.format(DifMaxMin);

		Resultado += "\r\nEstatísticas do Número de Prêmios Por Faixa de Bilhetes\r\n" +
				"=======================================================\r\n";
		Resultado += String.format("     Valor Médio Calculado:      %10s\r\n", sai_ValorEsperado);
		Resultado += String.format("     Valor Máximo Obtido:        %10s\r\n", sai_ValorMaximo);
		Resultado += String.format("     Valor Mínimo Obtido:        %10s\r\n", sai_ValorMinimo);
		Resultado += String.format("     Diferença: Máximo - Mínimo: %10s\r\n", sai_ValorDiferencaMaxMin);
		Resultado += String.format("     Valor Médio Obtido:         %10s\r\n", sai_ValorMedio);
		Resultado += String.format("     Desvio Padrão:              %10s\r\n", sai_ValorDesvioPadrao);
		Resultado += String.format("     Número de Iterações:        %10s\r\n", mf.format(Vezes - 1));
		Resultado += "-----------------------------------------------------------------\r\n";

		out.print(Resultado);
		// Fechar o arquivo.
		out.close();

		// Obter o HASH do resultado.
		byte[] res = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			InputStream is;
			try {
				is = new FileInputStream(en_NomeArquivoTXT);
				is = new DigestInputStream(is, md);
				byte[] buf = new byte[10240];
				try {
					while (is.read(buf, 0, 10240) != -1) {
						if (PararSorteio) {
							is.close();
							MsgErro = "A operação foi cancelada pelo operador!";
							EmExecucao = false;
							return;
						}
					}
					is.close();


				} catch (IOException e) {
					MsgErro = "Não conseguiu fechar " + en_NomeArquivoTXT + ". Deu erro: " + e.getMessage();
					EmExecucao = false;
					return;
				}
			} catch (FileNotFoundException e) {
				MsgErro = "Não conseguiu abrir " + en_NomeArquivoTXT + ". Deu erro: " + e.getMessage();
				EmExecucao = false;
				return;
			}
			res = md.digest();
			sai_HashResultado = (new HexBinaryAdapter()).marshal(res).toLowerCase();
		} catch (NoSuchAlgorithmException e) {
			MsgErro = "Não existe este algorítmo: " + e.getMessage();
			EmExecucao = false;
			return;
		}

		if (!Sorteio.AferindoPrograma) {
			// Criar o arquivo para gravar o HASH.
			String NomeArquivoMD5 = en_NomeArquivoTXT + ".md5";
			BufferedWriter Output;
			try {
				Output = new BufferedWriter(new FileWriter(NomeArquivoMD5));
			} catch (IOException e1) {
				MsgErro = "Não conseguiu criar " + NomeArquivoMD5 + ". Deu erro: " + e1.getMessage();
				EmExecucao = false;
				return;
			}
			Resultado = String.format("%s  %s%s", sai_HashResultado, en_NomeArquivoTXT, System.getProperty("line.separator"));
			try {
				Output.append(Resultado.toLowerCase());
			} catch (IOException e1) {
				MsgErro = "Não conseguiu gravar em " + NomeArquivoMD5 + ". Deu erro: " + e1.getMessage();
				try {
					Output.close();
				} catch (IOException e) {
					MsgErro += "\r\nNão conseguiu fechar " + NomeArquivoMD5 + ". Deu erro: " + e1.getMessage();
					EmExecucao = false;
					return;
				}
				EmExecucao = false;
				return;
			}
			// Fechar o arquivo.
			try {
				Output.close();
			} catch (IOException e1) {
				MsgErro = "Não conseguiu fechar " + NomeArquivoMD5 + ". Deu erro: " + e1.getMessage();
				EmExecucao = false;
				return;
			}

			abrir(en_NomeArquivoTXT);
		}
		if (Nome.equals("Aferir") && !MD5_Aferido.equals(sai_HashResultado)) {
			MsgErro = "Não confere com o resultado esperado!";
		}
		if (Sorteio.AferindoPrograma) {
			// Remover o arquivo temporário criado.
			(new File(en_NomeArquivoTXT)).delete();
		}
		EmExecucao = false;
	}

	private void abrir(String arquivo) {
		try {
			//tentando abrir o arquivo no WINDOWS
			Runtime.getRuntime().exec("explorer.exe "+arquivo);
		} catch (Exception e) {
			System.out.printf("Erro ao tentar abrir TXT no WINDOWS");
			try {
				//tentando abrir o arquivo no gedit do LINUX
				Runtime.getRuntime().exec("gedit "+arquivo);
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
}
