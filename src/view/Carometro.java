package view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.management.loading.PrivateClassLoader;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.DAO;
import utils.Validador;

public class Carometro extends JFrame {
	
	// Instanciar objetos
	DAO dao = new DAO();
	private Connection con;
	private PreparedStatement pst;

	//Instanciar Objeto para o fluxo de bytes
	private FileInputStream fis;
	
	//Variavel global para armazenar o tamanho da imagem(bytes)
	private int tamanho;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblStatus;
	private JLabel lblData;
	private JTextField txtRA;
	private JTextField txtNome;
	private JLabel lblFoto;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Carometro frame = new Carometro();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Carometro() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				status();
				setarData();
			}
		});
		setTitle("Carômetro");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Carometro.class.getResource("/img/instagram.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.textHighlight);
		panel.setBounds(0, 260, 624, 51);
		contentPane.add(panel);
		panel.setLayout(null);
		
		lblStatus = new JLabel("");
		lblStatus.setIcon(new ImageIcon(Carometro.class.getResource("/img/dboff.png")));
		lblStatus.setBounds(565, 11, 32, 32);
		panel.add(lblStatus);
		
		lblData = new JLabel("");
		lblData.setForeground(SystemColor.text);
		lblData.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblData.setBounds(25, 11, 390, 32);
		panel.add(lblData);
		
		JLabel lblNewLabel = new JLabel("RA");
		lblNewLabel.setBounds(32, 28, 22, 14);
		contentPane.add(lblNewLabel);
		
		txtRA = new JTextField();
		txtRA.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String caracteres = "0123456789";
				if(!caracteres.contains(e.getKeyChar() + "")) {
					e.consume();
				}
			}
		});
		txtRA.setBounds(70, 25, 94, 20);
		contentPane.add(txtRA);
		txtRA.setColumns(10);
		// Uso do PlainDocument para limitar os campos
		txtRA.setDocument(new Validador(6));
		
		JLabel lblNewLabel_1 = new JLabel("Nome");
		lblNewLabel_1.setBounds(32, 78, 41, 14);
		contentPane.add(lblNewLabel_1);
		
		txtNome = new JTextField();
		txtNome.setBounds(70, 75, 223, 20);
		contentPane.add(txtNome);
		txtNome.setColumns(10);
		// Uso do PlainDocument para limitar os campos
		txtNome.setDocument(new Validador(30));
		
		lblFoto = new JLabel("");
		lblFoto.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblFoto.setIcon(new ImageIcon(Carometro.class.getResource("/img/camera.png")));
		lblFoto.setBounds(397, 25, 200, 200);
		contentPane.add(lblFoto);
		
		JButton btnCarregar = new JButton("Carregar foto");
		btnCarregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarFoto();
			}
		});
		btnCarregar.setForeground(SystemColor.textHighlight);
		btnCarregar.setBounds(165, 118, 128, 23);
		contentPane.add(btnCarregar);
		
		JButton btnAdicionar = new JButton("");
		btnAdicionar.setToolTipText("Adicionar");
		btnAdicionar.setIcon(new ImageIcon(Carometro.class.getResource("/img/create.png")));
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adicionar();
			}
		});
		btnAdicionar.setBounds(32, 185, 60, 60);
		contentPane.add(btnAdicionar);

	}
	
	private void status() {
		try {
			con = dao.conectar();
			if(con == null) {
				//System.out.println("Erro na conexão");
				lblStatus.setIcon(new ImageIcon(Carometro.class.getResource("/img/dboff.png")));
			}else {
				//System.out.println("Conectado com sucesso");
				lblStatus.setIcon(new ImageIcon(Carometro.class.getResource("/img/dbon.png")));
			}
			con.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	private void setarData() {
		Date data = new Date();
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL);
		lblData.setText(formatador.format(data));
	}
	
	private void carregarFoto() {
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Selecionar arquivo");
		jfc.setFileFilter(new FileNameExtensionFilter("Arquivo de imagens(*.PNG, *JPG, *JPEG)", "png", "jpg", "jpeg"));
		int resultado = jfc.showOpenDialog(this);
		if(resultado == JFileChooser.APPROVE_OPTION) {
			try {
				fis = new FileInputStream(jfc.getSelectedFile());
				tamanho = (int) jfc.getSelectedFile().length();
				Image foto = ImageIO.read(jfc.getSelectedFile()).getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(), Image.SCALE_SMOOTH);
				lblFoto.setIcon(new ImageIcon(foto));
				lblFoto.updateUI();
			}
			catch(Exception e) {
				System.out.println(e);
			}
		}
	}
	
	private void adicionar() {
		String insert =  "INSERT INTO alunos(nome, foto) VALUES (?, ?)";
		try {
			 con = dao.conectar();
			 pst = con.prepareStatement(insert);
			 pst.setString(1, txtNome.getText());
			 pst.setBlob(2, fis, tamanho);
			 int confirma = pst.executeUpdate();
			 if(confirma == 1) {
				 JOptionPane.showMessageDialog(null, "Aluno cadastrado com sucesso!");
			 }else {
				 JOptionPane.showMessageDialog(null, "Erro!! Aluno não cadastrado.");
			 }
			 con.close();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
