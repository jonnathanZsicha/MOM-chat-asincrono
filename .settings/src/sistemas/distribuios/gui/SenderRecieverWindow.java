package sistemas.distribuios.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sistemas.distribuidos.model.Reciever;
import sistemas.distribuidos.model.Sender;

public class SenderRecieverWindow {

	private JFrame frame;
	private JTextArea msgReceivedLeft;
	private JTextField textSendLeft;
	private JTextArea msgReceivedRight;
	private JTextField textSendRight;
	private JButton btnSendRight;

	// Init remitente y receptora

	Sender senderLeft = new Sender("sentMsgQueue");
	Sender senderRight = new Sender("recMsgQueue");

	Reciever recieverLeft = new Reciever("recMsgQueue");
	Reciever recieverRight = new Reciever("sentMsgQueue");
	
	Thread tRecieverLeft = new Thread(recieverLeft);
	Thread tRecieverRight = new Thread(recieverRight);
	
	// Remitente de la cola para la izquierda - >> recMsgQueue << - Recibir cola para la derecha
	// Remitente de la cola para la derecha - >> sentMsgQueue << - Cola de recepción para la izquierda

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SenderRecieverWindow window = new SenderRecieverWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SenderRecieverWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 701, 385);
		frame.getContentPane().setBackground(Color.orange);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		msgReceivedLeft = new JTextArea();
		msgReceivedLeft.setEditable(false);
		msgReceivedLeft.setBounds(10, 11, 308, 248);
		frame.getContentPane().add(msgReceivedLeft);
		msgReceivedLeft.setColumns(10);

		textSendLeft = new JTextField();
		textSendLeft.setBounds(10, 270, 308, 20);
		frame.getContentPane().add(textSendLeft);
		textSendLeft.setColumns(10);

		JButton btnSendLeft = new JButton("Emviar");
		btnSendLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// Remitente izquierdo
				String msg = textSendLeft.getText();
				if (msg != "" || msg != null) {
					senderLeft.sendMessage(msg);
					msgReceivedLeft.setText(msgReceivedLeft.getText() + "\nEmviado :"
							+ msg);
					textSendLeft.setText("");
				}
			}
		});
		btnSendLeft.setBounds(121, 313, 89, 23);
		frame.getContentPane().add(btnSendLeft);

		msgReceivedRight = new JTextArea();
		msgReceivedRight.setEditable(false);
		msgReceivedRight.setColumns(10);
		msgReceivedRight.setBounds(367, 11, 308, 248);
		frame.getContentPane().add(msgReceivedRight);

		textSendRight = new JTextField();
		textSendRight.setColumns(10);
		textSendRight.setBounds(367, 270, 308, 20);
		frame.getContentPane().add(textSendRight);

		btnSendRight = new JButton("Emviar");
		btnSendRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Remitente derecho
				String msg = textSendRight.getText();
				if (msg != "" || msg != null) {
					senderRight.sendMessage(msg);
					msgReceivedRight.setText(msgReceivedRight.getText()
							+ "\nEmviado :" + msg);
					textSendRight.setText("");
				}
			}
		});
		btnSendRight.setBounds(478, 313, 89, 23);
		frame.getContentPane().add(btnSendRight);

		// Iniciar funciones asincrónicas de devolución de llamada
		try {
			recieverLeft.receiver.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message msg) {
					TextMessage tmsg = (TextMessage) msg;
					try {
						msgReceivedLeft.setText(msgReceivedLeft.getText()
								+ "\nRecivido:>" + tmsg.getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});

			recieverRight.receiver.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message msg) {
					TextMessage tmsg = (TextMessage) msg;
					try {
						msgReceivedRight.setText(msgReceivedRight.getText()
								+ "\nRecivido:" + tmsg.getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (JMSException e1) {
			e1.printStackTrace();
		}
	}

}
