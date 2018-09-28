package TeamP;

import java.awt.Point;
import java.util.Random;

import javax.swing.ImageIcon;

public class MapNode {
	//맵 구성 노드
	public boolean[][] node = new boolean[9][9];	// 사용 가능한 노드 여부
	private int[][] edge = new int[81][81];
	public ImageIcon[] ob = new ImageIcon[7];		// 장애물
	public ImageIcon exit = new ImageIcon(".\\img\\exit.png");
	public int[] num_ob = new int[7];		// 장애물 개수

	private int[] falseNode;		// 2차원을 1차원으로 바꾸어 다익스트라
	private boolean[] visitNode;	// 다익스트라에 쓰이는 정점들의 방문여부
	private int[] touch;
	private int[] length;
	public int[] moveNode;			// 역추적용
	private int exitNode;
	public int exit_i;
	public int exit_j;

	MapNode()
	{
		//노드 초기화
		for(int i=0;i<9;i++)
			for(int j=0;j<9;j++)
				node[i][j] = false;

		//인접행렬 초기화
		for(int i=0;i<81;i++)
		{
			for(int j=0;j<81;j++)
			{
				edge[i][j] = 1000;
			}
		}
		for(int i=0;i<81;i++)
		{
			edge[i][i] = 0;	// 대각선

			// 기본
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

			//가중치
			if(9 <= i && i < 17) edge[i][i+1] = 4;
			else if(45 <= i && i < 53) edge[i][i+1] = 3;
			else if(63 <= i && i < 71) edge[i][i+1] = 2;
		}

		//이미지
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

	int countNode()		// 포함해야할 노드 수 계산
	{
		int count = 0;
		//출구 포함
		node[exitNode/9][exitNode%9] = false;
		node[0][0] = false;
		
		for(int i=0;i<9;i++)
			for(int j=0;j<9;j++)
			{
				if(node[i][j]) continue;		// 아일랜드 노드 탐색
				if(j+1<9 && !node[i][j+1]) continue;
				if(j-1>0 && !node[i][j-1]) continue;
				if(i+1<9 && !node[i+1][j]) continue;
				if(i-1>0 && !node[i-1][j]) continue;

				node[i][j] = true;
			}

		for(int i=0;i<9;i++)				// 사용 가능한 노드 수 카운팅
			for(int j=0;j<9;j++)
			{
				if(!node[i][j]) count++; 
			}

		return count;
	}
	void createFalseNode()		// 포함해야할 노드 번호 넣은 배열
	{
		falseNode = new int[countNode()];
		visitNode = new boolean[countNode()];
		int index = 0;

		for(int i=0;i<9;i++)
			for(int j=0;j<9;j++)
			{
				if(!node[i][j])
				{
					falseNode[index] = i*9 + j; 	// 사용할 번호의 노드만 따로 모은 배열, 역추적 가능
					visitNode[index] = false;		// 따로 모은 배열의 방문 여부
					index++;
				}
			}
		visitNode[0] = true;
	}

	// 다익스트라 알고리즘
	public boolean findLoad()
	{	
		createFalseNode();
		int count = countNode();	// 배열 만들때 용

		int vnear = 0, min = 1000;
		touch = new int[count];		// 제일 가까운 정점
		length = new int[count];

		for(int i=1;i<count;i++)
		{
			touch[i] = 0;
			length[i] = edge[0][falseNode[i]];
		}
		for(int i=0;i<count-1;i++)
		{
			min = 1000;

			// 거리 제일 짧은 노드 잡음
			for(int j=1;j<count;j++)
			{
				if(!visitNode[j] && length[j] < min)	
				{
					min = length[j];
					vnear = j;
				}
			}

			// 들른 노드 체크
			visitNode[vnear] = true;

			// 연결되어있지 않은 경우
			if(min == 1000) break;

			// 더 짧은 거리가 있는 경우
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

		// 역추적
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
	//적절한 노드 계산
	public int[] selectNode(int x, int y)
	{
		int[] temp = new int[2];
		if((x<=827 && x>=72 ) && (y<=827 && y>=72))
		{
			x = x-72;	// 기준 좌표 수정
			y = y-72;

			temp[0] = x/84;		//j
			temp[1] = y/84;		//i

			System.out.println("Node["+temp[1]+"]["+temp[0]+"], "+node[temp[1]][temp[0]]);
		}
		return temp;
	}

	public Point placeExit()		// 출구 위치 랜덤
	{
		node[0][0] = true;
		node[0][3] = true;
		node[0][7] = true;
		node[4][0] = true;
		node[8][3] = true;
		node[3][8] = true;

		Random ran = new Random();

		//랜덤한 노드위치
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

		//노드가 막힘
		node[i][j] = true;
		exitNode = i*9 + j;
		exit_i=i;
		exit_j=j;

		System.out.println("("+i+", "+j+")"+exitNode);

		//그림그릴 좌표로 변환
		Point place = new Point();
		place.x = j*84 + 72;
		place.y = i*84 + 72;

		return place;
	}


	public int selectOb(int x, int y)		// 장애물 선택 함수
	{
		int index = -1;		// 장애물 선택 안되었을 때의 인덱스

		if(!((x<950 || x>1235) && (y<370 || y>825)))		// 장애물 인덱스 결정
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

	public Point placedOb(int index, int x, int y)		// 장애물 최종 위치 선정 함수
	{
		Point infoP = new Point();		// 그림그릴 좌표
		int[] info = new int[2]; 		// 노드 위치
		info = selectNode(x, y);
		infoP.x = -200;
		infoP.y = -200;

		// 노드 사용불가로 변경 및 갯수 차감
		if(index == 3 && ((info[0]+1<9) && (info[1]+1<9)))		// 장애물 3, 시작 노드가 다름
		{
			if(node[info[1]+1][info[0]] || node[info[1]+1][info[0]+1] || node[info[1]][info[0]+1])
				return infoP;

			node[info[1]+1][info[0]] = true;
			node[info[1]+1][info[0]+1] = true;
			node[info[1]][info[0]+1] = true;
			num_ob[3]--;

			infoP.x = info[0]*84 + 72;		// 그림그릴 처음 좌표
			infoP.y = info[1]*84 + 72;

		}
		else if(!node[info[1]][info[0]])
		{

			if(index == 0)		// 장애물 0
			{
				node[info[1]][info[0]] = true;
				num_ob[0]--;

				infoP.x = info[0]*84 + 72;		// 그림그릴 처음 좌표
				infoP.y = info[1]*84 + 72;
			}
			if(index == 1 && (info[0]+1<9))		// 장애물 1
			{
				if(node[info[1]][info[0]+1])
					return infoP;
				node[info[1]][info[0]] = true;
				node[info[1]][info[0]+1] = true;
				num_ob[1]--;

				infoP.x = info[0]*84 + 72;		// 그림그릴 처음 좌표
				infoP.y = info[1]*84 + 72;
			}
			else if(index == 2 && (info[1]+1<9))		// 장애물 2
			{
				if(node[info[1]+1][info[0]])
					return infoP;
				node[info[1]][info[0]] = true;
				node[info[1]+1][info[0]] = true;
				num_ob[2]--;

				infoP.x = info[0]*84 + 72;		// 그림그릴 처음 좌표
				infoP.y = info[1]*84 + 72;
			}
			else if((info[1]+1<9) && (info[0]+1<9))
			{
				if(index == 4)		// 장애물 4
				{
					if(node[info[1]+1][info[0]] || node[info[1]+1][info[0]+1] || node[info[1]][info[0]+1])
						return infoP;

					node[info[1]][info[0]] = true;
					node[info[1]+1][info[0]] = true;
					node[info[1]+1][info[0]+1] = true;
					node[info[1]][info[0]+1] = true;
					num_ob[4]--;
				}
				else if(index == 5)		// 장애물
				{
					if(node[info[1]][info[0]+1] || node[info[1]+1][info[0]])
						return infoP;
					node[info[1]][info[0]] = true;
					node[info[1]+1][info[0]] = true;
					node[info[1]][info[0]+1] = true;
					num_ob[5]--;
				}
				infoP.x = info[0]*84 + 72;		// 그림그릴 처음 좌표
				infoP.y = info[1]*84 + 72;
			}
		}

		return infoP;
	}
}
