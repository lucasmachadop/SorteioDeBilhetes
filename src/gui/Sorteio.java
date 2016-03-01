package gui;
import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.MaskFormatter;
import javax.swing.SwingConstants;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.awt.event.WindowAdapter;
import java.io.File;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;

import model.EfetuarSorteio;

public class Sorteio extends JFrame {
	public static String versao = "0.1";

	public static int numDeFaixas = 100;
	public static boolean programaAferido = false;

	// Valores da Extração n° 04957 de 28/03/2015 da Loteria Federal


	private String afrPremio1 = "63.487";
	private String afrPremio2 = "65.079";
	private String afrPremio3 = "43.554";
	private String afrPremio4 = "91.016";
	private String afrPremio5 = "76.226";
	private String afrDataExtracao = "28/03/2015";
	private String afrExtracao = "04957";
	// Valores utilizados para aferição deste programa
	private String afrSorteio = "00001";
	private String afrDataSorteio = "29/03/2015";
	private String afrDataDiarioOficial = "30/03/2015";
	private String afrBilhetes = "10.000.000";
	private String afrPremios = "1.000.000";

	static Sorteio frame;
	private JFormattedTextField textExtracao;
	private JFormattedTextField textDataExtracao;
	private JFormattedTextField textPremio1;
	private JFormattedTextField textPremio2;
	private JFormattedTextField textPremio3;
	private JFormattedTextField textPremio4;
	private JFormattedTextField textPremio5;
	private JFormattedTextField textSorteioNumero;
	private JFormattedTextField textDataSorteio;
	private JFormattedTextField textDataDiarioOficial;
	private JFormattedTextField textTotalBilhetes;
	private JFormattedTextField textTotalPremios;
	private JLabel lblValorNumeroFaixas;
	private JLabel lblValorEsperado;
	private JLabel lblValorMaximo;
	private JLabel lblValorMinimo;
	private JLabel lblValorDiferencaMaxMin;
	private JLabel lblValorMedio;
	private JLabel lblValorDesvioPadrao;
	private JLabel lblNumeroIteracoes;
	private JLabel lblNumeroPremios;
	private JLabel lblHashResultado;
	private JLabel lblValorTempoTranscorrido;
	private JLabel lblValorIteracoesPorSegundo;
	private JLabel lblProgramaAferido;
	private JLabel lblErroExtracao;
	private JLabel lblErroDataExtracao;
	private JLabel lblErroPremio1;
	private JLabel lblErroPremio2;
	private JLabel lblErroPremio3;
	private JLabel lblErroPremio4;
	private JLabel lblErroPremio5;
	private JLabel lblErroSorteioNumero;
	private JLabel lblErroDataSorteio;
	private JLabel lblErroDataDiarioOficial;
	private JLabel lblErroTotalBilhetes;
	private JLabel lblErroTotalPremios;
	private JButton btnAferir;
	private JButton btnApagar;
	private JButton btnSortear;
	private JButton btnCancelar;
	private JButton btnAbrir;
	private JLabel lblTituloHashResultado;
	private JCheckBox chckbxUmaLinha;
	private JLabel lblArquivoImportacaoExtracoes;

	private Timer timerSorteio;
	private NumberFormat formatoNumero = NumberFormat.getNumberInstance(Locale.GERMAN);
	private long tempoInicial, deltaAnterior = 0;
	private String nomeArquivoSaida;
	private ArrayList<String> arquivosTxt = new ArrayList<String>();

	static int proximaExtracao;

