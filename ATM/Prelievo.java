package ATM;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;


	public class Prelievo extends JFrame {
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
	
	

	
	// Metodo che preleva i soldi dal database
    public static boolean effettuaPrelievo(double importo) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
        	//Otteniamo il saldoAttuale con il metodo ottieniSaldo()
        	double saldoAttuale = ottieniSaldo(conn);

        	//Controllo che il saldoAttuale sia maggiore dell'importo richiesto
            if (saldoAttuale >= importo) {
            	//Inseriamo in nuovoSaldo la differenza tra il saldo dell'account e l'importo richiesto
                double nuovoSaldo = saldoAttuale - importo;
                //Aggiorniamo il saldo dell'account
                updateSaldo(nuovoSaldo, conn);
                //return true indica al chiamante del metodo che l'operazione è avvenuta con successo
                return true;
            } else {
                System.out.println("Saldo insufficiente per effettuare il prelievo.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //Metodo che ottiene il saldo dal database
    private static double ottieniSaldo(Connection conn) throws SQLException {
        double saldo = 0.0;
        String query = "SELECT saldo FROM account WHERE accountNumber = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            // Imposta il numero di account nella query utilizzando il valore memorizzato durante l'autenticazione
            statement.setInt(1, accountNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    saldo = resultSet.getDouble("saldo");
                }
            }
        }
        return saldo;
    }


    //Metodo che aggiorna il saldo nel database
    private static void updateSaldo(double nuovoSaldo, Connection conn) throws SQLException {
        String query = "UPDATE account SET saldo = ? WHERE accountNumber = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setDouble(1, nuovoSaldo);
            // Imposta il numero di account nella query utilizzando il valore memorizzato durante l'autenticazione
            statement.setInt(2, accountNumber);
            statement.executeUpdate();
        }
    }
    private static double visualizzaSaldo(Connection conn) throws SQLException {
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
	
   
    //METODI--------------
   
   
    public Prelievo(int accountNumber) {
	setTitle("Prelievi");
	setLocationRelativeTo(null);
	setSize(600,500);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	this.accountNumber = accountNumber;
	
	 // Creazione del pannello principale con GridLayout
    JPanel panelPrelievi = new JPanel(new GridLayout(6,1,10,10)); 

    // Creazione dei campi di input e dei bottoni
    JButton home = new JButton("Torna alla pagina principale");
    JButton preleva = new JButton("Effettua prelievo");
	
	saldoAttuale = new JLabel("Saldo Attuale:");
	 JTextArea importoTextArea = new JTextArea(1, 10); // Creazione di un JTextArea per l'importo
     //JPanel panel = new JPanel();
     //panel.add(new JLabel("Importo da prelevare:"));
     //panel.add(importoTextArea);
	 saldoFinale = new JTextArea(1,10);
	saldoFinale.setEditable(false);
	JScrollPane scrollPane = new JScrollPane(saldoFinale);
	JLabel saldoFinale1 = new JLabel("Saldo Finale: ");
	
	
	home.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			main.setVisible(true);
		}
	});
	
	
	preleva.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Recupero dell'importo inserito dall'utente
            String importoInput = importoTextArea.getText().trim();
            if (!importoInput.isEmpty()) {
                double importoPrelievo = Double.parseDouble(importoInput);

                // Chiamata al metodo che gestisce il prelievo
                boolean prelievoSuccesso = effettuaPrelievo(importoPrelievo);
                
                // Visualizzazione del risultato del prelievo
                if (prelievoSuccesso) {
                    JOptionPane.showMessageDialog(null, "Prelievo avvenuto con successo!\nHai prelevato: " + importoPrelievo + " €", "Prelievo Effettuato", JOptionPane.INFORMATION_MESSAGE);
                    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                        double saldoAggiornato = visualizzaSaldo(conn);
                        // Ora puoi fare ciò che vuoi con il saldo aggiornato, ad esempio mostrarlo in un JTextArea
                        saldoFinale.setText("Saldo Attuale: " + saldoAggiornato + " €");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        // Gestisci l'eccezione
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Non c'è saldo sufficiente per il prelievo.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Inserisci un importo valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
	 	
	 
	  
	    panelPrelievi.add(home);
	    panelPrelievi.add(new JLabel("Inserisci l'Importo da prelevare:"));
	    panelPrelievi.add(importoTextArea);
	    panelPrelievi.add(preleva);
	   
	    panelPrelievi.add(saldoFinale1);
	    panelPrelievi.add(saldoFinale);
	    
	    getContentPane().add(panelPrelievi);
	
	
}
	}
	