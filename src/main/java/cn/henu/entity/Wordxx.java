package cn.henu.entity;

public class Wordxx {

	private Integer  id ;
	 // Word名字
	private String wordName ;

	// word存储路径+名称
	private String pathName ;
	
	 // swf相对路径+重新命名
	private String swfShowPathName ;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWordName() {
		return wordName;
	}

	public void setWordName(String wordName) {
		this.wordName = wordName;
	}

	public String getPathName() {
		return pathName;
	}

	public void setPathName(String pathName) {
		this.pathName = pathName;
	}

	public String getSwfShowPathName() {
		return swfShowPathName;
	}

	public void setSwfShowPathName(String swfShowPathName) {
		this.swfShowPathName = swfShowPathName;
	}
}
