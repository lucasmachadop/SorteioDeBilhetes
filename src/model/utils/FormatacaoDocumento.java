package model.utils;

import java.text.NumberFormat;
import java.util.Locale;

import model.EfetuarSorteio;
import gui.Sorteio;

public class FormatacaoDocumento {
	public static NumberFormat mf = NumberFormat.getInstance(Locale.GERMAN);
	public static String montaCabeçalhoArquivo(){
		String cabecalho =  "Secretaria da Fazenda do Estado de Goiás\r\n"
				+ "====================================================\r\n\r\n"
				+ "Versão do Programa: "
				+ Sorteio.Versao
				+ "\r\n\r\n"
				+ "Sorteio de Bilhetes\r\n"
				+ "-------------------\r\n"
				+ "Loteria Federal ExtraÃ§Ã£o nÂº "
				+ EfetuarSorteio.en_Extracao
				+ " de "
				+ EfetuarSorteio.en_DataExtracao
				+ "\r\n"
				+ "1Âº Premio: "
				+ EfetuarSorteio.en_Premio1
				+ "\r\n"
				+ "2Âº Premio: "
				+ EfetuarSorteio.en_Premio2
				+ "\r\n"
				+ "3Âº Premio: "
				+ EfetuarSorteio.en_Premio3
				+ "\r\n"
				+ "4Âº Premio: "
				+ EfetuarSorteio.en_Premio4
				+ "\r\n"
				+ "5Âº Premio: "
				+ EfetuarSorteio.en_Premio5
				+ "\r\n\r\n"
				+ "Sorteio nÂº "
				+ EfetuarSorteio.en_SorteioNumero
				+ "\r\n";

		cabecalho += "Data do Sorteio:         " + EfetuarSorteio.en_DataSorteio + "\r\n"
				+ "Data no Diário Oficial:  " + EfetuarSorteio.en_DataDiarioOficial + "\r\n";
		cabecalho += String.format("Número de Bilhetes:     %11s\r\n",
				EfetuarSorteio.en_TotalBilhetes);
		cabecalho += String.format("Número de Premios:      %11s\r\n",
				EfetuarSorteio.en_TotalPremios);
		cabecalho += "\r\nRelação dos Bilhetes Premiados:\r\n"
				+ "-------------------------------\r\n"
				+ "  Iteração      Premio     Bilhete  Observação\r\n"
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
		
		String Resultado = "\r\nAVALIAÇÃO DO SORTEIO\r\n"
				+ "====================\r\n" + "Número de Faixas de Bilhetes: "
				+ EfetuarSorteio.sai_ValorNumeroFaixas + "\r\n";

		// Obter os valores Máximo e Mínimo de Prêmios por faixa de valores de
		// bilhetes.
		Resultado += "\r\nRelação das Faixas de Bilhetes:\r\n"
				+ "-------------------------------\r\n"
				+ "     Faixa  Bilhete Inicial  Bilhete Final  Prêmios na Faixa\r\n"
				+ "==========  ===============  =============  ================\r\n";
		return Resultado;
	}
	
	public static String montaEstatisticas(){
	 String Resultado = "\r\nEstatisticas do Número de Prêmios Por Faixa de Bilhetes\r\n"
			+ "=======================================================\r\n";
	Resultado += String.format("     Valor Médio Calculado:      %10s\r\n",
			EfetuarSorteio.sai_ValorEsperado);
	Resultado += String.format("     Valor Máximo Obtido:        %10s\r\n",
			EfetuarSorteio.sai_ValorMaximo);
	Resultado += String.format("     Valor Mínimo Obtido:        %10s\r\n",
			EfetuarSorteio.sai_ValorMinimo);
	Resultado += String.format(
			"     Diferenças: Máximo - Mínimo: %10s\r\n",
			EfetuarSorteio.sai_ValorDiferencaMaxMin);
	Resultado += String.format("     Valor Médio Obtido:         %10s\r\n",
			EfetuarSorteio.sai_ValorMedio);
	Resultado += String.format("     Desvio Padrão:              %10s\r\n",
			EfetuarSorteio.sai_ValorDesvioPadrao);
	Resultado += String.format("     Número de Iterações:        %10s\r\n",
			FormatacaoDocumento.mf.format(EfetuarSorteio.Vezes - 1));
	Resultado += "-----------------------------------------------------------------\r\n";
	return Resultado;
	}
	
	
}
