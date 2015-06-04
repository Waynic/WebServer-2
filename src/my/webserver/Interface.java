package my.webserver;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;




//jgoodies�����
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
//import com.jgoodies.looks.windows.WindowsLookAndFeel;
//import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
//import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.*;

public class Interface {
	public Interface()
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				frame = new MyFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});//magic
	}
	/*
	 * ��ȡӦ�õ�ip
	 */
	public String getIPAddr()
	{
		return frame.curIPAddr;
	}
	/*
	 * ��ȡӦ�õĶ˿�
	 */
	public int getPort()
	{
		return frame.curPort;
	}
	/*
	 * ��ȡӦ�õ�Ŀ¼
	 */
	public String getFolder()
	{
		return frame.curDir;
	}
	/*
	 * ���logText
	 */
	public JTextArea getLog()
	{
		return frame.logText;
	}
	private MyFrame frame;
}

class MyFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	public MyFrame()
	{
		GridBagLayout layout;
		
		//����frame�Ŀ��, �߶ȣ��� platformѡ�񴰿ڵ�λ�ã��̶����ڴ�С
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocationByPlatform(true);
		setResizable(false);
		
		//����frame��icon��title
		//Image img = kit.getImage("icon.gif");
		//setIconImage(img);
		setTitle("WebServer");
		
		//���ò˵���
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		setting = new JMenu("Setting");
		menuBar.add(setting);
		JMenuItem start = new JMenuItem("Start");
		setting.add(start);
		JMenuItem turn = new JMenuItem("Stop");
		setting.add(turn);
		JMenuItem quit = new JMenuItem("Quit");
		setting.add(quit);
		help = new JMenu("Help");
		menuBar.add(help);
		JMenuItem about = new JMenuItem("About");
		help.add(about);
		
		//�����������
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(contentPane);
		
		layout = new GridBagLayout();
		contentPane.setLayout(layout);
		
		//ip�ı���
		JLabel ipLabel = new JLabel("IP");
		ipText = new JTextField("127.0.0.1");
		ipText.setEditable(true);
		ipText.setBorder(BorderFactory.createEtchedBorder());
		ipText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				applyBtn.setEnabled(true);
			}
		});
		//port�ı���
		JLabel portLabel = new JLabel("Port");
		portText = new JTextField("6789");
		portText.setEditable(true);
		portText.setBorder(BorderFactory.createEtchedBorder());
		portText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				applyBtn.setEnabled(true);
			}
		});
		//folder�ı���
		JLabel folderLabel = new JLabel("Folder");
		folderText = new JTextArea();
		//folderText.setText("Choose main folder");
		folderText.setEditable(false);
		folderScrollPane = new JScrollPane(folderText);
		folderScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		folderScrollPane.setBorder(BorderFactory.createEtchedBorder());//���ù������߿�
		//folderScrollPane.setPreferredSize(new Dimension(220, 150));
		folderBtn = new JButton("...");
		try {
			sltDir = new File(".").getCanonicalPath();//��õ�ǰ��׼·��
			folderText.setText(sltDir);//Ĭ�ϵ�ǰ·��
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		//���������ť
		folderBtn.setPreferredSize(new Dimension(20, 20));
		folderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//if (StFlag) {
				//	JOptionPane.showMessageDialog(null, "���������������У���رշ�������޸�", "Error", 0);
				//	return;
				//}				
				folderChooser = new JFileChooser();
				folderChooser.setCurrentDirectory(new File("."));
				folderChooser.setDialogTitle("Select a folder");
				folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				folderChooser.setAcceptAllFileFilterUsed(false);				
				if (folderChooser.showOpenDialog(folderBtn) == JFileChooser.APPROVE_OPTION) {
					sltDir = folderChooser.getSelectedFile().toString();
					folderText.setText(sltDir);
				}
				applyBtn.setEnabled(true);
			}
		});
		//log�ı��������
		JLabel logLabel = new JLabel("Log ");
		logText = new JTextArea();
		logText.setWrapStyleWord(true);
		logText.setLineWrap(true);
		logText.setEditable(false);
		//���������
		logScrollPane = new JScrollPane(logText);
		logScrollPane.setPreferredSize(new Dimension(220, 150));
		//���logText���ݰ�ť
		cleanBtn = new JButton("<html><font size=2>Clean</font></html>");
		cleanBtn.setPreferredSize(new Dimension(60, 25));
		cleanBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logText.setText("");
			}
		});
		//Ӧ�õ�ǰ���ð�ť
		applyBtn = new JButton("<html><font size=2>Apply</font></html>");
		applyBtn.setPreferredSize(new Dimension(60, 25));
		applyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//���applyʱ����ǰ������Ӧ��
				if( isIP(ipText.getText()) )
				{	//���IP����������ǺϷ�ip��ַ��ʽ
					goodIP = true; //IP�Ϸ�
					curIPAddr = ipText.getText();
				}
				else
				{
					goodIP = false; //IP���Ϸ�
					ipText.setText("");
					JOptionPane.showMessageDialog(	JOptionPane.getRootFrame(), 
												  	"Invalid IP address!", 
												  	"ERROR", 
												  	JOptionPane.ERROR_MESSAGE);					
				}
				int temp;
				if( portText.getText().length() < 7
						&& portText.getText().matches("[0-9]+")
						&& (temp = Integer.parseInt(portText.getText().trim())) > 0 
						&& temp < 65536 )
				{	//���Port����������ǺϷ�port
					goodPort = true; //port�Ϸ�
					curPort = temp;
				}
				else
				{
					goodPort = false; //port���Ϸ�
					portText.setText("");
					JOptionPane.showMessageDialog(	JOptionPane.getRootFrame(), 
													"Invalid port number!", 
													"ERROR", 
													JOptionPane.ERROR_MESSAGE);
				}
				curDir = sltDir + "\\";
				if(setFlag = goodIP && goodPort)
					//���ip port�ԺϷ�������Ӧ�óɹ�
					applyBtn.setEnabled(false);
			}
		});
		//������ť
		startBtn = new JButton("<html><b><font size=3 color=green>Start</font></b></html>");
		startBtn.setPreferredSize(new Dimension(60, 25));
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(runFlag)
				{	//����������ڽ��У����ʱΪstop��ť
					runFlag = false; //��ֹ����
					startBtn.setText("<html><b><font size=3 color=green>Start</font></b></html>");
					logText.append("Stoping...\n");
				}
				else
				{	//��������ڽ��У����ʱΪstart��ť
					if(setFlag)
					{	//���Ѿ�����ip��port
						logText.append("Starting...\n");
						runFlag = true; //��������
						startBtn.setText("<html><b><font size=3 color=red>Stop</font></b></html>");
					}
					else
					{
						JOptionPane.showMessageDialog(	JOptionPane.getRootFrame(), 
								"Apply your settings first!", 
								"ERROR", 
								JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		//�������������, �õ�GBC������(GBC.java)
		contentPane.add(ipLabel, new GBC(0, 0).setAnchor(GBC.WEST).setInsets(0,5,0,0));
		contentPane.add(ipText, new GBC(1, 0).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(2));
		contentPane.add(portLabel, new GBC(0, 1).setAnchor(GBC.WEST).setInsets(0,5,0,0));
		contentPane.add(portText, new GBC(1, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(2));
		contentPane.add(folderLabel, new GBC(0, 2).setAnchor(GBC.WEST).setInsets(0,5,0,0));
		contentPane.add(folderScrollPane, new GBC(1, 2).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(2));
		contentPane.add(folderBtn, new GBC(2, 2).setAnchor(GBC.CENTER).setInsets(2,0,2,5));
		contentPane.add(logLabel, new GBC(0, 3).setAnchor(GBC.WEST).setInsets(20,5,0,0));
		contentPane.add(logScrollPane, new GBC(0, 4).setSpan(3, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(0,5,0,5));
		contentPane.add(cleanBtn, new GBC(0, 5).setAnchor(GBC.CENTER).setInsets(5,5,0,0));
		contentPane.add(applyBtn, new GBC(1, 5).setAnchor(GBC.CENTER).setInsets(5,5,0,0));
		contentPane.add(startBtn, new GBC(1, 5).setSpan(2, 1).setAnchor(GBC.EAST).setInsets(5,0,0,5));
		
		//��������
		PlasticLookAndFeel.setPlasticTheme(new DesertBluer());
        try {
            //���ù۸�
            UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
            //UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
            //UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticLookAndFeel");
            //UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
            //UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {}
	}
	
	private boolean isIP(String text)
	{
		 if (text != null && !text.isEmpty()) {
	            // ����������ʽ
	            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
	                    	 + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
	                    	 + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
	                    	 + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
	            // �ж�ip��ַ�Ƿ���������ʽƥ��
	            if (text.matches(regex))
	            	return true;
		 }
		return false;
	}
	
	private JPanel contentPane;
	public JTextField ipText;		//ip�ı�
	public JTextField portText;		//�˿��ı�
	public JTextArea folderText;	//�ļ�·���ı�
	public JTextArea logText;		//��־�ı�
	private JScrollPane logScrollPane;	//���������
	private JScrollPane folderScrollPane;
	private JButton folderBtn;		//���������ť
	private JButton cleanBtn;		//���log���ݰ�ť
	private JButton applyBtn;		//��Ӧ�á���ť
	private JButton startBtn;		//����������ť
	private JMenu setting;
	private JMenu help;
	private JFileChooser folderChooser;
	
	public String curIPAddr;	//��ǰӦ�õ�ip
	public int curPort;		//��ǰӦ�õĶ˿�
	public String curDir;		//��ǰӦ�õ�Ŀ¼
	private String sltDir;
	
	public boolean runFlag = false;		//�������ڽ���
	public boolean setFlag = false;		//�����ú�ip�Ͷ˿�
	private boolean goodIP = true; 		//��ǰ����ip�Ƿ�Ϸ�
	private boolean goodPort = true;	//��ǰ����port�Ƿ�Ϸ�
	
	final int WINDOW_WIDTH = 260;
	final int WINDOW_HEIGHT = 360;
}
