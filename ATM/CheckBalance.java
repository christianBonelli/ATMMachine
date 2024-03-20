package ATM;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;


public class CheckBalance extends JFrame{
	private static final String DB_URL = "jdbc:mysql://localhost:3306/atmmachine";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "SQLpassword10_";
	private static int accountNumber;
	private static int pin;
	private static double importoPrelievo;
	private static JTextField accountNumberField;
	private static JTextField pinField;
	private static Connection conn;
	private static JLabel saldoAttuale;
	private static JTextArea saldoFinale;
	private static double saldo;
	private static double importo;
	private static Main main;
	
	//Metodi--------
	//Questo metodo accede al database, recupera il saldo asscoaito all'accountNumber e restituisce questo saldo
	private static double visualizzaSaldo(Connection conn, int accountNumber) throws SQLException {
	   //Dichiarazione e inizializzazione variabile slado di tipo double.
		double saldo = 0.0;
		//Preparazione della query per selezionare il saldo associato all'account specificato
	    String query = "SELECT saldo FROM account WHERE accountNumber = ?";
	    //uTILIZZANDO LA CONNESSIONE AL DATABASE ('conn)' viene preparata una dichiarazione contenente la QUERY
	    
	    try (PreparedStatement statement = conn.prepareStatement(query)) {
	    	//Il accountNumber fornito viene impostato come parametro nella dichiarazione preparata usando setInt()
	        statement.setInt(1, accountNumber);
	        //esecuzione della query
	        //La query viene eseguita utilizzando il metodo executeQuery() della dichiarazione preparata, restituendo un oggetto ResultSet che contiene i risultati della query.
	        try (ResultSet resultSet = statement.executeQuery()) {
	        	//Se esiste una riga nel risultato, il saldo viene estratto usando il metodo getDouble("saldo") e assegnato alla variabile saldo
	            if (resultSet.next()) {
	                saldo = resultSet.getDouble("saldo");
	            }
	        }
	    }
	    //cHIUSURA DELLA CONNESSIONE E RITORNO DEL SALDO
	    return saldo;
	}
	

	 
	public CheckBalance(int accountNumber) {
		setTitle("Controlla Saldo");
		setLocationRelativeTo(null);
		setSize(600,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTextField saldoTextField = new JTextField();
		
		
		JPanel mainPanel = new JPanel(new GridLayout(6,1, 15, 15));
		JLabel checkBalance = new JLabel("Per controllare il saldo del tuo conto clicca qui sotto.");
		
		JButton home = new JButton("Torna alla pagina principale");
		JButton check = new JButton("Controlla");
		
		JLabel saldoTotale = new JLabel("Il tuo saldo:");
		JTextArea saldoFinale = new JTextArea();
		
		//EventListener---------------
		check.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
			            // Ottenere il saldo dal database utilizzando il numero di account memorizzato durante l'autenticazione
			            double saldo = visualizzaSaldo(conn, accountNumber);
			            
			            // Visualizza il saldo in una finestra di dialogo o in un altro modo appropriato
			            JOptionPane.showMessageDialog(null, "Il saldo del tuo account è: " + saldo, "Saldo", JOptionPane.INFORMATION_MESSAGE);
			            saldoFinale.setText(saldo + "€");
			        } catch (SQLException ex) {
			            // Gestione dell'eccezione SQLException
			            ex.printStackTrace();
			            JOptionPane.showMessageDialog(null, "Errore durante il recupero del saldo dal database.", "Errore", JOptionPane.ERROR_MESSAGE);
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
		mainPanel.add(checkBalance);
		mainPanel.add(check);
		mainPanel.add(saldoTotale);
		mainPanel.add(saldoFinale);
		
		getContentPane().add(mainPanel);
		
		
	}
}
