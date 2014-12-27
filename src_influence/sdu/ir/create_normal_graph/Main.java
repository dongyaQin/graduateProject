package sdu.ir.create_normal_graph;

import io.AppendFile;
import io.FileOp;

import java.sql.SQLException;

public class Main {
	static String[] ss3 = new String[]{"D:\\dataset\\finalsn\\part-00000","D:\\dataset\\finalsn\\part-00001","D:\\dataset\\finalsn\\part-00002","D:\\dataset\\finalsn\\part-00003","D:\\dataset\\finalsn\\part-00004","D:\\dataset\\finalsn\\part-00005","D:\\dataset\\finalsn\\part-00006","D:\\dataset\\finalsn\\part-00007","D:\\dataset\\finalsn\\part-00008","D:\\dataset\\finalsn\\part-00009","D:\\dataset\\finalsn\\part-00010","D:\\dataset\\finalsn\\part-00011","D:\\dataset\\finalsn\\part-00012","D:\\dataset\\finalsn\\part-00013","D:\\dataset\\finalsn\\part-00014","D:\\dataset\\finalsn\\part-00015","D:\\dataset\\finalsn\\part-00016","D:\\dataset\\finalsn\\part-00017","D:\\dataset\\finalsn\\part-00018","D:\\dataset\\finalsn\\part-00019","D:\\dataset\\finalsn\\part-00020","D:\\dataset\\finalsn\\part-00021","D:\\dataset\\finalsn\\part-00022","D:\\dataset\\finalsn\\part-00023","D:\\dataset\\finalsn\\part-00024","D:\\dataset\\finalsn\\part-00025","D:\\dataset\\finalsn\\part-00026","D:\\dataset\\finalsn\\part-00027","D:\\dataset\\finalsn\\part-00028","D:\\dataset\\finalsn\\part-00029"};


	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		String url = "/home/qinyadong/dataset/ccir2014/soc_pokec_relationships.txt";
		TxtGraphData data = new TxtGraphData();
		data.load(url,"\t", "utf-8");
		data.creatCorrespondMap();
		data.write2File("/home/qinyadong/dataset/ccir2014/dealed_soc_pokec.txt");
//		FileOp.mergeFile(ss,"D:\\dataset\\finalsn\\mergeFile");
//		data.printUserNumbers("D:\\dataset\\finalsn\\mergeFile","\t");
//		data.readFileFast("/home/qinyadong/dataset/mergeFile","\t");
//		AppendFile.append("/home/qinyadong/dataset/tempfile", "number of users==>"+number);
//		data.load("dataset\\digRelation.txt"," ","utf-8");
//		data.creatCorrespondMap();
//		data.writeRelations2File("dataset\\finalDigRelation.txt");
//		data.load("dataset\\digVotes.txt"," ","utf-8");
		
//		data.createVoteGraph("dataset\\finalDigVotes.txt", " ", "utf-8");
	}

}
