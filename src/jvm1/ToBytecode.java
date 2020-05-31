package jvm1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

public class ToBytecode {
	static InputStreamReader inputStreamRead;
	static BufferedReader bufferedReader;
	static List<String> list = new ArrayList<String>();  //
	static String [][]form;   //存放字节码
	//读出源代码并将其存储到链表中
	public static void ReadCoding() {
		try {
		String filePath="F:\\STSProjects\\JVMproject\\coding1.txt";
		//String filePath="F:\\STSProjects\\JVMproject\\coding2.txt";
		String encoding="GBK";
		File file = new File(filePath);
		if(file.isFile() && file.exists()) {
			inputStreamRead = new InputStreamReader(new FileInputStream(file),encoding);
			bufferedReader = new BufferedReader(inputStreamRead);
			String line=null;
			while((line = bufferedReader.readLine()) != null){
				list.add(line);      //将该行增加到链表中
				System.out.println(line);
			}
			System.out.println(list);
			//inputStreamRead.close();
		}
		else {
			System.out.println("请检查文件是否存在或文件的路径是否正确！");
		}
		}catch(Exception e) {
			System.out.println("文件读取出错！");
			e.printStackTrace();
		}
	
	}
	//将链表中的源代码转换成数组，并判断源代码的正确性
	public static int Translate() throws IOException {
		String []str_pushopera1 = {"LOAD","STORE","PUSH"};  //后面连接数字
		String []str_pushopera2 = {"POP","DUP","SWAP"};     //后面不连接数字
		String []str_calculate = {"ADD","SUB","MUL","DIV"};
		String []str_controlflow = {"IFEQ","IFNE"};
		String []str_stop = {"HALT","NOP"};
		String str="";
		String str_num="";   //存放指令集后面涵盖的数值
		
		
		int pc=0;
		int n=list.size();
		form=new String[n][5];   
		//form= {"0"};
		
		for(int i=0;i<n;i++) 
			for(int t=0;t<5;t++)
				form[i][t]="0";
	/*
	 * 链表可以清晰的获取到链表中的某一行的长度，并遍历该行中的字符list.get(i).charAt(k);该行中的k字符
	 */
		
		int j=0;   //记录存放指令集str_push...数组中位置
		int m=0;   //记录数组中特定位置的字符串的长度
		for(int i=0;i<n;i++)
			if(list.get(i).contains(str_pushopera1[0])||list.get(i).contains(str_pushopera1[1])||list.get(i).contains(str_pushopera1[2])) {
				for(j=0;j<2;j++)
					if(j<2&&list.get(i).contains(str_pushopera1[j])) {
						m = str_pushopera1[j].length();  //得到指令集的长度
						//System.out.println("LLLLLLLLLLL"+m);
						break;}
						//str=str_pushopera1[j].substring(0, m-1);
				for(int k=str_pushopera1[j].length();k<list.get(i).length();k++)
					if(list.get(i).charAt(k)>='0'&&list.get(i).charAt(k)<='9')
						str_num+=list.get(i).charAt(k);   //
					else 
						System.out.println("请检查LOAD,STORE,PUSH的代码书写是否正确");
				
				form[pc/5][pc%5]=str_pushopera1[j];
				//System.out.println("LLLLLLLLLLL"+str_pushopera1[j]);
				pc+=4;
				form[pc/5][pc%5]=str_num;
				pc++;
				System.out.println("LOAD,STORE,PUSH的代码书写正确"+str_num);
				str_num="";   //
				
			}else if(list.get(i).contains(str_pushopera2[0])||list.get(i).contains(str_pushopera2[1])||list.get(i).contains(str_pushopera2[2])) {
				for(j=0;j<2;j++)
					if(j<2&&list.get(i).contains(str_pushopera2[j])) {
						m = str_pushopera1[j].length();
						break;}
				for(int k=m;k<list.get(i).length();k++)
					if(list.get(i).charAt(k)!=' ')
						System.out.println("请检查POP,DUP,SWAP的代码书写是否正确");
				form[pc/5][pc%5]=str_pushopera2[j];
				pc++;
				str_num="";
			}else if(list.get(i).contains(str_calculate[0]) || list.get(i).contains(str_calculate[1])||list.get(i).contains(str_calculate[2])||list.get(i).contains(str_calculate[3])) {
				for(j=0;j<3;j++)
					if(j<3&&list.get(i).contains(str_calculate[j])) {
						m=str_calculate[j].length();break;}
				
				form[pc/5][pc%5]=str_calculate[j];
				pc++;
				
			}else if(list.get(i).contains(str_controlflow[0])||list.get(i).contains(str_controlflow[1])) {
				for(j=0;j<1;j++)
					if(j<3&&list.get(i).contains(str_controlflow[j])) {
						m=str_controlflow[j].length();break;}
				for(int k=m;k<list.get(i).length();k++)
					if(list.get(i).charAt(k)>='0'&&list.get(i).charAt(k)<='9')
						str_num+=list.get(i).charAt(k);
					else 
						System.out.println("请检查IFNE,IFEQ的代码书写是否正确");
				
				form[pc/5][pc%5]=str_controlflow[j];
				pc+=4;
				form[pc/5][pc%5]=str_num;
				pc++;
				str_num="";
			}else if(list.get(i).contains(str_stop[0])||list.get(i).contains(str_stop[1])) {
				for(j=0;j<1;j++)
					if(j<3&&list.get(i).contains(str_stop[j])) {
						m=str_stop[j].length();break;}
				
				form[pc/5][pc%5]=str_stop[j];
				pc++;
				
					
			}else
				System.out.println("源代码中出现了不可识别的字符串");	
					//for(int j=0;j<list.get(i).length();j++)
			
		for(int i=0;i<pc/5;i++) {
			for(int t=0;t<5;t++)
				System.out.print(form[i][t]);
			System.out.println();}
		//System.out.println(pc);
	return pc;   //返回字节码中的个数（5*6），pc/5为字节的行数
	}
	
