package my.webserver;

import java.io.*;
import java.net.*;

import javax.swing.JFrame;

public class WebServer {

	public static void main(String[] args) throws IOException 
	{
		String clientSentence;
		
		Interface config;
		config = new Interface();
		
		ServerSocket welcomeSocket = 
				new ServerSocket(6789, 0, InetAddress.getByName ("10.12.57.184"));//�˿ڣ����г��ȣ��󶨵�ַ
		while(true) {
			Socket connectionSocket = welcomeSocket.accept();
			clientSentence = connectionSocket.getInetAddress().toString();
			config.frame.logText.append("Client IP:"+clientSentence.substring(1)+"\n");
			config.frame.logText.setCaretPosition(config.frame.logText.getDocument().getLength());
			//���ù�굽���һ�дӶ����ֹ�����һֱ����׶� 
		}
	}

}
