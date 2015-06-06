package my.webserver;

import java.awt.EventQueue;
import java.io.*;
import java.net.*;

import javax.swing.JFrame;

public class WebServer {

	public static void main(String[] args) throws IOException 
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{		
				config = new Interface(status, threadCount);
				config.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				config.setVisible(true);
			}
		});
		//System.out.println("1");
		ServerSocket welcomeSocket = null;
		Socket connectionSocket = null;
		
		try
		{
			while(true) {
				EventQueue.getCurrentEvent();
				//����������䣨�ĳɿ���̨������ʲôҲ�У������config�ǿ�û��invoke����
				//����֪��Ϊʲô���������¼������й�_(:�١���)_
				if(status[0] == true)
				{	//���������������
					if(welcomeSocket == null)
					{	//�����δ��������
						//System.out.println("");
						welcomeSocket = 
								new ServerSocket(	config.getPort(), 
													0, 
													InetAddress.getByName (config.getIPAddr())
													);//�˿ڣ����г��ȣ��󶨵�ַ
					}//System.out.println("1");
					connectionSocket = welcomeSocket.accept();					
					config.incCounter();//counter++
					new ChildThread(connectionSocket, 
									config.getFolder(), 
									config.getLog(),  
									config.getCounter(),
									threadCount,
									status
									).start();//�½��߳�
					
				}
				else if(welcomeSocket != null)
				{	//�����ǰ������������֮ǰ����������
					welcomeSocket.close();//�ر����ӣ�close֮�����Ϊnull��
					config.cleanCounter();
					System.gc();
				}
			}
		}
		catch(IOException ex)
		{
			System.err.println(ex);
		}
		finally
		{
			if(welcomeSocket != null)
			{
				welcomeSocket.close();
				connectionSocket.close();
			}
		}
	}
	private static Interface config;
	private static boolean[] status = {false};
	private static int[] threadCount = {0};	//�߳�����
}
