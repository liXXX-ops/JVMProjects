package jvm1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import jvm1.ToBytecode;
//装载器
public class LoadBytecode{
	private static Stack<Map<Integer,String>> stack = new Stack<>();

	private static String[][] form;
	private static int pc;
	public static Stack<Map<Integer,String>> Loading() throws IOException {
		pc=ToBytecode.Translate();      //获取PC的最大值，即字节码的个数
		form=ToBytecode.WriteToTxt();   //获取字节码的数组
		/*
		 * 前期测试代码
		Map<Integer,String> map = new HashMap<>();
		map.put(1, "lzx");
		stack.push(map);
		Map<Integer,String> maping= new HashMap<>();
		maping=stack.pop();
		Set<Integer> set = maping.keySet();   //Set获得到键值
		String integer=maping.get(1);          //get(k)获得到键值为key的String值
		System.out.println(set+"???"+integer);
		*/
		//由于栈的结构性质，逆序装入，保证执行时顺序
		Map<Integer,String> map = new HashMap<>();
		for(int i=pc/5-1;i>=0;i--)   //逆序装载
			for(int j=4;j>=0;j--) {
				map.put(5*i+j, form[i][j]);
				stack.push(map);   //PC与对应的值进栈
				map = new HashMap<>();
			}
		//for(int k=map.size()-1;k>=0;k--)
			//stack.push(map);
		//for(int k=map.size()-1;k>0;k--)
		//System.out.println(stack.pop());   //否则enginecode中返回过去的栈顶已经出栈
		return stack;
	}
}