	/**
	 * Create the panel.
	 * @throws ParseException
	 */
	public Sorteio() throws ParseException {
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				inicializarCampos();
				inicializarResultados();
				lblProgramaAferido.setText("");
			}
		});
		setTitle("Sorteio de Bilhetes - Secretaria da Fazenda do Estado de Goiás");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(636, 594));
		setLocationRelativeTo(null);

		JPanel panel_InfoLoteria = new JPanel();
		panel_InfoLoteria.setBounds(28, 10, 265, 217);
		panel_InfoLoteria.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Informações da Loteria Federal"),
				BorderFactory.createEmptyBorder(5,5,5,5)));

		JLabel lblNewLabel = new JLabel("Extração n°:");
		lblNewLabel.setBounds(25, 20, 93, 20);
		lblNewLabel.setForeground(new Color(0, 128, 0));
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		MaskFormatter mf = new MaskFormatter("#####");
		mf.setPlaceholder("00000");
		mf.setPlaceholderCharacter('0');

		textExtracao = new JFormattedTextField(mf);
		textExtracao.setBounds(120, 20, 99, 20);
		textExtracao.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Realizado em:");
		lblNewLabel_1.setBounds(22, 47, 96, 20);
		lblNewLabel_1.setForeground(new Color(0, 128, 0));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);

		mf = new MaskFormatter("##/##/####");
		mf.setPlaceholderCharacter('0');
		textDataExtracao = new JFormattedTextField(mf);
		textDataExtracao.setBounds(120, 47, 99, 20);
		textDataExtracao.setColumns(10);

		JLabel lblPremio1 = new JLabel("1º Prêmio:");
		lblPremio1.setBounds(22, 74, 96, 20);
		lblPremio1.setForeground(new Color(0, 128, 0));
		lblPremio1.setHorizontalAlignment(SwingConstants.RIGHT);

		mf = new MaskFormatter("##.###");
		mf.setPlaceholder("00.000");
		mf.setPlaceholderCharacter('0');
		textPremio1 = new JFormattedTextField(mf);
		textPremio1.setBounds(120, 74, 99, 20);
		textPremio1.setColumns(10);

		JLabel lblPremio2 = new JLabel("2º Prêmio:");
		lblPremio2.setBounds(22, 101, 96, 20);
		lblPremio2.setForeground(new Color(0, 128, 0));
		lblPremio2.setHorizontalAlignment(SwingConstants.RIGHT);

		mf = new MaskFormatter("##.###");
		mf.setPlaceholder("00.000");
		mf.setPlaceholderCharacter('0');
		textPremio2 = new JFormattedTextField(mf);
		textPremio2.setBounds(120, 101, 99, 20);
		textPremio2.setColumns(10);

		JLabel lblPremio3 = new JLabel("3º Prêmio:");
		lblPremio3.setBounds(22, 128, 96, 20);
		lblPremio3.setForeground(new Color(0, 128, 0));
		lblPremio3.setHorizontalAlignment(SwingConstants.RIGHT);

		mf = new MaskFormatter("##.###");
		mf.setPlaceholder("00.000");
		mf.setPlaceholderCharacter('0');
		textPremio3 = new JFormattedTextField(mf);
		textPremio3.setBounds(120, 128, 99, 20);
		textPremio3.setColumns(10);

		JLabel lblPremio4 = new JLabel("4º Prêmio:");
		lblPremio4.setBounds(22, 155, 96, 20);
		lblPremio4.setForeground(new Color(0, 128, 0));
		lblPremio4.setHorizontalAlignment(SwingConstants.RIGHT);

		mf = new MaskFormatter("##.###");
		mf.setPlaceholder("00.000");
		mf.setPlaceholderCharacter('0');
		textPremio4 = new JFormattedTextField(mf);
		textPremio4.setBounds(120, 155, 99, 20);
		textPremio4.setColumns(10);

		JLabel lblPremio5 = new JLabel("5º Prêmio:");
		lblPremio5.setBounds(22, 182, 96, 20);
		lblPremio5.setForeground(new Color(0, 128, 0));
		lblPremio5.setHorizontalAlignment(SwingConstants.RIGHT);

		mf = new MaskFormatter("##.###");
		mf.setPlaceholder("00.000");
		mf.setPlaceholderCharacter('0');
		textPremio5 = new JFormattedTextField(mf);
		textPremio5.setBounds(120, 182, 99, 20);
		textPremio5.setColumns(10);

		lblErroExtracao = new JLabel("*");
		lblErroExtracao.setBounds(223, 20, 15, 20);
		lblErroExtracao.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblErroExtracao.setForeground(new Color(255, 0, 0));

		lblErroDataExtracao = new JLabel("*");
		lblErroDataExtracao.setBounds(223, 47, 15, 20);
		lblErroDataExtracao.setForeground(Color.RED);
		lblErroDataExtracao.setFont(new Font("Tahoma", Font.BOLD, 11));

		lblErroPremio1 = new JLabel("*");
		lblErroPremio1.setBounds(223, 74, 15, 20);
		lblErroPremio1.setForeground(Color.RED);
		lblErroPremio1.setFont(new Font("Tahoma", Font.BOLD, 11));

		lblErroPremio2 = new JLabel("*");
		lblErroPremio2.setBounds(223, 101, 15, 20);
		lblErroPremio2.setForeground(Color.RED);
		lblErroPremio2.setFont(new Font("Tahoma", Font.BOLD, 11));

		lblErroPremio3 = new JLabel("*");
		lblErroPremio3.setBounds(223, 128, 15, 20);
		lblErroPremio3.setForeground(Color.RED);
		lblErroPremio3.setFont(new Font("Tahoma", Font.BOLD, 11));

		lblErroPremio4 = new JLabel("*");
		lblErroPremio4.setBounds(223, 155, 15, 20);
		lblErroPremio4.setForeground(Color.RED);
		lblErroPremio4.setFont(new Font("Tahoma", Font.BOLD, 11));

		lblErroPremio5 = new JLabel("*");
		lblErroPremio5.setBounds(223, 182, 15, 20);
		lblErroPremio5.setForeground(Color.RED);
		lblErroPremio5.setFont(new Font("Tahoma", Font.BOLD, 11));

		JPanel panel_Sorteio = new JPanel();
		panel_Sorteio.setBounds(323, 10, 277, 217);

		TitledBorder border = BorderFactory.createTitledBorder("Informações do Sorteio");
		panel_Sorteio.setBorder(BorderFactory.createCompoundBorder(
				border,
				BorderFactory.createEmptyBorder(5,5,5,5)));

		JLabel lblNewLabel_2 = new JLabel("Sorteio n°:");
		lblNewLabel_2.setBounds(78, 20, 72, 20);
		lblNewLabel_2.setForeground(new Color(0, 128, 0));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);

		mf = new MaskFormatter("#####");
		mf.setPlaceholder("00000");
		mf.setPlaceholderCharacter('0');

		textSorteioNumero = new JFormattedTextField(mf);
		textSorteioNumero.setBounds(151, 20, 99, 20);
		textSorteioNumero.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("Data do Sorteio:");
		lblNewLabel_3.setBounds(10, 90, 140, 20);
		lblNewLabel_3.setForeground(new Color(0, 128, 0));
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.RIGHT);

		mf = new MaskFormatter("##/##/####");
		mf.setPlaceholderCharacter('0');
		textDataSorteio = new JFormattedTextField(mf);
		textDataSorteio.setBounds(151, 90, 99, 20);
		textDataSorteio.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("Data no Diário Oficial:");
		lblNewLabel_4.setBounds(10, 123, 140, 20);
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.RIGHT);

		mf = new MaskFormatter("##/##/####");
		mf.setPlaceholderCharacter('0');
		textDataDiarioOficial = new JFormattedTextField(mf);
		textDataDiarioOficial.setBounds(151, 123, 99, 20);
		textDataDiarioOficial.setColumns(10);

		JLabel lblNewLabel_5 = new JLabel("número de Bilhetes:");
		lblNewLabel_5.setBounds(10, 156, 140, 20);
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.RIGHT);

		textTotalBilhetes = new JFormattedTextField(formatoNumero);
		textTotalBilhetes.setBounds(151, 156, 99, 20);
		textTotalBilhetes.setColumns(10);

		JLabel lblNewLabel_6 = new JLabel("número de prêmios:");
		lblNewLabel_6.setBounds(10, 188, 140, 20);
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.RIGHT);

		textTotalPremios = new JFormattedTextField(formatoNumero);
		textTotalPremios.setBounds(151, 188, 99, 20);
		textTotalPremios.setColumns(10);

		lblErroSorteioNumero = new JLabel("*");
		lblErroSorteioNumero.setBounds(254, 20, 15, 20);
		lblErroSorteioNumero.setForeground(Color.RED);
		lblErroSorteioNumero.setFont(new Font("Tahoma", Font.BOLD, 11));

		lblErroDataSorteio = new JLabel("*");
		lblErroDataSorteio.setBounds(254, 89, 15, 20);
		lblErroDataSorteio.setForeground(Color.RED);
		lblErroDataSorteio.setFont(new Font("Tahoma", Font.BOLD, 11));

		lblErroDataDiarioOficial = new JLabel("*");
		lblErroDataDiarioOficial.setBounds(254, 123, 15, 20);
		lblErroDataDiarioOficial.setForeground(Color.RED);
		lblErroDataDiarioOficial.setFont(new Font("Tahoma", Font.BOLD, 11));

		lblErroTotalBilhetes = new JLabel("*");
		lblErroTotalBilhetes.setBounds(254, 156, 15, 20);
		lblErroTotalBilhetes.setForeground(Color.RED);
		lblErroTotalBilhetes.setFont(new Font("Tahoma", Font.BOLD, 11));

		lblErroTotalPremios = new JLabel("*");
		lblErroTotalPremios.setBounds(254, 188, 15, 20);
		lblErroTotalPremios.setForeground(Color.RED);
		lblErroTotalPremios.setFont(new Font("Tahoma", Font.BOLD, 11));

		btnCancelar = new JButton("Encerrar");
		btnCancelar.setToolTipText("Encerrar a execução do programa.");
		btnCancelar.setBounds(486, 522, 75, 26);
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (EfetuarSorteio.isRunning()) {
					EfetuarSorteio.pararSorteio();
					inicializarCampos();
					inicializarResultados();
				} else {
					// Programa está parado. Encerrar o programa.
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				}
			}
		});


