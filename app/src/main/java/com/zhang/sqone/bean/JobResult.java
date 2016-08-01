package com.zhang.sqone.bean;

	
import java.util.List;
/**
*在这个程序的数据传输格式没有什么用
*@author ZJP
*created at 2016/3/14 13:51
*/
public class JobResult {

	public Integer Stu;
	public ResultDetail Rst; // 兼职列表

	public class ResultDetail {
		public List<Job> Lst;
	}

	public class Job {
		public String XID;
		public String JID;  //经纪人编号
		public String T; // 项目名称
		public String JJR;   //经纪人
		public String PH;  //头像
		public String ZW; // 职位名称
		public String GT; // 时间
		public String GD; // 地点
		public String S; // 薪水(人名币)
		public String C; // 单位(小时、天)
		public String ST; // 来源
		public String PS;  //评价状态

		//获取项目中的职位列表用到
		public String ZID;   //职位id
		public String ZT;    //职位名称

	}

}
