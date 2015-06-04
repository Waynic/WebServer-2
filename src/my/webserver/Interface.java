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
	 * ��ȡ��ǰip�������ı�
	 */
	public String getIPAddr()
	{
		return frame.ipText.getText();
	}
	/*
	 * ��ȡ��ǰport�������ı�
	 */
	public String getPort()
	{
		return frame.portText.getText();
	}
	/*
	 * ��ȡ��ǰfolder����ı�
	 */
	public String getFolder()
	{
		return frame.folderText.getText();
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
		ipText = new JTextArea();
		ipText.setText("127.0.0.1");
		ipText.setEditable(true);
		ipText.setLineWrap(false);
		ipText.setBorder(BorderFactory.createEtchedBorder());
		//port�ı���
		JLabel portLabel = new JLabel("Port");
		portText = new JTextArea();
		portText.setText("6789");
		portText.setEditable(true);
		portText.setLineWrap(false);
		portText.setBorder(BorderFactory.createEtchedBorder());
		//folder�ı���
		JLabel folderLabel = new JLabel("Folder");
		folderText = new JTextArea();
		//folderText.setText("Choose main folder");
		folderText.setEditable(false);
		folderText.setLineWrap(false);
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
		//������ť
		startBtn = new JButton("<html><font size=2>Start</font></html>");
		startBtn.setPreferredSize(new Dimension(60, 25));
		
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
	private JPanel contentPane;
	public JTextArea ipText;		//ip�ı�
	public JTextArea portText;		//�˿��ı�
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
	private String sltDir;
	final int WINDOW_WIDTH = 260;
	final int WINDOW_HEIGHT = 360;
}
