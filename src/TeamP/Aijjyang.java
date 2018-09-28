package TeamP;



import javax.swing.ImageIcon;

public class Aijjyang extends Thread{

	public ImageIcon[] ai = new ImageIcon[5];		// ����»
	public int x=0,y=0;//����» ���ִ� ���
	private int aiMotion=0;//����»�� ���� 0~4
	public int mx=0,my=0;//�ɾ�� ���
	public int aiWayX[]=null;//����»�� �� ���x
	public int aiWayY[]=null;//����»�� �� ���y
	private boolean start=true;//��������
	private boolean up=false,down=false,left=false,right=false;
	public boolean f_clear = false;
	
	Aijjyang(){
		ai[0] = new ImageIcon(".\\img\\char_front.png");
		ai[1] = new ImageIcon(".\\img\\char_walk1.png");
		ai[2] = new ImageIcon(".\\img\\char_walk2.png");
		ai[3] = new ImageIcon(".\\img\\char_walk3.png");
		ai[4] = new ImageIcon(".\\img\\char_walk4.png");
	}
	
	int getX(){return x;}
	int getY(){return y;}
	int getAiMotion(){ return aiMotion;}
	void setX(int px){x=px;}
	void setY(int py){y=py;}
//	void setStart(boolean s){start=s;}
	void incMotion(){ //��� ����
		if(aiMotion>=8)
			aiMotion=2;
		else
			aiMotion++;
		
		//System.out.println("aiMotion="+aiMotion);
	}
	
	void resetAI(){//����» ����
		x=0;y=0;aiMotion=0;mx=0;my=0;aiWayX=null;aiWayY=null;start=true;up=false;down=false;left=false;right=false;
	}

	void setMoveNode(int arry[]){//moveNode�迭�� �޾ƿͼ� ����»�� �� ���� ����.
		if(arry!=null){
			
			for(int i=0;i<arry.length;i++){
				System.out.print(arry[i]+",");
				
			}
			System.out.println("");
			for(int i=0;i<arry.length;i++){
				System.out.print(arry[i]%9+"/"+arry[i]/9+", ");
				
			}
			System.out.println("");
			
			int length=arry.length;
			aiWayX=new int[length+1];
			aiWayY=new int[length+1];
			aiWayX[0]=0;aiWayY[0]=0;
			for(int i=0;i<arry.length;i++){
				aiWayX[i+1]=(arry[i]%9)*84;
				aiWayY[i+1]=(arry[i]/9)*84;
			}
		}
		start=true;
	}

	public void run(){
		for(int i=0;i<aiWayX.length-1;i++)
		{
			for(float a=0;a<=1.0;a+=0.1f)
			{
				mx = (int)(aiWayX[i]*(1-a) + aiWayX[i+1]*a);
				my = (int)(aiWayY[i]*(1-a) + aiWayY[i+1]*a);
				//System.out.println(mx+", "+my);
				incMotion();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		f_clear = true;
		System.out.println(f_clear);
	}
}
