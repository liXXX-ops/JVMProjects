package jvm1;

import java.io.IOException;

public class JVMTest {
	public static void main(String[] args) throws IOException {
		ToBytecode.ReadCoding();
		//System.out.println("111"+JVM1.table());
		ToBytecode.Translate();
		ToBytecode.WriteToTxt();
		LoadBytecode.Loading();
		Enginecode.engining();
		//System.out.println("000");
	}

}
