package ATM;

import javax.swing.*;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class Main extends JFrame {
		private JPanel homePanel;
		private JLabel welcomeLabel;
		private JButton prelievoButton;
		private JButton depositoButton;
		private JButton controllaBalanceButton;
		private JLabel benvenutoScritta;
		private static int accountNumber;
		
		
		public Main(int accountNumber) {
			this.accountNumber = accountNumber;
			
			setTitle("ATM Machine");
			setLocationRelativeTo(null);
			setSize(600, 500);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			
			//inizializzazione delle parti che comporranno l'homePage
			homePanel = new JPanel(new GridLayout(6, 1, 15, 15));
			welcomeLabel = new JLabel("Benvenuti nel nostro ATM Machine");
			benvenutoScritta = new JLabel("Scegli una delle seguenti ozpioni");
			prelievoButton = new JButton("PRELEVA");
			depositoButton = new JButton("DEPOSITA");
			controllaBalanceButton = new JButton("CONTROLLA SALDO");
			
			//Aggiunta dei componenti al pannello
			homePanel.add(welcomeLabel);
			homePanel.add(benvenutoScritta);
			homePanel.add(prelievoButton);
			homePanel.add(depositoButton);
			homePanel.add(controllaBalanceButton);

			//Reindirizzamento pagine
			prelievoButton.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent e) {
			       
			       Prelievo prelievo =  new Prelievo(accountNumber);
			       prelievo.setVisible(true);// Apri la pagina Prelievi
			    }
			});
			
			depositoButton.addActionListener(new ActionListener() {
			    @Override
			    public void actionPerformed(ActionEvent e) {
			         // Chiudi la finestra corrente
			        Deposito deposito = new Deposito(accountNumber);
			        deposito.setVisible(true);// Apri la pagina DepositoPage
			    }
			});

		controllaBalanceButton.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        // Chiudi la finestra corrente
		        CheckBalance check = new CheckBalance(accountNumber);
		        check.setVisible(true);// Apri la pagina CheckBalance
		    }
		});
		
getContentPane().add(homePanel);
		}
		
		}

