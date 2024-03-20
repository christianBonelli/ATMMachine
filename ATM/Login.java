package ATM;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Login extends JFrame{
	
	private static final String DB_URL = "jdbc:mysql://localhost:3306/atmmachine";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "SQLpassword10_";
	 private JTextField accountNumberField;
	    private JPasswordField pinField;
	    private static int accountNumber;
	    private static Main main;

	    public Login(int accountNumber) {
	    	this.accountNumber = accountNumber;
	    	
	        setTitle("ATM Login");
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setSize(300, 200);
	        setLocationRelativeTo(null); // Centra la finestra
	        setLayout(new BorderLayout());

	        // Pannello per i campi di inserimento e il pulsante di login
	        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 righe, 2 colonne, spazio tra le celle

	        JLabel accountNumberLabel = new JLabel("Account Number:");
	        accountNumberField = new JTextField();

	        JLabel pinLabel = new JLabel("PIN:");
	        pinField = new JPasswordField();

	        JButton loginButton = new JButton("Login");
	        loginButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	//Ottiene il testo inserito dall'utente e lo assegna alla variabile accountNumberText
	            	//Il metodo trim rimuove eventuali spazi
	                String accountNumberText = accountNumberField.getText().trim();
	                //Ottiene il testo inserito dall'utente nel campo  della password e lo assegna alla variabile pinText
	                //Il metodo getPassword restituisce un array di caratteri della password e viene convertito in stringa utilizzando String.valueOf()
	                String pinText = String.valueOf(pinField.getPassword());

	                // Controlla che i campi non siano vuoti
	                if (!accountNumberText.isEmpty() && !pinText.isEmpty()) {
	                	//Conversione delle stringhe in numeri interi
	                    int accountNumber = Integer.parseInt(accountNumberText);
	                    int pin = Integer.parseInt(pinText);

	                    //Connessione al database
	                    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
	                    	//Se la connessione è andata a buon fine viene richiamato il metodo autenticaUtente
	                        boolean authenticated = autenticaUtente(accountNumber, pin, conn);
	                        
	                        //Se l'autenticazione ha successo esce l'avviso autenticazione riuscita, chiude la pagina login e apre il main
	                        if (authenticated) {
	                            // Apri la schermata successiva dopo l'autenticazione
	                            JOptionPane.showMessageDialog(null, "Autenticazione riuscita!");
	                            dispose(); // Chiudi la finestra di login
	                            new Main(accountNumber).setVisible(true); // Apri la MainPage
	                            
	                        } else {
	                            JOptionPane.showMessageDialog(null, "Account Number o PIN non validi.");
	                        }
	                    } catch (SQLException ex) {
	                        ex.printStackTrace();
	                        JOptionPane.showMessageDialog(null, "Errore di connessione al database.");
	                    }
	                } else {
	                    JOptionPane.showMessageDialog(null, "Inserisci l'Account Number e il PIN.");
	                }
	            }
	        });


	        inputPanel.add(accountNumberLabel);
	        inputPanel.add(accountNumberField);
	        inputPanel.add(pinLabel);
	        inputPanel.add(pinField);
	        inputPanel.add(new JLabel()); // Spazio vuoto
	        inputPanel.add(loginButton);

	        // Aggiungi il pannello dei campi di inserimento alla finestra
	        add(inputPanel, BorderLayout.CENTER);

	        setVisible(true);
	    }
	    //Metodo per autenticare l'utente
	    //I parametri accountNumber e pin vengono passati al metodo perchè rappresentano le informazioni necessarie per identificare l'utente
	    //Abbiamo passato 'Connection conn' come parametro  che rappresenta  la connessione al database
	    //Le operazioni di accesso  al database sono eseguite usando  oggetti 'Connection' 
	    //Utilizzando 'throws SQLException' , il metodo autenticaUtente() dichiara esplicitamente che potrebbe verificarsi  un'eccezione durante l'esecuzione  e che questa eccezione deve essere gestita da chi chiama il metodo
	    private static boolean autenticaUtente(int accountNumber, int pin, Connection conn) throws SQLException {
	    	//La query SELECT prende i dati dal database
	        String query = "SELECT * FROM account WHERE accountNumber = ? AND pin = ?";
	        
	        try (PreparedStatement statement = conn.prepareStatement(query)) {
	        	//Dopo aver preparato la query, usiamo i metodi setInt() per impostare i valori numerici nella query 
	            statement.setInt(1, accountNumber);
	            statement.setInt(2, pin);
	            
	            //Una volta che i parametri sono stati impostati, eseguiamo la 'auery' usando executeQueery() sull'oggetto preparedSTatement
	            try (ResultSet result = statement.executeQuery()) {
	            	
	            	//Utilizzo del metodo next() per spostare il cursore sulla prima riga di risulatati.
	            	//Se next() restituisce true, significa che esiste almeno una riga nel risultato, e l'utente è stato trovato nel database
	                return result.next(); 
	                //Usiamo return in modo che l'oggetto ResultSet venga chiuso dopo l'esecuzione del blocco trycatch
	            }
	        }
	    }

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                new Login(accountNumber);
	            }
	        });
	    }
	}

