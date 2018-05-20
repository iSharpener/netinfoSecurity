package netinfoSecurity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

public class rule_mate {
	private ArrayList<rule> rules;
	private ArrayList<String> lines;
	private ArrayList<String> outputs;		
	private ArrayList<String> durations;   //通过简单的一个数据类型模拟数据库，其中存放中间信息和中间结果
	private ArrayList<String> antidurations;
	private HashSet<String> outcome;
	public rule_mate() {
		try {
			initdata();
			initoutput();
			gethash();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	printrules();
	}
	/**
	 * 获取每条规则的结果
	 */
	public void gethash() {
		outcome = new HashSet<String>();
		for(int i =0;i<rules.size();i++)
			outcome.add(rules.get(i).getOutput());
	}
	/**
	 * 生成知识库
	 * @return
	 * @throws IOException
	 */
	public boolean initdata() throws IOException{
		rules = new ArrayList<rule>();
		lines = new ArrayList<String>();
		File filename = new File("rules.txt");
		InputStreamReader ir = new InputStreamReader(new FileInputStream(filename));
		BufferedReader br = new BufferedReader(ir);
		String line = br.readLine();
		while((line = br.readLine())!= null) {
	//		System.out.println(line);
			String[] sp = line.split("=>");
			String ifstate = sp[0];
			String outcome = sp[1];
//			System.out.println(sp[0]);
//			System.out.println(sp[1]);
			rule s = new rule();
			ArrayList<String> r = new ArrayList<String>();
			String[] ifs = ifstate.split("\\|");
			for(int i = 0;i<ifs.length;i++) {
				r.add(ifs[i]);
			//	System.out.println(ifs[i]);
			}
			s.setIfstate(r);
			s.setOutput(outcome);
			rules.add(s);
			lines.add(line);
		}
		return true;
	}
	/**
	 * 生成最终结果的知识库
	 * @return
	 * @throws IOException
	 */
	public boolean initoutput() throws IOException{
		outputs = new ArrayList<String>();
		File filename = new File("animal.txt");
		InputStreamReader ir = new InputStreamReader(new FileInputStream(filename));
		BufferedReader br = new BufferedReader(ir);
		String line = br.readLine();
		while((line = br.readLine())!= null) {
			outputs.add(line);
		}
		return true;
	}
	public void printrules() {
		for(int i=0;i<rules.size();i++) {
			ArrayList<String> ifs = rules.get(i).getIfstate();
			for(int j=0;j<ifs.size();j++)
				System.out.println(ifs.get(j));
			String outcome = rules.get(i).getOutput();
			System.out.println(outcome);
			System.out.println();
		}
	}
	public boolean isChild(ArrayList<String> firstli,ArrayList<String> secondli) {
		boolean status = true;
		for(int i=0;i<firstli.size();i++) {
			if(!secondli.contains(firstli.get(i))) {
				status = false;
			}
		}
		return status;
	}
	/**
	 * 推理机，正向推理得出中间结果和最后结果
	 * @param list 返回推理结果
	 * @return
	 * @throws IOException 
	 */
	public ArrayList<String> mate(ArrayList<String> list) throws IOException {
		initdata();
		initoutput();
		gethash();
		boolean flag = false;
		durations = new ArrayList<String>();
		while(!flag) {
			int num = 0;
			for(int i=0;i<rules.size();i++) {
				ArrayList<String> ifstate = rules.get(i).getIfstate();
				if(isChild(ifstate, list)) {
					if(!list.contains(rules.get(i).getOutput())) {
						String s ="";
						for(int k = 0;k<list.size();k++) {
							if(k!=list.size()-1) {
								s+=list.get(k)+"|";
							}
							else {
								s+=list.get(k);
							}
						}
						durations.add(s+" 匹配 "+lines.get(i));
						durations.add("加入"+rules.get(i).getOutput());
//						System.out.println(s+" 匹配 "+lines.get(i));
//						System.out.println("加入"+rules.get(i).getOutput());
						list.add(rules.get(i).getOutput());
						rules.remove(i);
						lines.remove(i);
						num++;
					}
				}
			}
			if(num == 0) {
				flag = true;
			}
		}
		if(durations.size()==0)
			durations.add("没有任何匹配结果，推理机无法进行推理，请尝试重新输入");
		return recommend(list);
	}
	public boolean isinOutcome(ArrayList<String> ifs) {
		for(int i=0;i<ifs.size();i++) {
			if(outcome.contains(ifs.get(i)))
				return true;
		}
		return false;
	}
	/**
	 * 反向推理机
	 * @param str
	 * @return
	 * @throws IOException 
	 */
	public ArrayList<String> antimate(String str) throws IOException {
		initdata();
		initoutput();
		gethash();
		boolean flag = false;
		ArrayList<String> iflist = new ArrayList<String>();
		iflist.add(str);
		ArrayList<String> ifli = new ArrayList<String>();
		ArrayList<String> ifli2 = new ArrayList<String>();
		ifli.add(str);
		antidurations = new ArrayList<String>();
		while(!flag) {
			for(int i =0;i<rules.size();i++) {
				String outcome = rules.get(i).getOutput();
				for(int k =0;k<iflist.size();k++) {
					if(iflist.get(k).equals(outcome)) {
							String ifstate = "";
							for(int j=0;j<rules.get(i).getIfstate().size();j++) {
								ifstate+=rules.get(i).getIfstate().get(j)+"、";
								iflist.add(rules.get(i).getIfstate().get(j));
								ifli.add(rules.get(i).getIfstate().get(j));
								ifli2.add(rules.get(i).getIfstate().get(j));
							}
							String duration = iflist.get(k)+"=>"+ifstate;
						//	System.out.println(duration);
							antidurations.add(duration);
							rules.remove(i);
							ArrayList<String> m = new ArrayList<String>();
							for(int o =0;o<rules.size();o++)
								m.add(rules.get(o).getOutput());
							if(!m.contains(iflist.get(k)))
								ifli.remove(iflist.get(k));
						}
				}
			}
			if(!isinOutcome(ifli))
				flag = true;
		}
		return ifli2;
	}
	/**
	 * 返回推荐结果
	 * @param output
	 * @return
	 */
	public ArrayList<String> recommend(ArrayList<String> output) {
		//打印推理结果
		int status = 0;
		for(int j = 0;j<outputs.size();j++) {
			if(output.contains(outputs.get(j))) {
				output.add("推荐推理结果为"+outputs.get(j));
			//	System.out.println("推荐推理结果为"+outputs.get(j));
				status = 1;
			}
		}
		if(status == 0)
			output.add("没有最终推荐结果，请参考上述结果");
		return output;
	}
	public static void main(String[] args) throws IOException {
		rule_mate test = new rule_mate();
		ArrayList<String> li = new ArrayList<String>();
		li.add("有奶");
		li.add("有蹄");
		li.add("脖子有黄褐色暗斑点");
//		ArrayList<String> li2 = new ArrayList<String>();
//		li2.add("有奶");
//		li2.add("有蹄");
//		System.out.println(test.isChild(li2, li));
		//接受一个参数ArrayList<String>，进行正向推理，返回正向推理结果，并使用推荐函数进行处理,得到
		ArrayList<String> output = test.mate(li);
		System.out.println();
		//得到中间推理步骤和结果
		ArrayList<String> dur = test.durations;
		
		//打印正向推理中间步骤
		for(int m =0;m<dur.size();m++) {
			System.out.println(dur.get(m));
		}
		//打印正向推理中间结果
		System.out.println();
		for(int n =0;n<output.size();n++) {
			System.out.println(output.get(n));
		}
		
		System.out.println();
		//反向推理
		ArrayList<String> s = test.antimate("长颈鹿");
		//反向推理中间步骤及过程
		for(int i=0;i<test.antidurations.size();i++)
			System.out.println(test.antidurations.get(i));
		//反向推理结果
		System.out.println();
		for(int i =0;i<s.size();i++)
			System.out.println(s.get(i));
		
	}
}
