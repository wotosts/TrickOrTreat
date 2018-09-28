package TeamP;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class MapOb
{
	int ob_index;
	Point ob_point;
	MapOb(int i, Point p)
	{
		ob_index = i;
		ob_point = p;
	}
}
public class MyFrame extends JFrame implements ActionListener{

	private ImageIcon img_field = null;		// 맵 이미지
	private ImageIcon img_user = null;			// 캐릭터
	private Image img_icon = null;
	private ImageIcon img_clear = new ImageIcon(".\\img\\clear.png");
	private ImageIcon img_fail = new ImageIcon(".\\img\\fail.png");
	private JPanel p;
	private JPanel p_ob;
	private Toolkit kit;
	private Dimension f;
	private JButton s_btn;
	private JButton r_btn;

	private MapNode m = new MapNode();
	private int lim=0,use=0;        //횟수
	private int index = -1;			//선택한 장애물 
	private Point ob_p = new Point();				//현재 마우스 좌표
	Point temp = new Point();   

	private JPanel p_paint;
	LinkedList <MapOb>ob_l = new LinkedList();
	MapOb onMapOb;

	private Aijjyang aI = new Aijjyang();

	boolean isDragging = false;
	boolean fail = false;
	boolean clear = false;
	boolean start = false;
	Point p_start = new Point();
	Point p_end = new Point();

	MyFrame()
	{
		img_field = new ImageIcon(".\\img\\map_full.png");
		img_user = new ImageIcon(".\\img\\char_front.png");
		Random rand = new Random();
		lim = rand.nextInt(15)+18;
		Point exit_d = m. placeExit();


		p = new JPanel()
		{		
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.drawImage(img_field.getImage(), 0, 0, f.width, f.height, this);
				g.drawImage(m.exit.getImage(), (int)exit_d.getX(), (int)exit_d.getY(), 80, 80, null);

				
				//고정장애물
				g.drawImage(m.ob[6].getImage(), 326, 74, 80, 80, null);
				g.drawImage(m.ob[6].getImage(), 746, 326, 80, 80, null);
				g.drawImage(m.ob[6].getImage(), 326, 746, 80, 80, null);
				g.drawImage(m.ob[6].getImage(), 74, 410, 80, 80, null);
				g.drawImage(m.ob[6].getImage(), 662, 74, 80, 80, null);


				g.setFont(new Font("",Font.BOLD,100));  //폰트
				g.setColor(Color.ORANGE);  //색
				g.drawString(Integer.toString(lim), 980, 188);  //str,글씨 바닥을 기준으로 시작점x,천장부터 글씨 바닥까지
	            g.drawString(Integer.toString(use), 1180, 188);

				if(index != -1)
				{
					if(index == 0)
					{
						g.drawImage(m.ob[index].getImage(), (int)ob_p.getX(), (int)ob_p.getY(), 80, 80, null);
						// 이미지 저장
					}
					else if(index == 1)
					{
						g.drawImage(m.ob[index].getImage(), (int)ob_p.getX(), (int)ob_p.getY(), 160, 80, null);
					}
					else if(index == 2)
					{
						g.drawImage(m.ob[index].getImage(), (int)ob_p.getX(), (int)ob_p.getY(), 80, 160, null);
					}
					else
					{
						g.drawImage(m.ob[index].getImage(), (int)ob_p.getX(), (int)ob_p.getY(), 160, 160, null);
					}

				}
				
				p.requestFocus();
	            p.setFocusable(true);
			}
		};
		p.requestFocus();
		p.setFocusable(true);

		f = new Dimension();

		p.setLayout(null);
		playMusic(".\\bgm.wav", true);

		//변수에 화면크기 저장
		kit = Toolkit.getDefaultToolkit();
		f.setSize(1300, 900);

		//프레임 크기 및 제목 설정
		setTitle("trick or treat");

		//전체 패널 사이즈 설정
		p.setPreferredSize(new Dimension(1300, 900));

		//프레임에 패널 추가
		this.add(p);

		//프레임 아이콘 이미지 설정
		img_icon = InputImage(img_icon, ".\\img\\frame_Icon.png");
		this.setIconImage(img_icon);

