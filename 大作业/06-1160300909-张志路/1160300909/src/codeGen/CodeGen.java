package codeGen;

import java.util.ArrayList;
import java.util.List;

import optimization.Optimization;
import semantic.FourAddr;
import semantic.Smantic;


public class CodeGen
{
	/*
	public static void main(String[] args)
	{
		List<FourAddr> four_addr = new ArrayList<FourAddr>();  // 三地址指令序列
		Smantic se = new Smantic("test1.txt",four_addr);
		//System.out.println(asm(four_addr));
	}*/
	
	String ss;
	
	public CodeGen(String file_name)
	{
		analyze(file_name);
	}

	public String getss()
	{
		return ss;
	}
	
	public void analyze(String filename)
	{
		Optimization op = new Optimization(filename);
		List<List<FourAddr>> ll = op.getRe();

		List<FourAddr> l =new ArrayList<FourAddr>();
		for (int i=0; i<ll.size(); i++)
		{
			l.addAll(ll.get(i));
		}
		
		ss = str_asm(l);
	}
	
	public static String str_asm(List<FourAddr> list)
	{
		String asm_code = new String();
		asm_code = asm_code + "-----Assembly code-----\n";
		for(int i = 0;i<list.size();i++)
		{
			//System.out.println(""+i+":"+three_address.get(i));
			FourAddr skl = list.get(i);
			String s = asm(skl);
			if(s.equals(""))
				continue;
			asm_code = asm_code+"\ncode"+(i+1)+": \n"+s;
		}
		return asm_code;
	}
	
	/**
	 * 四元式到汇编代码的转换函数
	 * @param skl  四元式
	 * @return 汇编代码
	 */
	public static String asm(FourAddr skl)
	{
		String out_r = "";
		if(skl.getOp().equals("j"))
		{
			out_r = out_r+"JMP code"+skl.getToaddr()+"\n";
		}
		else if(skl.getOp().equals("+"))
		{
			out_r = out_r+"MOV %eax, "+skl.getParam1()+"\n";
			out_r = out_r+"ADD %eax, "+skl.getParam2()+"\n";
			out_r = out_r+"MOV "+skl.getToaddr()+", %eax\n";
		}
		else if(skl.getOp().equals("*"))
		{
			out_r = out_r+"MOV %eax, "+skl.getParam1()+"\n";
			out_r = out_r+"MUL %eax, "+skl.getParam2()+"\n";
			out_r = out_r+"MOV "+skl.getToaddr()+", %eax\n";
		}
		else if(skl.getOp().equals("="))
		{
			out_r = out_r+"MOV %eax, "+skl.getParam1()+"\n";
			out_r = out_r+"MOV "+skl.getToaddr()+", %eax\n";
		}
		else if(skl.getOp().equals("j<"))
		{
			out_r = out_r+"MOV %eax, "+skl.getParam1()+"\n";
			out_r = out_r+"MOV %ebx, "+skl.getParam2()+"\n";
			out_r = out_r+"CMP %eax,%ebx\n";
			out_r = out_r+"JL code"+skl.getToaddr()+"\n";
		}
		else if(skl.getOp().equals("j>"))
		{
			out_r = out_r+"MOV %eax, "+skl.getParam1()+"\n";
			out_r = out_r+"MOV %ebx, "+skl.getParam2()+"\n";
			out_r = out_r+"CMP %eax,%ebx\n";
			out_r = out_r+"JNLE code"+skl.getToaddr()+"\n";
		}
		else if(skl.getOp().equals("[]="))
		{
			String[] re = arraySplit(skl.getToaddr());
			out_r = out_r+"MOV %ecx, "+re[1]+"\n";
 			out_r = out_r+"MOV %eax, "+skl.getParam1()+"\n";
			out_r = out_r+"MOV (%ecx)+"+re[0]+", %eax\n";
		}
		else if(skl.getOp().equals("=[]"))
		{
			String[] re = arraySplit(skl.getParam1());
			out_r = out_r+"MOV %ecx, "+re[1]+"\n"; 			
			out_r = out_r+"MOV %eax, (%ecx)+"+re[0]+"\n";			
			out_r = out_r+"MOV "+skl.getToaddr()+", %eax\n";			
		}
		return out_r;
	}
	
	public static String[] arraySplit(String s)
	{
		//System.out.println(s);
		s = s.replace("]","");
		String[] re = s.split("\\[");
		return re;
	}
}
