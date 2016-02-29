import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.Random;
import java.util.SortedMap;

import model.EfetuarSorteio;

public class RefatoraEfetuarSorteio {
	//RefatorarEfetuarSorteio � nome temporario, iremos refatorar o codigo aqui e depois refatorar essa classe e criar os testes em cima dela!
	public boolean verificaHash(int numPremios) {
		if (numPremios > 1000000) {
			// Se o número de prêmios é superior a 1 milhão então utiliza o
			// método DIRETO
			// de guardar o número de cada bilhete premiado.
			// Este método consome mais memória para o programa armazenar os
			// bilhetes
			// premiados, mas a execução é mais rápida.
			return false;
		} else {
			// Se o número de prêmios é inferior a 1 milhão então utiliza o
			// método HASH
			// de guardar o número de cada bilhete premiado.
			// Este método consome menos memória para o programa armazenar os
			// bilhetes
			// premiados, mas a execução é mais lenta.
			return true;
		}
	}

	public int[] determinaQntValores(boolean metodoHash, int nBilhetes) {

		if (!metodoHash) {
			// M�todo DIRETO.
			return new int[nBilhetes + 1];
		} else {
			// M�todo HASH.
			return new int[1];
		}

	}

	

	public void imprimeResultado(OutputStreamWriter outP, String resultado) {
		PrintWriter out = new PrintWriter(new BufferedWriter(outP));
		out.print(resultado);
	}

	public void sorteioDosBilhetes(PrintWriter out, Random rg, int numPremios,
			int nBilhetes, SortedMap<Integer, Integer> bilhetesSorteados,
			NumberFormat mf, int[] valores) {
		String resultado = "";
		for (EfetuarSorteio.Premios = 1; EfetuarSorteio.Premios <= numPremios; EfetuarSorteio.Vezes++) {
			if (EfetuarSorteio.PararSorteio) {
				EfetuarSorteio.MsgErro = "A opera��o foi cancelada pelo operador!";
				EfetuarSorteio.EmExecucao = false;
				return;
			}
			int ri = rg.nextInt(nBilhetes) + 1;

			if (verificaHash(numPremios)) {
				// Método HASH
				if (!bilhetesSorteados.containsKey(ri)) {
					bilhetesSorteados.put(ri, EfetuarSorteio.Vezes);
				} else {
					resultado = String.format(
							"%10s  %10s %11s  %s\r\n",
							mf.format(EfetuarSorteio.Vezes),
							"-",
							mf.format(ri),
							"Bilhete j� foi sorteado na itera��o "
									+ mf.format(bilhetesSorteados.get(ri)));
					out.print(resultado);
					continue;
				}
			} else {
				// Método DIRETO.
				if (valores[ri] > 0) {
					resultado = String.format(
							"%10s  %10s %11s  %s\r\n",
							mf.format(EfetuarSorteio.Vezes),
							"-",
							mf.format(ri),
							"Bilhete j� foi sorteado na itera��o "
									+ mf.format(valores[ri]));
					out.print(resultado);
					continue;
				}
				valores[ri] = EfetuarSorteio.Vezes;
			}
			resultado = String.format("%10s  %10s %11s  %s\r\n",
					mf.format(EfetuarSorteio.Vezes), mf.format(EfetuarSorteio.Premios), mf.format(ri), "");
			EfetuarSorteio.Premios++;
			out.print(resultado);
		}

	}

}
