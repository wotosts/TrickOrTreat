package TeamP;

import java.awt.Point;
import java.util.Random;

import javax.swing.ImageIcon;

public class MapNode {
	//�� ���� ���
	public boolean[][] node = new boolean[9][9];	// ��� ������ ��� ����
	private int[][] edge = new int[81][81];
	public ImageIcon[] ob = new ImageIcon[7];		// ��ֹ�
	public ImageIcon exit = new ImageIcon(".\\img\\exit.png");
	public int[] num_ob = new int[7];		// ��ֹ� ����

	private int[] falseNode;		// 2������ 1�������� �ٲپ� ���ͽ�Ʈ��
	private boolean[] visitNode;	// ���ͽ�Ʈ�� ���̴� �������� �湮����
	private int[] touch;
	private int[] length;
	public int[] moveNode;			// ��������
	private int exitNode;
	public int exit_i;
	public int exit_j;

	MapNode()
	{
		//��� �ʱ�ȭ
		for(int i=0;i<9;i++)
			for(int j=0;j<9;j++)
				node[i][j] = false;

		//������� �ʱ�ȭ
		for(int i=0;i<81;i++)
		{
			for(int j=0;j<81;j++)
			{
				edge[i][j] = 1000;
			}
		}
		for(int i=0;i<81;i++)
		{
			edge[i][i] = 0;	// �밢��

			// �⺻
			if((i+1)%9 != 0)
			{
				edge[i][i+1] = 1;
				edge[i+1][i] = 1;
			}
			if(i+9 < 81)
			{
				edge[i][i+9] = 1;
				edge[i+9][i] = 1;
				if((i%9) == 1)
					edge[i][i+9] = 4;
				else if((i%9) == 5)
					edge[i][i+9] = 3;
				else if((i%9) == 7)
					edge[i][i+9] = 2;
			}

			//����ġ
			if(9 <= i && i < 17) edge[i][i+1] = 4;
			else if(45 <= i && i < 53) edge[i][i+1] = 3;
			else if(63 <= i && i < 71) edge[i][i+1] = 2;
		}

		//�̹���
		ob[0] = new ImageIcon(".\\img\\obstacle\\ob1.png");
		ob[1] = new ImageIcon(".\\img\\obstacle\\ob2.png");
		ob[2] = new ImageIcon(".\\img\\obstacle\\ob3.png");
		ob[3] = new ImageIcon(".\\img\\obstacle\\ob4.png");
		ob[4] = new ImageIcon(".\\img\\obstacle\\ob5.png");
		ob[5] = new ImageIcon(".\\img\\obstacle\\ob6.png");
		ob[6] = new ImageIcon(".\\img\\obstacle\\ob7.png");

		for(int i=0;i<7;i++) num_ob[i] = 2;
		num_ob[0] = 5;
		num_ob[4] = 1;
	}

	int countNode()		// �����ؾ��� ��� �� ���
	{
		int count = 0;
		//�ⱸ ����
		node[exitNode/9][exitNode%9] = false;
		node[0][0] = false;
		
		for(int i=0;i<9;i++)
			for(int j=0;j<9;j++)
			{
				if(node[i][j]) continue;		// ���Ϸ��� ��� Ž��
				if(j+1<9 && !node[i][j+1]) continue;
				if(j-1>0 && !node[i][j-1]) continue;
				if(i+1<9 && !node[i+1][j]) continue;
				if(i-1>0 && !node[i-1][j]) continue;

				node[i][j] = true;
			}

		for(int i=0;i<9;i++)				// ��� ������ ��� �� ī����
			for(int j=0;j<9;j++)
			{
				if(!node[i][j]) count++; 
			}

		return count;
	}
	void createFalseNode()		// �����ؾ��� ��� ��ȣ ���� �迭
	{
		falseNode = new int[countNode()];
		visitNode = new boolean[countNode()];
		int index = 0;

		for(int i=0;i<9;i++)
			for(int j=0;j<9;j++)
			{
				if(!node[i][j])
				{
					falseNode[index] = i*9 + j; 	// ����� ��ȣ�� ��常 ���� ���� �迭, ������ ����
					visitNode[index] = false;		// ���� ���� �迭�� �湮 ����
					index++;
				}
			}
		visitNode[0] = true;
	}

