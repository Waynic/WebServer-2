package my.webserver;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;





//jgoodies主题包
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
//import com.jgoodies.looks.windows.WindowsLookAndFeel;
//import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
//import com.jgoodies.looks.plastic.PlasticXPLookAndFeel;
import com.jgoodies.looks.plastic.theme.*;

public class Interface extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	public Interface(boolean[] status, int[] threadCount)
	{
		this.runFlag = status[0];
		//this.threadCount = threadCount;
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setVisible(true);
		
		GridBagLayout layout;
		
		//设置frame的宽度, 高度；由 platform选择窗口的位置；固定窗口大小
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLocationByPlatform(true);
		setResizable(true);
		
		//设置frame的icon和title
		//Image img = kit.getImage("icon.gif");
		//setIconImage(img);
		setTitle("WebServer");
		
		//设置菜单栏
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
		
		//设置内容面板
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(contentPane);
		
		layout = new GridBagLayout();
		contentPane.setLayout(layout);
		
		//ip文本框
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
		//port文本框
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
		//folder文本框
		JLabel folderLabel = new JLabel("Folder");
		folderText = new JTextField();
		//folderText.setText("Choose main folder");
		folderText.setEditable(false);
		//folderScrollPane = new JScrollPane(folderText);
		//folderScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		folderText.setBorder(BorderFactory.createEtchedBorder());//设置滚动栏边框
		//folderScrollPane.setPreferredSize(new Dimension(220, 150));
		folderBtn = new JButton("...");
		try {
			sltDir = new File(".").getCanonicalPath();//获得当前标准路径
			folderText.setText(sltDir);//默认当前路径
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		//“浏览”按钮
		folderBtn.setPreferredSize(new Dimension(20, 20));
		folderBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//if (StFlag) {
				//	JOptionPane.showMessageDialog(null, "服务器正在运行中，请关闭服务后再修改", "Error", 0);
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
		//log文本框（输出）
		JLabel logLabel = new JLabel("Log ");
		logText = new JTextArea();
		logText.setWrapStyleWord(true);
		logText.setLineWrap(true);
		logText.setEditable(false);
		//滚动条面板
		logScrollPane = new JScrollPane(logText);
		logScrollPane.setPreferredSize(new Dimension(logScrollPane.getWidth(), 150));
		//清空logText内容按钮
		cleanBtn = new JButton("<html><font size=3>Clean</font></html>");
		cleanBtn.setPreferredSize(new Dimension(60, 25));
		cleanBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logText.setText("");
				counter = 0;
			}
		});
		//应用当前设置按钮
		applyBtn = new JButton("<html><font size=3>Apply</font></html>");
		applyBtn.setPreferredSize(new Dimension(60, 25));
		applyBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//点击apply时将当前的设置应用
				if( isIP(ipText.getText()) )
				{	//如果IP框中输入的是合法ip地址格式
					goodIP = true; //IP合法
					curIPAddr = ipText.getText();
				}
				else
				{
					goodIP = false; //IP不合法
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
				{	//如果Port框中输入的是合法port
					goodPort = true; //port合法
					curPort = temp;
				}
				else
				{
					goodPort = false; //port不合法
					portText.setText("");
					JOptionPane.showMessageDialog(	JOptionPane.getRootFrame(), 
													"Invalid port number!", 
													"ERROR", 
													JOptionPane.ERROR_MESSAGE);
				}
				curDir = sltDir + "\\";
				if(setFlag = goodIP && goodPort)
					//如果ip port皆合法则设置应用成功
					applyBtn.setEnabled(false);
			}
		});
		//启动按钮
		startBtn = new JButton("<html><b><font size=3 color=green>Start</font></b></html>");
		startBtn.setPreferredSize(new Dimension(60, 25));
		startBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(runFlag == true)
				{	//如果服务正在进行，则此时为stop按钮
					status[0] = runFlag = false; //终止服务
					startBtn.setText("<html><b><font size=3 color=green>Start</font></b></html>");
					logText.append("Service stopped\n");
					ipText.setEnabled(true);
					portText.setEnabled(true);
					folderBtn.setEnabled(true);
				}
				else
				{	//如果服务不在进行，则此时为start按钮
					if(setFlag == true)
					{	//若已经设置ip和port
						ipText.setEnabled(false);
						portText.setEnabled(false);
						folderBtn.setEnabled(false);
						logText.append("Service started\n");
						status[0] = runFlag = true; //启动服务
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
		//状态栏
		//toolBar = new JToolBar();
		//JLabel label=new JLabel("状态栏");//("共有"+threadCount[0]+"个线程正在执行");
		//toolBar.add(label);//把标签加到工具栏上
		//toolBar.setFloatable(false);
		
		//将组件加入网格, 用到GBC帮助类(GBC.java)
		contentPane.add(ipLabel, new GBC(0, 0).setAnchor(GBC.WEST).setInsets(10,5,0,0));
		contentPane.add(ipText, new GBC(1, 0).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(10,2,2,2));
		contentPane.add(portLabel, new GBC(0, 1).setAnchor(GBC.WEST).setInsets(0,5,0,0));
		contentPane.add(portText, new GBC(1, 1).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(2));
		contentPane.add(folderLabel, new GBC(0, 2).setAnchor(GBC.WEST).setInsets(0,5,0,0));
		contentPane.add(folderText, new GBC(1, 2).setFill(GBC.HORIZONTAL).setWeight(100, 0).setInsets(2));
		contentPane.add(folderBtn, new GBC(2, 2).setAnchor(GBC.CENTER).setInsets(2,0,2,5));
		contentPane.add(logLabel, new GBC(0, 3).setAnchor(GBC.WEST).setInsets(10,5,0,0));
		contentPane.add(logScrollPane, new GBC(0, 4).setSpan(3, 1).setFill(GBC.BOTH).setWeight(100, 100).setInsets(0,5,0,5));
		contentPane.add(cleanBtn, new GBC(0, 5).setAnchor(GBC.CENTER).setInsets(5,5,10,0));
		contentPane.add(applyBtn, new GBC(1, 5).setAnchor(GBC.CENTER).setInsets(5,5,10,0));
		contentPane.add(startBtn, new GBC(1, 5).setSpan(2, 1).setAnchor(GBC.EAST).setInsets(5,0,10,5));
		//contentPane.add(toolBar,new GBC(0, 6).setSpan(3, 1).setAnchor(GBC.WEST).setInsets(15,5,0,5));
		
		//设置主题
		PlasticLookAndFeel.setPlasticTheme(new DesertBluer());
        try {
            //设置观感
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
	            // 定义正则表达式
	            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
	                    	 + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
	                    	 + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
	                    	 + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
	            // 判断ip地址是否与正则表达式匹配
	            if (text.matches(regex))
	            	return true;
		 }
		return false;
	}
	/*
	 * 获取应用的ip
	 */
	public String getIPAddr()
	{
		return curIPAddr;
	}
	/*
	 * 获取应用的端口
	 */
	public int getPort()
	{
		return curPort;
	}
	/*
	 * 获取应用的目录
	 */
	public String getFolder()
	{
		return curDir;
	}
	/*
	 * 获得logText
	 */
	public JTextArea getLog()
	{
		return logText;
	}
	/*
	 * 获得当前运行状态runFlag
	 */
	public boolean getRunFlag()
	{
		return runFlag;
	}
	/*
	 * 获得counter
	 */
	public int getCounter()
	{
		return counter;
	}
	public void incCounter()
	{
		++counter;
	}
	public void cleanCounter()
	{
		counter = 0;
	}
	private JPanel contentPane;
	private JTextField ipText;		//ip文本
	private JTextField portText;		//端口文本
	private JTextField folderText;	//文件路径文本
	private JTextArea logText;		//日志文本
	private JScrollPane logScrollPane;	//滚动条面板
	private JButton folderBtn;		//“浏览”按钮
	private JButton cleanBtn;		//清除log内容按钮
	private JButton applyBtn;		//“应用”按钮
	private JButton startBtn;		//“启动”按钮
	private JMenu setting;
	private JMenu help;
	private JFileChooser folderChooser;
	//private JToolBar toolBar;
	
	private int counter = 0; //进程序号
	private int curPort;		//当前应用的端口
	//private int[] threadCount;	//当前线程总数（只读）
	private String curIPAddr;	//当前应用的ip
	private String curDir;		//当前应用的目录
	private String sltDir;
	
	private boolean runFlag;		//服务正在进行
	private boolean setFlag = false;		//已设置好ip和端口
	private boolean goodIP = true; 		//当前输入ip是否合法
	private boolean goodPort = true;	//当前输入port是否合法
	
	private final int WINDOW_WIDTH = 320;
	private final int WINDOW_HEIGHT = 450;
}