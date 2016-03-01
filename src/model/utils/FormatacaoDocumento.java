package model.utils;

import java.text.NumberFormat;
import java.util.Locale;

import model.EfetuarSorteio;
import gui.Sorteio;

public class FormatacaoDocumento {
	public static NumberFormat mf = NumberFormat.getInstance(Locale.GERMAN);
	public static String montaCabe�alhoArquivo(){
		String cabecalho =  "Secretaria da Fazenda do Estado de Goi�s\r\n"
				+ "====================================================\r\n\r\n"
				+ "Vers�o do Programa: "
				+ Sorteio.Versao
				+ "\r\n\r\n"
				+ "Sorteio de Bilhetes\r\n"
				+ "-------------------\r\n"
				+ "Loteria Federal Extração nº "
				+ EfetuarSorteio.en_Extracao
				+ " de "
				+ EfetuarSorteio.en_DataExtracao
				+ "\r\n"
				+ "1º Premio: "
				+ EfetuarSorteio.en_Premio1
				+ "\r\n"
				+ "2º Premio: "
				+ EfetuarSorteio.en_Premio2
				+ "\r\n"
				+ "3º Premio: "
				+ EfetuarSorteio.en_Premio3
				+ "\r\n"
				+ "4º Premio: "
				+ EfetuarSorteio.en_Premio4
				+ "\r\n"
				+ "5º Premio: "
				+ EfetuarSorteio.en_Premio5
				+ "\r\n\r\n"
				+ "Sorteio nº "
				+ EfetuarSorteio.en_SorteioNumero
				+ "\r\n";

		cabecalho += "Data do Sorteio:         " + EfetuarSorteio.en_DataSorteio + "\r\n"
				+ "Data no Di�rio Oficial:  " + EfetuarSorteio.en_DataDiarioOficial + "\r\n";
		cabecalho += String.format("N�mero de Bilhetes:     %11s\r\n",
				EfetuarSorteio.en_TotalBilhetes);
		cabecalho += String.format("N�mero de Premios:      %11s\r\n",
				EfetuarSorteio.en_TotalPremios);
		cabecalho += "\r\nRela��o dos Bilhetes Premiados:\r\n"
				+ "-------------------------------\r\n"
				+ "  Itera��o      Premio     Bilhete  Observa��o\r\n"
				+ "==========  ==========  ==========  =====================================\r\n";
		return cabecalho;
		}
	
	public static String montaResultado(int vezes, int premios,int ri){
		String resultado = "";
		resultado = String.format("%10s  %10s %11s  %s\r\n",
				mf.format(EfetuarSorteio.Vezes),
				mf.format(EfetuarSorteio.Premios), mf.format(ri), "");
		return resultado;
		}
	
	public static String montaCabecalhoFaixas(){
		
		String Resultado = "\r\nAVALIA��O DO SORTEIO\r\n"
				+ "====================\r\n" + "N�mero de Faixas de Bilhetes: "
				+ EfetuarSorteio.sai_ValorNumeroFaixas + "\r\n";

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
			EfetuarSorteio.sai_ValorEsperado);
	Resultado += String.format("     Valor M�ximo Obtido:        %10s\r\n",
			EfetuarSorteio.sai_ValorMaximo);
	Resultado += String.format("     Valor M�nimo Obtido:        %10s\r\n",
			EfetuarSorteio.sai_ValorMinimo);
	Resultado += String.format(
			"     Diferen�as: M�ximo - M�nimo: %10s\r\n",
			EfetuarSorteio.sai_ValorDiferencaMaxMin);
	Resultado += String.format("     Valor M�dio Obtido:         %10s\r\n",
			EfetuarSorteio.sai_ValorMedio);
	Resultado += String.format("     Desvio Padr�o:              %10s\r\n",
			EfetuarSorteio.sai_ValorDesvioPadrao);
	Resultado += String.format("     N�mero de Itera��es:        %10s\r\n",
			FormatacaoDocumento.mf.format(EfetuarSorteio.Vezes - 1));
	Resultado += "-----------------------------------------------------------------\r\n";
	return Resultado;
	}
	
	
}
