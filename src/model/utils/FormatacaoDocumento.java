package model.utils;

import java.text.NumberFormat;
import java.util.Locale;

import model.EfetuarSorteio;
import gui.Sorteio;

public class FormatacaoDocumento {
	public static NumberFormat mf = NumberFormat.getInstance(Locale.GERMAN);
	public static String montaCabecalhoArquivo(){
		String cabecalho =  "Secretaria da Fazenda do Estado de Goi�s\r\n"
				+ "====================================================\r\n\r\n"
				+ "Vers�o do Programa: "
				+ Sorteio.versao
				+ "\r\n\r\n"
				+ "Sorteio de Bilhetes\r\n"
				+ "-------------------\r\n"
				+ "Loteria Federal Extração nº "
				+ EfetuarSorteio.extracao
				+ " de "
				+ EfetuarSorteio.dataExtracao
				+ "\r\n"
				+ "1º Premio: "
				+ EfetuarSorteio.premio1
				+ "\r\n"
				+ "2º Premio: "
				+ EfetuarSorteio.premio2
				+ "\r\n"
				+ "3º Premio: "
				+ EfetuarSorteio.premio3
				+ "\r\n"
				+ "4º Premio: "
				+ EfetuarSorteio.premio4
				+ "\r\n"
				+ "5º Premio: "
				+ EfetuarSorteio.premio5
				+ "\r\n\r\n"
				+ "Sorteio nº "
				+ EfetuarSorteio.numDoSorteio
				+ "\r\n";

		cabecalho += "Data do Sorteio:         " + EfetuarSorteio.dataSorteio + "\r\n"
				+ "Data no Di�rio Oficial:  " + EfetuarSorteio.dataDiarioOficial + "\r\n";
		cabecalho += String.format("N�mero de Bilhetes:     %11s\r\n",
				EfetuarSorteio.totalDeBilhetes);
		cabecalho += String.format("N�mero de Premios:      %11s\r\n",
				EfetuarSorteio.totalDePremios);
		cabecalho += "\r\nRela��o dos Bilhetes Premiados:\r\n"
				+ "-------------------------------\r\n"
				+ "  Itera��o      Premio     Bilhete  Observa��o\r\n"
				+ "==========  ==========  ==========  =====================================\r\n";
		return cabecalho;
		}
	
	public static String montaResultado(int vezes, int premios,int ri){
		String resultado = "";
		resultado = String.format("%10s  %10s %11s  %s\r\n",
				mf.format(EfetuarSorteio.vezes),
				mf.format(EfetuarSorteio.premios), mf.format(ri), "");
		return resultado;
		}
	
	public static String montaCabecalhoFaixas(){
		
		String Resultado = "\r\nAVALIA��O DO SORTEIO\r\n"
				+ "====================\r\n" + "N�mero de Faixas de Bilhetes: "
				+ EfetuarSorteio.numDeFaixas + "\r\n";

		// Obter os valores M�ximo e M�nimo de Pr�mios por faixa de valores de
		// bilhetes.
		Resultado += "\r\nRela��o das Faixas de Bilhetes:\r\n"
				+ "-------------------------------\r\n"
				+ "     Faixa  Bilhete Inicial  Bilhete Final  Pr�mios na Faixa\r\n"
				+ "==========  ===============  =============  ================\r\n";
		return Resultado;
	}
	
	public static String montaEstatisticas(){
	 String Resultado = "\r\nEstatisticas do N�mero de Pr�mios Por Faixa de Bilhetes\r\n"
			+ "=======================================================\r\n";
	Resultado += String.format("     Valor M�dio Calculado:      %10s\r\n",
			EfetuarSorteio.valorEsperado);
	Resultado += String.format("     Valor M�ximo Obtido:        %10s\r\n",
			EfetuarSorteio.valorMaximo);
	Resultado += String.format("     Valor M�nimo Obtido:        %10s\r\n",
			EfetuarSorteio.valorMinimo);
	Resultado += String.format(
			"     Diferen�as: M�ximo - M�nimo: %10s\r\n",
			EfetuarSorteio.diferencaValoresMaxMin);
	Resultado += String.format("     Valor M�dio Obtido:         %10s\r\n",
			EfetuarSorteio.valorMedio);
	Resultado += String.format("     Desvio Padr�o:              %10s\r\n",
			EfetuarSorteio.valorDesvioPadrao);
	Resultado += String.format("     N�mero de Itera��es:        %10s\r\n",
			FormatacaoDocumento.mf.format(EfetuarSorteio.vezes - 1));
	Resultado += "-----------------------------------------------------------------\r\n";
	return Resultado;
	}
	
	
}
