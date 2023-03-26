package view;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.DAO;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Carometro extends JFrame {
	
	// Instanciar objetos
	DAO dao = new DAO();
	private Connection con;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

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
	}
	
	private void status() {
		try {
			con = dao.conectar();
			if(con == null) {
				System.out.println("Erro na conexão");
			}else {
				System.out.println("Conectado com sucesso");
			}
			con.close();
		} catch(Exception e) {
			System.out.println(e);
		}
	}

}
