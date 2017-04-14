package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Servidor extends Thread {
	private static ArrayList<BufferedWriter> clientes; // usado para armazenar o
														// BufferedWriter de
														// cada cliente
														// conectado
	private static ServerSocket server; // usado para a criação do servidor
	private String nome;
	private Socket con;
	private InputStream in;
	private InputStreamReader inr;
	private BufferedReader bfr;

	public Servidor(Socket con) { // Método construtor
									// recebe um objeto socket como parâmetro e
									// cria um objeto do tipo BufferedReader,
									// que aponta para o stream do cliente
									// socket.
		this.con = con;
		try {
			in = con.getInputStream();
			inr = new InputStreamReader(in);
			bfr = new BufferedReader(inr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() { // É acionado toda vez que um cliente novo chega ao
						// servidor

		try {

			String msg;
			OutputStream ou = this.con.getOutputStream();
			Writer ouw = new OutputStreamWriter(ou);
			BufferedWriter bfw = new BufferedWriter(ouw);
			clientes.add(bfw);
			nome = msg = bfr.readLine();

			while (!"Sair".equalsIgnoreCase(msg) && msg != null) {// Fica
																	// verificando
																	// se há uma
																	// nova
																	// mensagem
				msg = bfr.readLine();
				sendToAll(bfw, msg);// Se existir uma nova mensagem ela será
									// enviada para esse método que irá enviar
									// para os demais usuários conectados no
									// chat
				System.out.println(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	public void sendToAll(BufferedWriter bwSaida, String msg) throws IOException {
		BufferedWriter bwS;

		for (BufferedWriter bw : clientes) {// Irá percorrer todos os clientes e
											// irá enviar a mensagem para cada
											// um deles
			bwS = (BufferedWriter) bw;
			if (!(bwSaida == bwS)) {
				bw.write(nome + " -> " + msg + "\r\n");
				bw.flush();
			}
		}
	}
}