//		btnAbrir = new JButton("Abrir");
//		btnSortear.setToolTipText("Abrir arquivo com resultados gerados pela execução.");
//		btnAbrir.setBounds(new Rectangle(60, 49, 166, 69));
//		btnAbrir.setFont(new Font("Dialog", Font.BOLD, 24));
//		btnAbrir.setText("Abrir");
//		btnAbrir.addActionListener(new java.awt.event.ActionListener() {
//			public void actionPerformed(java.awt.event.ActionEvent e) {
//				abrir();
//			}
//		});



		btnSortear = new JButton("Executar");
		btnSortear.setToolTipText("Executar o programa de sorteio dos bilhetes.");
		btnSortear.setBounds(400, 522, 75, 26);
		btnSortear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (EfetuarSorteio.isRunning()) {
					JOptionPane.showMessageDialog(null, "O programa já está efetuando o sorteio. Aguarde!", "Sorteio", JOptionPane.WARNING_MESSAGE);
					return;
				}
				efetuarUmSorteio();
			}
		});

		JPanel panelResultados = new JPanel();
		panelResultados.setBounds(74, 227, 489, 290);

		panelResultados.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Resultados do Sorteio"),
				BorderFactory.createEmptyBorder(5,5,5,5)));

		JLabel lblTituloNumeroFaixas = new JLabel("número de Faixas de Bilhetes:");
		lblTituloNumeroFaixas.setBounds(60, 21, 201, 16);
		lblTituloNumeroFaixas.setHorizontalAlignment(SwingConstants.RIGHT);

		lblValorNumeroFaixas = new JLabel("100");
		lblValorNumeroFaixas.setBounds(265, 21, 88, 16);
		lblValorNumeroFaixas.setForeground(Color.BLUE);
		lblValorNumeroFaixas.setHorizontalAlignment(SwingConstants.RIGHT);

		JPanel panelBilhetesPorFaixa = new JPanel();
		panelBilhetesPorFaixa.setBounds(35, 48, 407, 144);

		panelBilhetesPorFaixa.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createTitledBorder("Estatísticas do número de prêmios Por Faixa de Bilhetes"),
				BorderFactory.createEmptyBorder(5,5,5,5)));

		JLabel lblTituloValorEsperado = new JLabel("Valor Médio Teórico:");
		lblTituloValorEsperado.setBounds(25, 20, 201, 16);
		lblTituloValorEsperado.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel lblTituloValorMaximo = new JLabel("Valor Máximo Obtido:");
		lblTituloValorMaximo.setBounds(25, 40, 201, 16);
		lblTituloValorMaximo.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel lblTituloValorMinimo = new JLabel("Valor Mínimo Obtido:");
		lblTituloValorMinimo.setBounds(25, 60, 201, 16);
		lblTituloValorMinimo.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel lblTituloDiferencaMaxMin = new JLabel("Diferença: Máximo - Mínimo:");
		lblTituloDiferencaMaxMin.setBounds(25, 80, 201, 16);
		lblTituloDiferencaMaxMin.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel lblTituloValorMedio = new JLabel("Valor Médio Obtido:");
		lblTituloValorMedio.setBounds(25, 100, 201, 16);
		lblTituloValorMedio.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel lblTituloDesvioPadrao = new JLabel("Desvio Padrão:");
		lblTituloDesvioPadrao.setBounds(25, 120, 201, 16);
		lblTituloDesvioPadrao.setHorizontalAlignment(SwingConstants.RIGHT);

		lblValorEsperado = new JLabel("10.000");
		lblValorEsperado.setBounds(232, 20, 88, 16);
		lblValorEsperado.setForeground(Color.BLUE);
		lblValorEsperado.setHorizontalAlignment(SwingConstants.RIGHT);

		lblValorMaximo = new JLabel("10.100");
		lblValorMaximo.setBounds(232, 40, 88, 16);
		lblValorMaximo.setForeground(Color.BLUE);
		lblValorMaximo.setHorizontalAlignment(SwingConstants.RIGHT);

		lblValorMinimo = new JLabel("9.900");
		lblValorMinimo.setBounds(232, 60, 88, 16);
		lblValorMinimo.setForeground(Color.BLUE);
		lblValorMinimo.setHorizontalAlignment(SwingConstants.RIGHT);

		lblValorDiferencaMaxMin = new JLabel("200");
		lblValorDiferencaMaxMin.setBounds(232, 80, 88, 16);
		lblValorDiferencaMaxMin.setForeground(Color.BLUE);
		lblValorDiferencaMaxMin.setHorizontalAlignment(SwingConstants.RIGHT);

		lblValorMedio = new JLabel("9.950");
		lblValorMedio.setBounds(232, 100, 88, 16);
		lblValorMedio.setForeground(Color.BLUE);
		lblValorMedio.setHorizontalAlignment(SwingConstants.RIGHT);

		lblValorDesvioPadrao = new JLabel("99,3");
		lblValorDesvioPadrao.setBounds(232, 120, 88, 16);
		lblValorDesvioPadrao.setForeground(Color.BLUE);
		lblValorDesvioPadrao.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel lblTituloNumeroIteracoes = new JLabel("número de Iterações e de prêmios:");
		lblTituloNumeroIteracoes.setBounds(60, 200, 201, 16);
		lblTituloNumeroIteracoes.setHorizontalAlignment(SwingConstants.RIGHT);

		lblNumeroIteracoes = new JLabel("102.200.100");
		lblNumeroIteracoes.setBounds(267, 200, 88, 16);
		lblNumeroIteracoes.setForeground(Color.BLUE);
		lblNumeroIteracoes.setHorizontalAlignment(SwingConstants.RIGHT);

		lblNumeroPremios = new JLabel("102.200.100");
		lblNumeroPremios.setBounds(354, 200, 88, 16);
		lblNumeroPremios.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNumeroPremios.setForeground(Color.BLUE);

		lblTituloHashResultado = new JLabel("HASH MD5 do Resultado:");
		lblTituloHashResultado.setBounds(10, 265, 179, 16);
		lblTituloHashResultado.setHorizontalAlignment(SwingConstants.RIGHT);

		lblHashResultado = new JLabel("804fb67f240f516e8ebcd6a0d68c0114");
		lblHashResultado.setFont(new Font("Courier New", Font.PLAIN, 13));
		lblHashResultado.setBounds(192, 265, 287, 16);
		lblHashResultado.setForeground(Color.BLUE);

		JLabel lblTituloTempoTranscorrido = new JLabel("Tempo Transcorrido (segundos):");
		lblTituloTempoTranscorrido.setBounds(60, 220, 201, 16);
		lblTituloTempoTranscorrido.setHorizontalAlignment(SwingConstants.RIGHT);

		lblValorTempoTranscorrido = new JLabel("102.200.100");
		lblValorTempoTranscorrido.setBounds(267, 220, 88, 16);
		lblValorTempoTranscorrido.setHorizontalAlignment(SwingConstants.RIGHT);
		lblValorTempoTranscorrido.setForeground(Color.BLUE);

		JLabel lblTituloPremiosPorSegundo = new JLabel("Iterações / segundo:");
		lblTituloPremiosPorSegundo.setBounds(60, 240, 201, 16);
		lblTituloPremiosPorSegundo.setHorizontalAlignment(SwingConstants.RIGHT);

		lblValorIteracoesPorSegundo = new JLabel("102.200.100");
		lblValorIteracoesPorSegundo.setBounds(267, 240, 88, 16);
		lblValorIteracoesPorSegundo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblValorIteracoesPorSegundo.setForeground(Color.BLUE);

		timerSorteio = new Timer(100, (ActionListener) timerSorteio);
		timerSorteio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				iniciarTimer();
			}
		});
		timerSorteio.stop();

		btnAferir = new JButton("Aferir");
		btnAferir.setBounds(72, 522, 75, 26);
		btnAferir.setToolTipText("Efetuar a aferição do funcionamento do programa.");
		btnAferir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (EfetuarSorteio.isRunning()) {
					JOptionPane.showMessageDialog(null, "O programa já está efetuando a aferição. Aguarde!", "aferição do Programa", JOptionPane.WARNING_MESSAGE);
					return;
				}
				programaAferido = true;
				inicializarResultados();
				carregarValoresPadrao();
				habilitarCampos(false);
				lblProgramaAferido.setText("");
				if (!VerificarErrosCampos()) {
					tempoInicial =(new Date()).getTime();
					new EfetuarSorteio("Aferir", textExtracao.getText(), textDataExtracao.getText(),
							textPremio1.getText(), textPremio2.getText(), textPremio3.getText(),
							textPremio4.getText(), textPremio5.getText(),
							textSorteioNumero.getText(), textDataSorteio.getText(),
							textDataDiarioOficial.getText(), textTotalBilhetes.getText(),
							textTotalPremios.getText(), "").start();
					timerSorteio.start();
				}
			}
		});

		lblProgramaAferido = new JLabel("Programa aferido");
		lblProgramaAferido.setBounds(70, 551, 212, 14);
		lblProgramaAferido.setForeground(Color.BLUE);

		btnApagar = new JButton("Limpar");
		btnApagar.setToolTipText("Limpar os campos digitados.");
		btnApagar.setBounds(158, 522, 75, 26);
		btnApagar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				inicializarCampos();
				inicializarResultados();
			}
		});
		getContentPane().setLayout(null);
		panel_InfoLoteria.setLayout(null);
		panel_InfoLoteria.add(lblNewLabel);
		panel_InfoLoteria.add(textExtracao);
		panel_InfoLoteria.add(lblErroExtracao);
		panel_InfoLoteria.add(lblNewLabel_1);
		panel_InfoLoteria.add(textDataExtracao);
		panel_InfoLoteria.add(lblErroDataExtracao);
		panel_InfoLoteria.add(lblPremio1);
		panel_InfoLoteria.add(textPremio1);
		panel_InfoLoteria.add(lblErroPremio1);
		panel_InfoLoteria.add(lblPremio2);
		panel_InfoLoteria.add(textPremio2);
		panel_InfoLoteria.add(lblErroPremio2);
		panel_InfoLoteria.add(lblPremio3);
		panel_InfoLoteria.add(textPremio3);
		panel_InfoLoteria.add(lblErroPremio3);
		panel_InfoLoteria.add(lblPremio4);
		panel_InfoLoteria.add(textPremio4);
		panel_InfoLoteria.add(lblErroPremio4);
		panel_InfoLoteria.add(lblPremio5);
		panel_InfoLoteria.add(textPremio5);
		panel_InfoLoteria.add(lblErroPremio5);
		getContentPane().add(panel_InfoLoteria);
		getContentPane().add(panel_Sorteio);
		panel_Sorteio.setLayout(null);
		panel_Sorteio.add(lblNewLabel_2);
		panel_Sorteio.add(textSorteioNumero);
		panel_Sorteio.add(lblErroSorteioNumero);
		panel_Sorteio.add(lblNewLabel_3);
		panel_Sorteio.add(textDataSorteio);
		panel_Sorteio.add(lblErroDataSorteio);
		panel_Sorteio.add(lblNewLabel_4);
		panel_Sorteio.add(textDataDiarioOficial);
		panel_Sorteio.add(lblErroDataDiarioOficial);
		panel_Sorteio.add(lblNewLabel_5);
		panel_Sorteio.add(textTotalBilhetes);
		panel_Sorteio.add(lblErroTotalBilhetes);
		panel_Sorteio.add(lblNewLabel_6);
		panel_Sorteio.add(textTotalPremios);
		panel_Sorteio.add(lblErroTotalPremios);

		lblArquivoImportacaoExtracoes = new JLabel("FulanoDeTalPasso [linha 1 de 10]");
		lblArquivoImportacaoExtracoes.setForeground(Color.BLUE);
		lblArquivoImportacaoExtracoes.setToolTipText("Nome do arquivo de importação das extrações.");
		lblArquivoImportacaoExtracoes.setHorizontalAlignment(SwingConstants.RIGHT);
		lblArquivoImportacaoExtracoes.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblArquivoImportacaoExtracoes.setBounds(10, 70, 240, 14);
		lblArquivoImportacaoExtracoes.setVisible(false);
		panel_Sorteio.add(lblArquivoImportacaoExtracoes);

		getContentPane().add(panelResultados);
		panelResultados.setLayout(null);
		panelResultados.add(lblTituloNumeroFaixas);
		panelResultados.add(lblValorNumeroFaixas);
		panelResultados.add(panelBilhetesPorFaixa);
		panelBilhetesPorFaixa.setLayout(null);
		panelBilhetesPorFaixa.add(lblTituloValorEsperado);
		panelBilhetesPorFaixa.add(lblValorEsperado);
		panelBilhetesPorFaixa.add(lblTituloValorMaximo);
		panelBilhetesPorFaixa.add(lblValorMaximo);
		panelBilhetesPorFaixa.add(lblTituloValorMinimo);
		panelBilhetesPorFaixa.add(lblValorMinimo);
		panelBilhetesPorFaixa.add(lblTituloDiferencaMaxMin);
		panelBilhetesPorFaixa.add(lblValorDiferencaMaxMin);
		panelBilhetesPorFaixa.add(lblTituloValorMedio);
		panelBilhetesPorFaixa.add(lblValorMedio);
		panelBilhetesPorFaixa.add(lblTituloDesvioPadrao);
		panelBilhetesPorFaixa.add(lblValorDesvioPadrao);
		panelResultados.add(lblTituloNumeroIteracoes);
		panelResultados.add(lblNumeroIteracoes);
		panelResultados.add(lblTituloTempoTranscorrido);
		panelResultados.add(lblValorTempoTranscorrido);
		panelResultados.add(lblTituloPremiosPorSegundo);
		panelResultados.add(lblValorIteracoesPorSegundo);
		panelResultados.add(lblTituloHashResultado);
		panelResultados.add(lblHashResultado);

		panelResultados.add(lblNumeroPremios);
		getContentPane().add(btnAferir);
		getContentPane().add(btnApagar);
		getContentPane().add(btnSortear);
		getContentPane().add(btnCancelar);
		getContentPane().add(lblProgramaAferido);

		JLabel lblVersao = new JLabel("Versão do Programa: " + versao);
		lblVersao.setToolTipText("Clique aqui para obter mais Informações.");
		lblVersao.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(null, "Versão do Programa de Sorteio: " + versao + "\r\n\r\n" +
						"MÃ¡quina Virtual JAVA\r\n" +
						"Versão: " + System.getProperty("java.version") + " - " +
						System.getProperty("java.vendor") + "\r\n" +
						"Local: " + System.getProperty("java.home") + "\r\n" +
						"MemÃ³ria Ocupada: " + Runtime.getRuntime().totalMemory() / (1024 * 1024) + " Mbytes\r\n" +
						"MemÃ³ria MÃ¡xima:  " + Runtime.getRuntime().maxMemory() / (1024 * 1024) + " Mbytes\r\n\r\n" +
						"Sistema Operacional: " + System.getProperty("os.name") + "\r\n" +
						"Arquitetura: " + System.getProperty("os.arch") + "\r\nVersão: " +
						System.getProperty("os.version") + "\r\n\r\n" +// "\r\n\r\n"+
						"Este programa é uma adaptaÃ§Ã£o do Programa de " + "\r\n" +
						"Sorteios de Bilhetes utilizado e disponibilizado " + "\r\n" +
						"publicamente pela Secretaria da Fazenda do Estado " + "\r\n" +
						"do Rio Grande do Sul." + "\r\n", "Informações Sobre o Programa", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		lblVersao.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVersao.setBounds(473, 551, 147, 14);
		getContentPane().add(lblVersao);

		chckbxUmaLinha = new JCheckBox("Uma linha");
		chckbxUmaLinha.setSelected(true);
		chckbxUmaLinha.setToolTipText("Executar uma linha do arquivo de importação de extrações.");
		chckbxUmaLinha.setBounds(319, 524, 75, 23);
		chckbxUmaLinha.setVisible(false);
		getContentPane().add(chckbxUmaLinha);
	}



	protected void efetuarUmSorteio() {
		programaAferido = false;
		inicializarResultados();
		if (!VerificarErrosCampos()) {
			String nomeArquivoTxt = "";
			String extensao = "";
			String[] vals= textDataSorteio.getText().split("/");
			Date dataLocal = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy-HHmmss");
			String data = sdf.format(dataLocal);
//			if ((TipoExtracao.TipoExtracaoEscolhida != TipoExtracao.ValoresTipoExtracao.IMPORTADA) || (ProximaExtracao == 0)) {
			if ((proximaExtracao == 0)) {
				JFileChooser fc = new JFileChooser();
				FileFilter ff = fc.getFileFilter();		// Salvando o filtro Padrão.
				fc.setAcceptAllFileFilterUsed(false);	// Apagando todos os filtros.
				fc.addChoosableFileFilter(new TextFileFilter());	// Adicionando o filtro para arquivos de extensÃ£o txt.
				fc.addChoosableFileFilter(ff);			// Adicionando o filtro Padrão.

				// Gerar o nome do arquivo.
				String codigo;
				String nome = "";
				codigo = "0000";
				extensao = "txt";
				nome = String.format("SorteioNFG_%s_%s_%s%s%s_%s.%s",
						textSorteioNumero.getText(), codigo, vals[0], vals[1], vals[2], data, extensao);
				fc.setSelectedFile(new File(nome));
				fc.setDialogTitle("Escolhendo o arquivo para salvar ...");
				int ret = fc.showSaveDialog(frame);
				if (ret != JFileChooser.APPROVE_OPTION) {
					return;
				}
				nomeArquivoSaida = fc.getSelectedFile().getAbsolutePath();
				if (!nomeArquivoSaida.substring(nomeArquivoSaida.length() - 4).toLowerCase().contains("." + extensao)) {
					// O nome do arquivo não contém a extensÃ£o correta.
					nomeArquivoSaida += "." + extensao;	// Colocar a extensÃ£o no nome do arquivo.
				}
				arquivosTxt.clear();
			}
			nomeArquivoTxt = nomeArquivoSaida;
			tempoInicial =(new Date()).getTime();
			habilitarCampos(false);
			new EfetuarSorteio("Sortear", textExtracao.getText(), textDataExtracao.getText(),
					textPremio1.getText(), textPremio2.getText(), textPremio3.getText(),
					textPremio4.getText(), textPremio5.getText(),
					textSorteioNumero.getText(), textDataSorteio.getText(),
					textDataDiarioOficial.getText(), textTotalBilhetes.getText(),
					textTotalPremios.getText(), nomeArquivoTxt).start();
			timerSorteio.start();
		}
	}

	protected boolean VerificarErrosCampos() {
		boolean erroEncontrado = false;
		CharSequence target = ".";
		CharSequence replacement = "";
		int numBilhetes = 0;
		int numPremios = 0;

		if (textExtracao.getText().contains(" ") || textExtracao.getText().equals("00000")) {
			lblErroExtracao.setVisible(true);
			erroEncontrado = true;
		} else {
			lblErroExtracao.setVisible(false);
		}
		if (textDataExtracao.getText().contains(" ") || textDataExtracao.getText().equals("00/00/0000")) {
			lblErroDataExtracao.setVisible(true);
			erroEncontrado = true;
		} else {
			lblErroDataExtracao.setVisible(false);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		@SuppressWarnings("unused")
		Date dt;
		try {
			dt = sdf.parse(textDataExtracao.getText());
		} catch (ParseException e) {
			lblErroDataExtracao.setVisible(true);
			erroEncontrado = true;
		}
		if (textPremio1.getText().contains(" ") || textPremio1.getText().equals("00.000")) {
			lblErroPremio1.setVisible(true);
			erroEncontrado = true;
		} else {
			lblErroPremio1.setVisible(false);
		}
		if (textPremio2.getText().contains(" ") || textPremio2.getText().equals("00.000")) {
			lblErroPremio2.setVisible(true);
			erroEncontrado = true;
		} else {
			lblErroPremio2.setVisible(false);
		}
		if (textPremio3.getText().contains(" ") || textPremio3.getText().equals("00.000")) {
			lblErroPremio3.setVisible(true);
			erroEncontrado = true;
		} else {
			lblErroPremio3.setVisible(false);
		}
		if (textPremio4.getText().contains(" ") || textPremio4.getText().equals("00.000")) {
			lblErroPremio4.setVisible(true);
			erroEncontrado = true;
		} else {
			lblErroPremio4.setVisible(false);
		}
		if (textPremio5.getText().contains(" ") || textPremio5.getText().equals("00.000")) {
			lblErroPremio5.setVisible(true);
			erroEncontrado = true;
		} else {
			lblErroPremio5.setVisible(false);
		}
		if (textSorteioNumero.getText().contains(" ") || (!programaAferido && textSorteioNumero.getText().equals("00000"))) {
			lblErroSorteioNumero.setVisible(true);
			erroEncontrado = true;
		} else {
			lblErroSorteioNumero.setVisible(false);
		}
		if (textDataSorteio.getText().contains(" ") || textDataSorteio.getText().equals("00/00/0000")) {
			lblErroDataSorteio.setVisible(true);
			erroEncontrado = true;
		} else {
			lblErroDataSorteio.setVisible(false);
		}
		try {
			dt = sdf.parse(textDataSorteio.getText());
		} catch (ParseException e) {
			lblErroDataSorteio.setVisible(true);
			erroEncontrado = true;
		}
		if (textDataDiarioOficial.getText().contains(" ") || textDataDiarioOficial.getText().equals("00/00/0000")) {
			lblErroDataDiarioOficial.setVisible(true);
			erroEncontrado = true;
		} else {
			lblErroDataDiarioOficial.setVisible(false);
		}
		try {
			dt = sdf.parse(textDataDiarioOficial.getText());
		} catch (ParseException e) {
			lblErroDataDiarioOficial.setVisible(true);
			erroEncontrado = true;
		}
		if (textTotalBilhetes.getText().length() == 0) {
			lblErroTotalBilhetes.setVisible(true);
			erroEncontrado = true;
		} else {
			String strBilhetes = textTotalBilhetes.getText().replace(target , replacement);
			numBilhetes = Integer.parseInt(strBilhetes);
			if (numBilhetes == 0) {
				lblErroTotalBilhetes.setVisible(true);
				erroEncontrado = true;
			} else {
				lblErroTotalBilhetes.setVisible(false);
			}
		}
		if (textTotalPremios.getText().length() == 0) {
			lblErroTotalPremios.setVisible(true);
			erroEncontrado = true;
		} else {
			String strPremios = textTotalPremios.getText().replace(target , replacement);
			numPremios = Integer.parseInt(strPremios);
			if (numPremios == 0) {
				lblErroTotalPremios.setVisible(true);
				erroEncontrado = true;
			} else {
				lblErroTotalPremios.setVisible(false);
			}
		}
		if (!erroEncontrado && (numBilhetes < numPremios)) {
			lblErroTotalBilhetes.setVisible(true);
			lblErroTotalPremios.setVisible(true);
			erroEncontrado = true;
			JOptionPane.showMessageDialog(null, "O número de bilhetes deve ser superior ao número de prêmios!\r\nCorrija estes campos.", "Sorteio", JOptionPane.WARNING_MESSAGE);
		} else if (!erroEncontrado && !programaAferido && (Integer.parseInt(textSorteioNumero.getText()) == 0)) {
			lblErroSorteioNumero.setVisible(true);
			erroEncontrado = true;
			JOptionPane.showMessageDialog(null, "O número do sorteio não pode ser zero!\r\nCorrija este campo.", "Sorteio", JOptionPane.WARNING_MESSAGE);
		} else if (erroEncontrado) {
			JOptionPane.showMessageDialog(null, "Prencher corretamente os campos assinalados com asterísco vermelho!", "Sorteio", JOptionPane.WARNING_MESSAGE);
		}
		return erroEncontrado;
	}

	protected void iniciarTimer() {
		if (EfetuarSorteio.isRunning()) {
			long horaLocal =(new Date()).getTime();
			long delta = (horaLocal - tempoInicial) / 1000;
			lblValorNumeroFaixas.setText(EfetuarSorteio.numDeFaixas);
			lblValorEsperado.setText(EfetuarSorteio.valorEsperado);
			lblNumeroIteracoes.setText(formatoNumero.format(EfetuarSorteio.vezes - 1));
			lblNumeroPremios.setText(formatoNumero.format(EfetuarSorteio.premios - 1));
			lblValorTempoTranscorrido.setText(formatoNumero.format(delta));
			if (deltaAnterior != delta) {
				int val;
				if (delta > 0)
					val = EfetuarSorteio.vezes / (int)delta;
				else
					val = 0;
				lblValorIteracoesPorSegundo.setText(formatoNumero.format(val));
				deltaAnterior = delta;
			}
		} else {
			long tempoFinal =(new Date()).getTime();
			long delta = (tempoFinal - tempoInicial) / 1000;
			timerSorteio.stop();
			lblValorDesvioPadrao.setText(EfetuarSorteio.valorDesvioPadrao);
			lblValorEsperado.setText(EfetuarSorteio.valorEsperado);
			lblValorMedio.setText(EfetuarSorteio.valorMedio);
			lblValorMaximo.setText(EfetuarSorteio.valorMaximo);
			lblValorMinimo.setText(EfetuarSorteio.valorMinimo);
			lblValorDiferencaMaxMin.setText(EfetuarSorteio.diferencaValoresMaxMin);
			lblNumeroIteracoes.setText(formatoNumero.format(EfetuarSorteio.vezes - 1));
			lblNumeroPremios.setText(formatoNumero.format(EfetuarSorteio.premios - 1));
			if (!programaAferido)
				lblHashResultado.setText(EfetuarSorteio.hashResultado);
			lblValorTempoTranscorrido.setText(formatoNumero.format(delta));
			boolean execucaoContinua = false;
			if (EfetuarSorteio.msgDeErro.length() > 0) {
				String msg, titulo;
				if (programaAferido) {
					msg = "da aferição: ";
					titulo = "Executando a aferição do Programa";
				} else {
					msg = "do sorteio: ";
					titulo = "Executando o Sorteio";
				}
				if (EfetuarSorteio.msgDeErro.contains("cancelada"))
					JOptionPane.showMessageDialog(null, EfetuarSorteio.msgDeErro, titulo, JOptionPane.WARNING_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, "Falhou a execução " + msg + EfetuarSorteio.msgDeErro, titulo, JOptionPane.ERROR_MESSAGE);
				if (EfetuarSorteio.nomeSorteio.equals("Aferir")) {
					if (EfetuarSorteio.msgDeErro.contains("cancelada")) {
						lblProgramaAferido.setForeground(Color.RED);
						lblProgramaAferido.setText("Cancelada a aferição!");
					} else {
						lblProgramaAferido.setForeground(Color.RED);
						lblProgramaAferido.setText("Programa com erro na aferição!");
					}
				}
			} else {
				if (EfetuarSorteio.nomeSorteio.equals("Aferir")) {
					lblProgramaAferido.setForeground(Color.BLUE);
					lblProgramaAferido.setText("Programa Aferido");
				} else {
				}
			}
			if (!execucaoContinua)
				habilitarCampos(true);
		}
	}

	protected void habilitarCampos(boolean Sim) {
		textExtracao.setEnabled(Sim);
		textDataExtracao.setEnabled(Sim);
		textPremio1.setEnabled(Sim);
		textPremio2.setEnabled(Sim);
		textPremio3.setEnabled(Sim);
		textPremio4.setEnabled(Sim);
		textPremio5.setEnabled(Sim);
		textSorteioNumero.setEnabled(Sim);
		textDataSorteio.setEnabled(Sim);
		textDataDiarioOficial.setEnabled(Sim);
		textTotalBilhetes.setEnabled(Sim);
		textTotalPremios.setEnabled(Sim);
		btnApagar.setEnabled(Sim);
		if (Sim) {
			btnCancelar.setText("Encerrar");
			btnCancelar.setToolTipText("Encerrar a execução do programa.");
		} else {
			btnCancelar.setText("Parar");
			btnCancelar.setToolTipText("Parar a execução do programa.");
		}
		if (programaAferido) {
			// Aferindo programa.
			btnSortear.setEnabled(Sim);
			lblTituloHashResultado.setVisible(false);
		} else {
			// Executando o sorteio.
			btnAferir.setEnabled(Sim);
			lblTituloHashResultado.setVisible(true);
		}
	}

	protected void carregarValoresPadrao() {
		textExtracao.setText(afrExtracao);
		textDataExtracao.setText(afrDataExtracao);
		textPremio1.setText(afrPremio1);
		textPremio2.setText(afrPremio2);
		textPremio3.setText(afrPremio3);
		textPremio4.setText(afrPremio4);
		textPremio5.setText(afrPremio5);
		textSorteioNumero.setText(afrSorteio);
		textDataSorteio.setText(afrDataSorteio);
		textDataDiarioOficial.setText(afrDataDiarioOficial);
		textTotalBilhetes.setText(afrBilhetes);
		textTotalPremios.setText(afrPremios);
		lblArquivoImportacaoExtracoes.setVisible(false);
		chckbxUmaLinha.setVisible(false);
	}

	protected void inicializarCampos() {
		// Limpar os campos de entrada de Informações
		textExtracao.setText("");
		lblErroExtracao.setVisible(false);
		textDataExtracao.setValue(null);
		lblErroDataExtracao.setVisible(false);
		textPremio1.setValue(null);
		lblErroPremio1.setVisible(false);
		textPremio2.setValue(null);
		lblErroPremio2.setVisible(false);
		textPremio3.setValue(null);
		lblErroPremio3.setVisible(false);
		textPremio4.setValue(null);
		lblErroPremio4.setVisible(false);
		textPremio5.setValue(null);
		lblErroPremio5.setVisible(false);
		textSorteioNumero.setText("");
		lblErroSorteioNumero.setVisible(false);
		textDataSorteio.setValue(null);
		lblErroDataSorteio.setVisible(false);
		textDataDiarioOficial.setValue(null);
		lblErroDataDiarioOficial.setVisible(false);
		textTotalBilhetes.setValue(null);
		lblErroTotalBilhetes.setVisible(false);
		textTotalPremios.setValue(null);
		lblErroTotalPremios.setVisible(false);
		chckbxUmaLinha.setVisible(false);
		lblArquivoImportacaoExtracoes.setVisible(false);
	}
	protected void inicializarResultados() {
		// Limpar os campos de resultados
		lblValorNumeroFaixas.setText("");
		lblValorEsperado.setText("");
		lblValorMaximo.setText("");
		lblValorMinimo.setText("");
		lblValorDiferencaMaxMin.setText("");
		lblValorMedio.setText("");
		lblValorDesvioPadrao.setText("");
		lblNumeroIteracoes.setText("");
		lblNumeroPremios.setText("");
		lblValorTempoTranscorrido.setText("");
		lblValorIteracoesPorSegundo.setText("");
		lblHashResultado.setText("");
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); // .getCrossPlatformLookAndFeelClassName()); //("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new Sorteio();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public class TextFileFilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File file) {
			//Converter para minÃºsculo antes de verificar a extensÃ£o
			return (file.getName().toLowerCase().endsWith(".txt")  || file.isDirectory());
		}

		public String getDescription() {
			return "Arquivos Texto (*.txt)";
		}
	}
}