		//창 크기 조절 유무 설정 및 창 띄우기
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		this.pack();
		setVisible(true);
	}

	public Image InputImage(Image img, String name)		// 이미지 불러오는 함수
	{
		img = kit.getImage(name);
		return img;
	}

	public void InGame()		// 인게임 화면
	{
		
		p_ob = new JPanel()
		{
			public void paint(Graphics g)
			{
				if(m.num_ob[0] != 0) g.drawImage(m.ob[0].getImage(), 250, 70, 80, 80, null);	//1x1
				if(m.num_ob[1] != 0) g.drawImage(m.ob[1].getImage(), 50, 70, 160, 80, null);	//1x2
				if(m.num_ob[2] != 0) g.drawImage(m.ob[2].getImage(), 70, 170, 80, 160, null);	//2x1
				if(m.num_ob[3] != 0) g.drawImage(m.ob[3].getImage(), 170, 370, 160, 160, null);	//2x2
				if(m.num_ob[4] != 0) g.drawImage(m.ob[4].getImage(), 180, 170, 160, 160, null);	//2x2
				if(m.num_ob[5] != 0) g.drawImage(m.ob[5].getImage(), 60, 360, 160, 160, null);	//2x2
			}
		};
		p_ob.setBounds(898,303,388,585);
		p.add(p_ob);

		p_paint = new JPanel()
		{
			public void paint(Graphics g)
			{
				//링크드리스트에 저장된 만큼 맵에 장애물 그리기
				for(int i = 0; i < ob_l.size(); i++)
				{
					ImageIcon ob_img = m.ob[ob_l.get(i).ob_index];
					int x = ob_l.get(i).ob_point.x;
					int y = ob_l.get(i).ob_point.y;
					g.drawImage(ob_img.getImage(), x, y, null);
				}

				Graphics2D g2 = (Graphics2D)g;
				g2.setColor(Color.orange);
				g2.setStroke(new BasicStroke(5,BasicStroke.CAP_ROUND,0));
				
				
				//img_user and Line

				
				if(start && m.findLoad())
				{
					for(int i=0; i<m.moveNode.length; i++)
					{

						p_end.x = m.moveNode[i]%9*84+114;
						p_end.y = m.moveNode[i]/9*84+114;

						if(i==0)
						{
							p_start.x=114;	p_start.y=114;
						}
						
						g2.drawLine(p_start.x, p_start.y, p_end.x, p_end.y);
						repaint();
						p_start.x = p_end.x;
						p_start.y = p_end.y;	

					}
				}

				// draw ai
				if(start && m.findLoad()){					
					g.drawImage(aI.ai[aI.getAiMotion()/2].getImage(), 89+aI.mx, 74+aI.my, 50, 80, null);//////////////////////////////////////////////////

				}
				else
					g.drawImage(aI.ai[0].getImage(), 89, 74, 50, 80, null);
				
	
				if(fail && aI.f_clear)
					g.drawImage(img_fail.getImage(), 0, 0, null);
				else if(aI.f_clear && clear)
					g.drawImage(img_clear.getImage(), 0, 0, 1300, 900, null);
			}
			
		};

		
		
		p_paint.setBounds(0,0,1300,900);
		p_paint.setOpaque(false);
		p.add(p_paint);


		s_btn = new JButton(new ImageIcon(".\\img\\start.png"));
		r_btn = new JButton(new ImageIcon(".\\img\\reset.png"));
		s_btn.setBounds(898,208,193,89);
		r_btn.setBounds(1097,208,189,89);

		//버튼 이벤트 추가
		s_btn.addActionListener(this);
		r_btn.addActionListener(this);

		//패널에 요소추가
		p.add(s_btn);
		p.add(r_btn);

		
		p.addMouseListener(new MouseAdapter()
		{   
			public void mousePressed(MouseEvent e)
			{
				if(SwingUtilities.isLeftMouseButton(e) == true)
				{
					index = m.selectOb(e.getX(), e.getY());      // 장애물 선택
					System.out.println(index);
					if(index==-1){isDragging = false;}
					else isDragging = true;
				}
			}
			public void mouseReleased(MouseEvent e)
			{
				System.out.println(e.getX()+", "+e.getY());

				if(isDragging)
				{
					if((e.getX()>827 || e.getX()<72 ) || (e.getY()>827 || e.getY()<72))
					{
						ob_p.x = -200;
						ob_p.y = -200;
						repaint();
						return;
					}
					Point temp = new Point();   
					temp = m.placedOb(index, e.getX(), e.getY());      // 노드 선택
					ob_p.x = (int) temp.getX();
					ob_p.y = (int) temp.getY();


					//장애물 인덱스,놓여진 위치 저장 후 링크드리스트에 추가
					onMapOb = new MapOb(index,temp);
					ob_l.add(onMapOb);

					repaint();
				}
			}
		});
		
		p.addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseDragged(MouseEvent e) {
				if(index == -1) return;

				Point temp = new Point();
				temp.x = m.ob[index].getIconWidth()/2;
				temp.y = m.ob[index].getIconHeight()/2;
				ob_p.x = (int)(e.getX() - temp.getX());
				ob_p.y = (int)(e.getY() - temp.getY());

				repaint();
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		});
		
		p.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && ob_l.size()>0){
					updateRemoveState();
					ob_l.removeLast();
					
					p.requestFocus();
					p.setFocusable(true);
				}
			}
		});
		setVisible(true);
	}

	@SuppressWarnings("deprecation")
	public void playMusic(String filename, boolean loop)
	{
		//사운드재생용 메소드.
		AudioClip clip;
		try {
			File file = new File(filename);
			clip = Applet.newAudioClip(file.toURL());
			clip.play();
			if(loop == true) clip.loop();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == s_btn) 
		{
			//JOptionPane.showMessageDialog(null, "start");
			s_btn.setActionCommand(null);

			int temp;
	        temp = m.btracking();
	        if(temp == 0)
	        {
	        	aI.f_clear = true;
	        	fail = true;
	        	repaint();
	        	return;
	        }
	      
	        start = true;
	        use = temp;
	        aI.setMoveNode(m.moveNode);
	        aI.start();
	        
	        if(use<=lim+1 && use>=lim-1) clear = true;
	        else fail = true;

	        repaint();
		}
		else if(e.getSource() == r_btn) 
		{
			//JOptionPane.showMessageDialog(null, "reset");
			r_btn.setActionCommand(null);
			start=false;
			for(int i=0;i<7;i++) m.num_ob[i] = 2;
			m.num_ob[0] = 5;
			m.num_ob[4] = 1;
			//장애물 개수 초기화
			
			ob_l.removeAll(ob_l);
			
			for(int i=0;i<9;i++)
				for(int j=0;j<9;j++)
					m.node[i][j] = false;
			//전체 노드 사용가능
			
			m.node[0][0] = true; //시작점
			m.node[0][3] = true;
			m.node[0][7] = true;
			m.node[4][0] = true;
			m.node[8][3] = true;
			m.node[3][8] = true; //고정장애물
			m.node[m.exit_i][m.exit_j] = true; //출구
			use=0; //이동거리
			
			aI = null;
			aI = new Aijjyang();
			//aI.resetAI();
			fail = false;
			clear = false;
		}
	}
	
	void updateRemoveState(){
		
		int y = (ob_l.getLast().ob_point.x - 72)/84;
		int x = (ob_l.getLast().ob_point.y - 72)/84;
		
		System.out.println("last:"+ob_l.getLast());
		
		System.out.println("ob_l.x:"+ob_l.getLast().ob_point.x);
		System.out.println("ob_l.y:"+ob_l.getLast().ob_point.y);
		
		System.out.println("x:"+x);
		System.out.println("y:"+y);

		switch(ob_l.getLast().ob_index){
			case 0:{
				m.node[x][y] = false;
				m.num_ob[0]++; break;
			}
			case 1:{
				m.node[x][y] = false;
				m.node[x][y+1] = false;
				m.num_ob[1]++; break;
			}
			case 2:{
				m.node[x][y] = false;
				m.node[x+1][y] = false;
				m.num_ob[2]++; break;
			}
			case 3:{
				m.node[x+1][y] = false;
				m.node[x+1][y+1] = false;
				m.node[x][y+1] = false;
				m.num_ob[3]++; break;
			}
			case 4:{
				m.node[x][y] = false;
				m.node[x+1][y] = false;
				m.node[x+1][y+1] = false;
				m.node[x][y+1] = false;
				m.num_ob[4]++; break;
			}
			case 5:{
				m.node[x][y] = false;
				m.node[x+1][y] = false;
				m.node[x][y+1] = false;
				m.num_ob[5]++; break;
			}
		}
		
	}
}