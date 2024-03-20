package ATM;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;


	public class Deposito extends JFrame{
	private static final String DB_URL = "jdbc:mysql://localhost:3306/atmmachine";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "SQLpassword10_";
	private static JButton depositButton;
	private static Connection conn;
	private static int accountNumber;
	private static double saldo;
	private static Main main;
	
	
	
	//MEtodi-------------
	
	private static void depositaSoldi(double saldo, int accountNumber) {
	    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
	       //Otteniamo il saldo attuale dell'account specificato
	        double saldoAttuale = ottieniSaldo(accountNumber, conn);
	        
	      //Una volta ottenuto il saldoAttuale viene depositato e agggiunto al saldo attuale
	        double nuovoSaldo = saldoAttuale + saldo;
	        
	      //Aggiorniamo il saldo dell'account nel database. QUesto metodo prende come input il nuovoSaldo, il accountNumber e connessione al database
	        updateSaldo(nuovoSaldo,accountNumber, conn);
	        
	       
	        System.out.println("Saldo depositato con successo.");
	        JOptionPane.showMessageDialog(null, "Deposito avvenuto con successo!\nHai depositato: " + saldo + " €", "Deposito Effettuato", JOptionPane.INFORMATION_MESSAGE);
	    } catch (SQLException e) {
	        // Gestione degli errori
	        e.printStackTrace();
	        System.out.println("Errore durante il deposito del saldo nel database.");
	    }
	}
	// Metodo che ottiene il saldo dal database per un determinato accountNumber
	private static double ottieniSaldo(int accountNumber, Connection conn) throws SQLException {
	    double saldo = 0.0;
	    String query = "SELECT saldo FROM account WHERE accountNumber = ?";
	    try (PreparedStatement statement = conn.prepareStatement(query)) {
	        statement.setInt(1, accountNumber);
	        try (ResultSet resultSet = statement.executeQuery()) {
	            if (resultSet.next()) {
	                saldo = resultSet.getDouble("saldo");
	            }
	        }
	    }
	    return saldo;
	}

	// Metodo che aggiorna il saldo nel database per un determinato accountNumber
	private static void updateSaldo(double nuovoSaldo, int accountNumber, Connection conn) throws SQLException {
	    String query = "UPDATE account SET saldo = ? WHERE accountNumber = ?";
	    try (PreparedStatement statement = conn.prepareStatement(query)) {
	        statement.setDouble(1, nuovoSaldo);
	        statement.setInt(2, accountNumber);
	        statement.executeUpdate();
	    }
	}
	private static double visualizzaSaldo(Connection conn, int accountNumber) throws SQLException {
	    double saldo = 0.0;
	    String query = "SELECT saldo FROM account WHERE accountNumber = ?";
	    try (PreparedStatement statement = conn.prepareStatement(query)) {
	        statement.setInt(1, accountNumber);
	        try (ResultSet resultSet = statement.executeQuery()) {
	            if (resultSet.next()) {
	                saldo = resultSet.getDouble("saldo");
	            }
	        }
	    }
	    return saldo;
	}
	
	public Deposito(int accountNumber) {
		setTitle("Deposito");
		setLocationRelativeTo(null);
		setSize(600,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTextField saldoTextField = new JTextField();
		
		JPanel mainPanel = new JPanel(new GridLayout(6,1, 15, 15));
		
		JLabel InserisciSoldi = new JLabel("Inserisci l'ammontare dei soldi che vuoi depositare:");
		JTextArea campoInputSoldi = new JTextArea();
		
		 JButton home = new JButton("Torna alla pagina principale");
		depositButton = new JButton("Depoisita");
		
		JLabel saldoTotale = new JLabel("Saldo Finale:");
		JTextArea saldoFinale = new JTextArea();
		
		depositButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
		            // Ottenere il saldo da inserire nel deposito dal campo saldoTextField
		            double saldo = Double.parseDouble(saldoTextField.getText());
		            
		            // Qui chiameremo il metodo depositaSoldi passando il saldo e l'accountNumber
		            depositaSoldi(saldo, accountNumber);
		            
		            // Visualizza il saldo aggiornato
		            double saldoAggiornato = visualizzaSaldo(conn, accountNumber);
		            saldoFinale.setText(saldoAggiornato + " €");
		            
		            // Chiusura della connessione dopo l'utilizzo
		            conn.close();
		        } catch (SQLException ex) {
		            // Gestione dell'eccezione SQLException
		            ex.printStackTrace();
		            JOptionPane.showMessageDialog(null, "Errore durante il deposito del saldo nel database.", "Errore", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});
		home.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				main.setVisible(true);
			}
		});
		
		
		mainPanel.add(home);
		mainPanel.add(InserisciSoldi);
		mainPanel.add(saldoTextField);
		mainPanel.add(depositButton);
		mainPanel.add(saldoTotale);
		mainPanel.add(saldoFinale);
		
		getContentPane().add(mainPanel);
	}
		
		
		
		
	}