	// ���ͽ�Ʈ�� �˰���
	public boolean findLoad()
	{	
		createFalseNode();
		int count = countNode();	// �迭 ���鶧 ��

		int vnear = 0, min = 1000;
		touch = new int[count];		// ���� ����� ����
		length = new int[count];

		for(int i=1;i<count;i++)
		{
			touch[i] = 0;
			length[i] = edge[0][falseNode[i]];
		}
		for(int i=0;i<count-1;i++)
		{
			min = 1000;

			// �Ÿ� ���� ª�� ��� ����
			for(int j=1;j<count;j++)
			{
				if(!visitNode[j] && length[j] < min)	
				{
					min = length[j];
					vnear = j;
				}
			}

			// �鸥 ��� üũ
			visitNode[vnear] = true;

			// ����Ǿ����� ���� ���
			if(min == 1000) break;

			// �� ª�� �Ÿ��� �ִ� ���
			for(int j = 1;j<count;j++)
			{
				if((length[vnear] + edge[falseNode[vnear]][falseNode[j]]) < length[j])
				{
					length[j] = length[vnear] + edge[falseNode[vnear]][falseNode[j]];
					touch[j] = vnear;
				}
			}
		}

		int k=0;
		for(int i=countNode()-1;i>0;i--)
		{
			if(falseNode[i] == exitNode)
			{
				k = i;
				break;
			}
		}

		if(length[k] == 1000) return false; 
		else return true;
	}
	public int btracking()
	{
		if(!findLoad()) return 0;

		// ������
		int k = 0;
		int num = 0, final_len = 0;
		int count = 0, tempk = 0;

		for(int i=countNode()-1;i>0;i--)
		{
			if(falseNode[i] == exitNode)
			{
				//	            System.out.println(length[i]);
				final_len = length[i];
				k = i;
				tempk = i;
				break;
			}
		}
		while(k!=0)
		{
			num++;
			k = touch[k];
		}

		moveNode = new int[num];
		count = num;
		k = tempk;

		while(k!=0)
		{
			moveNode[count-1] = falseNode[k];
			count--;
			k = touch[k];
		}
		//	      System.out.print(" 0");      
		/*      for(int i=0;i<num;i++)
	      {
	         System.out.print(" "+moveNode[i]);
	      }*/
		return final_len;
	}
	//������ ��� ���
	public int[] selectNode(int x, int y)
	{
		int[] temp = new int[2];
		if((x<=827 && x>=72 ) && (y<=827 && y>=72))
		{
			x = x-72;	// ���� ��ǥ ����
			y = y-72;

			temp[0] = x/84;		//j
			temp[1] = y/84;		//i

			System.out.println("Node["+temp[1]+"]["+temp[0]+"], "+node[temp[1]][temp[0]]);
		}
		return temp;
	}

	public Point placeExit()		// �ⱸ ��ġ ����
	{
		node[0][0] = true;
		node[0][3] = true;
		node[0][7] = true;
		node[4][0] = true;
		node[8][3] = true;
		node[3][8] = true;

		Random ran = new Random();

		//������ �����ġ
		int i = ran.nextInt(2)+6;
		int j = ran.nextInt(2)+6;

		if(i>j)
		{
			i = 8;
			j = ran.nextInt(2)+6;
		}
		else 
		{
			i = ran.nextInt(2)+6;
			j = 8;
		}

		//��尡 ����
		node[i][j] = true;
		exitNode = i*9 + j;
		exit_i=i;
		exit_j=j;

		System.out.println("("+i+", "+j+")"+exitNode);

		//�׸��׸� ��ǥ�� ��ȯ
		Point place = new Point();
		place.x = j*84 + 72;
		place.y = i*84 + 72;

		return place;
	}