	//将数组存储到文件中
	public static String[][] WriteToTxt() throws IOException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		File file = new File("F:\\STSProjects\\JVMproject\\zijiema.txt");
		if(!file.exists()) {
			file.createNewFile();
		}
		int n=Translate();
		try {
			FileWriter fileWriter = new FileWriter(file,true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.newLine();
			bufferedWriter.write("写入时间："+simpleDateFormat.format(new Date()));
			bufferedWriter.newLine();
			for(int t=0;t<n/5;t++) {
				for(int k=0;k<5;k++)
					bufferedWriter.write(form[t][k]+"     ");//write(form[t]);
				bufferedWriter.newLine();
			}
			bufferedWriter.flush();
			bufferedWriter.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return form;
		
	}
}
	
/*
	public class CreatedTable {
		public CreatedTable(List<String> columnNameListTmp,List<List<String>> columnValueListTmp) {
			//
		}
		public CreatedTable(List<String> columnNameListTmp,List<List<String>> columnValueListTmp,Map<String,Integer> m) {
			//)
		}
	}*/
	

/*
 * 字节码模块
 * nt ConvertSrc2Bytecodes(
    const char * pstrSrcFileName,//源文件名
    const char * pstrBytecodesFileName//字节码文件名
)
{
    //1.打开源文件（只读）和字节码文件（只写）
    if(fopen(…)==NULL)
        return FLAG_ERR_OPEN_FILENAME;

    //2.逐行读入源文件，逐行转换
    while(!feof(…))
    {
        从源文件读入一行；该行第一个单词是否是合法指令？如果是的话则
        判断该指令后面有无参数，如果指令后应跟参数但是源文件没有则出
        错；如果指令不应有参数但是后跟参数也出错。等等。
        将指令转换为对应的字节码，并写入到字节码文件中。
    }
    //3.关闭文件
    return FLAG_SUCCESS;
}
*装载
*int stack[100];//栈
int SP=-1;//栈指针
int PC=0;//程序计数器
char *pBuffer=NULL;

int LoadBytecodes( const char * pstrBytecodesFileName)//字节码文件名)
{
    //0.为方法区申请内存
    pBuffer=malloc(…);
    if(NULL==pBuffer)
        return FLAG_ERR_MEMROY;

    //1.打开字节码文件
    if(fopen(…)==NULL)
        return FLAG_ERR_OPEN_FILENAME;
    
    //2.读入文件,并装载到方法区中。
    //3.关闭文件
    return FLAG_SUCCESS;
    *
    *执行引擎
    *int Engine()
{
    for(;;)
    {
        //获得指令
        char op=*(pBuffer+PC);
        if(op==HALT)
            break;

        switch(op)
        {
        case PUSH:
            num=getnum();//取得PUSH指令后紧跟着的4字节整数
            PC+=5;
            SP++;
            stack[SP]=num;
            break;
        …
        }
    }

    return FLAG_SUCCESS;
}


 
*/