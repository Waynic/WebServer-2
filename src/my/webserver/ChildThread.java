package my.webserver;

import java.net.InetAddress;
import java.net.Socket;
import java.util.StringTokenizer;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextArea;


public class ChildThread extends Thread{
	
	int n;   //�������
	int[] count; //�߳���
	boolean[] isRun; //��ǰ������������
	String CRLF = "\r\n";
	Socket serverSocket; //�������˵��׽���
	String fileRoute;	//�ͻ��������ļ���·��
	String resultLine;	//������������Ĵ�����
	JTextArea text;		
	PrintStream output;	//�ͻؿͻ������ֽ���
	String requestLine;	//���������ĵ�������
	DateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	Date date = new Date();
	
	
	/* skt: �������׽���
	 * mainlist: ��������Ŀ¼
	 * log�� �����������ı���
	 * counter: ���������
	 */
	public ChildThread(	Socket skt, 
						String mainlist, 
						JTextArea log, 
						int threadNum, 
						int[] threadCount,
						boolean[] status ) throws IOException
	{
		serverSocket = skt;
		fileRoute = mainlist;
		text = log;
		n = threadNum;
		count = threadCount;
		//isRun = status;//ֻ��
		output = new PrintStream(serverSocket.getOutputStream());
	}
	public void run()
	{
		try	{
			//++count[0]; //�߳�������1
			//if(isRun[0] == true)
			//{
				InputStream byteStream = serverSocket.getInputStream(); //��ȡ�ֽ���
				InputStreamReader charStream = new  InputStreamReader(byteStream); //�ֽ���ת��Ϊ�ַ���
				BufferedReader input = new BufferedReader(charStream); //�ַ������뻺����
				requestLine = input.readLine(); //��ȡ�ͻ���������HTTP������
				String clientIP = serverSocket.getInetAddress().toString().substring(1); //��ȡ�ͻ���IP��ַ
				int clientPort = serverSocket.getPort();//��ȡ�ͻ����˿ں�
				String clientInf = new String("[Connection " + n + "]" + CRLF
												+ clientIP + ": " + clientPort 
												+ CRLF + requestLine + CRLF); //�ͻ�����Ϣ
				ProcessRequest(); //��ȡ����������������Ӧ���ĸ��ͻ���
				String inf = new String(clientInf + resultLine);
				text.append(inf);
				putLogToFile(inf);		//	����������־���ļ���
				text.setCaretPosition(text.getDocument().getLength());
			//}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void ProcessRequest() throws Exception {
		
		String headLine = null;     //������������Ӧ����
		//��Ӧ��������
		String statusLine = null;	//״̬��
		String closeLine = "Connection: close" + CRLF; 
		String timeLine = null;    //�ײ���Date��������Ӧ���ĵ�����
		String ServerLine = "Server: " + InetAddress.getLocalHost().getHostName() + CRLF;
		String lengthLine = null; //�ײ��� Content-Length�����Ͷ�����ֽ���
		String typeLine = null;	//�ײ��� Content-Type��ʵ�������ж�������
		String entityBody = null;	//ʵ������
		
		//�Կո��ַ��ֽ�������
		StringTokenizer token = new StringTokenizer(requestLine);
		String method = token.nextToken();
		
		if( method.equals("GET") || method.equals("HEAD")){
			
			fileRoute += token.nextToken().substring(1); //�ͻ�����������ڷ�������·��		
			
			try{
				FileInputStream fileStream = new FileInputStream(fileRoute); //���ļ�
				String type = getFileExtension(fileRoute);	//��ȡ�ļ���չ��

				//��Ӧ��������
				statusLine = "HTTP/1.1 200 OK" + CRLF;
				timeLine = "Date: " + fullDateFormat.format(date) + CRLF;
				lengthLine = "Content-Length: " + (new Integer(fileStream.available())).toString() + CRLF;
				typeLine = getContentType(type) + CRLF;
				headLine = statusLine + closeLine + timeLine + ServerLine + lengthLine + typeLine + CRLF;
				output.print(headLine);				
				
				if( !(method.equals("HEAD")) ){
					//����ʵ������
					byte[] buffer = new byte[1024];
					int bytes = 0;	
					while ((bytes = fileStream.read(buffer)) != -1) {
						output.write(buffer, 0, bytes);
					}
				}
				
				fileStream.close();
				resultLine = "Result: succeed!" + CRLF + CRLF;
			}
			catch(IOException e){
				//�ļ����쳣����
				statusLine = "HTTP/1.1 404 Not Found" + CRLF;
				timeLine = "Date: " + fullDateFormat.format(date) + CRLF;
				headLine = statusLine + closeLine + timeLine + ServerLine + CRLF;
				output.print(headLine);
				entityBody = "<HTML>" + "<HEAD><TITLE>404 Not Found</TITLE></HEAD>" +
						"<BODY>404 Not Found" + "</BODY></HTML>\n";
				output.write(entityBody.getBytes());
				resultLine = "Result: file is not found!" + CRLF + CRLF;
			}
		}
		
	}
	//��ȡ�ļ���չ��
	private static String getFileExtension(String fileName) {
		
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		} else {
			return "";
		}
	}
	//��ȡ Content��Type ����
	private  String getContentType(String suffix) {
		
		String contentType = null;
		switch(suffix){
		
			case "jpe":
			case "jpeg":
			case "jpg":
				contentType = "Content-Type: image/jpeg";
				break;
			case "txt":
			case "stm":
			case "htm":
			case "html":
				contentType = "Content-Type: text/html";
				break;
			case "css":
				contentType = "Content-Type: text/css";
				break;
			case "gif":
				contentType = "Content-Type: image/gif";
				break;
			case "png":
				contentType = "Content-Type: image/png";
				break;
			case "pdf":
				contentType = "Content-Type: application/pdf";
				break;
			case "doc":
			case "docx":
				contentType = "Content-Type: application/msword";
				break;
			case "mp3":
				contentType = "Content-Type: audio/mp3";
				break;
			default:
				resultLine = "�޷�ʶ���ļ���ʽ";
				break;
		}
		return contentType;
	}
	
	//����־д�ر����ļ�
	private void putLogToFile(String log) {
		
		String time = null;
		String route = "C:\\Users\\Tangyingjie\\Desktop\\log\\log.txt";
		try{
			
			FileWriter writer = new FileWriter(route, true);	//��������ڻᱻ����
			time = fullDateFormat.format(date) + CRLF;
			writer.write(time + log);
			writer.close();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	}
}