	public int selectOb(int x, int y)		// ��ֹ� ���� �Լ�
	{
		int index = -1;		// ��ֹ� ���� �ȵǾ��� ���� �ε���

		if(!((x<950 || x>1235) && (y<370 || y>825)))		// ��ֹ� �ε��� ����
		{
			if((1148<x && x<1148+80) && (373<y && y<373+80) && num_ob[0] != 0) index=0;
			if((948<x && x<958+160) && (373<y && y<373+80) && num_ob[1] != 0) index=1;
			if((968<x && x<968+80) && (473<y && y<473+160) && num_ob[2] != 0) index=2;
			if((1068<x && x<1068+160) && (673<y && y<673+160) && num_ob[3] != 0) index=3;
			if((1078<x && x<1078+160) && (473<y && y<473+160) && num_ob[4] != 0) index=4;
			if((958<x && x<958+160) && (663<y && y<663+160) && num_ob[5] != 0) index=5;
		}

		return index;
	}

	public Point placedOb(int index, int x, int y)		// ��ֹ� ���� ��ġ ���� �Լ�
	{
		Point infoP = new Point();		// �׸��׸� ��ǥ
		int[] info = new int[2]; 		// ��� ��ġ
		info = selectNode(x, y);
		infoP.x = -200;
		infoP.y = -200;

		// ��� ���Ұ��� ���� �� ���� ����
		if(index == 3 && ((info[0]+1<9) && (info[1]+1<9)))		// ��ֹ� 3, ���� ��尡 �ٸ�
		{
			if(node[info[1]+1][info[0]] || node[info[1]+1][info[0]+1] || node[info[1]][info[0]+1])
				return infoP;

			node[info[1]+1][info[0]] = true;
			node[info[1]+1][info[0]+1] = true;
			node[info[1]][info[0]+1] = true;
			num_ob[3]--;

			infoP.x = info[0]*84 + 72;		// �׸��׸� ó�� ��ǥ
			infoP.y = info[1]*84 + 72;

		}
		else if(!node[info[1]][info[0]])
		{

			if(index == 0)		// ��ֹ� 0
			{
				node[info[1]][info[0]] = true;
				num_ob[0]--;

				infoP.x = info[0]*84 + 72;		// �׸��׸� ó�� ��ǥ
				infoP.y = info[1]*84 + 72;
			}
			if(index == 1 && (info[0]+1<9))		// ��ֹ� 1
			{
				if(node[info[1]][info[0]+1])
					return infoP;
				node[info[1]][info[0]] = true;
				node[info[1]][info[0]+1] = true;
				num_ob[1]--;

				infoP.x = info[0]*84 + 72;		// �׸��׸� ó�� ��ǥ
				infoP.y = info[1]*84 + 72;
			}
			else if(index == 2 && (info[1]+1<9))		// ��ֹ� 2
			{
				if(node[info[1]+1][info[0]])
					return infoP;
				node[info[1]][info[0]] = true;
				node[info[1]+1][info[0]] = true;
				num_ob[2]--;

				infoP.x = info[0]*84 + 72;		// �׸��׸� ó�� ��ǥ
				infoP.y = info[1]*84 + 72;
			}
			else if((info[1]+1<9) && (info[0]+1<9))
			{
				if(index == 4)		// ��ֹ� 4
				{
					if(node[info[1]+1][info[0]] || node[info[1]+1][info[0]+1] || node[info[1]][info[0]+1])
						return infoP;

					node[info[1]][info[0]] = true;
					node[info[1]+1][info[0]] = true;
					node[info[1]+1][info[0]+1] = true;
					node[info[1]][info[0]+1] = true;
					num_ob[4]--;
				}
				else if(index == 5)		// ��ֹ�
				{
					if(node[info[1]][info[0]+1] || node[info[1]+1][info[0]])
						return infoP;
					node[info[1]][info[0]] = true;
					node[info[1]+1][info[0]] = true;
					node[info[1]][info[0]+1] = true;
					num_ob[5]--;
				}
				infoP.x = info[0]*84 + 72;		// �׸��׸� ó�� ��ǥ
				infoP.y = info[1]*84 + 72;
			}
		}

		return infoP;
	}
}
