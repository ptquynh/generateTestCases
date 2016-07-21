package com.generateTool.fromCSV;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Generate {

	/**
	 * Create java class
	 * @param args
	 */
	public static void main(String[] args) {
		String directoryInputFile = "D:/Work/Workspace/Testcases/";
		String fileName = "ValidationData.csv";
		String packageName = "com.tekexperts.selenium.func.administration";
		String directoryOfjavaFile = "D:/Work/Workspace/ui-test/ui-testsuite/src/main/java";
		readFile(directoryInputFile,fileName,packageName,directoryOfjavaFile);
		
	}
	/**
	 * Read data from CSV file
	 */
	public static void readFile(String directoryInputFile,String fileName,String packageName,String directoryOfjavaFile){
		try (BufferedReader stream = new BufferedReader(new FileReader(
				directoryInputFile + fileName))) {
			String packagePath = packageName.replace(".", "/");
			String className = fileName.split("\\.")[0];
			String javaOutputDirPath = directoryOfjavaFile + "/" + packagePath
					+ "/";
			System.out.println("creating directory ->" + javaOutputDirPath);
			File f = new File(javaOutputDirPath);
			if (f.mkdirs()) {
				System.out.println("directory :" + javaOutputDirPath
						+ " created succesfully..");
			} else {
				System.out.println("directory :" + javaOutputDirPath
						+ " already exist..");
			}
			String javaOutputFilePath = directoryOfjavaFile + "/" + packagePath
					+ "/" + className + ".java";
			File javaOutPutFile = new File(javaOutputFilePath);
			javaOutPutFile.createNewFile();
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(javaOutputFilePath)));
			System.out.println("generating class..");
			out.println("package " + packageName + ";");
			out.println("import org.testng.annotations.Test;");
			out.println("public class " + className + " {");
			String line = null;
			String[] fields = null;
			String currentkey=null;
			String previouskey=null;
			Boolean isFirstFlag=false;
			if (stream.readLine() != null) {
				while ((line = stream.readLine()) != null) {
					if (line.isEmpty() || line.startsWith("#")) {
						continue;
					} else {
						fields  = line.replaceAll("^\"", "").split("\"?(,|$)(?=(([^\"]*\"){2})*[^\"]*$) *\"?");
						currentkey=fields[2];
						isFirstFlag=true;
						for (int i = 0; i < fields.length; i++) {
							if(!currentkey.equalsIgnoreCase(previouskey)){
								if(i==2){
									if(isFirstFlag){
										if(previouskey!=null){
											out.append("\t\t*/\n");
										}
										String result=upperCaseAllFirst(fields[3]);
									  out.append("\t\t@Test\n")
									  .append("\t\tpublic void "+fields[2].replace("-","")+"_"
									  +result.replace(" ","").replace("\"","").replace("'","")
									  .replace("/","").replace("-","").replace("[","").replace("]","")+"(){"+"\n")
									  .append("\t\t}\n");
									}
									out.append("\t\t/**\n");
									out.append("\t\t* Test case ID:"+fields[2]+"\n");
								}
								if (i == 3) {
									out.print("\t\t* Test case name:"+ fields[3] + "\n");
									out.print("\t\t* Precondition:\n");
									out.print("\t\t* Test details:\n");
									out.print("\t\t* Test Step "+fields[17]+":"+fields[18]+"\n");
								}
							}else{
								if(i==17){
									isFirstFlag=false;
									out.print("\t\t* Test Step "+fields[17]+":"+fields[18]+"\n");
									if(!fields[19].isEmpty())
									out.print("\t\t* Test Data:"+fields[19]+"\n");
									if(!fields[20].isEmpty())
										out.print("\t\t* Expected Result:"+fields[20]+"\n");
								}
							}
						}
						previouskey=currentkey;
					}
					
				}
			}
			 out.print("\t\t*/\n");
			 out.print("}");
			 out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String upperCaseAllFirst(String value) {

		char[] array = value.toCharArray();
		// Uppercase first letter.
		array[0] = Character.toUpperCase(array[0]);

		// Uppercase all letters that follow a whitespace character.
		for (int i = 1; i < array.length; i++) {
			if (Character.isWhitespace(array[i - 1])) {
				array[i] = Character.toUpperCase(array[i]);
			}
		}

		// Result.
		return new String(array);
	}
	
}
