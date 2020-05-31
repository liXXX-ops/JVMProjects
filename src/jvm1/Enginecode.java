package jvm1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
//执行引擎
public class Enginecode {
	//源码的错误性书写都是在ToBytecode中判断的，故人为stack中的内容均为正确的；
	
	private static Stack<Map<Integer,String>> stack;
	private static Stack<Integer> stackInt = new Stack<>();   //存放过程中处理的数字
	private static Stack<Character> stackChar = new Stack<>();
	private String []str_pushopera1 = {"LOAD","STORE","PUSH"};  //后面跟连续数字
	private String []str_pushopera2 = {"POP","DUP","SWAP"};     //后面跟不连续的数字
	private String []str_calculate = {"ADD","SUB","MUL","DIV"};
	private String []str_controlflow = {"IFEQ","IFNE"};
	private String []str_stop = {"HALT","NOP"};
	private static int PC;
	private static String progress="";
/*
 *遍历map栈然后找到数字与运算符，
 */	
	
	public static void engining() throws IOException {
		Map<Integer,String> map = new HashMap<>();
		stack = LoadBytecode.Loading();
		//System.out.println(stack.pop());
		int i=0;
		PC=0;
		while(i<stack.size()){
		//for(int i=0;i<stack.size();i++) {
			//System.out.println(stack.pop());
			map = stack.pop();
			String value = map.get(PC);
			Set<Integer> set = map.keySet();
			map = new HashMap<>();
			System.out.print(PC+"  "+i);
			
			System.out.println(value);
			switch (value) {
				case "LOAD":{  //LOAD  address(4bytes)说明：将指定地址的数据压栈，address是指定的地址。执行完该指令后栈顶加1，即sp=sp+1
					Stack<Map<Integer,String>>stack_temp = new Stack<>();
					PC+=4;
					i+=4;
					for(int t=0;t<3;t++)
						stack.pop();
					map = stack.pop();  //将map增加到-----将指定地址的数据压栈，address是指定的地址ַ
					int number = Integer.parseInt(map.get(PC));
					if(number<=PC) {  //转接要处理对应地址的数据
						System.out.println("ָ指定地址的数据已经处理");
					}else {           //转接地址在当前地址的后面
						for(int pc=PC;pc<number;pc++)    //取出指定位置的数据后将弹出的临时栈中的所有元素再押入到此栈中
							stack_temp.push(stack.pop());
						String str_temp = stack.pop().get(number);
						for(int pc=PC;pc<number;pc++) {
							stack.push(stack_temp.pop());
							PC++;
						}
						for(int t=0;t<str_temp.length();t++)   //在临时栈中找到指定地址的数据，并遍历该数据的每一个元素是否为数字
							if(str_temp.charAt(t)<'0'||str_temp.charAt(t)>'9') {
								System.out.println("指定地址的数据为非整数，处理失败");}
							else
								stackInt.push(Integer.parseInt(str_temp)); 
					}
					PC++;
					i++;
					
					//
					break;
				}
				case "STORE":{     //STORE:address(4bytes)说明：将栈顶元素放入指定的地址，然后弹栈，address是指定的地址。执行完该指令后，栈顶减1，即sp=sp-1
					Stack<Map<Integer,String>>stack_temp = new Stack<>();
					PC+=4;
					i+=4;
					for(int t=0;t<3;t++)
						stack.pop();
					map = stack.pop();  //将map增加到------将栈顶元素放入指定的地址，然后弹栈
					//
					
					int number = Integer.parseInt(map.get(PC));
					if(number<=PC) {  //转接要处理对应地址的数据
						System.out.println("ָ指定地址的数据已经处理");
					}else {           //转接地址在当前地址的后面
						for(int pc=PC;pc<number;pc++)    //取出指定位置的数据后将弹出的临时栈中的所有元素再押入到此栈中
							stack_temp.push(stack.pop());
						String str_temp = stack.pop().get(number);
						for(int pc=PC;pc<number;pc++) {
							stack.push(stack_temp.pop());
							PC++;
						}
						for(int t=0;t<str_temp.length();t++)   //在临时栈中找到指定地址的数据，并遍历该数据的每一个元素是否为数字
							if(str_temp.charAt(t)<'0'||str_temp.charAt(t)>'9') {
								System.out.println("指定地址的数据为非整数，处理失败");}
							else
								stackInt.push(Integer.parseInt(str_temp)); 
					}
					PC++;
					i++;
					break;
				}
				case "PUSH":{
					PC+=4;
					i+=4;
					for(int t=0;t<3;t++)
						stack.pop();
					map = stack.pop();
					int number = Integer.parseInt(map.get(PC));
					stackInt.push(number);
					
					PC++;
					i++;
					//System.out.println(number+stackInt.pop()); //使得add时栈中数据为空
					
					break;
					//return ;
					
				}
				case "DUP":{
					if(stackInt.size()<1) {
						System.out.println("程序在执行DUP指令时出现错误，请检查装载是否有误");
						return ;
					}
					int num=stackInt.pop();
					stackInt.push(num);
					stackInt.push(num);
					PC++;
					i++;
					break;
				}
				case "SWAP":{
					if(stackInt.size()<2) {
						System.out.println("程序在执行SWAP指令时出现错误，请检查装载是否有误");
						return ;
					}
					int num2=stackInt.pop();
					int num1=stackInt.pop();
					stackInt.push(num2);
					stackInt.push(num1);
					PC++;
					i++;
					break;
				}
				case "POP":{
					if(stackInt.size()<1) {
						System.out.println("程序在执行POP指令时出现错误，请检查装载是否有误");
						return ;
					}
					stackInt.pop();
					
					PC++;
					i++;
					break;
				}
				case "ADD":{
					if(stackInt.size()<2) {
						System.out.println("程序在执行ADD指令时出现错误，请检查装载是否有误");
						return ;
					}
					System.out.println("StackInt的大小："+stackInt.size());
					int num2=stackInt.pop();
					int num1=stackInt.pop();
					int num=num1+num2;
					stackInt.push(num);
					progress=progress+"\n"+num1+"+"+num2+"="+num;
					PC++;
					i++;
					break;
				}
				case "SUB":{
					if(stackInt.size()<2) {
						System.out.println("程序在执行SUB指令时出现错误，请检查装载是否有误");
						return ;
					}
					int num2=stackInt.pop();
					int num1=stackInt.pop();
					int num=num1-num2;
					System.out.println("num"+num);
					stackInt.push(num);
					progress=progress+"\n"+num1+"-"+num2+"="+num;
					PC++;
					i++;
					break;
				}
				case "MUL":{
					if(stackInt.size()<2) {
						System.out.println("程序在执行MUL指令时出现错误，请检查装载是否有误");
						return ;
					}
					int num2=stackInt.pop();
					int num1=stackInt.pop();
					int num=num1*num2;
					stackInt.push(num);
					progress=progress+"\n"+num1+"*"+num2+"="+num;
					PC++;
					i++;
					break;
				}
				case "DIV":{
					if(stackInt.size()<2) {
						System.out.println("程序在执行DIV指令时出现错误，请检查装载是否有误");
						return ;
					}
					int num2=stackInt.pop();
					int num1=stackInt.pop();
					int num=num1/num2;
					stackInt.push(num);
					progress=progress+"\n"+num1+"/"+num2+"="+num;
					PC++;
					i++;
					break;
				}
				case "IFEQ":{  //IFEQ offset(4bytes)说明：首先弹栈，然后将弹出的栈值和0比较，如果等于0，则转移到偏移量处执行。
					PC+=4;
					i+=4;
					for(int t=0;t<3;t++)
						stack.pop();
					map = stack.pop();
					String sp_value=map.get(PC);
					int number = Integer.parseInt(map.get(PC));
					System.out.println(i+"IFNEֵ值："+sp_value+number);
					PC++;
					i++;
					int num1=stackInt.pop();
					stackInt.push(num1);
					if(num1==0) {
						PC+=number;
						i+=number;
						if(stack.size()<PC) {
							System.out.println("IFEQָ指令越界！");
							return ;
						}
						for(int t=0;t<number;t++)
							stack.pop();
					}
					break;
				} 
				case "IFNE":{  //IFEQ offset(4bytes)说明：首先弹栈，然后将弹出的栈值和0比较，如果不等于0，则转移到偏移量处执行。
					PC+=4;
					i+=4;
					for(int t=0;t<3;t++)
						stack.pop();
					map = stack.pop();
					String sp_value=map.get(PC);
					//System.out.println("IFNEֵ��"+sp_value);
					//int sp=Integer.parseInt(sp_value);   //6
					int number = Integer.parseInt(map.get(PC));
					System.out.println(i+"IFNEֵ值："+sp_value+number);
					PC++;
					i++;
					int num1=stackInt.pop();   //栈顶元素出栈，获取其值
					stackInt.push(num1);
					if(num1!=0) {
						PC+=number;
						i+=number;
						if(stack.size()<PC) {
							System.out.println("IFNE指令越界！");
							return ;
						}
						for(int t=0;t<number;t++)
							stack.pop();
					}
					break;
				}
				case "NOP":{
					PC++;
					i++;
					break;
				}
				case "HALT":{
					System.out.println("执行过程为："+progress);
					System.out.println("程序执行完成！！！最终的结果为："+stackInt.pop());
					//for(int t=0;t<stackInt.size();t++)
					//	System.out.println("栈中的其他元素"+stackInt.pop());
					System.out.println();
					System.out.println();
					System.out.println();
					return ;
				}
			}
		}
	
	}	
}

